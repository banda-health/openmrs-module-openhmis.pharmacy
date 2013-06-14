/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
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

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openmrs.DrugOrder;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.ModuleException;
import org.openmrs.module.openhmis.pharmacy.api.IPharmacyService;
import org.openmrs.module.openhmis.pharmacy.api.util.PharmacyWorkOrder;
import org.openmrs.module.openhmis.pharmacy.inv.InventoryUtilFactory;
import org.openmrs.module.openhmis.workorder.api.IWorkOrderService;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;

public class PharmacyServiceImpl extends BaseOpenmrsService implements IPharmacyService {
	private static final Logger log = Logger.getLogger(PharmacyServiceImpl.class);
	@Override
	public WorkOrder addDrugOrder(DrugOrder drugOrder) {
		return addDrugOrder(drugOrder, null);
	}

	@SuppressWarnings("deprecation")
	@Override
	public WorkOrder addDrugOrder(DrugOrder drugOrder, String name) {
		IWorkOrderService workOrderService = Context.getService(IWorkOrderService.class);
		drugOrder = (DrugOrder) Context.getOrderService().saveOrder(drugOrder);
		WorkOrder workOrder = createPharmacyWorkOrder(drugOrder, name);
		return workOrderService.save(workOrder);
	}
	
	public WorkOrder addDrugOrderBatch(Set<DrugOrder> drugOrderBatch) {
		return addDrugOrderBatch(drugOrderBatch, null);
	}
	
	@SuppressWarnings("deprecation")
	public WorkOrder addDrugOrderBatch(Set<DrugOrder> drugOrderBatch, String orderName) {
		IWorkOrderService workOrderService = Context.getService(IWorkOrderService.class);
		WorkOrder workOrder = new WorkOrder();
		workOrder.setWorkOrderType(PharmacyWorkOrder.getWorkOrderType());
		for (DrugOrder drugOrder : drugOrderBatch) {
			drugOrder = (DrugOrder) Context.getOrderService().saveOrder(drugOrder);
			WorkOrder subOrder = createPharmacyWorkOrder(drugOrder, null);
			workOrder.addWorkOrder(subOrder);
		}
		if (orderName != null)
			workOrder.setName(orderName);
		else
			workOrder.setName(PharmacyWorkOrder.getName(workOrder));
		return workOrderService.save(workOrder);
	}
	
	private WorkOrder createPharmacyWorkOrder(DrugOrder drugOrder, String name) {
		WorkOrder workOrder = new WorkOrder();
		workOrder.setWorkOrderType(PharmacyWorkOrder.getWorkOrderType());
		PharmacyWorkOrder.setDrugOrder(workOrder, drugOrder);
		if (StringUtils.isEmpty(name))
			workOrder.setName(PharmacyWorkOrder.getName(workOrder));
		else
			workOrder.setName(name);
		try {
			InventoryUtilFactory.getInstance().createAndSetDrugTransaction(drugOrder, workOrder);
		} catch (ModuleException e) {
			log.info(e.getMessage(), e);
		}
		return workOrder;
	}
}