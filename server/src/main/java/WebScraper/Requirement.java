package WebScraper;

import java.util.HashSet;
/**
 * Models a concentration requirement, complete with features including the name of the requirement
 * itself, the options students have for fulfilling the requirement and the number of hours
 * required to complete the requirement. The Requirement class also stores the row in the Banner
 * concentration requirements table in which it is located.
 */
public class Requirement {
  int rowNumber;
  HashSet<Option> options;
  String hours;
  String name;
  Requirement(String name, HashSet<Option> options, String hours, int rowNumber) {
    this.name = name;
    this.options = options;
    this.hours = hours;
    this.rowNumber = rowNumber;
  }
  @Override
  public String toString() {
    System.out.println("New Requirement: " + name);
    System.out.println("Hours: " + hours);
    for (Option option : this.options) {
      System.out.println("New Option");
      for (Course course : option.courses) {
        System.out.println("Code: " + course.code + ", Name: " + course.name);
      }
    }
    return "";
  }
}
