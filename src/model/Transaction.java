package model;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Transaction {

  private double amount;
  private String category;
  private String timestamp;
  
  public static final String TIMESTAMP_DATE_FORMAT = "dd-MM-yyyy HH:mm";

  /**
   * Creates a new Transaction with the given amount and category.
   * 
   * @param amount The amount
   * @param category The category
   * @throws IllegalArgumentException if the amount or category is invalid
   */
  public Transaction(double amount, String category) {
	  if (! InputValidation.isValidAmount(amount)) {
		  throw new IllegalArgumentException("The amount must be greater than 0 and less than 1000.");
	  }
	  if (! InputValidation.isValidCategory(category)) {
		  throw new IllegalArgumentException("The categories must be one of the following: " + Arrays.toString(InputValidation.VALID_CATEGORIES) + ".");
	  }
	  this.amount = amount;
	  this.category = category;
	  this.timestamp = this.generateTimestamp();
  }
  
  /* package */ Transaction(double amount, String category, String timestamp) {
	  this.amount = amount;
	  this.category = category;
	  this.timestamp = timestamp;
  }

  public double getAmount() {
    return amount;
  }

  public String getCategory() {
    return category;
  }
  
  public String getTimestamp() {
    return timestamp;
  }

  private String generateTimestamp() {
    SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_DATE_FORMAT);  
    return sdf.format(new Date());
  }
}
