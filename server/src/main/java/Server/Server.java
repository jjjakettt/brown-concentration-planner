package Server;

import static spark.Spark.after;

import Server.Handlers.BroadbandHandler;
//import Server.Handlers.CourseHandler;
import Server.Handlers.CourseHandler;
import Server.Handlers.GeneralHandler;
import Server.Handlers.LoadCSVHandler;
import Server.Handlers.PrerequisiteHandler;
import Server.Handlers.RequirementHandler;
import Server.Handlers.SearchCSVHandler;
import Server.Handlers.ViewCSVHandler;
import datasource.sharedState.SharedState;
import spark.Spark;

/**
 * Starts the server and sets up routes for handling various requests. This class initializes the
 * Spark server and sets up the endpoints for handling CSV-related operations and broadband data
 * requests.
 */
public class Server {
  public Server() {
    int port = 3231;
    Spark.port(port);

    // Add OPTIONS endpoint for CORS preflight
    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
          response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
      if (accessControlRequestMethod != null) {
          response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          // response.header("Access-Control-Allow-Methods", "*");
          response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
          response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
          // Only set content type if it hasn't been set already
          if (response.raw().getContentType() == null) {
            response.type("application/json");
          }
        });

    int maxCacheSize = 100; // Specify the cache size here

    // Pass the cache size to the handler via the CacheProxyDatasource
    // CacheProxyDatasource proxyDatasource = new CacheProxyDatasource(maxCacheSize);

    SharedState sharedState = new SharedState();
    // CensusAPIAdapter adapter = new CensusAPIAdapter();
    // Setting up the handler for the GET /view* load* and search* endpoints, * = csv
    Spark.get("viewcsv", new ViewCSVHandler(sharedState));
    Spark.get("loadcsv", new LoadCSVHandler(sharedState));
    Spark.get("searchcsv", new SearchCSVHandler(sharedState));
    Spark.get("broadband", new BroadbandHandler(100));
    Spark.get("requirements", new RequirementHandler());
    Spark.get("courses", new CourseHandler());
    Spark.get("variable", new GeneralHandler());
    Spark.get("/check-prerequisites", new PrerequisiteHandler());



    Spark.get(
        "/",
        (req, res) -> {
          res.type("text/plain");
          return "Available Endpoints:\n"
              + "/loadcsv - Load a CSV file (ex: /loadcsv?filepath=yourfile.csv)\n"
              + "/viewcsv - View the loaded CSV file (ex: /viewcsv)\n"
              + "/searchcsv - Search the loaded CSV file (ex: /searchcsv?searchValue=yourkeyword&columnIdentifier=yourcolumn)\n"
              + "/broadband - Broadband data endpoint (ex: /broadband?state=yourstate&county=yourcounty\n"
              + "/variable - General variable endpoint (ex: /variable?input_variable=yourvariable&state=yourstate&county=yourcounty)";
        });

    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }

  //  Starts the server
  public static void main(String[] args) {

    Server server = new Server();

    System.out.println("Server started; exiting main...");
  }
}
