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
package org.openmrs.module.openhmis.pharmacy.web;

import org.openmrs.module.webservices.rest.web.RestConstants;

public class ModuleRestConstants {
	public static final String MODULE_REST_ROOT = RestConstants.VERSION_2 + "/pharmacy/";

	public static final String ORDER_RESOURCE = MODULE_REST_ROOT + "order";
	public static final String ORDER_BATCH_RESOURCE = MODULE_REST_ROOT + "orderbatch";
}
