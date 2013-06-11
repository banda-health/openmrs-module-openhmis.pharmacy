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
