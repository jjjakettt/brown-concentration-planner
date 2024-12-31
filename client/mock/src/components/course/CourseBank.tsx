import React, { useState, useEffect } from "react";
import LoadingSpinner from './LoadingSpinner';

// Interface for Course object
interface Course {
  name: string;
  code: string;
  professor: string;
  description: string;
  term: "Fall" | "Spring"; // Strict typing for terms
}



// Function to fetch courses from backend web scraping
const getListOfLists = async (): Promise<string[][]> => {
  try {
    const response = await fetch("http://localhost:3231/courses");
    const json = await response.json();

    if (json.response_type !== "Success") {
      console.log("Data could not be viewed");
      return [];
    } else {
      return json.data;
    }
  } catch (error) {
    console.error("Error fetching data:", error);
    return [];
  }
};

interface CourseBankProps {
  onBack: () => void;
  semesters: { courses: Course[] }[];
  onAddCourse: (course: Course, semesterIndex: number) => void;
  onRemoveCourse: (courseCode: string, semesterIndex: number) => void;
}

export function CourseBank({
  onBack,
  semesters,
  onAddCourse,
  onRemoveCourse,
}: CourseBankProps) {

  // Loading state
  const [isLoading, setIsLoading] = useState(true);
  const [selectedCourse, setSelectedCourse] = useState<Course | null>(null);
  const [showSemesterSelect, setShowSemesterSelect] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchQuery, setSearchQuery] = useState("");
  const [filteredCourses, setFilteredCourses] = useState<string[][]>([]); // State to store filtered courses
  const [isSearching, setIsSearching] = useState(false);

  // prereqError state to handle prerequisite information display
  const [prereqError, setPrereqError] = useState<{show: boolean, message: string}>({
    show: false,
    message: ''
  });

  // Function to get all completed courses from previous semesters
  const getCompletedCourses = (upToSemesterIndex: number) => {
    return semesters
      .slice(0, upToSemesterIndex)
      .flatMap(semester => semester.courses.map(course => course.code));
  };
  // prerequisite checking function to just fetch and display prerequisites
  const checkPrerequisites = async (courseCode: string) => {
    try {
      const response = await fetch(`http://localhost:3231/check-prerequisites?courseCode=${courseCode}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      const data = await response.json();
      
      if (data.response_type === "success") {
        // Just return the prerequisite text if it exists
        return {
          hasPrereqs: data.missing_prerequisites.length > 0,
          message: data.missing_prerequisites.length > 0 ? data.missing_prerequisites[0] : ""
        };
      }
      return { hasPrereqs: false, message: "" };
    } catch (error) {
      console.error("Error checking prerequisites:", error);
      return { hasPrereqs: false, message: "" };
    }
  };

  // Checks if course is in planner
  const isCourseInPlan = (courseCode: string) => {
    return semesters.some((semester) =>
      semester.courses.some((course) => course.code === courseCode)
    );
  };

  // Function to filter courses based on search query
  const getFilteredCourses = async (query: string): Promise<string[][]> => {
    const courses = await getListOfLists();
    const lowerQuery = query.toLowerCase().trim();

    if (!lowerQuery) return courses;

    return courses.filter(
      (course) =>
        course[0].toLowerCase().includes(lowerQuery) ||
        course[1].toLowerCase().includes(lowerQuery) ||
        course[2].toLowerCase().includes(lowerQuery) ||
        course[3].toLowerCase().includes(lowerQuery)
    );
  };

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setSearchQuery(value);
    setIsSearching(value.trim() !== "");
  };

  const handleClearSearch = () => {
    setSearchQuery("");
    setIsSearching(false);
  };

  // course fetching logic to use caching (jjjakettt)
  useEffect(() => {
    const loadCourses = async () => {
      setIsLoading(true);
      try {
        // Try to get data from localStorage instead of sessionStorage
        const cachedData = localStorage.getItem('courseBankData');
        const cachedTimestamp = localStorage.getItem('courseBankTimestamp');
        const currentTime = Date.now();
        
        // Check if cache is valid (less than 1 hour old)
        if (cachedData && cachedTimestamp && 
            currentTime - parseInt(cachedTimestamp) < 3600000) {
          setFilteredCourses(JSON.parse(cachedData));
        } else {
          // Fetch fresh data
          const courses = await getListOfLists();
          localStorage.setItem('courseBankData', JSON.stringify(courses));
          localStorage.setItem('courseBankTimestamp', currentTime.toString());
          setFilteredCourses(courses);
        }
      } catch (error) {
        console.error('Error loading courses:', error);
      } finally {
        setIsLoading(false);
      }
    };

    loadCourses();
  }, []); // Only run on mount

  // Search effect should respect loading state
  useEffect(() => {
    if (!isLoading) {
      const fetchAndFilterCourses = async () => {
        const filteredData = await getFilteredCourses(searchQuery);
        setFilteredCourses(filteredData);
      };
      fetchAndFilterCourses();
    }
  }, [searchQuery, isLoading]);



  return (
    <div className="course-bank">
      {isLoading ? (
        <LoadingSpinner />
      ) : (
        <>
          {/* Search Section */}
          <div
            style={{
              display: "flex",
              alignItems: "center",
              gap: "12px",
              paddingLeft: "20px",
              paddingRight: "20px",
            }}
          >
            <input
              type="text"
              placeholder="Search courses..."
              value={searchQuery}
              onChange={handleSearchChange}
              style={{
                padding: "8px 12px",
                fontSize: "14px",
                border: "1px solid rgb(151, 132, 123)",
                borderRadius: "4px",
                backgroundColor: "rgba(255, 255, 255, 0.1)",
                color: "rgb(51, 30, 3)",
                width: "300px",
                fontWeight: "300",
              }}
            />
            {isSearching && (
              <button
                onClick={handleClearSearch}
                style={{
                  padding: "8px 16px",
                  backgroundColor: "rgb(58, 30, 26)",
                  color: "#d9d4d3",
                  border: "none",
                  borderRadius: "4px",
                  cursor: "pointer",
                }}
              >
                Show All Courses
              </button>
            )}
          </div>

          {/* Course Table Section */}
          <div
            className="select-history"
            aria-label="Select history visualization"
            style={{ paddingLeft: "20px", paddingRight: "20px" }}
          >
            <table
              style={{
                width: "100%",
                borderCollapse: "collapse",
                color: "rgb(51, 30, 3)",
                border: "#black",
              }}
            >
              <thead>
                <tr>
                  <th
                    style={{
                      textAlign: "left",
                      padding: "12px 8px",
                      borderBottom: "2px solid rgb(51, 30, 3)",
                    }}
                  >
                    Code
                  </th>
                  <th
                    style={{
                      textAlign: "left",
                      padding: "12px 8px",
                      borderBottom: "2px solid rgb(51, 30, 3)",
                    }}
                  >
                    Course
                  </th>
                  <th
                    style={{
                      textAlign: "left",
                      padding: "12px 8px",
                      borderBottom: "2px solid rgb(51, 30, 3)",
                    }}
                  >
                    Professor
                  </th>
                  <th
                    style={{
                      textAlign: "center",
                      padding: "12px 8px",
                      borderBottom: "2px solid rgb(51, 30, 3)",
                    }}
                  >
                    Term
                  </th>
                  <th
                    style={{
                      textAlign: "center",
                      padding: "12px 8px",
                      borderBottom: "2px solid rgb(51, 30, 3)",
                    }}
                  >
                    +/-
                  </th>
                </tr>
              </thead>
              <tbody>
                {filteredCourses.map((course, index) => (
                  <tr key={index} style={{ borderBottom: "1px solid rgb(51, 30, 3)" }}>
                    <td style={{ padding: "12px 8px", textAlign: "left" }}>
                      {course[1]}
                    </td>
                    <td style={{ padding: "12px 8px", textAlign: "left" }}>
                      {course[0]}
                    </td>
                    <td style={{ padding: "12px 8px", textAlign: "left" }}>
                      {course[3]}
                    </td>
                    <td style={{ padding: "12px 8px", textAlign: "center" }}>
                      {course[2]}
                    </td>
                    <td style={{ padding: "12px 8px", textAlign: "center" }}>

                      <button
                        onClick={async () => {
                          if (isCourseInPlan(course[0])) {
                            semesters.forEach((semester, index) => {
                              if (semester.courses.some((c) => c.code === course[0])) {
                                onRemoveCourse(course[0], index);
                              }
                            });
                          } else {
                            // UPDATED: Check for prerequisites and show info if they exist
                            const prereqCheck = await checkPrerequisites(course[1]);
                            
                            // Save the course details for later use
                            const termFromBackend = course[2].trim();
                            const courseTerm = termFromBackend.includes('Fall') ? "Fall" as const : "Spring" as const;
                            setSelectedCourse({
                              name: course[1],
                              code: course[0],
                              professor: course[3],
                              description: "",
                              term: courseTerm
                            });

                            if (prereqCheck.hasPrereqs) {
                              // Show prerequisite information
                              setPrereqError({
                                show: true,
                                message: `This course has the following prerequisite(s): ${prereqCheck.message}`
                              });
                            } else {
                              // No prerequisites - go straight to semester selection
                              setShowSemesterSelect(true);
                            }
                          }
                        }}
                        style={{
                          padding: "4px",
                          backgroundColor: isCourseInPlan(course[0]) ? "#dc3545" : "rgb(58, 30, 26)",
                          color: isCourseInPlan(course[0]) ? "white" : "#d9d4d3",
                          border: "none",
                          borderRadius: "4px",
                          cursor: "pointer",
                          minWidth: "28px",
                          height: "28px",
                          display: "inline-flex",
                          alignItems: "center",
                          justifyContent: "center",
                          marginLeft: "auto",
                          fontSize: "16px",
                        }}
                      >
                        {isCourseInPlan(course[0]) ? "-" : "+"}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}

        {showSemesterSelect && selectedCourse && (
          <div
            style={{
              position: "fixed",
              top: 0,
              left: 0,
              right: 0,
              bottom: 0,
              backgroundColor: "rgba(0,0,0,0.6)",
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              zIndex: 1001,
            }}
          >
                      <div
              style={{
                backgroundColor: "#c5bbb5", // Matching the brown theme
                borderRadius: "8px",
                padding: "24px",
                maxWidth: "400px",
                width: "90%",
              }}
            >
              <h3
                style={{
                  marginBottom: "16px",
                  textAlign: "center",
                  color: "rgb(58, 30, 26)",
                }}
              >
                Select Semester
              </h3>
              <div
                style={{
                  display: "grid",
                  gridTemplateColumns: "1fr 1fr",
                  gap: "12px",
                  marginBottom: "16px", // Added margin for cancel button
                }}
              >
                {semesters.map((semester, index) => {
                  const isSemesterOdd = (index + 1) % 2 !== 0;
                  // Term checking logic
                  const isDisabled =
                    semester.courses.length >= 5 ||
                    (selectedCourse?.term === "Fall" && !isSemesterOdd) ||
                    (selectedCourse?.term === "Spring" && isSemesterOdd);
                  return (
                    <button
                      key={index}
                      onClick={() => {
                        if (selectedCourse) {
                          onAddCourse(selectedCourse, index);
                          setShowSemesterSelect(false);
                        }
                      }}
                      disabled={isDisabled}
                      style={{
                        padding: "8px",
                        backgroundColor: isDisabled
                          ? "#e0e0e0"
                          : "rgb(58, 30, 26)",
                        color: isDisabled ? "#666" : "#dfd9d7",
                        border: "none",
                        borderRadius: "4px",
                        cursor: isDisabled ? "not-allowed" : "pointer",
                        opacity: isDisabled ? 0.7 : 1,
                      }}
                    >
                      Semester {index + 1}
                      {semester.courses.length >= 5 && " (Full)"}
                      {selectedCourse?.term === "Fall" &&
                        !isSemesterOdd &&
                        " (Spring Only)"}
                      {selectedCourse?.term === "Spring" &&
                        isSemesterOdd &&
                        " (Fall Only)"}
                    </button>
                  );
                })}
              </div>
              {/* Cancel Button */}
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                }}
              >
                <button
                  onClick={() => setShowSemesterSelect(false)}
                  style={{
                    padding: "8px 24px",
                    // backgroundColor: "rgb(51, 30, 3)",
                    backgroundColor: "rgb(58, 30, 26)",
                    color: "#dfd9d7",
                    border: "none",
                    borderRadius: "4px",
                    cursor: "pointer",
                    fontSize: "14px",
                  }}
                >
                  Cancel
                </button>
              </div>
            </div>
          </div>
        )}

        {/* prerequisite information modal */}
        {prereqError.show && (
          <div style={{
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(0,0,0,0.6)',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            zIndex: 1001,
          }}>
            <div style={{
              backgroundColor: '#c5bbb5',
              borderRadius: '8px',
              padding: '24px',
              maxWidth: '400px',
              width: '90%',
            }}>
              <h3 style={{
                marginBottom: '16px',
                textAlign: 'center',
                color: 'rgb(58, 30, 26)',
              }}>
                Course Prerequisites
              </h3>
              <p style={{
                marginBottom: '16px',
                textAlign: 'center',
                color: 'rgb(58, 30, 26)',
              }}>
                {prereqError.message}
              </p>
              <div style={{
                display: 'flex',
                justifyContent: 'center',
                gap: '12px'
              }}>
                {/* ADDED: Cancel and Next buttons */}
                <button
                  onClick={() => setPrereqError({show: false, message: ''})}
                  style={{
                    padding: '8px 24px',
                    backgroundColor: 'rgb(58, 30, 26)',
                    color: '#dfd9d7',
                    border: 'none',
                    borderRadius: '4px',
                    cursor: 'pointer',
                    fontSize: '14px',
                  }}
                >
                  Cancel
                </button>
                <button
                  onClick={() => {
                    setPrereqError({show: false, message: ''});
                    setShowSemesterSelect(true);
                  }}
                  style={{
                    padding: '8px 24px',
                    backgroundColor: 'rgb(58, 30, 26)',
                    color: '#dfd9d7',
                    border: 'none',
                    borderRadius: '4px',
                    cursor: 'pointer',
                    fontSize: '14px',
                  }}
                >
                  Next
                </button>
              </div>
            </div>
          </div>
        )}

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