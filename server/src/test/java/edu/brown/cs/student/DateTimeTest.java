package edu.brown.cs.student;

import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertEquals;

import Broadband.LocalDateTimeAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import org.testng.annotations.Test;

public class DateTimeTest {

  private LocalDateTimeAdapter adapter = new LocalDateTimeAdapter();

  /**
   * Tests the behavior of deserialization with invalid JSON strings. Ensures that an exception is
   * thrown when the input is not a valid date-time string.
   */
  @Test
  public void testInvalidDeserialization() {
    String invalidJson = "Invalid Date";

    // Assert that the method throws DateTimeParseException
    assertThrows(
        DateTimeParseException.class,
        () -> {
          adapter.fromJson(invalidJson); // Should throw the exception
        });
  }

  /**
   * Tests the serialization of LocalDateTime objects to JSON strings. Verifies that the date and
   * time are correctly formatted in the output string.
   */
  @Test
  public void testSerialization() {
    LocalDateTime dateTime = LocalDateTime.of(2024, 10, 5, 14, 30);
    String expected = "2024-10-05T14:30:00";

    String json = adapter.toJson(dateTime);

    assertEquals(expected, json);
  }

  /**
   * Tests the deserialization of JSON strings to LocalDateTime objects. Verifies that the JSON
   * string is correctly parsed into a LocalDateTime object.
   */
  @Test
  public void testDeserialization() {
    String json = "2024-10-05T14:30:00";

    LocalDateTime dateTime = adapter.fromJson(json);

    assertEquals(LocalDateTime.of(2024, 10, 5, 14, 30), dateTime);
  }
}
