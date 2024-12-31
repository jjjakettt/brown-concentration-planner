package Broadband;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CensusAPIAdapter {

  private static final String BASE_URL = "https://api.census.gov/data/2021/acs/acs1/subject";
  private static final String STATE_CODES_URL =
      "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";
  private static final String COUNTY_CODES_URL_TEMPLATE =
      "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:%s";

  private static final String API_KEY =
      "378e269e02682534c7f0cd4ebc7152139a4c4419"; // Replace with your actual API key

  /**
   * Queries the Census API for broadband data for a specific state and county.
   *
   * @param stateName the name of the state to query.
   * @param countyName the name of the county to query.
   * @return a map containing the retrieved data from the Census API.
   * @throws Exception if there is an issue with the API request or the data is invalid.
   */
  public Map<String, String> queryCensusAPI(String stateName, String countyName) throws Exception {
    if (API_KEY == null || API_KEY.isEmpty()) {
      throw new IllegalStateException("API key is not set in the environment variables.");
    }

    // Log state and county before querying
    System.out.println("Querying for state: " + stateName + " and county: " + countyName);

    // Get the state code dynamically from the Census API
    String stateCode = getStateCode(stateName);
    if (stateCode == null) {
      throw new IllegalArgumentException("Invalid state name");
    }

    // Get the county code dynamically based on the state code
    String countyCode = getCountyCode(stateCode, countyName);
    if (countyCode == null) {
      throw new IllegalArgumentException("Invalid county name for the given state");
    }

    // https://api.census.gov/data/2021/acs/acs1/subject?get=NAME,S2802_C03_022E&for=county:131&in=state:05
    // Build the query URL with the specific county code
    StringBuilder urlBuilder = new StringBuilder(BASE_URL);
    urlBuilder.append("?get=NAME,S2802_C03_022E");
    urlBuilder.append("&for=county:").append(countyCode); // Use the specific county code here
    urlBuilder.append("&in=state:").append(stateCode); // State code
    urlBuilder.append("&key=").append(API_KEY); // API key

    // Log the constructed URL for debugging
    System.out.println("Constructed API URL: " + urlBuilder.toString());

    // Create the connection and handle the response
    Map<String, String> dataMap = new HashMap<>();
    HttpURLConnection conn = null;
    BufferedReader in = null;

    try {
      // Create and send the HTTP request
      URL url = new URL(urlBuilder.toString());
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      // Read the response
      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String inputLine;
      StringBuilder response = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }

      // Print the raw response (for debugging)
      System.out.println("Raw API Response: " + response.toString());

      // Parse the response into a Map
      dataMap = parseCensusResponse(response.toString());

    } catch (MalformedURLException e) {
      System.err.println("The URL is malformed: " + e.getMessage());
      throw new Exception("Failed to build the query URL.", e);
    } catch (IOException e) {
      System.err.println("An I/O error occurred: " + e.getMessage());
      throw new Exception("Failed to connect to the Census API.", e);
    } finally {
      // Close the resources if open
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          System.err.println("Failed to close the input stream: " + e.getMessage());
        }
      }
      if (conn != null) {
        conn.disconnect();
      }
    }
    return dataMap;
  }

  private String getStateCode(String stateName) throws Exception {
    String url = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";
    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
    conn.setRequestMethod("GET");

    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuilder response = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    // Parse the response to find the state code
    String[] rows = response.toString().split("\\],\\[");
    for (String row : rows) {
      String[] columns = row.split("\",\"");
      if (columns[0].replaceAll("\"", "").trim().equalsIgnoreCase(stateName)) {
        String stateCode = columns[1].replaceAll("\"", "").trim();
        System.out.println("Found state code: " + stateCode);
        return stateCode;
      }
    }

    return null; // State not found
  }

  private String getCountyCode(String stateCode, String countyName) throws Exception {
    System.out.println(
        "Entered getCountyCode method for stateCode: "
            + stateCode
            + " and countyName: "
            + countyName);

    String countyUrl =
        String.format(
            "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:%s",
            stateCode);
    System.out.println("Querying Census API for counties: " + countyUrl);

    HttpURLConnection conn = (HttpURLConnection) new URL(countyUrl).openConnection();
    conn.setRequestMethod("GET");

    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuilder response = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    // Log the API response for debugging
    System.out.println("County API Response: " + response.toString());

    // Parse the response to find the county code
    String[] rows = response.toString().split("\\],\\[");

    // Skip the first row since it contains the headers
    for (int i = 1; i < rows.length; i++) {
      String row = rows[i];
      String[] columns = row.split("\",\"");

      // Ensure the line contains at least three columns: County Name, State Code, and County Code
      if (columns.length >= 3) {
        String countyNameInResponse = columns[0].replaceAll("\"", "").trim();
        String countyCode =
            columns[2]
                .replaceAll("\"", "")
                .trim(); // Updated index to match correct column for county code

        // Perform case-insensitive match for the county name, ignoring the state name part in the
        // response
        if (countyNameInResponse.toLowerCase().startsWith(countyName.toLowerCase())) {
          System.out.println("Found county code: " + countyCode); // Log the retrieved county code
          return countyCode;
        }
      } else {
        System.out.println("Unexpected format in response line: " + row);
      }
    }

    System.out.println("No matching county found for: " + countyName); // Log when no match is found
    return null; // If no matching county is found
  }

  // Parse the raw response from the Census API into a Map<String, String>
  public Map<String, String> parseCensusResponse(String response) {
    Map<String, String> dataMap = new HashMap<>();

    // Remove the outer brackets and cleanup
    response = response.replaceFirst("\\[", "").replaceFirst("\\]$", "");

    // Split the response into individual parts (header and data)
    String[] rows = response.split("\\],\\[");

    if (rows.length == 2) {
      // First row contains headers
      String[] headers = rows[0].split("\",\"");

      // Second row contains values
      String[] values = rows[1].split("\",\"");

      // Ensure headers and values are of the same length
      if (headers.length == values.length) {
        for (int i = 0; i < headers.length; i++) {
          String cleanHeader = headers[i].replaceAll("\"", "").trim();
          String cleanValue = values[i].replaceAll("\"", "").trim();
          dataMap.put(cleanHeader, cleanValue);
        }
      }
    }
    return dataMap;
  }
  /**
   * Fetches broadband data for a specific key (usually representing a combination of state and
   * county).
   *
   * @param key The key, typically a combination of state and county names, separated by a comma
   *     (e.g., "California,Los Angeles").
   * @return A BroadbandEntry containing the fetched broadband data.
   * @throws Exception If there is an error during the API call.
   */
  public BroadbandEntry fetchData(String key) throws Exception {
    // Assume the key is a combination of state and county names
    String[] keyParts = key.split(",");
    if (keyParts.length != 2) {
      throw new IllegalArgumentException(
          "Invalid key format. Expected format: 'StateName,CountyName'");
    }

    String stateName = keyParts[0].trim();
    String countyName = keyParts[1].trim();

    // Query the Census API for broadband data based on state and county
    Map<String, String> dataMap = queryCensusAPI(stateName, countyName);

    // Extract the broadband data value from the response map (using "S2802_C03_022E" as the key for
    // broadband data)
    String broadbandValueStr = dataMap.get("S2802_C03_022E");

    if (broadbandValueStr == null) {
      throw new Exception("No broadband data found for the given location.");
    }

    double broadbandValue = Double.parseDouble(broadbandValueStr); // Parse the broadband value

    // Create and return a new BroadbandEntry with the fetched data
    return new BroadbandEntry(stateName, countyName, broadbandValue);
  }
}

