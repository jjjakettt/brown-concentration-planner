package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import Server.Handlers.RequirementHandler;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class BannerScraperTest {

  /** This class tests the BannerScraper class. */
  static String sampleHtml;

  static String invalidHtml;

  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);

  }

  private static HttpURLConnection TryRequest(String string) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + string);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    // The default method is "GET", which is what we're using here.
    // If we were using "POST", we'd need to say so.
    clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  @Test
  public void firstCSRequirement() throws IOException {
    RequirementHandler requirementHandler = new RequirementHandler();
    Spark.get("/requirements", requirementHandler);
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection connection = TryRequest("requirements");
    assertEquals(200, connection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);

    Map<String, Object> jsonResponse =
        jsonAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));

    assertNotNull(jsonResponse);
    assertEquals("Success", jsonResponse.get("response_type"));
    assertTrue(jsonResponse.containsKey("data"));

    List<List<String>> geoJsonData = (List<List<String>>) jsonResponse.get("data");
    List<String> firstArray = geoJsonData.get(0);
    String string = firstArray.get(0);
    assertEquals(string, "Select one of the following introductory course Series:");

    connection.disconnect();

    Spark.unmap("/requirements");
  }

  @Test
  public void twoCSRequirement() throws IOException {
    RequirementHandler requirementHandler = new RequirementHandler();
    Spark.get("/requirements", requirementHandler);
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection connection = TryRequest("requirements");
    assertEquals(200, connection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);

    Map<String, Object> jsonResponse =
        jsonAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));

    assertNotNull(jsonResponse);
    assertEquals("Success", jsonResponse.get("response_type"));
    assertTrue(jsonResponse.containsKey("data"));

    List<List<String>> geoJsonData = (List<List<String>>) jsonResponse.get("data");
    List<String> firstArray = geoJsonData.get(0);
    String string = firstArray.get(0);
    assertEquals(string, "Select one of the following introductory course Series:");

    String string1 = firstArray.get(1);
    assertEquals(string1, "2");

    connection.disconnect();

    Spark.unmap("/requirements");
  }

  @Test
  public void BDSRequirement1() throws IOException {
    RequirementHandler requirementHandler = new RequirementHandler();
    Spark.get("/requirements", requirementHandler);
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection connection =
        TryRequest("requirements?url=https://bulletin.brown.edu/the-college/concentrations/bds/");
    assertEquals(200, connection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);

    Map<String, Object> jsonResponse =
        jsonAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));

    assertNotNull(jsonResponse);
    assertEquals("Success", jsonResponse.get("response_type"));
    assertTrue(jsonResponse.containsKey("data"));

    List<List<String>> geoJsonData = (List<List<String>>) jsonResponse.get("data");
    List<String> firstArray = geoJsonData.get(0);
    String string = firstArray.get(0);
    assertEquals(string, "CPSY 0220");

    connection.disconnect();

    Spark.unmap("/requirements");
  }

  @Test
  public void BDSRequirement2() throws IOException {
    RequirementHandler requirementHandler = new RequirementHandler();
    Spark.get("/requirements", requirementHandler);
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection connection =
        TryRequest("requirements?url=https://bulletin.brown.edu/the-college/concentrations/bds/");
    assertEquals(200, connection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);

    Map<String, Object> jsonResponse =
        jsonAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));

    assertNotNull(jsonResponse);
    assertEquals("Success", jsonResponse.get("response_type"));
    assertTrue(jsonResponse.containsKey("data"));

    List<List<String>> geoJsonData = (List<List<String>>) jsonResponse.get("data");
    List<String> firstArray = geoJsonData.get(0);
    String string = firstArray.get(0);
    assertEquals(string, "CPSY 0220");

    String string1 = firstArray.get(1);
    assertEquals(string1, "1");

    connection.disconnect();

    Spark.unmap("/requirements");
  }

  @Test
  public void CsThenBDSRequirements() throws IOException {
    RequirementHandler requirementHandler = new RequirementHandler();
    Spark.get("/requirements", requirementHandler);
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection connection = TryRequest("requirements");
    assertEquals(200, connection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);

    Map<String, Object> jsonResponse =
        jsonAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));

    assertNotNull(jsonResponse);
    assertEquals("Success", jsonResponse.get("response_type"));
    assertTrue(jsonResponse.containsKey("data"));

    List<List<String>> geoJsonData = (List<List<String>>) jsonResponse.get("data");
    List<String> firstArray = geoJsonData.get(0);
    String string = firstArray.get(0);
    assertEquals(string, "Select one of the following introductory course Series:");

    connection.disconnect();

    HttpURLConnection connection1 =
        TryRequest("requirements?url=https://bulletin.brown.edu/the-college/concentrations/bds/");
    assertEquals(200, connection1.getResponseCode());

    Map<String, Object> jsonResponse1 =
        jsonAdapter.fromJson(new Buffer().readFrom(connection1.getInputStream()));

    assertNotNull(jsonResponse1);
    assertEquals("Success", jsonResponse1.get("response_type"));
    assertTrue(jsonResponse1.containsKey("data"));

    List<List<String>> geoJsonData1 = (List<List<String>>) jsonResponse1.get("data");
    List<String> firstArray1 = geoJsonData1.get(0);
    String string3 = firstArray1.get(0);
    assertEquals(string3, "CPSY 0220");

    String string4 = firstArray1.get(1);
    assertEquals(string4, "1");

    connection1.disconnect();

    Spark.unmap("/requirements");
  }

  @Test
  public void CsThenBDSThenEconRequirements() throws IOException {
    RequirementHandler requirementHandler = new RequirementHandler();
    Spark.get("/requirements", requirementHandler);
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection connection = TryRequest("requirements");
    assertEquals(200, connection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);

    Map<String, Object> jsonResponse =
        jsonAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));

    assertNotNull(jsonResponse);
    assertEquals("Success", jsonResponse.get("response_type"));
    assertTrue(jsonResponse.containsKey("data"));

    List<List<String>> geoJsonData = (List<List<String>>) jsonResponse.get("data");
    List<String> firstArray = geoJsonData.get(0);
    String string = firstArray.get(0);
    assertEquals(string, "Select one of the following introductory course Series:");

    connection.disconnect();

    HttpURLConnection connection1 =
        TryRequest("requirements?url=https://bulletin.brown.edu/the-college/concentrations/bds/");
    assertEquals(200, connection1.getResponseCode());

    Map<String, Object> jsonResponse1 =
        jsonAdapter.fromJson(new Buffer().readFrom(connection1.getInputStream()));

    assertNotNull(jsonResponse1);
    assertEquals("Success", jsonResponse1.get("response_type"));
    assertTrue(jsonResponse1.containsKey("data"));

    List<List<String>> geoJsonData1 = (List<List<String>>) jsonResponse1.get("data");
    List<String> firstArray1 = geoJsonData1.get(0);
    String string3 = firstArray1.get(0);
    assertEquals(string3, "CPSY 0220");

    String string4 = firstArray1.get(1);
    assertEquals(string4, "1");

    connection1.disconnect();

    HttpURLConnection connection2 =
        TryRequest("requirements?url=https://bulletin.brown.edu/the-college/concentrations/econ/");
    assertEquals(200, connection1.getResponseCode());

    Map<String, Object> jsonResponse2 =
        jsonAdapter.fromJson(new Buffer().readFrom(connection2.getInputStream()));

    assertNotNull(jsonResponse2);
    assertEquals("Success", jsonResponse2.get("response_type"));
    assertTrue(jsonResponse1.containsKey("data"));

    List<List<String>> geoJsonData2 = (List<List<String>>) jsonResponse2.get("data");
    List<String> firstArray2 = geoJsonData2.get(0);
    String string5 = firstArray2.get(0);
    assertEquals(string5, "ECON 0110");

    String string6 = firstArray2.get(1);
    assertEquals(string6, "1");

    connection1.disconnect();

    Spark.unmap("/requirements");
  }

  @Test
  public void Error() throws IOException {
    RequirementHandler requirementHandler = new RequirementHandler();
    Spark.get("/requirements", requirementHandler);
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection connection = TryRequest("requirements?url=willisthebest");
    assertEquals(200, connection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);

    Map<String, Object> jsonResponse =
        jsonAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));

    assertNotNull(jsonResponse);
    assertEquals("error_bad_json", jsonResponse.get("result"));
    assertEquals("Malformed JSON or parsing issue", jsonResponse.get("error_code"));

    connection.disconnect();

    Spark.unmap("/requirements");
  }
}

