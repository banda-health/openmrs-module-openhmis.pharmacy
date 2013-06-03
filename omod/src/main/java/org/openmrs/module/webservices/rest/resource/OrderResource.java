package org.openmrs.module.webservices.rest.resource;

import org.openmrs.DrugOrder;
import org.openmrs.Order;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.pharmacy.api.IPharmacyService;
import org.openmrs.module.openhmis.pharmacy.api.util.PharmacyWorkOrder;
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
			return PharmacyWorkOrder.getDrugOrder(newWorkOrder);
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
