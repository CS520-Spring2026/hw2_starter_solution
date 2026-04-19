package model;
import java.util.List;

/**
 * Strategy interface for exporting transactions to various formats.
 */
public interface TransactionExporter {

  /**
   * Export the provided transactions to the target identified by filename.
   * @param txns list of transactions to export
   * @param filename destination filename
   * @return null on success, non-null error message on failure
   */
  String exportTransactions(List<Transaction> txns, String filename);

}