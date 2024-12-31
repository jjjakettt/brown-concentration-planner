package Server.Handlers;


import Server.Handlers.LoadCSVHandler.ErrorResponse;
import WebScraper.BannerScraper;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import datasource.sharedState.SharedState;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handles requests to view parsed CSV data stored in shared state. This handler retrieves CSV data
 * from the shared state and returns it in a JSON format. In case of errors, such as if the data
 * source is not accessible, it returns an appropriate error response.
 */
public class RequirementHandler implements Route {
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
      String url = request.queryParams("url");
      if (url == null || url.isEmpty()) {
        url = "https://bulletin.brown.edu/the-college/concentrations/comp/";
      //  url = "https://bulletin.brown.edu/the-college/concentrations/econ/";
//        url = "https://bulletin.brown.edu/the-college/concentrations/engl/";
//        url = "https://bulletin.brown.edu/the-college/concentrations/visa/";

      }
      Document document = Jsoup.connect(url).get();
      String html = document.html();
      //String html = "<table class=\"sc_courselist\" width=\"100%\"><colgroup><col class=\"codecol\"><col class=\"titlecol\"><col align=\"char\" char=\".\" class=\"hourscol\"></colgroup><tbody><tr class=\"even areaheader firstrow\"><td colspan=\"2\"><span class=\"courselistcomment areaheader\">Prerequisites (0-3 courses)</span></td><td class=\"hourscol\"></td></tr> <tr class=\"odd\"><td colspan=\"2\"><span class=\"courselistcomment\">Calculus prerequisite: students must complete or place out of second semester calculus.</span></td><td class=\"hourscol\"></td></tr> <tr class=\"even\"><td class=\"codecol\"><div style=\"margin-left: 20px;\"><a data-code=\"MATH 0100\" href=\"/search/?P=MATH%200100\" title=\"MATH&nbsp;0100\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'MATH 0100');\">MATH&nbsp;0100</a></div></td><td>Single Variable Calculus, Part II</td><td class=\"hourscol\"></td></tr> <tr class=\"orclass even\"><td class=\"codecol orclass\"><div style=\"margin-left: 20px;\">or&nbsp;<a data-code=\"MATH 0170\" href=\"/search/?P=MATH%200170\" title=\"MATH&nbsp;0170\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'MATH 0170');\">MATH&nbsp;0170</a></div></td><td colspan=\"2\"> Single Variable Calculus, Part II (Accelerated)</td></tr> <tr class=\"orclass even\"><td class=\"codecol orclass\"><div style=\"margin-left: 20px;\">or&nbsp;<a data-code=\"MATH 0190\" href=\"/search/?P=MATH%200190\" title=\"MATH&nbsp;0190\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'MATH 0190');\">MATH&nbsp;0190</a></div></td><td colspan=\"2\"> Single Variable Calculus, Part II (Physics/Engineering)</td></tr> <tr class=\"odd areaheader\"><td colspan=\"2\"><span class=\"courselistcomment areaheader\">Concentration Requirements</span></td><td class=\"hourscol\"></td></tr> <tr class=\"even areaheader\"><td colspan=\"2\"><span class=\"courselistcomment areaheader\">Core-Computer Science:</span></td><td class=\"hourscol\"></td></tr> <tr class=\"odd\"><td colspan=\"2\"><span class=\"courselistcomment\">Select one of the following introductory course Series:</span></td><td class=\"hourscol\">2</td></tr> <tr class=\"even\"><td colspan=\"2\"><div style=\"margin-left: 20px;\"><span class=\"courselistcomment commentindent\">Series A</span></div></td><td class=\"hourscol\"></td></tr> <tr class=\"odd\"><td class=\"codecol\"><div style=\"margin-left: 20px;\"><a data-code=\"CSCI 0150\" href=\"/search/?P=CSCI%200150\" title=\"CSCI&nbsp;0150\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 0150');\">CSCI&nbsp;0150</a><br><span style=\"margin-left:20px;\" class=\"blockindent\">&amp;&nbsp;<a data-code=\"CSCI 0200\" href=\"/search/?P=CSCI%200200\" title=\"CSCI&nbsp;0200\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 0200');\">CSCI&nbsp;0200</a></span></div></td><td>Introduction to Object-Oriented Programming and Computer Science<br><span style=\"margin-left:20px;\" class=\"blockindent\">and Program Design with Data Structures and Algorithms</span></td><td class=\"hourscol\"></td></tr> <tr class=\"even\"><td colspan=\"2\"><div style=\"margin-left: 20px;\"><span class=\"courselistcomment commentindent\">Series B</span></div></td><td class=\"hourscol\"></td></tr> <tr class=\"odd\"><td class=\"codecol\"><div style=\"margin-left: 20px;\"><a data-code=\"CSCI 0170\" href=\"/search/?P=CSCI%200170\" title=\"CSCI&nbsp;0170\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 0170');\">CSCI&nbsp;0170</a><br><span style=\"margin-left:20px;\" class=\"blockindent\">&amp;&nbsp;<a data-code=\"CSCI 0200\" href=\"/search/?P=CSCI%200200\" title=\"CSCI&nbsp;0200\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 0200');\">CSCI&nbsp;0200</a></span></div></td><td>Computer Science: An Integrated Introduction<br><span style=\"margin-left:20px;\" class=\"blockindent\">and Program Design with Data Structures and Algorithms</span></td><td class=\"hourscol\"></td></tr> <tr class=\"even\"><td colspan=\"2\"><div style=\"margin-left: 20px;\"><span class=\"courselistcomment commentindent\">Series C</span></div></td><td class=\"hourscol\"></td></tr> <tr class=\"odd\"><td class=\"codecol\"><div style=\"margin-left: 20px;\"><a data-code=\"CSCI 0190\" href=\"/search/?P=CSCI%200190\" title=\"CSCI&nbsp;0190\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 0190');\">CSCI&nbsp;0190</a></div></td><td>Accelerated Introduction to Computer Science</td><td class=\"hourscol\"></td></tr> <tr class=\"even\"><td colspan=\"2\"><div style=\"margin-left: 20px;\"><span class=\"courselistcomment commentindent\">AND</span></div></td><td class=\"hourscol\"></td></tr> <tr class=\"odd\"><td colspan=\"2\"><div style=\"margin-left: 20px;\"><span class=\"courselistcomment commentindent\">an additional CS course not otherwise used to satisfy a concentration requirement; this course may be <a data-code=\"CSCI 0200\" href=\"/search/?P=CSCI%200200\" title=\"CSCI&nbsp;0200\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 0200');\">CSCI&nbsp;0200</a>, a Foundations course, or a 1000-level course. </span></div></td><td class=\"hourscol\"></td></tr> <tr class=\"even\"><td colspan=\"2\"><div style=\"margin-left: 20px;\"><span class=\"courselistcomment commentindent\">Series D</span> <sup>1</sup></div></td><td class=\"hourscol\"></td></tr> <tr class=\"odd\"><td class=\"codecol\"><div style=\"margin-left: 20px;\"><a data-code=\"CSCI 0111\" href=\"/search/?P=CSCI%200111\" title=\"CSCI&nbsp;0111\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 0111');\">CSCI&nbsp;0111</a><br><span style=\"margin-left:20px;\" class=\"blockindent\">&amp;&nbsp;<a data-code=\"CSCI 0112\" href=\"/search/?P=CSCI%200112\" title=\"CSCI&nbsp;0112\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 0112');\">CSCI&nbsp;0112</a></span><br><span style=\"margin-left:20px;\" class=\"blockindent\">&amp;&nbsp;<a data-code=\"CSCI 0200\" href=\"/search/?P=CSCI%200200\" title=\"CSCI&nbsp;0200\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 0200');\">CSCI&nbsp;0200</a></span></div></td><td>Computing Foundations: Data<br><span style=\"margin-left:20px;\" class=\"blockindent\">and Computing Foundations: Program Organization</span><br><span style=\"margin-left:20px;\" class=\"blockindent\">and Program Design with Data Structures and Algorithms</span></td><td class=\"hourscol\"></td></tr> <tr class=\"even areaheader\"><td colspan=\"2\"><span class=\"courselistcomment areaheader\">Introductory Math Foundations </span></td><td class=\"hourscol\">1</td></tr> <tr class=\"odd\"><td class=\"codecol\"><div style=\"margin-left: 20px;\"><a data-code=\"CSCI 0220\" href=\"/search/?P=CSCI%200220\" title=\"CSCI&nbsp;0220\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 0220');\">CSCI&nbsp;0220</a></div></td><td>Introduction to Discrete Structures and Probability</td><td class=\"hourscol\"></td></tr> <tr class=\"orclass odd\"><td class=\"codecol orclass\"><div style=\"margin-left: 20px;\">or&nbsp;<a data-code=\"APMA 1650\" href=\"/search/?P=APMA%201650\" title=\"APMA&nbsp;1650\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'APMA 1650');\">APMA&nbsp;1650</a></div></td><td colspan=\"2\"> Statistical Inference I</td></tr> <tr class=\"orclass odd\"><td class=\"codecol orclass\"><div style=\"margin-left: 20px;\">or&nbsp;<a data-code=\"CSCI 1450\" href=\"/search/?P=CSCI%201450\" title=\"CSCI&nbsp;1450\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 1450');\">CSCI&nbsp;1450</a></div></td><td colspan=\"2\"> Advanced Introduction to Probability for Computing and Data Science</td></tr> <tr class=\"orclass odd\"><td class=\"codecol orclass\"><div style=\"margin-left: 20px;\">or&nbsp;<a data-code=\"MATH 1530\" href=\"/search/?P=MATH%201530\" title=\"MATH&nbsp;1530\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'MATH 1530');\">MATH&nbsp;1530</a></div></td><td colspan=\"2\"> Abstract Algebra</td></tr> <tr class=\"even areaheader\"><td colspan=\"2\"><span class=\"courselistcomment areaheader\">Foundations Courses</span></td><td class=\"hourscol\"></td></tr> <tr class=\"odd\"><td colspan=\"2\"><span class=\"courselistcomment\">ScB students must take one course from each foundation area. </span></td><td class=\"hourscol\"></td></tr> <tr class=\"even areaheader\"><td colspan=\"2\"><span class=\"courselistcomment areaheader\">Foundations Areas</span></td><td class=\"hourscol\"></td></tr> <tr class=\"odd\"><td colspan=\"2\"><span class=\"courselistcomment\">a. Algorithms/Theory Foundations (Choose one)</span></td><td class=\"hourscol\">1</td></tr> <tr class=\"even\"><td class=\"codecol\"><div style=\"margin-left: 20px;\"><span class=\"courselistcomment commentindent\">CSCI 0500</span></div></td><td><div><span class=\"courselistcomment commentindent\"> Data Structures, Algorithms, and Intractability: An Introduction</span></div></td><td class=\"hourscol\"></td></tr> <tr class=\"odd\"><td class=\"codecol\"><div style=\"margin-left: 20px;\"><a data-code=\"CSCI 1010\" href=\"/search/?P=CSCI%201010\" title=\"CSCI&nbsp;1010\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 1010');\">CSCI&nbsp;1010</a></div></td><td>Theory of Computation</td><td class=\"hourscol\"></td></tr> <tr class=\"even\"><td class=\"codecol\"><div style=\"margin-left: 20px;\"><a data-code=\"CSCI 1550\" href=\"/search/?P=CSCI%201550\" title=\"CSCI&nbsp;1550\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 1550');\">CSCI&nbsp;1550</a></div></td><td>Probabilistic Methods in Computer Science </td><td class=\"hourscol\"></td></tr> <tr class=\"odd\"><td class=\"codecol\"><div style=\"margin-left: 20px;\"><a data-code=\"CSCI 1570\" href=\"/search/?P=CSCI%201570\" title=\"CSCI&nbsp;1570\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 1570');\">CSCI&nbsp;1570</a></div></td><td>Design and Analysis of Algorithms</td><td class=\"hourscol\"></td></tr> <tr class=\"even\"><td colspan=\"2\"><span class=\"courselistcomment\">b. AI/Machine Learning/Data Science Foundations </span></td><td class=\"hourscol\">1</td></tr> <tr class=\"odd\"><td class=\"codecol\"><div style=\"margin-left: 20px;\"><a data-code=\"CSCI 1410\" href=\"/search/?P=CSCI%201410\" title=\"CSCI&nbsp;1410\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 1410');\">CSCI&nbsp;1410</a></div></td><td>Artificial Intelligence</td><td class=\"hourscol\"></td></tr> <tr class=\"orclass odd\"><td class=\"codecol orclass\"><div style=\"margin-left: 20px;\">or&nbsp;<a data-code=\"CSCI 1420\" href=\"/search/?P=CSCI%201420\" title=\"CSCI&nbsp;1420\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 1420');\">CSCI&nbsp;1420</a></div></td><td colspan=\"2\"> Machine Learning</td></tr> <tr class=\"orclass odd\"><td class=\"codecol orclass\"><div style=\"margin-left: 20px;\">or&nbsp;<a data-code=\"CSCI 1430\" href=\"/search/?P=CSCI%201430\" title=\"CSCI&nbsp;1430\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 1430');\">CSCI&nbsp;1430</a></div></td><td colspan=\"2\"> Computer Vision</td></tr> <tr class=\"orclass odd\"><td class=\"codecol orclass\"><div style=\"margin-left: 20px;\">or&nbsp;<a data-code=\"CSCI 1460\" href=\"/search/?P=CSCI%201460\" title=\"CSCI&nbsp;1460\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 1460');\">CSCI&nbsp;1460</a></div></td><td colspan=\"2\"> Computational Linguistics</td></tr> <tr class=\"orclass odd\"><td class=\"codecol orclass\"><div style=\"margin-left: 20px;\">or&nbsp;<a data-code=\"CSCI 1470\" href=\"/search/?P=CSCI%201470\" title=\"CSCI&nbsp;1470\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 1470');\">CSCI&nbsp;1470</a></div></td><td colspan=\"2\"> Deep Learning</td></tr> <tr class=\"orclass odd\"><td class=\"codecol orclass\"><div style=\"margin-left: 20px;\">or&nbsp;<a data-code=\"CSCI 1951A\" href=\"/search/?P=CSCI%201951A\" title=\"CSCI&nbsp;1951A\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 1951A');\">CSCI&nbsp;1951A</a></div></td><td colspan=\"2\"> Data Science</td></tr> <tr class=\"even\"><td colspan=\"2\"><span class=\"courselistcomment\">c. Systems Foundations</span></td><td class=\"hourscol\">1</td></tr> <tr class=\"odd\"><td class=\"codecol\"><div style=\"margin-left: 20px;\"><a data-code=\"CSCI 0300\" href=\"/search/?P=CSCI%200300\" title=\"CSCI&nbsp;0300\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 0300');\">CSCI&nbsp;0300</a></div></td><td>Fundamentals of Computer Systems</td><td class=\"hourscol\"></td></tr> <tr class=\"orclass odd\"><td class=\"codecol orclass\"><div style=\"margin-left: 20px;\">or&nbsp;<a data-code=\"CSCI 0330\" href=\"/search/?P=CSCI%200330\" title=\"CSCI&nbsp;0330\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 0330');\">CSCI&nbsp;0330</a></div></td><td colspan=\"2\"> Introduction to Computer Systems</td></tr> <tr class=\"even areaheader\"><td colspan=\"2\"><span class=\"courselistcomment areaheader\">CSCI Electives </span></td><td class=\"hourscol\">5</td></tr> <tr class=\"odd\"><td colspan=\"2\"><div style=\"margin-left: 20px;\"><span class=\"courselistcomment commentindent\">Five CSCI courses at the 1000 level</span> <sup>2</sup></div></td><td class=\"hourscol\"></td></tr> <tr class=\"even areaheader\"><td colspan=\"2\"><span class=\"courselistcomment areaheader\">Four Additional Electives.  These can include:</span></td><td class=\"hourscol\">4</td></tr> <tr class=\"odd\"><td class=\"codecol\"><div style=\"margin-left: 20px;\"><a data-code=\"CSCI 0320\" href=\"/search/?P=CSCI%200320\" title=\"CSCI&nbsp;0320\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'CSCI 0320');\">CSCI&nbsp;0320</a></div></td><td>Introduction to Software Engineering</td><td class=\"hourscol\"></td></tr> <tr class=\"even\"><td colspan=\"2\"><div style=\"margin-left: 20px;\"><span class=\"courselistcomment commentindent\">1000-level and 2000-level CSCI courses (no more than three arts/policy/humanities courses)</span></div></td><td class=\"hourscol\"></td></tr> <tr class=\"odd\"><td colspan=\"2\"><div style=\"margin-left: 20px;\"><span class=\"courselistcomment commentindent\">Linear algebra (<a data-code=\"MATH 0520\" href=\"/search/?P=MATH%200520\" title=\"MATH&nbsp;0520\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'MATH 0520');\">MATH&nbsp;0520</a>, <a data-code=\"MATH 0540\" href=\"/search/?P=MATH%200540\" title=\"MATH&nbsp;0540\" class=\"bubblelink code\" onclick=\"return showCourse(this, 'MATH 0540');\">MATH&nbsp;0540</a>, or APMA 0260) </span></div></td><td class=\"hourscol\"></td></tr> <tr class=\"even\"><td colspan=\"2\"><div style=\"margin-left: 20px;\"><span class=\"courselistcomment commentindent\">Approved 1000-level courses outside of CS (see the concentration handbook for the current list)</span></div></td><td class=\"hourscol\"></td></tr> <tr class=\"odd areaheader\"><td colspan=\"2\"><span class=\"courselistcomment areaheader\">Capstone</span></td><td class=\"hourscol\"></td></tr> <tr class=\"even lastrow\"><td colspan=\"2\"><span class=\"courselistcomment\">A capstone taken in the senior year (from the list of approved capstone courses in the concentration handbook). The capstone may also be used to satisfy another requirement.</span></td><td class=\"hourscol\"></td></tr> <tr class=\"listsum\"><td colspan=\"2\">Total Credits</td><td class=\"hourscol\">15</td></tr></tbody></table>";
      BannerScraper bannerScraper = new BannerScraper(html);
      List<List<String>> data = bannerScraper.requirementsToList();
      return new RequirementSuccessResponse(data).serialize();

    } catch (Exception e) {
      return new ErrorResponse("error_bad_json", "Malformed JSON or parsing issue").serialize();
    }
  }

  /** A record representing a successful response containing CSV data. */
  public record RequirementSuccessResponse(String response_type, List<List<String>> data) {

    /**
     * Constructs a success response for the CSV data request.
     *
     * @param data the CSV data to be returned.
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