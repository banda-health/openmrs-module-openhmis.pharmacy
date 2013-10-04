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

import org.apache.log4j.Logger;
import org.openmrs.module.ModuleException;
import org.openmrs.module.ModuleFactory;

public class InventoryUtilFactory {
	private static final Logger log = Logger.getLogger(InventoryUtilFactory.class);
	private static final String INVENTORY_UTIL_CLASSNAME = "org.openmrs.module.openhmis.pharmacy.inv.InventoryUtil";
	private static IInventoryUtil instance;

	// Don't allow new instances of this class
	private InventoryUtilFactory() {}

	public static IInventoryUtil getInstance() {
		if (!ModuleFactory.isModuleStarted("openhmis.inventory")) {
			throw new ModuleException("Inventory module is not available");
		}

		if (instance == null) {
			try {
				instance = (IInventoryUtil) Class.forName(INVENTORY_UTIL_CLASSNAME).newInstance();
			} catch (Throwable t) {
				log.error("Error loading util class " + INVENTORY_UTIL_CLASSNAME);
			}
		}

		return instance;
	}
}