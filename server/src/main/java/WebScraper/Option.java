package WebScraper;

import java.util.HashSet;
/**
 * Models an option a student can choose when fulfilling a requirement.
 */
public class Option {
  HashSet<Course> courses;
  Option(HashSet<Course> courses) {
    this.courses = courses;
  }
}
