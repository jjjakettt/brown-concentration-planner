package Broadband;

import java.time.LocalDateTime;

/**
 * Represents broadband information for a specific state and county. This class stores data about
 * the broadband coverage percentage, the date and time the data was retrieved, and the result
 * status (e.g., "success" or "error").
 */
public class Broadband {
  private String state;
  private String county;
  private double broadbandPercentage;
  private LocalDateTime dateTime;
  private String result;

  /**
   * Constructs a Broadband object with the specified state, county, broadband percentage, date and
   * time of retrieval, and result status.
   *
   * @param state the state of the broadband entry.
   * @param county the county of the broadband entry.
   * @param broadbandPercentage the percentage of broadband coverage in the specified state and
   *     county.
   * @param dateTime the date and time the broadband data was retrieved.
   * @param result the result status, such as "success" or "error."
   */
  public Broadband(
      String state,
      String county,
      double broadbandPercentage,
      LocalDateTime dateTime,
      String result) {
    this.state = state;
    this.county = county;
    this.broadbandPercentage = broadbandPercentage;
    this.dateTime = dateTime;
    this.result = result;
  }

  public String getState() {
    return state;
  }

  public String getCounty() {
    return county;
  }

  public double getBroadbandPercentage() {
    return broadbandPercentage;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public String getResult() {
    return result;
  }

  /**
   * Returns a string representation of the Broadband object, including the state, county, broadband
   * percentage, date and time of data retrieval, and result status.
   *
   * @return a formatted string representing the broadband data.
   */
  @Override
  public String toString() {
    return "State: "
        + state
        + ", County: "
        + county
        + ", Broadband: "
        + broadbandPercentage
        + "%, Date: "
        + dateTime
        + ", Result: "
        + result;
  }
}
