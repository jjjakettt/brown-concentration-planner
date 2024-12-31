import { Dispatch, SetStateAction } from "react";
import "../../styles/main.css";

/**
 * Interface for SelectElements.
 *
 * @params
 * concentrationRequirements: the array storing Computer Science concentration requirements
 * setConcentrationRequirements: function to add Computer Science concentration requirements to concentrationRequirements array
 * messageString: displays error messages
 * setMessageString: updates error messages
 * showCourseBank: displays course bank if true
 * setShowCourseBank: switches between course bank and course planner
 */
interface SelectElementsProps {
  concentrationRequirements: string[][];
  messageString: string;
  setConcentrationRequirements: Dispatch<SetStateAction<string[][]>>;
  setMessageString: Dispatch<SetStateAction<string>>;
  setShowCourseBank: Dispatch<SetStateAction<boolean>>;
  showCourseBank: boolean;
}
/**
 * SelectInput component allows users to select a dataset and a visualization format,
 * then submit the selection to view the data.
 *
 * @param props - The properties passed to the component (see SelectElementsProps for details)
 * @returns JSX that provides concentration planning elements.
 */
export function SelectElements(props: SelectElementsProps) {
  /**
   * Handles the generation of a table of concentration requirements. It fetches data from a back-end server
   * and updates the concentrationRequirements.
   */
  function handleSubmit(): void {
    props.concentrationRequirements.length = 0; // Reset concentrationRequirements again
    fetch("http://localhost:3231/requirements")
      .then((response) => response.json())
      .then((json) => {
        if (json.response_type !== "Success") {
          props.setMessageString("Data could not be viewed");
        } else {
          props.setConcentrationRequirements(json.data);
          props.setMessageString(""); // Clear any error messages
          console.log(history.length);
        }
      });
    1;
  }
  return (
    <div className="dropdown-container" style={{ marginBottom: "20px" }}>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          paddingLeft: "20px",
          paddingRight: "20px",
        }}
      >
        <button
          style={{
            backgroundColor: "rgb(58, 30, 26)",
            color: "#d9d4d3",
          }}
          onClick={() => {
            handleSubmit();
          }}
          className="submit"
          name="submit"
          id="submitButton"
          aria-label="Submit the selected dataset and visualization"
        >
          View Computer Science Concentration Requirements
        </button>

        <button
          style={{
            backgroundColor: "rgb(58, 30, 26)",
            color: "#d9d4d3",
          }}
          onClick={() => props.setShowCourseBank(!props.showCourseBank)}
          className="submit"
        >
          {props.showCourseBank ? "Back to Planner" : "Course Bank"}
        </button>
      </div>
    </div>
  );
}
