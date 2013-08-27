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

import org.openmrs.api.context.Context;

public class ModuleConstants {
	public static final String MODULE_NAME = "pharmacy";

	public static final String DISPENSARY_STOCKROOM_ID_PROPERTY = "openhmis.pharmacy.dispensaryStockRoomId";
	public static final String DISPENSING_ENCOUNTER_ID_PROPERTY = "openhmis.pharmacy.dispensingEncounterId";
	
	public static final String WORKORDER_TYPE_UUID_PROPERTY = "openhmis.pharmacy.workOrderTypeUuid";
	
	public static Integer tryToGetDispensingStockRoomId() {
		Integer stockRoomId = null;
		try {
			stockRoomId = Integer.parseInt(Context.getAdministrationService()
					.getGlobalProperty(ModuleConstants.DISPENSARY_STOCKROOM_ID_PROPERTY));
		}
		catch (NumberFormatException e) { return null; }
		return stockRoomId;
	}
}
