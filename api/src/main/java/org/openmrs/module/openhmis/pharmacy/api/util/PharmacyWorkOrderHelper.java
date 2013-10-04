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
package org.openmrs.module.openhmis.pharmacy.api.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openmrs.AttributableDrugOrder;
import org.openmrs.DrugOrder;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.workorder.api.IWorkOrderTypeDataService;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrderType;
import org.openmrs.module.openhmis.workorder.api.util.WorkOrderHelper;

public class PharmacyWorkOrderHelper {
	private static final Logger log = Logger.getLogger(PharmacyWorkOrderHelper.class);

	private static WorkOrderType pharmacyWorkOrderType = null;

	private PharmacyWorkOrderHelper() {}

	/**
	 * Generates a work order name.
	 * @param workOrder The work order to create the name for.
	 * @return The work order name.
	 */
	public static String generateName(WorkOrder workOrder) {
		/*
		If the work order already has a name, just return it.  Otherwise, create a new name:
			If part of a batch then use the first order in the batch as the name
			If part of a drug order, use the drug name as the name
		 */

		String name = workOrder.getName();

		if (name == null || name.isEmpty()) {
			if (workOrder.getWorkOrders() != null && !workOrder.getWorkOrders().isEmpty()) {
				name = "Batch with " + workOrder.getWorkOrders().get(0).getName();
			} else {
				DrugOrder drugOrder = getDrugOrder(workOrder);
				if (drugOrder != null && drugOrder.getDrug() != null) {
					name = drugOrder.getDrug().getName();
				}
			}
		}

		return name;
	}

	/**
	 * Gets the {@link DrugOrder} attribute from the specified {@link WorkOrder}.
	 * @param workOrder The {@link WorkOrder}.
	 * @return The {@link DrugOrder} or {@code null} if the {@link WorkOrder} does not have a drug order attribute.
	 */
	public static DrugOrder getDrugOrder(WorkOrder workOrder) {
		return WorkOrderHelper.getAttributeTypeValue(workOrder, AttributableDrugOrder.class);
	}

	/**
	 * Gets the pharmacy work order type.
	 * @return The pharmacy work order type.
	 */
	public static WorkOrderType getWorkOrderType() {
		if (pharmacyWorkOrderType == null) {
			AdministrationService service = Context.getAdministrationService();
			String uuid = service.getGlobalProperty(ModuleConstants.WORKORDER_TYPE_UUID_PROPERTY);
			if (StringUtils.isEmpty(uuid)) {
				log.warn("Could not find the pharmacy work order type (UUID='" + uuid + "')");

				throw new APIException("Could not load pharmacy work order type.");
			}

			pharmacyWorkOrderType = Context.getService(IWorkOrderTypeDataService.class).getByUuid(uuid);
		}

		return pharmacyWorkOrderType;
	}
}
