package Broadband;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Adapter class for serializing and deserializing LocalDateTime objects to and from JSON */
public class LocalDateTimeAdapter {
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  /**
   * Serializes a LocalDateTime object to a JSON string.
   *
   * @param value the LocalDateTime object to serialize.
   * @return a formatted JSON string representing the LocalDateTime.
   */
  @ToJson
  public String toJson(LocalDateTime value) {
    return value.format(FORMATTER);
  }

  /**
   * Deserializes a JSON string into a LocalDateTime object.
   *
   * @param value the JSON string to deserialize.
   * @return the LocalDateTime object parsed from the JSON string.
   */
  @FromJson
  public LocalDateTime fromJson(String value) {
    return LocalDateTime.parse(value, FORMATTER);
  }
}
