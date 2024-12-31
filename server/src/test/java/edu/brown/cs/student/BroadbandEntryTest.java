package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;

import Broadband.BroadbandEntry;
import org.junit.Test;

public class BroadbandEntryTest {

  /**
   * Tests the constructor of BroadbandEntry. Verifies that the state, county, and broadband
   * percentage are correctly set.
   */
  @Test
  public void testBroadbandEntryConstructor() {
    BroadbandEntry entry = new BroadbandEntry("California", "Butte County", 83.5);
    assertEquals("California", entry.state);
    assertEquals("Butte County", entry.county);
    assertEquals(83.5, entry.broadbandPercentage, 0.0);
  }

  /**
   * Tests the toString method of BroadbandEntry. Verifies that the output string is formatted
   * correctly.
   */
  @Test
  public void testBroadbandEntryToString() {
    BroadbandEntry entry = new BroadbandEntry("California", "Butte County", 83.5);
    String expected = "State: California, County: Butte County, Broadband: 83.5";
    assertEquals(expected, entry.toString());
  }

  /**
   * Tests the equals and hashCode methods of BroadbandEntry. Verifies that two entries with the
   * same data are considered equal, and different entries are not.
   */
  @Test
  public void testEqualsAndHashCode() {
    BroadbandEntry entry1 = new BroadbandEntry("California", "Butte County", 83.5);
    BroadbandEntry entry2 = new BroadbandEntry("California", "Butte County", 83.5);
    BroadbandEntry entry3 = new BroadbandEntry("Texas", "Bexar County", 75.0);

    assertEquals(entry1, entry2);
    assertNotEquals(entry1, entry3);
    assertEquals(entry1.hashCode(), entry2.hashCode());
    assertNotEquals(entry1.hashCode(), entry3.hashCode());
  }
}
