package Broadband;

import com.squareup.moshi.Json;
import java.util.Objects;

/**
 * Represents an entry containing broadband information for a specific state and county. This class
 * is used to map JSON data to Java objects using the Moshi library.
 */
public class BroadbandEntry {
  @Json(name = "state")
  public String state;

  @Json(name = "county")
  public String county;

  @Json(name = "broadband_percentage")
  public double broadbandPercentage;

  /**
   * Constructs a BroadbandEntry object with the specified state, county, and broadband percentage.
   *
   * @param state the state of the broadband entry.
   * @param county the county of the broadband entry.
   * @param broadbandPercentage the percentage of broadband coverage.
   */
  public BroadbandEntry(String state, String county, double broadbandPercentage) {
    this.state = state;
    this.county = county;
    this.broadbandPercentage = broadbandPercentage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BroadbandEntry that = (BroadbandEntry) o;
    return Double.compare(that.broadbandPercentage, this.broadbandPercentage) == 0
        && this.state.equals(that.state)
        && this.county.equals(that.county);
  }

  @Override
  public int hashCode() {
    return Objects.hash(state, county, broadbandPercentage);
  }

  @Override
  public String toString() {
    return "State: " + state + ", County: " + county + ", Broadband: " + broadbandPercentage;
  }
}
