package Server.Handlers;

import Searcher.SearchHandler;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import datasource.sharedState.SharedState;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handles requests to search for specific values in the parsed CSV data. This handler searches for
 * the value across the CSV data and returns matches in a JSON format. In case of missing or invalid
 * query parameters or other errors, it returns an appropriate error response.
 */
public class SearchCSVHandler implements Route {

  private final SharedState sharedState;

  /**
   * Constructs a SearchCSVHandler with shared state to access parsed CSV data for searching.
   *
   * @param sharedState the shared state that holds the parsed CSV data.
   */
  public SearchCSVHandler(SharedState sharedState) {
    this.sharedState = sharedState;
  }

  /**
   * Handles the incoming request to search for a value in the CSV data.
   *
   * @param request the Spark request object containing query parameters for search value and
   *     column.
   * @param response the Spark response object.
   * @return A serialized JSON object containing the search results or an error message.
   */
  @Override
  public Object handle(Request request, Response response) {
    String searchValue = request.queryParams("searchValue");
    String columnIdentifier = request.queryParams("columnIdentifier");

    if (searchValue == null || searchValue.isEmpty()) {
      return new ErrorResponse("error_bad_request", "Query parameter is missing or invalid")
          .serialize();
    }

    try {
      List<List<String>> data = sharedState.getParsedCSV();
      List<List<String>> matches;

      if (data == null) {
        return new ErrorResponse("error_datasource", "Data source is not loaded or accessible")
            .serialize();
      }

      // Logic for searching CSV (simple contains search)
      SearchHandler searchHandler = new SearchHandler();
      matches = searchHandler.handleSearch(data, searchValue, columnIdentifier);

      return new SearchSuccessResponse(
              "success", searchValue, matches, this.sharedState.getFilename())
          .serialize();

    } catch (Exception e) {
      return new ErrorResponse("error_bad_json", "Malformed JSON or parsing issue").serialize();
    }
  }

  /** A record representing a successful response containing search results. */
  public record SearchSuccessResponse(
      String response_type, String query, List<List<String>> match, String filename) {

    /**
     * Constructs a success response for the CSV search request.
     *
     * @param query the search query.
     * @param match the list of matched CSV rows.
     */
    public SearchSuccessResponse(String query, List<List<String>> match, String filename) {
      this("Success", query, match, filename);
    }

    /**
     * Serializes the response to a JSON string.
     *
     * @return the serialized JSON string.
     */
    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SearchCSVHandler.SearchSuccessResponse> adapter =
            moshi.adapter(SearchCSVHandler.SearchSuccessResponse.class);
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
        JsonAdapter<SearchCSVHandler.ErrorResponse> adapter =
            moshi.adapter(SearchCSVHandler.ErrorResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        return "{\"result\": \"error\", \"error_code\": \"error_serialization\"}";
      }
    }
  }
}
