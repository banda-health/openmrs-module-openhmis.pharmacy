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
package org.openmrs;

import java.util.List;

import org.openmrs.Attributable;
import org.openmrs.DrugOrder;
import org.openmrs.api.context.Context;
import org.springframework.beans.BeanUtils;

@SuppressWarnings("serial")
public class AttributableDrugOrder extends DrugOrder implements Attributable<DrugOrder> {

	public AttributableDrugOrder() {
		super();
	}

	public AttributableDrugOrder(DrugOrder drugOrder) {
		super();
		BeanUtils.copyProperties(drugOrder, this);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public DrugOrder hydrate(String s) {
		return Context.getOrderService().getOrder(Integer.parseInt(s), DrugOrder.class);
	}

	@Override
	public String serialize() {
		if (getOrderId() != null)
			return "" + getOrderId();
		return "";
	}

	@Override
	public List<DrugOrder> getPossibleValues() {
		return null;
	}

	@Override
	public List<DrugOrder> findPossibleValues(String searchText) {
		return null;
	}

	@Override
	public String getDisplayString() {
		return null;
	}

}
