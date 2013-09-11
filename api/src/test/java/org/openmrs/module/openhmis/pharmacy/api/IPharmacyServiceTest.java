package org.openmrs.module.openhmis.pharmacy.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.workorder.api.IWorkOrderDataService;
import org.openmrs.module.openhmis.workorder.api.model.WorkOrder;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IPharmacyServiceTest extends BaseModuleContextSensitiveTest {
	private static final String WORKORDER_DATASET = TestConstants.BASE_DATASET_DIR + "WorkOrderTest.xml";

	private IPharmacyService service;
	private IWorkOrderDataService workOrderService;

	@Before
	public void before() throws Exception {
		executeDataSet(TestConstants.CORE_DATASET);
		executeDataSet(WORKORDER_DATASET);

		service = Context.getService(IPharmacyService.class);
		workOrderService = Context.getService(IWorkOrderDataService.class);
	}
	
	private WorkOrder addBatchOfTwoTestDrugOrders() throws Exception {
		Drug testDrug;
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
		Integer originalNumberOfWorkOrders = workOrderService.getAll().size();
		// getAll will only return the single, top-level grouping work order
		WorkOrder workOrder = addBatchOfTwoTestDrugOrders();
		List<WorkOrder> workOrders = workOrderService.getAll();
		Assert.assertEquals(originalNumberOfWorkOrders + 1, workOrders.size());
		Assert.assertEquals(2, workOrder.getWorkOrders().size());
	}

//	/**
//	 * @see IPharmacyService#addDrugOrderBatch(Set)
//	 * @verifies save inventory transactions if service is available
//	 */
//	@Test
//	public void addDrugOrderBatch_shouldSaveInventoryTransactionsIfServiceIsAvailable()
//			throws Exception {
//		if (inventoryAvailable) {		
//			executeDataSet(TestConstants.BASE_DATASET_DIR + "StockRoomTest.xml");
//		}
//		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty(ModuleConstants.DISPENSARY_STOCKROOM_ID_PROPERTY, "1"));
//		IStockRoomTransactionDataService txService = Context.getService(IStockRoomTransactionDataService.class); 
//		Integer originalNumberOfTransactions = txService.getAll().size();
//		// Should create a transaction for each order (2)
//		addBatchOfTwoTestDrugOrders();
//		List<StockRoomTransaction> transactions = txService.getAll();
//		Assert.assertEquals(originalNumberOfTransactions + 2, transactions.size());
//	}
}