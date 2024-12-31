package Parser;

import java.util.ArrayList;
import java.util.List;

public class FactoryFailureException extends Exception {
  final List<String> row;

  public FactoryFailureException(String message, List<String> row) {
    super(message);
    this.row = new ArrayList<>(row);
  }

  public List<String> getRow() {
    return row;
  }
}
