import { Dispatch, SetStateAction, useState } from "react";
import { Course, mockCourses } from "../../mockCourseData";
import { useUser } from "@clerk/clerk-react";
import { CourseBank } from "./CourseBank";

interface Semester {
  courses: Course[];
}

interface CoursePlannerProps {
  showCourseBank: boolean;
  setShowCourseBank: Dispatch<SetStateAction<boolean>>;
}

export function CoursePlanner(props: CoursePlannerProps) {
  const [semesters, setSemesters] = useState<Semester[]>(
    Array(8)
      .fill(null)
      .map(() => ({ courses: [] }))
  );
  const [error, setError] = useState<string | null>(null);
  const addCourseToSemester = (course: Course, semesterIndex: number) => {
    // Check semester capacity
    if (semesters[semesterIndex].courses.length >= 5) {
      setError("Cannot add more than 5 courses to a semester");
      return false;
    }
    

    // Check term restrictions
    const isSemesterOdd = (semesterIndex + 1) % 2 !== 0; // +1 because array is 0-based
    if (course.term === "Fall" && !isSemesterOdd) {
      setError(
        `${course.name} (${course.code}) can only be added to Fall semesters (odd-numbered semesters)`
      );
      return false;
    }
    if (course.term === "Spring" && isSemesterOdd) {
      setError(
        `${course.name} (${course.code}) can only be added to Spring semesters (even-numbered semesters)`
      );
      return false;
    }

    setSemesters((prev) => {
      const newSemesters = [...prev];
      newSemesters[semesterIndex] = {
        courses: [...newSemesters[semesterIndex].courses, course],
      };
      return newSemesters;
    });
    return true;
  };

  const removeCourse = (courseCode: string, semesterIndex: number) => {
    setSemesters((prev) => {
      const newSemesters = [...prev];
      newSemesters[semesterIndex] = {
        courses: newSemesters[semesterIndex].courses.filter(
          (course) => course.code !== courseCode
        ),
      };
      return newSemesters;
    });
  };

  if (props.showCourseBank) {
    return (
      <CourseBank
        onBack={() => props.setShowCourseBank(false)}
        semesters={semesters}
        onAddCourse={addCourseToSemester}
        onRemoveCourse={removeCourse}
      />
    );
  }

  return (
    <div className="course-planner">
      <div
        className="semester-grid"
        style={{
          display: "grid",
          alignItems: "start",
          paddingLeft: "20px",
          paddingRight: "20px",
        }}
      >
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fit, minmax(250px, 1fr))",
            gap: "16px",
            marginBottom: "24px",
          }}
        >
          {semesters.map((semester, index) => (
            <div
              key={index}
              style={{
                paddingRight: "12px",
                paddingLeft: "12px",
                paddingBottom: "12px",
                backgroundColor: "#dfd9d7", // Subtle transparent background
                borderRadius: "8px",
              }}
            >
              <h3
                style={{
                  marginBottom: "12px",
                  fontWeight: "600",
                  backgroundColor: "rgb(58, 30, 26)",
                  color: "#d9d4d3",
                  padding: "8px",
                  borderRadius: "4px",
                  textAlign: "center",
                }}
              >
                Semester {index + 1}
              </h3>
              <div
                style={{ display: "flex", flexDirection: "column", gap: "8px" }}
              >
                {semester.courses.map((course) => (
                  <div
                    key={course.code}
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      alignItems: "center",
                      backgroundColor: "#eae8e6",
                      borderRadius: "4px",
                      marginBottom: "5px",
                      paddingRight: "6px",
                    }}
                  >
                    <div>
                      <div
                        style={{
                          fontWeight: "750",
                          color: "rgb(58, 30, 26",
                          textAlign: "left",
                          paddingLeft: "6px",
                          paddingRight: "6px",
                        }}
                      >
                        {course.code}
                      </div>
                      <div
                        style={{
                          paddingLeft: "6px",
                          paddingRight: "6px",
                          fontSize: "14px",
                          color: "rgb(58, 30, 26",
                          textAlign: "left",
                        }}
                      >
                        {course.name}
                      </div>
                    </div>
                    <button
                      className="button"
                      onClick={() => removeCourse(course.code, index)}
                      style={{
                        fontWeight: "750",
                        paddingLeft: "4px",
                        paddingRight: "4px",
                        backgroundColor: "#eae8e6",
                        color: "#dc3545",
                        border: "none",
                        borderRadius: "4px",
                        cursor: "pointer",
                        width: "20px",
                        height: "20px",
                        display: "inline-flex",
                        alignItems: "center",
                        justifyContent: "center",
                        marginLeft: "auto",
                        textAlign: "center",
                        fontSize: "16px",
                      }}
                    >
                      x
                    </button>
                  </div>
                ))}
                {semester.courses.length === 0 && (
                  <div
                    style={{
                      padding: "12px",
                      textAlign: "center",
                      color: "gr",
                      backgroundColor: "#eae8e6",
                      borderRadius: "4px",
                      fontStyle: "italic",
                    }}
                  >
                    No courses added
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Error Message */}
      {error && (
        <div
          style={{
            position: "fixed",
            bottom: "20px",
            right: "20px",
            backgroundColor: "#dc3545",
            color: "white",
            padding: "12px 20px",
            borderRadius: "4px",
            zIndex: 1002,
          }}
        >
          {error}
        </div>
      )}
    </div>
  );
}
