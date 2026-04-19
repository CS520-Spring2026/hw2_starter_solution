package model;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * CSV (Comma Separated Value) implementation of {@link TransactionExporter}.
 */
public class CSVExporter implements TransactionExporter, CSVConstants {
  
  @Override
  public String exportTransactions(List<Transaction> txns, String filename) {
    if (txns == null) return TRANSACTION_LIST_ERROR_MESSAGE;
    if (!InputValidation.isValidFilename(filename)) return FILENAME_ERROR_MESSAGE;

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
      bw.write(CSV_HEADERS);
      bw.newLine();
      for (Transaction t : txns) {
        String line = String.format("%s" + COMMA_SEPARATOR + "%s" + COMMA_SEPARATOR + "%s", Double.toString(t.getAmount()), t.getCategory(), t.getTimestamp());
        bw.write(line);
        bw.newLine();
      }
      bw.flush();
      return null;
    } catch (IOException e) {
      return e.getMessage();
    }
  }

}