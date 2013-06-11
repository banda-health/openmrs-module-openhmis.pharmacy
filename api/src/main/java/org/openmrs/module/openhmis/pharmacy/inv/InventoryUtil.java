package org.openmrs.module.openhmis.pharmacy.inv;

import java.util.List;

import org.openmrs.DrugOrder;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.inventory.api.IStockRoomDataService;
import org.openmrs.module.openhmis.inventory.api.IStockRoomService;
import org.openmrs.module.openhmis.inventory.api.WellKnownTransactionTypes;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.StockRoom;
import org.openmrs.module.openhmis.inventory.api.model.StockRoomItem;
import org.openmrs.module.openhmis.inventory.api.model.StockRoomTransaction;
import org.openmrs.module.openhmis.inventory.api.search.ItemSearch;
import org.openmrs.module.openhmis.pharmacy.api.util.ModuleConstants;
import org.openmrs.module.openhmis.workorder.api.IWorkOrderAttributeTypeDataService;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrderAttribute;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrderAttributeType;
import org.openmrs.module.openhmis.workorder.api.util.WorkOrderUtil;

public class InventoryUtil implements IInventoryUtil {
	@Override
	public StockRoomTransaction getTransaction(WorkOrder workOrder) {
		return (StockRoomTransaction) WorkOrderUtil.getAttributeByTypeName(workOrder, StockRoomTransaction.class.getName()).getHydratedValue();
	}
	
	@Override
	public WorkOrderAttribute setTransaction(WorkOrder workOrder, StockRoomTransaction transaction) {
		WorkOrderAttribute attr = WorkOrderUtil.getAttributeByTypeName(workOrder, StockRoomTransaction.class.getName());
		WorkOrderAttributeType type;
		if (attr != null) {
			workOrder.removeAttribute(attr);
			type = attr.getAttributeType();
		}
		else
			type = Context.getService(IWorkOrderAttributeTypeDataService.class)
					.getByFormatUnique(StockRoomTransaction.class.getName(), workOrder.getWorkOrderType());
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
		IStockRoomService txService = Context.getService(IStockRoomService.class);
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
