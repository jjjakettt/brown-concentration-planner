package Searcher;

import Parser.*;
import java.util.ArrayList;
import java.util.List;

public class SearchHandler {

  public List<List<String>> handleSearch(
      List<List<String>> parsedData, String searchValue, String columnIdentifier) {
    try {

      // Parse the CSV data

      // If no column is specified, search all columns
      Searcher searcher = new Searcher();
      if (columnIdentifier == null || columnIdentifier.isEmpty()) {
        List<List<String>> matches = searcher.searchAllColumns(parsedData, searchValue);
        return matches;
      } else {
        // Check if columnIdentifier is a number (for column index) or a string (for column name)
        try {
          int columnIndex = Integer.parseInt(columnIdentifier);
          List<List<String>> matches = searcher.searchByIndex(parsedData, searchValue, columnIndex);
          return matches;
        } catch (NumberFormatException e) {
          // If it's not a number, assume it's a column name, but headers are not available
          String[] headers = parsedData.get(0).toArray(new String[0]);
          int columnIndex = Searcher.findColumnIndex(headers, columnIdentifier);
          List<List<String>> matches = searcher.searchByIndex(parsedData, searchValue, columnIndex);
          return matches;
        }
      }
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
    return new ArrayList<>();
  }
}
