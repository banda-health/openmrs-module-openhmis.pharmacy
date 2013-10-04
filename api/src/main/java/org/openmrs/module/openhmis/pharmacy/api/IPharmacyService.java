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
package org.openmrs.module.openhmis.pharmacy.api;

import org.openmrs.DrugOrder;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
public interface IPharmacyService extends OpenmrsService {

	/**
	 * Saves a DrugOrder using the specified {@link WorkOrder} name. This will create an inventory transaction if the
	 * service is available, and save these as attributes in a new {@link WorkOrder}.
	 * 
	 * @param drugOrder a valid DrugOrder
	 * @param name The name for the work order
	 * @return The pharmacy work order that was created to service the drug order.
	 */
	public WorkOrder addDrugOrder(DrugOrder drugOrder, String name);

	/**
	 * Saves a {@link DrugOrder}.
	 * @param drugOrder The {@link DrugOrder} to save.
	 * @return The pharmacy work order that was created to service the drug order.
	 */
	public WorkOrder addDrugOrder(DrugOrder drugOrder);
	
	/**
	 * Saves a batch of DrugOrders using the specified {@link WorkOrder} name.
	 * 
	 * @param drugOrderBatch The {@link DrugOrder}s to save.
	 * @param name The name for the work order
	 * @should save DrugOrders
	 * @should save WorkOrders
	 * @should save inventory transactions if service is available
	 * @return The pharmacy work order that was created to service the drug order.
	 */
	public WorkOrder addDrugOrderBatch(Set<DrugOrder> drugOrderBatch, String name);

	/**
	 * Saves a batch of {@link DrugOrder}s.
	 * @param drugOrderBatch The {@link DrugOrder}s to save.
	 * @return The pharmacy work order that was created to service the drug order.
	 */
	public WorkOrder addDrugOrderBatch(Set<DrugOrder> drugOrderBatch);
}