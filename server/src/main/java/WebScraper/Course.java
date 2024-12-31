package WebScraper;

public class Course {
  String code;
  String name;
  int row;
  String professor;
  String term;
  Prerequisites prerequisites;

  Course(String code, String name, int row) {
    this.row = row;
    this.code = code;
    this.name = name;
  }

  Course(String code, String name, String professor, String term) {
    this.code = code;
    this.name = name;
    this.professor = professor;
    this.term = term;
    this.prerequisites = null;
  }

  public Prerequisites getPrerequisites() {
    return prerequisites;
  } 

  public void setPrerequisites(Prerequisites prerequisites) {
      this.prerequisites = prerequisites;
  }
}