//
//    // Build the query URL
//    StringBuilder urlBuilder = new StringBuilder(BASE_URL);
//    urlBuilder.append("?get=NAME");  // Include 'NAME' by default to get the geographical name
//
//    // Append the requested variables
//    urlBuilder.append(",").append("S2802_C03_022E".trim());
//
//    // Append the county and state parameters
//    if (countyCode.equals("*")) {
//      urlBuilder.append("&for=county:*");
//    } else {
//      urlBuilder.append("&for=county:").append(countyCode);
//    }
//    urlBuilder.append("&in=state:").append(stateCode);
//    urlBuilder.append("&key=").append(API_KEY);
//
//    // Create the connection and handle the response
//    Map<String, String> dataMap = new HashMap<>();
//    HttpURLConnection conn = null;
//    BufferedReader in = null;
//
//    try {
//      // Create and send the HTTP request
//      URL url = new URL(urlBuilder.toString());
//      System.out.println("Request URL: " + urlBuilder.toString());
//
//      conn = (HttpURLConnection) url.openConnection();
//      conn.setRequestMethod("GET");
//
//      // Read the response
//      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//      String inputLine;
//      StringBuilder response = new StringBuilder();
//      while ((inputLine = in.readLine()) != null) {
//        response.append(inputLine);
//      }
//
//      // Print the raw response (for debugging)
//      System.out.println(response.toString());
//
//      // Parse the response into a Map
//      dataMap = parseCensusResponse(response.toString());
//
//    } catch (MalformedURLException e) {
//      System.err.println("The URL is malformed: " + e.getMessage());
//      throw new Exception("Failed to build the query URL.", e);
//    } catch (IOException e) {
//      System.err.println("An I/O error occurred: " + e.getMessage());
//      throw new Exception("Failed to connect to the Census API.", e);
//    } finally {
//      // Close the resources if open
//      if (in != null) {
//        try {
//          in.close();
//        } catch (IOException e) {
//          System.err.println("Failed to close the input stream: " + e.getMessage());
//        }
//      }
//      if (conn != null) {
//        conn.disconnect();
//      }
//    }
//    return dataMap;
//  }
//
////   Method to parse the raw response from the Census API into a Map<String, String>
//  public Map<String, String> parseCensusResponse(String response) {
//    Map<String, String> dataMap = new HashMap<>();
//
//    // Log the raw response for debugging
//    System.out.println("Raw response: " + response);
//
//    // Remove the outer brackets and cleanup
//    response = response.replaceFirst("\\[", "").replaceFirst("\\]$", "");
//
//    // Split the response into individual parts (header and data)
//    String[] rows = response.split("\\],\\[");
//
//    // Log the rows for debugging
//    System.out.println("Rows after split: " + Arrays.toString(rows));
//
//    if (rows.length == 2) {
//      // First row contains headers
//      String[] headers = rows[0].split("\",\"");
//
//      // Second row contains values
//      String[] values = rows[1].split("\",\"");
//
//      // Log headers and values for debugging
//      System.out.println("Headers: " + Arrays.toString(headers));
//      System.out.println("Values: " + Arrays.toString(values));
//
//      // Ensure headers and values are of the same length
//      if (headers.length == values.length) {
//        for (int i = 0; i < headers.length; i++) {
//          // Trim quotes and spaces
//          String cleanHeader = headers[i].replaceAll("\"", "").trim();
//          String cleanValue = values[i].replaceAll("\"", "").trim();
//
//          // Add the cleaned header and value to the data map
//          dataMap.put(cleanHeader, cleanValue);
//        }
//
//        // Log the resulting map for debugging
//        System.out.println("Parsed DataMap: " + dataMap);
//      } else {
//        System.out.println("Error: Headers and values count mismatch.");
//      }
//    } else {
//      System.out.println("Error: Response does not contain enough rows.");
//    }
//
//    return dataMap;
//  }
//
// }
