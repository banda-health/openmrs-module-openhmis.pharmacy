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

import java.util.List;
import java.util.Set;

import org.openmrs.DrugOrder;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.openhmis.inventory.api.IStockRoomDataService;
import org.openmrs.module.openhmis.inventory.api.IStockRoomService;
import org.openmrs.module.openhmis.inventory.api.WellKnownTransactionTypes;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.StockRoom;
import org.openmrs.module.openhmis.inventory.api.model.StockRoomItem;
import org.openmrs.module.openhmis.inventory.api.model.StockRoomTransaction;
import org.openmrs.module.openhmis.inventory.api.search.ItemSearch;
import org.openmrs.module.openhmis.pharmacy.api.IPharmacyService;
import org.openmrs.module.openhmis.pharmacy.api.util.ModuleConstants;
import org.openmrs.module.openhmis.pharmacy.api.util.PharmacyWorkOrder;
import org.openmrs.module.openhmis.workorder.api.IWorkOrderDataService;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrderAttribute;

public class PharmacyServiceImpl extends BaseOpenmrsService implements IPharmacyService {
	@Override
	public WorkOrder addDrugOrder(DrugOrder drugOrder) {
		return addDrugOrder(drugOrder, null);
	}

	@SuppressWarnings("deprecation")
	@Override
	public WorkOrder addDrugOrder(DrugOrder drugOrder, String name) {
		IWorkOrderDataService workOrderService = Context.getService(IWorkOrderDataService.class);
		drugOrder = (DrugOrder) Context.getOrderService().saveOrder(drugOrder);
		WorkOrder workOrder = createPharmacyWorkOrder(drugOrder, name);
		return workOrderService.save(workOrder);
	}
	
	public WorkOrder addDrugOrderBatch(Set<DrugOrder> drugOrderBatch) {
		return addDrugOrderBatch(drugOrderBatch, null);
	}
	
	@SuppressWarnings("deprecation")
	public WorkOrder addDrugOrderBatch(Set<DrugOrder> drugOrderBatch, String orderName) {
		IWorkOrderDataService workOrderService = Context.getService(IWorkOrderDataService.class);
		WorkOrder workOrder = new WorkOrder();
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
		PharmacyWorkOrder.setDrugOrder(workOrder, drugOrder);
		if (workOrder.getName() == null || workOrder.getName().isEmpty())
			workOrder.setName(drugOrder.getDrug().getName());
		StockRoomTransaction transaction = tryToCreateDrugTransaction(drugOrder);
		if (transaction != null) {
			if (transaction.getItems().size() > 1)
				workOrder.setDescription(Context.getMessageSourceService().getMessage("openhmis.pharmacy.verifyItem"));
			WorkOrderAttribute txAttribute = PharmacyWorkOrder.setTransaction(workOrder, transaction);
			workOrder.addAttribute(txAttribute);
		}
		return workOrder;
	}
	
	private StockRoomTransaction tryToCreateDrugTransaction(DrugOrder drugOrder) {
		IStockRoomService txService = Context.getService(IStockRoomService.class);
		if (txService == null) return null;
		Integer stockRoomId = ModuleConstants.tryToGetDispensingStockRoomId();
		if (stockRoomId == null) return null;
		if (drugOrder.getQuantity() == null)
			throw new APIException("Cannot create a transaction when drug order quantity is null.");
		StockRoom dispensary = Context.getService(IStockRoomDataService.class).getById(stockRoomId);
		if (txService != null) {
			ItemSearch itemSearch = new ItemSearch(new Item());
			itemSearch.getTemplate().setDrug(drugOrder.getDrug());
			List<StockRoomItem> results = Context.getService(IStockRoomDataService.class).findItems(dispensary, itemSearch, null);
			if (results.size() > 0) {
				StockRoomTransaction transaction = txService.createTransaction(WellKnownTransactionTypes.getDistribution(), dispensary, null);
				for (StockRoomItem item : results) {
					transaction.addItem(item, drugOrder.getQuantity());
				}
				txService.submitTransaction(transaction);
				return transaction;
			}
		}
		return null;
	}
}