package org.openmrs.module.openhmis.pharmacy.inv;

import org.apache.log4j.Logger;
import org.openmrs.module.ModuleException;
import org.openmrs.module.ModuleFactory;

public class InventoryUtilFactory {
	private static final Logger log = Logger.getLogger(InventoryUtilFactory.class);
	private static final String INVENTORY_UTIL_CLASSNAME = "org.openmrs.module.openhmis.pharmacy.inv.InventoryUtil";
	private static IInventoryUtil instance;
	
	public static IInventoryUtil getInstance() {
		if (!ModuleFactory.isModuleStarted("openhmis.inventory"))
			throw new ModuleException("Inventory module is not available");
		if (instance == null)
			try {
				instance = (IInventoryUtil) Class.forName(INVENTORY_UTIL_CLASSNAME).newInstance();
			} catch (Throwable t) {
				log.error("Error loading util class " + INVENTORY_UTIL_CLASSNAME);
			}
		return instance;
	}
}