package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the data model as a list of transactions.
 * 
 * NOTE) Represents the Model in the MVC architecture pattern.
 */
public class ExpenseTrackerModel {

	private List<Transaction> transactions = new ArrayList<>();
	
	public ExpenseTrackerModel() {
		super();
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void addTransaction(Transaction t) {
		transactions.add(t);
	}

	public boolean removeTransaction(int transactionID) {
  	  // Perform input validation
  	  if ((transactionID < 0) || (transactionID > this.getTransactions().size() - 1)) {
  		  return false;
  	  }
  	  else {
  		  transactions.remove(transactionID);
  		  return true;
  	  }
	}

	public double computeTransactionsTotalCost() {
		double totalCost=0;
		for(Transaction t : transactions) {
			totalCost+=t.getAmount();
		}
		return totalCost;
	}
}
