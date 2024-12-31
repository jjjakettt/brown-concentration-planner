package Searcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Searcher {

  // Search by column index
  public List<List<String>> searchByIndex(
      List<List<String>> data, String searchValue, int columnIndex) {
    List<List<String>> result = new ArrayList<>();
    for (List<String> record : data) {
      String[] row = record.toString().split(",");
      if (columnIndex < row.length && row[columnIndex].contains(searchValue)) {
        //        System.out.println(String.join(", ", row));
        result.add(Collections.singletonList(String.join(", ", row)));
      }
    }
    return result;
  }

  // Search across all columns
  public List<List<String>> searchAllColumns(List<List<String>> data, String searchValue) {
    List<List<String>> result = new ArrayList<>();
    for (List<String> record : data) {
      // Assuming T.toString() returns values as comma-separated strings
      String[] row = record.toString().split(",");
      for (String entry : row) {
        if (entry.contains(searchValue)) {
          result.add(Collections.singletonList(String.join(", ", row)));
          break;
        }
      }
    }
    return result;
  }

  // Helper method to find column index by name
  public static int findColumnIndex(String[] headers, String columnName) {
    for (int i = 0; i < headers.length; i++) {
      if (headers[i].equalsIgnoreCase(columnName)) {
        return i;
      }
    }
    return -1;
  }
}
