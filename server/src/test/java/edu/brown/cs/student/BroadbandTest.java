package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import Broadband.Broadband;
import Server.Handlers.BroadbandHandler;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BroadbandTest {

  private Broadband broadband;
  private BroadbandHandler broadbandHandler;

  @BeforeEach
  public void setUp() {
    // Passing the required 5 arguments to the Broadband constructor
    broadband =
        new Broadband(
            "Michigan", // state
            "Shiawassee County", // county
            81.5, // broadbandPercentage
            LocalDateTime.now(), // dateTime
            "Success" // result
            );

    // Assuming broadbandHandler doesn't need Broadband object or will be mocked later
    broadbandHandler = new BroadbandHandler(100);
  }

  @Test
  public void testBroadbandObject() {
    // Validate the Broadband object instantiation
    assertEquals("Michigan", broadband.getState());
    assertEquals("Shiawassee County", broadband.getCounty());
    assertEquals(81.5, broadband.getBroadbandPercentage());
    assertNotNull(broadband.getDateTime());
    assertEquals("Success", broadband.getResult());
  }

  @Test
  public void testWithNullAndEmptyFields() {
    // Null county, empty result
    Broadband broadband = new Broadband("Michigan", null, 81.5, LocalDateTime.now(), "");

    assertEquals("Michigan", broadband.getState());
    assertNull(broadband.getCounty());
    assertEquals(81.5, broadband.getBroadbandPercentage());
    assertNotNull(broadband.getDateTime());
    assertEquals("", broadband.getResult());
  }

  @Test
  public void testDateHandling() {
    LocalDateTime dateTime = LocalDateTime.of(2024, 9, 27, 10, 45);
    Broadband broadband = new Broadband("Michigan", "Wayne County", 75.3, dateTime, "Success");

    assertEquals(dateTime, broadband.getDateTime());
    assertEquals(2024, broadband.getDateTime().getYear());
    assertEquals(9, broadband.getDateTime().getMonthValue());
    assertEquals(10, broadband.getDateTime().getHour());
  }
}
