/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.openhmis.pharmacy.api.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openmrs.DrugOrder;
import org.openmrs.api.OrderService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.ModuleException;
import org.openmrs.module.openhmis.pharmacy.api.IPharmacyService;
import org.openmrs.module.openhmis.pharmacy.api.util.PharmacyWorkOrder;
import org.openmrs.module.openhmis.pharmacy.inv.InventoryUtilFactory;
import org.openmrs.module.openhmis.workorder.api.IWorkOrderService;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@SuppressWarnings("deprecation")
public class PharmacyServiceImpl extends BaseOpenmrsService implements IPharmacyService {
	private static final Logger log = Logger.getLogger(PharmacyServiceImpl.class);

	private OrderService orderService;
	private IWorkOrderService workOrderService;

	@Autowired
	public PharmacyServiceImpl(OrderService orderService, IWorkOrderService workOrderService)  {
		this.orderService = orderService;
		this.workOrderService = workOrderService;
	}

	@Override
	public WorkOrder addDrugOrder(DrugOrder drugOrder) {
		return addDrugOrder(drugOrder, null);
	}

	@Override
	public WorkOrder addDrugOrder(DrugOrder drugOrder, String name) {
		// Save the drug order
		drugOrder = (DrugOrder)orderService.saveOrder(drugOrder);

		// Create a new work order from the drug order
		WorkOrder workOrder = createPharmacyWorkOrder(drugOrder, name);
		workOrder = workOrderService.save(workOrder);

		return workOrder;
	}
	
	public WorkOrder addDrugOrderBatch(Set<DrugOrder> drugOrderBatch) {
		return addDrugOrderBatch(drugOrderBatch, null);
	}
	
	@SuppressWarnings("deprecation")
	public WorkOrder addDrugOrderBatch(Set<DrugOrder> drugOrderBatch, String orderName) {
		WorkOrder workOrder = new WorkOrder();
		workOrder.setWorkOrderType(PharmacyWorkOrder.getWorkOrderType());

		try {
			for (DrugOrder drugOrder : drugOrderBatch) {
				drugOrder = (DrugOrder) orderService.saveOrder(drugOrder);

				WorkOrder subOrder = createPharmacyWorkOrder(drugOrder, null);
				workOrder.addWorkOrder(subOrder);
			}

			if (orderName != null) {
				workOrder.setName(orderName);
			} else {
				workOrder.setName(PharmacyWorkOrder.getName(workOrder));
			}

			workOrder = workOrderService.save(workOrder);
		} catch (Exception ex) {
			log.error("Someth'n done broke", ex);

			workOrder = null;
		}

		return workOrder;
	}
	
	private WorkOrder createPharmacyWorkOrder(DrugOrder drugOrder, String name) {
		WorkOrder workOrder = new WorkOrder();
		workOrder.setWorkOrderType(PharmacyWorkOrder.getWorkOrderType());

		PharmacyWorkOrder.setDrugOrder(workOrder, drugOrder);

		if (StringUtils.isEmpty(name)) {
			workOrder.setName(PharmacyWorkOrder.getName(workOrder));
		} else {
			workOrder.setName(name);
		}

		try {
			InventoryUtilFactory.getInstance().createAndSetDrugTransaction(drugOrder, workOrder);
		} catch (ModuleException e) {
			log.info(e.getMessage(), e);
		}

		return workOrder;
	}
}