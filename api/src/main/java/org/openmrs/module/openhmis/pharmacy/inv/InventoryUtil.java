/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
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
package org.openmrs.module.openhmis.pharmacy.inv;

import org.openmrs.DrugOrder;
import org.openmrs.api.APIException;
import org.openmrs.module.openhmis.inventory.api.IStockRoomDataService;
import org.openmrs.module.openhmis.inventory.api.IStockRoomService;
import org.openmrs.module.openhmis.inventory.api.WellKnownTransactionTypes;
import org.openmrs.module.openhmis.inventory.api.model.StockRoom;
import org.openmrs.module.openhmis.inventory.api.model.StockRoomItem;
import org.openmrs.module.openhmis.inventory.api.model.StockRoomTransaction;
import org.openmrs.module.openhmis.inventory.api.search.ItemSearch;
import org.openmrs.module.openhmis.pharmacy.api.util.ModuleConstants;
import org.openmrs.module.openhmis.workorder.api.IWorkOrderAttributeTypeDataService;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrderAttribute;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrderAttributeType;
import org.openmrs.module.openhmis.workorder.api.util.WorkOrderHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InventoryUtil implements IInventoryUtil {
	private IWorkOrderAttributeTypeDataService workDataService;
	private IStockRoomService stockRoomService;
	private IStockRoomDataService stockRoomDataService;

	@Autowired
	public InventoryUtil(IWorkOrderAttributeTypeDataService workDataService,
	                     IStockRoomService stockRoomService,
	                     IStockRoomDataService stockRoomDataService) {
		this.workDataService = workDataService;
		this.stockRoomService = stockRoomService;
		this.stockRoomDataService = stockRoomDataService;
	}

	@Override
	public StockRoomTransaction getTransaction(WorkOrder workOrder) {
		return WorkOrderHelper.getAttributeTypeValue(workOrder, StockRoomTransaction.class);
	}
	
	@Override
	public WorkOrderAttribute setTransaction(WorkOrder workOrder, StockRoomTransaction transaction) {
		WorkOrderAttribute attr = WorkOrderHelper.getAttributeByType(workOrder, StockRoomTransaction.class);
		WorkOrderAttributeType type;
		if (attr != null) {
			workOrder.removeAttribute(attr);
			type = attr.getAttributeType();
		} else {
			type = workDataService.getByClass(workOrder.getInstanceType(), StockRoomTransaction.class);
		}

		WorkOrderAttribute workOrderAttr = new WorkOrderAttribute();
		workOrderAttr.setName(transaction.getDisplayString());
		workOrderAttr.setAttributeType(type);
		workOrder.addAttribute(workOrderAttr);

		return workOrderAttr;
	}
	
	public void createAndSetDrugTransaction(DrugOrder drugOrder, WorkOrder workOrder) {
		StockRoomTransaction transaction = createDrugTransaction(drugOrder);
		setTransaction(workOrder, transaction);
	}
	
	private StockRoomTransaction createDrugTransaction(DrugOrder drugOrder) {
		if (drugOrder.getQuantity() == null) {
			throw new APIException("Cannot create a transaction when drug order quantity is null.");
		}

		Integer stockRoomId = ModuleConstants.tryToGetDispensingStockRoomId();
		if (stockRoomId == null) {
			return null;
		}

		StockRoomTransaction transaction = null;
		StockRoom dispensary = stockRoomDataService.getById(stockRoomId);
		if (stockRoomService != null) {
			ItemSearch itemSearch = new ItemSearch();
			itemSearch.getTemplate().setDrug(drugOrder.getDrug());

			List<StockRoomItem> results = stockRoomDataService.findItems(dispensary, itemSearch, null);
			if (results.size() > 0) {
				transaction = stockRoomService.createTransaction(WellKnownTransactionTypes.getDistribution(), dispensary, null);

				for (StockRoomItem item : results) {
					transaction.addItem(item, drugOrder.getQuantity());
				}

				stockRoomService.submitTransaction(transaction);
			}
		}

		return transaction;
	}
}
