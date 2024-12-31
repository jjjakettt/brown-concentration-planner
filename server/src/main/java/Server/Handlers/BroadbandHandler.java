package Server.Handlers;

import Broadband.BroadbandEntry;
import Broadband.CacheProxyDatasource;
import Broadband.LocalDateTimeAdapter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

  private final CacheProxyDatasource datasource;
  private final LocalDateTimeAdapter localDateTimeAdapter;

  // Constructor initializes CacheProxyDatasource with a cache size
  public BroadbandHandler(int cacheSize) {
    this.datasource = new CacheProxyDatasource(cacheSize);
    this.localDateTimeAdapter = new LocalDateTimeAdapter(); // Instantiate the adapter
  }

  /**
   * Handles requests for broadband data, fetching data from the cache or the ACS API. This method
   * checks for required query parameters (state and county) and returns the broadband data or
   * appropriate error messages.
   *
   * @param request the incoming HTTP request.
   * @param response the outgoing HTTP response.
   * @return a map containing the result of the request, including success or error messages and
   *     data.
   */
  @Override
  public Object handle(Request request, Response response) {
    // Extract query parameters from the request
    String state = request.queryParams("state");
    String county = request.queryParams("county");

    Map<String, Object> responseMap = new HashMap<>();

    // Check if required parameters are present
    if (state == null || county == null) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("message", "Missing required query parameters: state, county.");
      response.status(400); // Set status to 400 Bad Request
      return responseMap;
    }

    // Get the current date and time
    LocalDateTime now = LocalDateTime.now();
    String dateTime = localDateTimeAdapter.toJson(now); // Format the dateTime

    try {
      // Generate a cache key using state and county
      String cacheKey = state + "," + county;

      // Fetch broadband data from the cache or ACS API using the CacheProxyDatasource
      BroadbandEntry broadbandEntry = datasource.getBroadbandData(cacheKey);

      // Check if broadbandEntry is successfully retrieved
      if (broadbandEntry == null) {
        responseMap.put("result", "error_datasource");
        responseMap.put("message", "No data found for the specified state and county.");
        response.status(404); // Set status to 404 Not Found
      } else {
        // Populate the response map with the retrieved data from the BroadbandEntry object
        responseMap.put("result", "success");
        responseMap.put("state", broadbandEntry.state);
        responseMap.put("county", broadbandEntry.county);
        responseMap.put("broadband_percentage", broadbandEntry.broadbandPercentage);
        responseMap.put("dateTime", dateTime);
        response.status(200); // Set status to 200 OK
      }
    } catch (Exception e) {
      responseMap.put("result", "error_internal");
      responseMap.put("message", "An unexpected error occurred: " + e.getMessage());
      response.status(500); // Set status to 500 Internal Server Error
      e.printStackTrace(); // Log the full stack trace for debugging
    }

    return responseMap;
  }
}
