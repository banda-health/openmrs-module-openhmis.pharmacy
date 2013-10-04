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
package org.openmrs.module.webservices.rest.resource;

import org.openmrs.DrugOrder;
import org.openmrs.Order;
import org.openmrs.annotation.Handler;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubclassHandler;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_8.DrugOrderSubclassHandler1_8;

@Handler(supports = DrugOrder.class)
public class DrugOrderResource extends DrugOrderSubclassHandler1_8 implements DelegatingSubclassHandler<Order, DrugOrder> {
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription desc = super.getCreatableProperties();
		/* Remove required property.  This will be automatically set to the
		 * drug's concept if null
		 */
		desc.removeProperty("concept");
		desc.addProperty("concept");
		return desc;
	}
}
