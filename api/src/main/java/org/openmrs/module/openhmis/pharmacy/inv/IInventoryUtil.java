package org.openmrs.module.openhmis.pharmacy.inv;

import org.openmrs.DrugOrder;
import org.openmrs.module.openhmis.inventory.api.model.StockRoomTransaction;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrderAttribute;

public interface IInventoryUtil {
	public StockRoomTransaction getTransaction(WorkOrder workOrder);
	public WorkOrderAttribute setTransaction(WorkOrder workOrder, StockRoomTransaction transaction);
	public void createAndSetDrugTransaction(DrugOrder drugOrder, WorkOrder workOrder);
	//public void createWorkOrderTypeTransactionAttribute(WorkOrderType workOrder);
}
