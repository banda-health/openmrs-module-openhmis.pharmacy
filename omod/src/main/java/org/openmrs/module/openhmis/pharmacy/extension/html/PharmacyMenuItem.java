package org.openmrs.module.openhmis.pharmacy.extension.html;

import org.openmrs.module.openhmis.commons.web.WebConstants;
import org.openmrs.module.openhmis.pharmacy.web.PharmacyWebConstants;
import org.openmrs.module.web.extension.LinkExt;

public class PharmacyMenuItem extends LinkExt {

	@Override
	public MEDIA_TYPE getMediaType() {
		return MEDIA_TYPE.html;
	}
	
	@Override
	public String getLabel() {
		return "openhmis.pharmacy.menuItem";
	}

	@Override
	public String getUrl() {
		return WebConstants.formUrl(PharmacyWebConstants.PHARMACY_PAGE);
	}

	@Override
	public String getRequiredPrivilege() {
		return "Manage Pharmacy";
	}

}
