package org.openmrs.module.openhmis.pharmacy.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.module.openhmis.pharmacy.web.PharmacyWebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(PharmacyWebConstants.PHARMACY_PAGE)
public class PharmacyPageController {
	@RequestMapping(method = RequestMethod.GET)
	public void pharmacy(ModelMap model, HttpServletRequest request) {
		
	}
}
