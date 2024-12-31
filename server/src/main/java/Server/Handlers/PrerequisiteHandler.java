package Server.Handlers;

import com.squareup.moshi.Moshi;
import WebScraper.Certificate.TrustAllCerts;
import spark.Request;
import spark.Response;
import spark.Route;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.List;

/**
 * Handles requests to view the prerequisites for a given course, returning it in a JSON format.
 * In case of errors, such as if the data source is not accessible, it returns an appropriate
 * error response.
 */
public class PrerequisiteHandler implements Route {
    /**
     * Handles the incoming request to view prerequisite data.
     *
     * @param request the Spark request object.
     * @param response the Spark response object.
     * @return A serialized JSON object containing the prerequisite data or an error message.
     */
    @Override
    public Object handle(Request request, Response response) {
        try {
            String courseCode = request.queryParams("courseCode");
            if (courseCode == null || courseCode.isEmpty()) {
                return new ErrorResponse("error_invalid_request", 
                    "Course code is required").serialize();
            }
            // Just get the prerequisite text
            String prereqText = getPrerequisiteText(courseCode);
            // If there are prerequisites, return them. If not, return empty list
            return new PrerequisiteResponse(prereqText != null, prereqText == null ?
                List.of() : List.of(prereqText)).serialize();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse("error_invalid_request", 
                "Could not process prerequisite check request: "
                    + e.getMessage()).serialize();
        }
    }
    
    // Method to get the prerequisite text
    private String getPrerequisiteText(String courseCode) {
        try {
            String url = String.format("https://cs.brown.edu/courses/info/%s/", 
                courseCode.toLowerCase());

            TrustAllCerts trust = new TrustAllCerts();
            Document doc = Jsoup.connect(url).get();
            
            Elements prereqElements = doc.select("p:contains(Prerequisites:)");
            if (prereqElements.isEmpty()) {
                return null;
            }
            
            String fullText = prereqElements.first().text();
            int startIndex = fullText.indexOf("Prerequisites:");
            if (startIndex == -1) {
                return null;
            }
            
            // Extract everything after "Prerequisites:"
            String prereqText = fullText.substring(startIndex
                + "Prerequisites:".length()).trim();
            
            // If prerequisites are "none" or empty, return null
            if (prereqText.isEmpty() || prereqText.toLowerCase().contains("none")) {
                return null;
            }
            
            return prereqText;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /** A record representing a response containing prerequisite data. */
    // Response records
    public record PrerequisiteResponse(
        String response_type,
        boolean prerequisites_met,
        List<String> missing_prerequisites
    ) {
        /**
         * Constructs a success response for the prerequisite data request.
         *
         * @param missing the prerequisite data to be returned.
         */
        public PrerequisiteResponse(boolean met, List<String> missing) {
            this("success", met, missing);
        }
        /**
         * Serializes the response to a JSON string.
         *
         * @return the serialized JSON string.
         */
        String serialize() {
            try {
                Moshi moshi = new Moshi.Builder().build();
                return moshi.adapter(PrerequisiteResponse.class).toJson(this);
            } catch (Exception e) {
                return "{\"response_type\": \"error\", \"error_code\": \"serialization_error\"}";
            }
        }
    }
    /** A record representing an error response containing prerequisite data. */
    private record ErrorResponse(String response_type, String error_code) {
        /**
         * Serializes the response to a JSON string.
         *
         * @return the serialized JSON string.
         */
        String serialize() {
            try {
                Moshi moshi = new Moshi.Builder().build();
                return moshi.adapter(ErrorResponse.class).toJson(this);
            } catch (Exception e) {
                return "{\"response_type\": \"error\", \"error_code\": \"serialization_error\"}";
            }
        }
    }
}