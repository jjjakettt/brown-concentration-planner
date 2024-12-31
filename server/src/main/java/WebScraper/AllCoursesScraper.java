package WebScraper;

import WebScraper.Certificate.TrustAllCerts;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class AllCoursesScraper {


  ArrayList<Course> allCourses;
  static String html;


  public AllCoursesScraper(String html) {
    AllCoursesScraper.html = html;


    allCourses = new ArrayList<>();
    parse();
  }


  public void parse() {


    Document doc = Jsoup.parse(html);


    Elements rows = doc.select("tr[class*=main][class*=current]");


    for (Element row : rows) {
      // getting the course code
      Element codeElement = row.selectFirst("a");
      if (codeElement != null) {
        String code = codeElement.text();


        // getting the course name
        Element nameElement = row.select("td").get(1);
        if (nameElement != null) {
          String name = nameElement.text();


          // going into each "details" row for this course to get the term and professor for each term this course is offered
          Element detailsRow = row.nextElementSibling();
          while (detailsRow != null && detailsRow.className().contains("details")) {
            String term = "";
            String professor = "";


            // getting the course term
            Element termElement = detailsRow.select("td").get(1);
            if (termElement != null) {
              String longterm = termElement.text();
//             splitting the string on "•" to just get "Fall" or "Spring"
              String[] parts = longterm.split("•");
              term = parts[0].trim();
            }


            // getting the course professor
            Element professorElement = detailsRow.selectFirst("a");
            if (professorElement != null) {
              professor = professorElement.text(); // Get the professor's name
            }


            // creating a Course object with all the information I just got
            Course course = new Course(code, name, term, professor);

            // Fetch prerequisites for this course (jjjakettt)
            try {
              String courseUrl = String.format("https://cs.brown.edu/courses/info/%s/", 
                  code.toLowerCase());
              Document courseDoc = Jsoup.connect(courseUrl).get();
              course.setPrerequisites(parsePrerequisites(courseDoc));
            } catch (Exception e) {
              // If we can't fetch prerequisites, continue without them
              course.setPrerequisites(null);
            }

            allCourses.add(course);
            // moving onto the next details row (if there is one, so if this course it offered during another term)
            detailsRow = detailsRow.nextElementSibling();
          }
        }
      }
    }
  }

  // parsing prequisites (jjjakettt)
  private Prerequisites parsePrerequisites(Document courseDoc) {
    Elements prereqElements = courseDoc.select("p:contains(Prerequisites)");
    if (prereqElements.isEmpty()) {
        return null;
    }

    String prereqText = prereqElements.first().text()
        .replace("Prerequisites:", "")
        .trim();

    if (prereqText.isEmpty() || prereqText.toLowerCase().contains("none")) {
        return null;
    }

    Prerequisites prerequisites = new Prerequisites();

    // Split by "and" to get AND conditions
    String[] andGroups = prereqText.split("(?i)\\s+and\\s+");

    for (String group : andGroups) {
        // Remove parentheses and split by "or" to get OR conditions
        String cleanGroup = group.replaceAll("[()]", "").trim();
        String[] orOptions = cleanGroup.split("(?i)\\s+or\\s+");

        // Clean up each course code and add to prerequisites
        List<String> cleanedOptions = Arrays.stream(orOptions)
            .map(option -> option.trim().replaceAll(",", ""))
            .filter(option -> !option.isEmpty())
            .toList();

        if (!cleanedOptions.isEmpty()) {
            prerequisites.addPrerequisiteGroup(cleanedOptions);
        }
    }

    return prerequisites;
  }


  public List<List<String>> coursesToList() {

    List<List<String>> result = new ArrayList<>();

    for (Course course : allCourses) {

//      turn each course object into a list of its attributes
      List<String> courseList = new ArrayList<>();
      courseList.add(course.name);
      courseList.add(course.code);
      courseList.add(course.professor);
      courseList.add(course.term);

//      add the list representing a course to the result
      result.add(courseList);
    }
    return result;
  }


}
