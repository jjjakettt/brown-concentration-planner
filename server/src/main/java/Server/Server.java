package Server;

import static spark.Spark.after;
import Server.Handlers.CourseHandler;
import Server.Handlers.PrerequisiteHandler;
import Server.Handlers.RequirementHandler;
import spark.Spark;

/**
 * Starts the server and sets up routes for handling various requests. This class initializes the
 * Spark server and sets up the endpoints for handling course planning-related requests.
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
          response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
          response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
          // Only set content type if it hasn't been set already
          if (response.raw().getContentType() == null) {
            response.type("application/json");
          }
        });
    Spark.get("requirements", new RequirementHandler());
    Spark.get("courses", new CourseHandler());
    Spark.get("/check-prerequisites", new PrerequisiteHandler());
    Spark.get(
        "/",
        (req, res) -> {
          res.type("text/plain");
          return "Available Endpoints:\n"
              + "/requirements"
              + "/courses"
              + "/check-prerequisites";
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
