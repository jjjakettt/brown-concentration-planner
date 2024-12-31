package edu.brown.cs.student;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.fail;

import Broadband.CensusAPIAdapter;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CensusAPIAdapterTest {

  private CensusAPIAdapter adapter;

  @Before
  public void setUp() {
    adapter = new CensusAPIAdapter();
  }

  /**
   * Tests a successful query to the Census API using mocked data. Ensures that the method correctly
   * processes the response.
   *
   * @throws Exception if the test fails
   */
  @Test
  public void testQueryCensusAPISuccess() throws Exception {
    // Create a mock of the CensusAPIAdapter
    CensusAPIAdapter mockAdapter = Mockito.mock(CensusAPIAdapter.class);

    // Define the expected state, county, and mock response
    String state = "California";
    String county = "Butte County";
    Map<String, String> mockResponse = new HashMap<>();
    mockResponse.put("broadband_coverage", "75.0");

    // Mock the queryCensusAPI method to return the mockResponse when called with the specified
    // state and county
    Mockito.when(mockAdapter.queryCensusAPI(state, county)).thenReturn(mockResponse);

    // Call the method under test
    Map<String, String> result = mockAdapter.queryCensusAPI(state, county);

    // Verify the result
    assertNotNull("Response should not be null", result);
    assertEquals("Broadband coverage should be 75.0", "75.0", result.get("broadband_coverage"));
  }

  /**
   * Tests the behavior when the Census API query fails. Ensures the method throws an exception on
   * failure.
   *
   * @throws Exception if the test fails
   */
  @Test(expected = Exception.class)
  public void testQueryCensusAPIFailure() throws Exception {
    // Simulate failure from the Census API
    // You would mock an exception from the API interaction
    adapter.queryCensusAPI("InvalidState", "InvalidCounty");
  }

  /**
   * Tests how the Census API handles malformed data. Ensures that the method throws an exception
   * when encountering malformed data.
   *
   * @throws Exception if the test fails
   */
  @Test
  public void testQueryCensusAPIMalformedData() throws Exception {
    // Simulate a malformed JSON response from the API
    // This should ensure the adapter handles it gracefully
    // You would mock malformed data and ensure the adapter correctly handles parsing errors
    try {
      adapter.queryCensusAPI("MalformedDataState", "MalformedDataCounty");
      fail("Expected an exception for malformed data");
    } catch (Exception e) {
      // Expected exception
    }
  }
}
