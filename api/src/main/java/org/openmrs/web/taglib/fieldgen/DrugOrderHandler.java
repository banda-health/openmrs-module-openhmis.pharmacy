package org.openmrs.web.taglib.fieldgen;

import org.openmrs.DrugOrder;

public class DrugOrderHandler extends AbstractFieldGenHandler implements FieldGenHandler {

	private String defaultUrl = "drugOrder.field";
	
	@Override
	public void run() {
		setUrl(defaultUrl);
		// the following lines are adapted from UserHandler:
		checkEmptyVal((DrugOrder) null);
		if (fieldGenTag != null) {
			Object initialValue = this.fieldGenTag.getVal();
			setParameter("initialValue", initialValue == null ? "" : initialValue);
		}
	}
}
