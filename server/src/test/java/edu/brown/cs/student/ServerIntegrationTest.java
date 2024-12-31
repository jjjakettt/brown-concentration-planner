package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.*;

import Server.Handlers.LoadCSVHandler;
import Server.Handlers.SearchCSVHandler;
import Server.Handlers.ViewCSVHandler;
import datasource.sharedState.SharedState;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class ServerIntegrationTest {
  //  private List<List<String>> parsedCSV;
  //  private tempDatasource state;
  private SharedState sharedState;

  //  This is a working link
  //
  // http://localhost:3232/searchcsv?filepath=data/RIT_Income/RIT_Data.csv&searchValue=Coventry&columnIdentifier=0

  @BeforeAll
  public static void setupBeforeAll() {
    // Set Spark to use port 0 (random available port)
    Spark.port(0);
  }

  @BeforeEach
  public void setup() {
    //    parsedCSV = new ArrayList<>();
    //    state = new tempDatasource(){};
    sharedState = new SharedState();

    // Initialize the server before each test
    Spark.get("loadcsv", new LoadCSVHandler(sharedState));
    Spark.get("viewcsv", new ViewCSVHandler(sharedState));
    Spark.get("searchcsv", new SearchCSVHandler(sharedState));
    Spark.init();
    Spark.awaitInitialization();
  }

  @AfterEach
  public void teardown() {
    // Stop the server after each test
    Spark.unmap("loadcsv");
    Spark.unmap("viewcsv");
    Spark.unmap("searchcsv");
    Spark.awaitStop();
  }

  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    return clientConnection;
  }

  @Test
  public void testLoadCSV() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("loadcsv?filepath=data/RIT_Income/RIT_Data.csv");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection.disconnect();
  }

  @Test
  public void testViewCSVWithoutLoading() throws IOException {
    HttpURLConnection clientConnection = tryRequest("viewcsv");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection.disconnect();
  }

  @Test
  public void testViewCSVAfterLoading() throws IOException {
    HttpURLConnection loadConnection = tryRequest("loadcsv?filepath=data/RIT_Income/RIT_Data.csv");
    assertEquals(200, loadConnection.getResponseCode());
    loadConnection.disconnect();

    HttpURLConnection viewConnection = tryRequest("viewcsv");
    assertEquals(200, viewConnection.getResponseCode());
    viewConnection.disconnect();
  }

  @Test
  public void testSearchCSVByColumn() throws IOException {
    HttpURLConnection loadConnection = tryRequest("loadcsv?filepath=data/RIT_Income/RIT_Data.csv");
    assertEquals(200, loadConnection.getResponseCode());
    loadConnection.disconnect();

    HttpURLConnection searchConnection =
        tryRequest("searchcsv?searchValue=Coventry&columnIdentifier=0");
    assertEquals(200, searchConnection.getResponseCode());
    searchConnection.disconnect();
  }

  @Test
  public void testSearchCSVAcrossAllColumns() throws IOException {
    HttpURLConnection loadConnection = tryRequest("loadcsv?filepath=data/RIT_Income/RIT_Data.csv");
    assertEquals(200, loadConnection.getResponseCode());
    loadConnection.disconnect();

    HttpURLConnection searchConnection = tryRequest("searchcsv?searchValue=Coventry");
    assertEquals(200, searchConnection.getResponseCode());
    searchConnection.disconnect();
  }

  @Test
  public void testSearchCSVHandlerErrorBadRequest() throws Exception {
    HttpURLConnection connection = tryRequest("searchcsv?searchValue="); // Empty query parameter
    assertEquals(200, connection.getResponseCode());

    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;
    StringBuilder content = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }
    in.close();
    connection.disconnect();

    assertTrue(content.toString().contains("error_bad_request"));
  }

  @Test
  public void testSearchCSVHandlerErrorDataSource() throws Exception {
    HttpURLConnection connection =
        tryRequest("searchcsv?searchValue=somequery"); // Valid query but no CSV loaded
    assertEquals(200, connection.getResponseCode());

    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;
    StringBuilder content = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }
    in.close();
    connection.disconnect();

    assertTrue(content.toString().contains("error_datasource"));
  }

  @Test
  public void testLoadCSVHandlerErrorBadRequest() throws Exception {
    HttpURLConnection connection = tryRequest("loadcsv?filepath="); // Empty filepath parameter
    assertEquals(200, connection.getResponseCode());

    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;
    StringBuilder content = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }
    in.close();
    connection.disconnect();

    assertTrue(content.toString().contains("error_bad_request"));
  }

  @Test
  public void testViewCSVHandlerErrorDataSource() throws Exception {
    HttpURLConnection connection = tryRequest("viewcsv"); // View CSV when none is loaded
    assertEquals(200, connection.getResponseCode());

    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;
    StringBuilder content = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }
    in.close();
    connection.disconnect();

    assertTrue(content.toString().contains("error_datasource"));
  }
}
