package org.openmrs.module.openhmis.pharmacy.web;

import org.openmrs.module.webservices.rest.web.RestConstants;

public class ModuleRestConstants {
	public static final String MODULE_REST_ROOT = RestConstants.VERSION_2 + "/pharmacy/";

	public static final String ORDER_RESOURCE = MODULE_REST_ROOT + "order";
	public static final String ORDER_BATCH_RESOURCE = MODULE_REST_ROOT + "orderbatch";
}
