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
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.pharmacy.api.IPharmacyService;
import org.openmrs.module.openhmis.pharmacy.api.util.PharmacyWorkOrderHelper;
import org.openmrs.module.openhmis.pharmacy.web.ModuleRestConstants;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_8.OrderResource1_8;

@Resource(name = ModuleRestConstants.ORDER_RESOURCE, supportedClass = Order.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*"})
public class OrderResource extends OrderResource1_8 {
	public static final String SAVE_ONLY_MSG = "This is a save-only resource.";
	@Override
	public Order save(Order delegate) {
		if (DrugOrder.class.isAssignableFrom(delegate.getClass())) {
			DrugOrder order = (DrugOrder) delegate;
			if (order.getConcept() == null)
				order.setConcept(order.getDrug().getConcept());
			WorkOrder newWorkOrder = Context.getService(IPharmacyService.class).addDrugOrder(order);
			return PharmacyWorkOrderHelper.getDrugOrder(newWorkOrder);
		}
		else
			throw new ResourceDoesNotSupportOperationException("This resource only supports saving Drug Orders.");
	}
	
	@Override
	public Order getByUniqueId(String uniqueId) {
		throw new ResourceDoesNotSupportOperationException(SAVE_ONLY_MSG);
	}

	@Override
	protected void delete(Order delegate, String reason, RequestContext context) {
		throw new ResourceDoesNotSupportOperationException(SAVE_ONLY_MSG);
	}

	public void purge(Order delegate, RequestContext context) {
		throw new ResourceDoesNotSupportOperationException(SAVE_ONLY_MSG);
	}
}
