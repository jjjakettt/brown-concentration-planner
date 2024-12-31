package GeneralAPIData;

import com.squareup.moshi.Json;
import java.util.Objects;

/**
 * Represents an entry containing broadband information for a specific state and county. This class
 * is used to map JSON data to Java objects using the Moshi library.
 */
public class GeneralEntry {
  @Json(name = "state")
  public String state;

  @Json(name = "county")
  public String county;

  @Json(name = "data")
  public Object data;

  /**
   * Constructs a BroadbandEntry object with the specified state, county, and broadband percentage.
   *
   * @param state the state of the broadband entry.
   * @param county the county of the broadband entry.
   * @param data the data received
   */
  public GeneralEntry(String state, String county, Object data) {
    this.state = state;
    this.county = county;
    this.data = data;
  }

  @Override
  public int hashCode() {
    return Objects.hash(state, county, data);
  }

  @Override
  public String toString() {
    return "State: " + state + ", County: " + county + ", data: " + data;
  }
}
