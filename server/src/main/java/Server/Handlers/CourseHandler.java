package Server.Handlers;

import WebScraper.AllCoursesScraper;
import WebScraper.Certificate.TrustAllCerts;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handles requests to view a list of CS courses, returning it in a JSON format.
 * In case of errors, such as if the data source is not accessible, it returns an appropriate
 * error response.
 */
public class CourseHandler implements Route {
  // static cache variables to improve loading speeds for the course bank (jjjakettt)
  private static List<List<String>> cachedCourses = null;
  private static long lastFetchTime = 0;
  private static final long CACHE_DURATION = 1000 * 60 * 60; // 1 hour cache duration
  /**
   * Handles the incoming request to view course data.
   *
   * @param request the Spark request object.
   * @param response the Spark response object.
   * @return A serialized JSON object containing the course data or an error message.
   */
  @Override
  public Object handle(Request request, Response response) {
    try {

      // Check if there's valid cached data (jjjakettt)
      long currentTime = System.currentTimeMillis();
      if (cachedCourses != null && (currentTime - lastFetchTime) < CACHE_DURATION) {
        return new CourseSuccessResponse(cachedCourses).serialize();
      }

      // Fetching courses if no cache
      String url = request.queryParams("url");
      if (url == null || url.isEmpty()) {
        url = "https://cs.brown.edu/courses/";
      }

      // calling helper class to disable SSL certification
      TrustAllCerts trust = new TrustAllCerts();
      Document document = Jsoup.connect(url).get();
      String html = document.html();

      AllCoursesScraper courseScraper = new AllCoursesScraper(html);
      List<List<String>> data = courseScraper.coursesToList();

      // Update cache (jjjakettt)
      cachedCourses = data;
      lastFetchTime = currentTime;

      return new CourseSuccessResponse(data).serialize();

    } catch (Exception e) {
      return new CourseHandler.ErrorResponse("error_bad_json",
          "Malformed JSON or parsing issue").serialize();
    }
  }

  /** A record representing a successful response containing course data. */
  public record CourseSuccessResponse(String response_type, List<List<String>> data) {

    /**
     * Constructs a success response for the course data request.
     *
     * @param data the course data to be returned.
     */
    public CourseSuccessResponse(List<List<String>> data) {
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
        JsonAdapter<CourseHandler.CourseSuccessResponse> adapter =
            moshi.adapter(CourseHandler.CourseSuccessResponse.class);
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
        JsonAdapter<CourseHandler.ErrorResponse> adapter =
            moshi.adapter(CourseHandler.ErrorResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        return "{\"result\": \"error\", \"error_code\": \"error_serialization\"}";
      }
    }
  }
}

