package org.openmrs.module.openhmis.pharmacy.api.util;

import org.openmrs.DrugOrder;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.inventory.api.model.StockRoomTransaction;
import org.openmrs.module.openhmis.workorder.api.IWorkOrderAttributeTypeDataService;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrderAttribute;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrderAttributeType;
import org.openmrs.module.openhmis.workorder.api.util.WorkOrderUtil;

public class PharmacyWorkOrder {
	private PharmacyWorkOrder() {}

	public static String getName(WorkOrder workOrder) {
		String name = workOrder.getName();
		if (name == null || name.isEmpty()) {
			if (workOrder.getWorkOrders() != null && !workOrder.getWorkOrders().isEmpty())
				return "Batch with " + workOrder.getWorkOrders().get(0).getName();
			DrugOrder drugOrder = getDrugOrder(workOrder);
			if (drugOrder != null && drugOrder.getDrug() != null)
				return drugOrder.getDrug().getName();
		}
		return name;
	}
	
	public static DrugOrder getDrugOrder(WorkOrder workOrder) {
		return (DrugOrder) WorkOrderUtil.getAttributeByTypeName(workOrder, DrugOrder.class.getName()).getHydratedValue();
	}
	
	public static StockRoomTransaction getTransaction(WorkOrder workOrder) {
		return (StockRoomTransaction) WorkOrderUtil.getAttributeByTypeName(workOrder, StockRoomTransaction.class.getName()).getHydratedValue();
	}
	
	public static WorkOrderAttribute setDrugOrder(WorkOrder workOrder, DrugOrder drugOrder) {
		WorkOrderAttribute attr = WorkOrderUtil.getAttributeByTypeName(workOrder, DrugOrder.class.getName());
		WorkOrderAttributeType type;
		if (attr != null) {
			workOrder.removeAttribute(attr);
			type = attr.getAttributeType();
		}
		else
			type = Context.getService(IWorkOrderAttributeTypeDataService.class)
					.getByFormatUnique(DrugOrder.class.getName(), workOrder.getWorkOrderType());
		WorkOrderAttribute workOrderAttr = new WorkOrderAttribute();
		workOrderAttr.setName(drugOrder.getDrug().getName());
		workOrderAttr.setAttributeType(type);
		workOrder.addAttribute(workOrderAttr);
		return workOrderAttr;
	}
	
	public static WorkOrderAttribute setTransaction(WorkOrder workOrder, StockRoomTransaction transaction) {
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
}
