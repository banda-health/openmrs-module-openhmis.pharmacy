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
package org.openmrs.module.openhmis.pharmacy;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.openhmis.pharmacy.api.util.ModuleConstants;
import org.openmrs.module.openhmis.pharmacy.api.util.PharmacyWorkOrderHelper;
import org.openmrs.module.openhmis.pharmacy.web.PharmacyWebConstants;
import org.openmrs.module.openhmis.workorder.api.IWorkOrderService;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrderType;
import org.openmrs.module.openhmis.workorder.api.util.WorkOrderHelper;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class PharmacyModuleActivator implements ModuleActivator {
	
	protected Log log = LogFactory.getLog(getClass());
		
	/**
	 * @see ModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		log.info("Refreshing OpenHMIS Pharmacy Module Module");
	}
	
	/**
	 * @see ModuleActivator#contextRefreshed()
	 */
	public void contextRefreshed() {
		log.info("OpenHMIS Pharmacy Module Module refreshed");
	}
	
	/**
	 * @see ModuleActivator#willStart()
	 */
	public void willStart() {
		log.info("Starting OpenHMIS Pharmacy Module Module");
	}
	
	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
		log.info("OpenHMIS Pharmacy Module Module started");

		setupWorkOrderType();

		Context.getService(IWorkOrderService.class).registerModuleJavascript(
				PharmacyWorkOrderHelper.getWorkOrderType(),
				PharmacyWebConstants.PHARMACY_WORKORDER_JS_MODULE_PATH
		);
	}
	
	/**
	 * @see ModuleActivator#willStop()
	 */
	public void willStop() {
		log.info("Stopping OpenHMIS Pharmacy Module Module");
	}
	
	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		log.info("OpenHMIS Pharmacy Module Module stopped");
	}

	private void setupWorkOrderType() {
		log.debug("Checking for Pharmacy module Work Order Type...");

		GlobalProperty typeProperty = new GlobalProperty(ModuleConstants.WORKORDER_TYPE_UUID_PROPERTY, null);
		if (!WorkOrderHelper.checkWorkOrderType(typeProperty)) {
			log.info("Creating Pharmacy module Work Order Type...");

			// Create a new work order type for pharmacy work orders
			MessageSourceService messages = Context.getMessageSourceService();
			WorkOrderType workOrderType = new WorkOrderType();
			workOrderType.setName(messages.getMessage("openhmis.pharmacy.workOrderTypeName"));
			workOrderType.setDescription(messages.getMessage("openhmis.pharmacy.workOrderTypeDescription"));
			workOrderType.addAttributeType(
					messages.getMessage("openhmis.pharmacy.drugOrder.name"),
					"org.openmrs.DrugOrder", null, null, true, 0);
			workOrderType.addAttributeType(
					messages.getMessage("openhmis.pharmacy.inventoryTransaction.name"),
					"org.openmrs.module.openhmis.inventory.api.model.StockRoomTransaction",
					null, null, false, 1);

			// Ensure that this work order type exists, which creates it if it does not
			WorkOrderHelper.ensureWorkOrderType(workOrderType, typeProperty);
		}
	}
}
