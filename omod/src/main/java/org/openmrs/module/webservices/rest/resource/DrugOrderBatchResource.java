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
package org.openmrs.module.webservices.rest.resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.openmrs.DrugOrder;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.pharmacy.api.IPharmacyService;
import org.openmrs.module.openhmis.pharmacy.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = ModuleRestConstants.ORDER_BATCH_RESOURCE, supportedClass = HashSet.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*"})
public class DrugOrderBatchResource extends DelegatingCrudResource<Set<DrugOrder>> {

	@Override
	public Set<DrugOrder> newDelegate() {
		return new HashSet<DrugOrder>();
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("batch");
		return description;
	}
	
	@PropertySetter("batch")
	public void setBatch(Set<DrugOrder> instance, HashSet<DrugOrder> batch) {
		instance.addAll(batch);
	}

	@Override
	public Set<DrugOrder> save(Set<DrugOrder> delegate) {
		for (DrugOrder order : delegate) {
			if (order.getConcept() == null)
				order.setConcept(order.getDrug().getConcept());
		}
		Context.getService(IPharmacyService.class).addDrugOrderBatch(delegate);
		return delegate;
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		return new DelegatingResourceDescription();
	}

	@Override
	public Set<DrugOrder> getByUniqueId(String uniqueId) {
		throw new ResourceDoesNotSupportOperationException(OrderResource.SAVE_ONLY_MSG);
	}

	@Override
	public String getUri(Object instance) {
		throw new ResourceDoesNotSupportOperationException(OrderResource.SAVE_ONLY_MSG);
	}

	@Override
	protected void delete(Set<DrugOrder> delegate, String reason, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException(OrderResource.SAVE_ONLY_MSG);
	}

	@Override
	public void purge(Set<DrugOrder> delegate, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException(OrderResource.SAVE_ONLY_MSG);		
	}
}
