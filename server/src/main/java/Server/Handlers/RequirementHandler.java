package Server.Handlers;
import WebScraper.BannerScraper;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handles requests to view the requirements for a concentration at Brown, returning it in a JSON
 * format. In case of errors, such as if the data source is not accessible, it returns an
 * appropriate error response.
 */
public class RequirementHandler implements Route {
  /**
   * Handles the incoming request to view concentration requirements.
   *
   * @param request the Spark request object.
   * @param response the Spark response object.
   * @return A serialized JSON object containing the requirement data or an error message.
   */
  @Override
  public Object handle(Request request, Response response) {
    try {
      String url = request.queryParams("url");
      if (url == null || url.isEmpty()) {
        url = "https://bulletin.brown.edu/the-college/concentrations/comp/";
      }
      Document document = Jsoup.connect(url).get();
      String html = document.html();
      BannerScraper bannerScraper = new BannerScraper(html);
      List<List<String>> data = bannerScraper.requirementsToList();
      return new RequirementSuccessResponse(data).serialize();
    } catch (Exception e) {
      return new ErrorResponse("error_bad_json",
          "Malformed JSON or parsing issue").serialize();
    }
  }

  /** A record representing a successful response containing concentration requirements. */
  public record RequirementSuccessResponse(String response_type, List<List<String>> data) {

    /**
     * Constructs a success response for the concentration requirements request.
     *
     * @param data the requirements to be returned.
     */
    public RequirementSuccessResponse(List<List<String>> data) {
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
        JsonAdapter<RequirementHandler.RequirementSuccessResponse> adapter =
            moshi.adapter(RequirementHandler.RequirementSuccessResponse.class);
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
        JsonAdapter<RequirementHandler.ErrorResponse> adapter =
            moshi.adapter(RequirementHandler.ErrorResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        return "{\"result\": \"error\", \"error_code\": \"error_serialization\"}";
      }
    }
  }
}