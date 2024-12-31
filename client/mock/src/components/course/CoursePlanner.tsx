import { Dispatch, SetStateAction, useState, useEffect } from "react";
// import { Course, mockCourses } from "../../mockCourseData";
import { useUser } from "@clerk/clerk-react";
import { CourseBank } from "./CourseBank";
import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
import {
  doc,
  setDoc,
  addDoc,
  getDoc,
  getDocs,
  collection,
  deleteDoc,
} from "firebase/firestore";

// Interface for Course object
interface Course {
  name: string;
  code: string;
  professor: string;
  description: string;
  term: "Fall" | "Spring"; // Strict typing for terms
  user: string;
  semester: number;
}


const firebaseConfig = {
  apiKey: "AIzaSyA6f23sunCFFNHF1l6Lq9SYtJRr90dd14Q",
  authDomain: "maps-ccahill5.firebaseapp.com",
  projectId: "maps-ccahill5",
  storageBucket: "maps-ccahill5.firebasestorage.app",
  messagingSenderId: "528453798675",
  appId: "1:528453798675:web:c2d1dbeb0712735ad0e8e6",
  measurementId: "G-NNKCWFK4H5",
};

const app = initializeApp(firebaseConfig);
const db = getFirestore(app);

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

  // added for Firebase
  const { user } = useUser();
  const userId = user?.id || "admin";
  const [history, setHistory] = useState<Course[]>([]);
  
   // mapping course codes to their firebase doc id
  const [docIDs, setDocIDs] = useState<Map<String, string>>(new Map());

  const [error, setError] = useState<string | null>(null);

  // function to check if a partial course is a real Course, not undefined
  function isFullCourse(course: Partial<Course>): course is Course {
    return (
      typeof course.name === 'string' &&
      typeof course.code === 'string' &&
      typeof course.professor === 'string' &&
      typeof course.description === 'string' &&
      (course.term === 'Fall' || course.term === 'Spring') &&
      typeof course.user === 'string' &&
      typeof course.semester === 'number'
    );
  }

  useEffect(() => {
  const fetchSemesters = async () => {
    try {
      // Fetch Firestore data

      // Create a base array of 8 empty semesters
      const baseSemesters: Semester[] = Array(8).fill(null).map(() => ({ courses: [] }));
      let fetchedSemesters: Semester[] = Array(8).fill(null).map(() => ({ courses: [] }));
    

      for (let i = 1; i < 9; i++) {

        const semestersCollectionRef = collection(db, i.toString());
        const querySnapshot = await getDocs(semestersCollectionRef);

        if (querySnapshot.empty) {
          // If there are not any courses in this collection (or this collection doesn't exist)
          // then skip to the next semester i
          continue
        }

        // Map through the documents to extract the semester data
        const fetchedCourses = querySnapshot.docs.map((doc) => {
          const data = doc.data() as Partial<Course>;
          const docId = doc.id;

        // adding this course's code and its document ID to the map
        if (isFullCourse(data)) {
          docIDs.set(data.code, docId);
          setDocIDs((prev) => {
            const newMap = new Map(prev);
            newMap.set(data.code, docId);
            return newMap;
          });
        }
  
        return { data };

        });

        const courses: Course[] = fetchedCourses.map((item) => {
          const data = item.data;
          if (isFullCourse(data)) {
            return data;
          } else {
            throw new Error("Invalid course data");
          }
        });

        const semester: Semester = {
          courses: courses
        };

        if (i < fetchedSemesters.length) {
          fetchedSemesters[i-1] = semester;
        }
      }

        // Overlay the fetched semesters onto the base semesters
        fetchedSemesters.forEach((semester, index) => {
          if (index < baseSemesters.length) {
            baseSemesters[index] = semester;
          }
        });
      
      // Update state with the merged semesters
      setSemesters(baseSemesters);

    } catch (error) {
      console.error("Error fetching semesters:", error);
    }
  };

  fetchSemesters();
}, []);

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

    async function onCourseClick(course: Course, semesterInd: number) {
      try {
        const doc = await addDoc(collection(db, semesterInd.toString()), course);
    
        const docId = doc.id;

        // adding this course's code and its document ID to the map
        docIDs.set(course.code, docId);
        setDocIDs((prev) => {
          const newMap = new Map(prev);
          newMap.set(course.code, docId);
          return newMap;
        });

        setHistory([...history, course]);
      } catch (error) {
        console.error("Error writing document: ", error);
      }
    }

    setSemesters((prev) => {
      const newSemesterInd = semesterIndex + 1;
      const updatedCourse = { ...course, semester: newSemesterInd };
      const newSemesters = [...prev];
      newSemesters[semesterIndex] = {
        courses: [...newSemesters[semesterIndex].courses, updatedCourse],
      };
        onCourseClick(updatedCourse, newSemesterInd);
      return newSemesters;
    });
    return true;
  };

  async function deleteCourse(courseCode: string, semesterInd: number) {
    try {
      const docId = docIDs.get(courseCode); // This now works correctly.
      if (!docId) {
        throw new Error(`No doc ID found for course code: ${courseCode}`);
      }
      const docRefToDelete = doc(db, semesterInd.toString(), docId);

      console.log("Document id to delete:", docId);
  
      // remove the course and its docID from docIDs
      setDocIDs((prev) => {
        const newMap = new Map(prev);
        newMap.delete(courseCode);
        return newMap;
      });

      const newHistory = [...history];

      // filtering out the course that was just deleted from the history
      newHistory.filter((course) => course.code !== courseCode)
      setHistory(newHistory);

      await deleteDoc(docRefToDelete);

    } catch (error) {
      console.error("Error deleting document:", error);
    }
  }

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

    console.log("doc ids in remove course, before calling delete: ", docIDs)
      const docId = docIDs.get(courseCode);
      if (docId) {
        deleteCourse(courseCode, semesterIndex);
      } else {
        console.warn("Course code not found in docIDs map:", courseCode);
      }
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
