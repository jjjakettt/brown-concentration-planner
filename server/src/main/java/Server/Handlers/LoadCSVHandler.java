package Server.Handlers;

import Creators.TrivialCreator;
import Parser.Parser;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import datasource.sharedState.SharedState;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handles requests to load CSV data from a specified file path. This handler parses the CSV file
 * and stores the parsed data in shared state. In case of errors, such as a missing file path or an
 * inaccessible data source, it returns an appropriate error response.
 */
public class LoadCSVHandler implements Route {

  private final SharedState sharedState;

  /**
   * Constructs a LoadCSVHandler with shared state to store the parsed CSV data.
   *
   * @param sharedState the shared state that holds the parsed CSV data.
   */
  public LoadCSVHandler(SharedState sharedState) {
    this.sharedState = sharedState;
  }

  /**
   * Handles the incoming request to load a CSV file from a specified file path.
   *
   * @param request the Spark request object containing the file path query parameter.
   * @param response the Spark response object.
   * @return A serialized JSON object containing success or error information.
   */
  @Override
  public Object handle(Request request, Response response) {
    String filepath = request.queryParams("filepath");

    if (filepath == null || filepath.isEmpty()) {
      return new ErrorResponse("error_bad_request", "Filepath is missing or invalid").serialize();
    }

    try {
      Parser<List<String>> parser =
          new Parser<List<String>>(
              new FileReader(filepath),
              new TrivialCreator(),
              true); // Use default creator and headers

      // Parse the CSV data
      parser.parse();
      List<List<String>> data = parser.parsedContent;

      sharedState.setFilename(filepath);
      sharedState.setParsedCSV(data);

      return new LoadSuccessResponse(filepath).serialize();

    } catch (IOException e) {
      return new ErrorResponse("error_datasource", "Failed to access the data source").serialize();
    } catch (Exception e) {
      return new ErrorResponse("error_bad_json", "Malformed JSON or parsing issue").serialize();
    }
  }

  /** A record representing a successful response indicating the CSV file was loaded. */
  public record LoadSuccessResponse(String response_type, String filepath) {

    /**
     * Constructs a success response for the CSV load request.
     *
     * @param filepath the file path of the loaded CSV.
     */
    public LoadSuccessResponse(String filepath) {
      this("Success", filepath);
    }

    /**
     * Serializes the response to a JSON string.
     *
     * @return the serialized JSON string.
     */
    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<LoadCSVHandler.LoadSuccessResponse> adapter =
            moshi.adapter(LoadCSVHandler.LoadSuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        return "{\"result\": \"error\", \"error_code\": \"error_serialization\"}";
      }
    }
  }

  /** A record representing an error response. */
  public record ErrorResponse(String result, String error_code) {

    /**
     * Serializes the error response to a JSON string.
     *
     * @return the serialized JSON string.
     */
    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<LoadCSVHandler.ErrorResponse> adapter =
            moshi.adapter(LoadCSVHandler.ErrorResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        return "{\"result\": \"error\", \"error_code\": \"error_serialization\"}";
      }
    }
  }
}
