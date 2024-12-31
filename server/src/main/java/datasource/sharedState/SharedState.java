package datasource.sharedState;

import java.util.List;

public class SharedState {
  private String filename;
  private List<List<String>> parsedCSV;

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public List<List<String>> getParsedCSV() {
    return parsedCSV;
  }

  public void setParsedCSV(List<List<String>> parsedCSV) {
    this.parsedCSV = parsedCSV;
  }
}
