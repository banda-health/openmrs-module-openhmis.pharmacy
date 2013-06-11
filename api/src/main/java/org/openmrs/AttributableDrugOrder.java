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
