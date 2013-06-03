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
