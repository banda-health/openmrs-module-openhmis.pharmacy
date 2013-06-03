package org.openmrs.module.openhmis.pharmacy.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dbunit.dataset.NoSuchTableException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.GlobalProperty;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.inventory.api.IStockRoomTransactionDataService;
import org.openmrs.module.openhmis.inventory.api.model.StockRoomTransaction;
import org.openmrs.module.openhmis.pharmacy.api.util.ModuleConstants;
import org.openmrs.module.openhmis.workorder.api.IWorkOrderDataService;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class IPharmacyServiceTest extends BaseModuleContextSensitiveTest {
	private static final String WORKORDER_DATASET = TestConstants.BASE_DATASET_DIR + "WorkOrderTest.xml";
	IPharmacyService service;
	boolean inventoryAvailable = true;
	
	@Before
	public void before() throws Exception {
		executeDataSet(TestConstants.CORE_DATASET);
		executeDataSet(WORKORDER_DATASET);
		try {
			executeDataSet(TestConstants.BASE_DATASET_DIR + "ItemTest.xml");
			inventoryAvailable = true;
		}
		catch(NoSuchTableException e) {
			inventoryAvailable = false;
		}
		service = Context.getService(IPharmacyService.class);
	}
	
	private WorkOrder addBatchOfTwoTestDrugOrders() throws Exception {
		Drug testDrug;
		if (inventoryAvailable)
			testDrug = Context.getConceptService().getDrug(0);
		else
			testDrug = Context.getConceptService().getAllDrugs().get(0);
		Set<DrugOrder> batch = new HashSet<DrugOrder>(2);
		DrugOrder order1 = new DrugOrder();
		order1.setDrug(testDrug);
		order1.setConcept(order1.getDrug().getConcept());
		order1.setPatient(new Patient(1));
		order1.setOrderType(new OrderType(1));
		order1.setQuantity(10);
		DrugOrder order2 = order1.copy();
		batch.add(order1);
		batch.add(order2);
		return service.addDrugOrderBatch(batch);
	}
	
	/**
	 * @throws Exception 
	 * @see IPharmacyService#addDrugOrderBatch(Set)
	 * @verifies save DrugOrders
	 */
	@Test
	public void addDrugOrderBatch_shouldSaveDrugOrders() throws Exception {
		Integer originalNumberOfDrugOrders = Context.getOrderService().getOrders(DrugOrder.class, null, null, null, null, null, null).size();
		// Should save the 2 DrugOrders
		addBatchOfTwoTestDrugOrders();
		List<DrugOrder> drugOrders = Context.getOrderService().getOrders(DrugOrder.class, null, null, null, null, null, null);
		Assert.assertEquals(originalNumberOfDrugOrders + 2, drugOrders.size());	
	}

	/**
	 * @see IPharmacyService#addDrugOrderBatch(Set)
	 * @verifies save WorkOrders
	 */
	@Test
	public void addDrugOrderBatch_shouldSaveWorkOrders() throws Exception {
		IWorkOrderDataService workOrderService = Context.getService(IWorkOrderDataService.class); 
		Integer originalNumberOfWorkOrders = workOrderService.getAll().size();
		// Should create a WorkOrder for each DrugOrder, plus 1 to group them
		WorkOrder workOrder = addBatchOfTwoTestDrugOrders();
		List<WorkOrder> workOrders = workOrderService.getAll();
		Assert.assertEquals(originalNumberOfWorkOrders + 3, workOrders.size());
		Assert.assertEquals(2, workOrder.getWorkOrders().size());
	}

	/**
	 * @see IPharmacyService#addDrugOrderBatch(Set)
	 * @verifies save inventory transactions if service is available
	 */
	@Test
	public void addDrugOrderBatch_shouldSaveInventoryTransactionsIfServiceIsAvailable()
			throws Exception {
		if (inventoryAvailable) {		
			executeDataSet(TestConstants.BASE_DATASET_DIR + "StockRoomTest.xml");
		}
		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty(ModuleConstants.DISPENSARY_STOCKROOM_ID_PROPERTY, "1"));
		IStockRoomTransactionDataService txService = Context.getService(IStockRoomTransactionDataService.class); 
		Integer originalNumberOfTransactions = txService.getAll().size();
		// Should create a transaction for each order (2)
		addBatchOfTwoTestDrugOrders();
		List<StockRoomTransaction> transactions = txService.getAll();
		Assert.assertEquals(originalNumberOfTransactions + 2, transactions.size());
	}
}