

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;

import controller.ExpenseTrackerController;
import model.CSVExporter;
import model.CSVImporter;
import model.ExpenseTrackerModel;
import model.InputValidation;
import model.Transaction;
import view.DataPanelView;
import view.ExpenseTrackerView;

public class ExpenseTrackerTest {
  public static final double[] EXPECTED_AMOUNT = { 50.0, 133.0, 55.0, 22.0 };
  public static final String[] EXPECTED_CATEGORY = {
		  InputValidation.VALID_CATEGORIES[0],
		  InputValidation.VALID_CATEGORIES[1],
		  InputValidation.VALID_CATEGORIES[1],
		  InputValidation.VALID_CATEGORIES[4]
  };
  public static final String TEST_CSV_FILE_NAME = "test/test_expenses.csv";
	
  // For unit testing
  private ExpenseTrackerModel model;
  // For end-to-end testing
  private ExpenseTrackerController controller;

  @Before
  public void setup() {
	model = new ExpenseTrackerModel();
	controller = new ExpenseTrackerController();
  }
  
  @Test
  public void testInitialConfiguration() {
    // There aren't any pre-conditions to be checked
    // The setup method called the constructors
    // Check the post-conditions
    assertEquals(0, model.getTransactions().size());
    assertEquals(0, model.computeTransactionsTotalCost(), 0.001);
  }
  
  private void checkTransaction(double expectedAmount, String expectedCategory, Transaction toBeCheckedTransaction) {
	    assertEquals(expectedAmount, toBeCheckedTransaction.getAmount(), 0.001);
	    assertEquals(expectedCategory, toBeCheckedTransaction.getCategory());
  }

