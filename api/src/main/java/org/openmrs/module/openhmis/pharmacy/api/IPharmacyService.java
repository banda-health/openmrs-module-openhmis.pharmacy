/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
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
package org.openmrs.module.openhmis.pharmacy.api;

import java.util.Set;

import org.openmrs.DrugOrder;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(OpenHMISPharmacyModuleService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface IPharmacyService extends OpenmrsService {

	/**
	 * Save a DrugOrder, create an inventory transaction if the service is
	 * available, and save these as attributes in a new WorkOrder
	 * 
	 * @param drugOrder a valid DrugOrder
	 * @param name a name for the work order
	 * @return saved DrugOrder
	 */
	public WorkOrder addDrugOrder(DrugOrder drugOrder, String name);

	public WorkOrder addDrugOrder(DrugOrder drugOrder);
	
	/**
	 * Save a batch of DrugOrders in the same way as {@link addDrugOrder}
	 * 
	 * @param drugOrderBatch a set of DrugOrders
	 * @param name a name for the work order
	 * @should save DrugOrders
	 * @should save WorkOrders
	 * @should save inventory transactions if service is available
	 */
	public WorkOrder addDrugOrderBatch(Set<DrugOrder> drugOrderBatch, String name);

	public WorkOrder addDrugOrderBatch(Set<DrugOrder> drugOrderBatch);
}