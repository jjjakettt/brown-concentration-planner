package Parser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Parser<T> {
  /** TODO is this defensive enough? Feel free to edit any variable declarations */
  public Reader reader;

  public List<T> parsedContent;

  public CreatorFromRow<T> creator;

  public List<List<String>> errorRows;

  private boolean hasHeader;
  private String[] headers;

  /**
   * TODO feel free to modify the header and body of this constructor however you wish
   *
   * @param reader - a reader object
   */
  public Parser(Reader reader, CreatorFromRow<T> creator, boolean hasHeader) throws IOException {
    if (reader == null) {
      throw new NullPointerException();
    }
    this.reader = reader;
    this.parsedContent = new ArrayList<>();
    this.creator = creator;
    this.hasHeader = hasHeader;
    this.errorRows = new ArrayList<>();
  }

  /**
   * TODO feel free to modify this method to incorporate your design choices
   *
   * <p>// * @throws IOException when buffer reader fails to read-in a line
   */
  public void parse() throws IOException {
    String line;
    Pattern regexSplitCSVRow = Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

    BufferedReader readInBuffer =
        new BufferedReader(reader); // wraps around readers to improve efficiency when reading

    // if (hasHeader) {
    //  line = readInBuffer.readLine(); // read the header row and store it
    //  this.headers = regexSplitCSVRow.split(line);
    // }

    while ((line = readInBuffer.readLine()) != null) {
      String[] result = regexSplitCSVRow.split(line);
      List<String> lineToArr = Arrays.stream(result).toList();

      T record;
      try {
        record = creator.create(lineToArr);
        parsedContent.add(record);
      } catch (FactoryFailureException e) {

        System.err.println("Error parsing row: " + e.getMessage());
        errorRows.add(lineToArr); // Add the error-causing row to errorRows list
      }
    }
    readInBuffer.close();
  }

  // public String[] getHeaders() {
  //  return this.headers;
  // }
}
