package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.*;

import Creators.*;
import Parser.Parser;
import java.io.*;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * TODO: add more tests in this file to build an extensive test suite for your parser and parsing
 * functionalities
 *
 * <p>Tests for the parser class
 */
public class ParserTest {

  Parser<List<String>> RITParser;

  @Test
  public void testParseRITDataSet() {
    try {
      RITParser =
          new Parser(new FileReader("data/RIT_Income/RIT_Data.csv"), new TrivialCreator(), true);
      RITParser.parse();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    // TODO: Complete this test

    // Assert the total number of rows parsed (based on the content of RIT_Data.csv)
    assertEquals(41, RITParser.parsedContent.size(), "The number of rows should be 40");

    // Test a specific row (let's check the second row as an example)
    List<String> thirdRow = List.of("Bristol", "\"80,727.00\"", "\"115,740.00\"", "\"42,658.00\"");
    assertEquals(thirdRow, RITParser.parsedContent.get(3), "The third row data should match");
  }
}