  private void testAddTransactionHelper(double amount, String category) {
	    // Check the pre-conditions
	    assertEquals(0, model.getTransactions().size());
	    assertEquals(0, model.computeTransactionsTotalCost(), 0.001);
		
	    // Create a new transaction and add it
	    Transaction transaction = new Transaction(amount, category);
	    model.addTransaction(transaction);

	    // Check the post-conditions: 
	    // Verify that the transaction was added appropriately
	    java.util.List<Transaction> transactions = model.getTransactions();
	    assertEquals(1, transactions.size());
	    checkTransaction(amount, category, transactions.get(0));
	    assertEquals(amount, model.computeTransactionsTotalCost(), 0.001);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testTransactionInvalidAmount() {
	  // Perform setup and check pre-conditions
	  double invalidAmount = -1.0;
	  String validCategory = InputValidation.VALID_CATEGORIES[0];
	  assertFalse(InputValidation.isValidAmount(invalidAmount));
	  assertTrue(InputValidation.isValidCategory(validCategory));
	  // Call the unit under test
	  new Transaction(invalidAmount, validCategory);
	  // Check the post-condition (see the @Test annotation)
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testTransactionInvalidCategory() {
	  // Perform setup and check pre-conditions
	  double validAmount = 1.0;
	  String invalidCategory = "Books";
	  assertTrue(InputValidation.isValidAmount(validAmount));
	  assertFalse(InputValidation.isValidCategory(invalidCategory));
	  // Call the unit under test
	  new Transaction(validAmount, invalidCategory);
	  // Check the post-condition (see the @Test annotation)
  } 
  
  @Test
  public void testAddTransaction() {
	  double amount = 100.0;
	  String category = "Food";
	  this.testAddTransactionHelper(amount, category);
  }
  
  @Test
  public void testRemoveTransactionInvalidIDLow() {
	  // Perform setup and check preconditions
	  int invalidID = -1;
	  assertTrue(invalidID < 0);
	  // Call the unit under test
	  boolean removed = model.removeTransaction(invalidID);
	  // Check the post-conditions
	  assertFalse(removed);
	  assertEquals(0, model.getTransactions().size());
	  assertEquals(0, model.computeTransactionsTotalCost(), 0.001);
  }
  
  @Test
  public void testRemoveTransactionInvalidIDHigh() {
	  // Perform setup and check preconditions
	  int invalidID = model.getTransactions().size() + 1;
	  assertTrue(invalidID > 0);
	  // Call the unit under test
	  boolean removed = model.removeTransaction(invalidID);
	  // Check the post-conditions
	  assertFalse(removed);
	  assertEquals(0, model.getTransactions().size());
	  assertEquals(0, model.computeTransactionsTotalCost(), 0.001);
  }
  
  @Test
  public void testRemoveTransaction() {
	  // Initialize: Add a new transaction
	  double amount = 100.0;
	  String category = "Food";
	  this.testAddTransactionHelper(amount, category);
	  // Remove that transaction
	  model.removeTransaction(0);
	  // Check the post-conditions
	  assertEquals(0, model.getTransactions().size());
	  assertEquals(0, model.computeTransactionsTotalCost(), 0.001);
  }
  
  @Test
  public void testExportImport() {
	  // Also tests the Transaction constructor with package visibility
	  //
	  // Perform setup and check pre-conditions
	  for (int i = 0; i < EXPECTED_AMOUNT.length; i++) {
		  model.addTransaction(new Transaction(EXPECTED_AMOUNT[i], EXPECTED_CATEGORY[i]));
	  }
	  List<Transaction> transactionsList = model.getTransactions();
	  assertEquals(4, transactionsList.size());
	  int expectedTotalCost = 0;
	  for (int j = 0; j < EXPECTED_AMOUNT.length; j++) {
		  Transaction currentTransaction = transactionsList.get(j);
		  expectedTotalCost += currentTransaction.getAmount();
		  checkTransaction(EXPECTED_AMOUNT[j], EXPECTED_CATEGORY[j], currentTransaction);
	  }
	  assertEquals(expectedTotalCost, model.computeTransactionsTotalCost(), 0.01);
	  CSVExporter exporter = new CSVExporter();
	  String exporterResult = exporter.exportTransactions(model.getTransactions(), TEST_CSV_FILE_NAME);
	  assertNull(exporterResult);
	  // Call the unit under test
	  CSVImporter importer = new CSVImporter();
	  List<Transaction> importedTransactions = null;
	  try {
		  importedTransactions = importer.importTransactions(TEST_CSV_FILE_NAME);
	  }
	  catch (IOException ioe) {
		  fail(ioe.getMessage());
	  }
	  // Check the postconditions
	  assertEquals(4, importedTransactions.size());
	  expectedTotalCost = 0;
	  for (int j = 0; j < transactionsList.size(); j++) {
		  Transaction exportedTransaction = transactionsList.get(j);
		  Transaction importedTransaction = importedTransactions.get(j);
		  expectedTotalCost += importedTransaction.getAmount();
		  checkTransaction(exportedTransaction.getAmount(), exportedTransaction.getCategory(), importedTransaction);
	  }
	  assertEquals(expectedTotalCost, model.computeTransactionsTotalCost(), 0.01);
  }
  
  @Test
  public void testAddTransactionE2E() {
	  // Perform initialization and check the preconditions
	  double newAmount = 44.0;
	  String newCategory = "Other";
	  DataPanelView view = controller.getView().getDataPanelView();
	  view.setAmount("" + newAmount);
	  view.setCategory(newCategory);
	  assertEquals(1, view.getTransactionsTableRowCount());
	  // Call the unit under test: Add the new transaction
	  view.getAddTransactionBtn().doClick();
	  // Check the post-conditions
	  assertEquals(2, view.getTransactionsTableRowCount());
	  assertEquals(newAmount, view.getTransactionsTableValueAt(0, 1));
	  assertEquals(newCategory, view.getTransactionsTableValueAt(0, 2));
	  assertEquals(newAmount, view.getTransactionsTableValueAt(1, 3));
  }
}
