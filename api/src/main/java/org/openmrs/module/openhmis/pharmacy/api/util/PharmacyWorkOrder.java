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
package org.openmrs.module.openhmis.pharmacy.api.util;

import org.apache.commons.lang.StringUtils;
import org.openmrs.AttributableDrugOrder;
import org.openmrs.DrugOrder;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.workorder.api.IWorkOrderAttributeTypeDataService;
import org.openmrs.module.openhmis.workorder.api.IWorkOrderTypeDataService;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrderAttribute;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrderAttributeType;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrderType;
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
		return (DrugOrder) WorkOrderUtil.getAttributeByTypeName(workOrder, AttributableDrugOrder.class.getName()).getHydratedValue();
	}
	
	public static WorkOrderAttribute setDrugOrder(WorkOrder workOrder, DrugOrder drugOrder) {
		WorkOrderAttribute attr = WorkOrderUtil.getAttributeByTypeName(workOrder, AttributableDrugOrder.class.getName());
		WorkOrderAttributeType type;
		if (attr != null) {
			workOrder.removeAttribute(attr);
			type = attr.getAttributeType();
		}
		else
			type = Context.getService(IWorkOrderAttributeTypeDataService.class)
					.getByFormatUnique(AttributableDrugOrder.class.getName(), workOrder.getWorkOrderType());
		WorkOrderAttribute workOrderAttr = new WorkOrderAttribute();
		workOrderAttr.setName(drugOrder.getDrug().getName());
		workOrderAttr.setAttributeType(type);
		workOrderAttr.setValue(drugOrder.getId().toString());
		workOrder.addAttribute(workOrderAttr);
		return workOrderAttr;
	}
	
	public static WorkOrderType getWorkOrderType() {
		AdministrationService service = Context.getAdministrationService();
		String uuid = service.getGlobalProperty(ModuleConstants.WORKORDER_TYPE_UUID_PROPERTY);
		if (StringUtils.isEmpty(uuid))
			return null;
		WorkOrderType type = Context.getService(IWorkOrderTypeDataService.class).getByUuid(uuid);
		return type;
	}
}
