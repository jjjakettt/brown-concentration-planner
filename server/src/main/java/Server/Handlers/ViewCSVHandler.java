package Server.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import datasource.sharedState.SharedState;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handles requests to view parsed CSV data stored in shared state. This handler retrieves CSV data
 * from the shared state and returns it in a JSON format. In case of errors, such as if the data
 * source is not accessible, it returns an appropriate error response.
 */
public class ViewCSVHandler implements Route {

  private final SharedState sharedState;

  /**
   * Constructs a ViewCSVHandler with shared state to access parsed CSV data.
   *
   * @param sharedState the shared state that holds the parsed CSV data.
   */
  public ViewCSVHandler(SharedState sharedState) {
    this.sharedState = sharedState;
  }

  /**
   * Handles the incoming request to view CSV data.
   *
   * @param request the Spark request object.
   * @param response the Spark response object.
   * @return A serialized JSON object containing the CSV data or an error message.
   */
  @Override
  public Object handle(Request request, Response response) {
    try {
      List<List<String>> data = sharedState.getParsedCSV();

      if (data == null) {
        return new ErrorResponse("error_datasource", "Data source is not loaded or accessible")
            .serialize();
      }

      return new ViewSuccessResponse(data).serialize();

    } catch (Exception e) {
      return new ErrorResponse("error_bad_json", "Malformed JSON or parsing issue").serialize();
    }
  }

  /** A record representing a successful response containing CSV data. */
  public record ViewSuccessResponse(String response_type, List<List<String>> data) {

    /**
     * Constructs a success response for the CSV data request.
     *
     * @param data the CSV data to be returned.
     */
    public ViewSuccessResponse(List<List<String>> data) {
      this("Success", data);
    }

    /**
     * Serializes the response to a JSON string.
     *
     * @return the serialized JSON string.
     */
    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ViewCSVHandler.ViewSuccessResponse> adapter =
            moshi.adapter(ViewCSVHandler.ViewSuccessResponse.class);
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
        JsonAdapter<ViewCSVHandler.ErrorResponse> adapter =
            moshi.adapter(ViewCSVHandler.ErrorResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        return "{\"result\": \"error\", \"error_code\": \"error_serialization\"}";
      }
    }
  }
}
