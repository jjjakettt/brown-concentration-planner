package Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CensusDataCreatorFromRow {

  private String[] headers;

  public CensusDataCreatorFromRow(String[] headers) {
    this.headers = headers;
  }

  public Map<String, String> create(List<String> row) throws FactoryFailureException {
    if (row.size() != headers.length) {
      throw new FactoryFailureException("Row length doesn't match headers", row);
    }

    Map<String, String> rowData = new HashMap<>();
    for (int i = 0; i < headers.length; i++) {
      rowData.put(headers[i].trim(), row.get(i).trim());
    }

    return rowData;
  }
}
