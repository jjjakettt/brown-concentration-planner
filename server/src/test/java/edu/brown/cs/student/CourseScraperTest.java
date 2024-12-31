package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import Server.Handlers.CourseHandler;
import Server.Handlers.RequirementHandler;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class CourseScraperTest {

  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(3131);
//    Logger.getLogger("").setLevel(Level.WARNING);
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
  public void correctAttributes() throws IOException {
    CourseHandler courseHandler = new CourseHandler();
    Spark.get("/courses", courseHandler);
    Spark.init();
    Spark.awaitInitialization();

    HttpURLConnection connection = TryRequest("courses");
    assertEquals(200, connection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);

    Map<String, Object> jsonResponse =
        jsonAdapter.fromJson(new Buffer().readFrom(connection.getInputStream()));

    assertNotNull(jsonResponse);
    assertEquals("Success", jsonResponse.get("response_type"));
    assertTrue(jsonResponse.containsKey("data"));

    List<List<String>> courseJsonData = (List<List<String>>) jsonResponse.get("data");

//    checking that the right number of courses were loaded in
    assertEquals(114, courseJsonData.size());

    List<String> firstArray = courseJsonData.get(0);

    ArrayList<String> correct = new ArrayList<>();
    correct.add("The Digital World");
    correct.add("CSCI0020");
    correct.add("Fall");
    correct.add("Donald L Stanford");

    assertEquals(firstArray, correct);


    List<String> secondArray = courseJsonData.get(1);

    ArrayList<String> correct2 = new ArrayList<>();
    correct2.add("TA Apprenticeship: Full Credit");
    correct2.add("CSCI0081");
    correct2.add("Fall");
    correct2.add("Kathi Fisler");

    assertEquals(secondArray, correct2);

    connection.disconnect();

    Spark.unmap("/courses");
  }

}
