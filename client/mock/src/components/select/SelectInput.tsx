import { Dispatch, SetStateAction } from "react";
import "../../styles/main.css";

/**
 * Interface for SelectInput.
 *
 * @params
 * history: the array storing all previous history entries
 * setHistory: function to add new history entry to history array
 */
interface SelectInputProps {
  history: string[][];
  messageString: string;
  setHistory: Dispatch<SetStateAction<string[][]>>;
  setMessageString: Dispatch<SetStateAction<string>>;
  setShowCourseBank: Dispatch<SetStateAction<boolean>>;
  showCourseBank: boolean;
}
/**
 * SelectInput component allows users to select a dataset and a visualization format,
 * then submit the selection to view the data.
 *
 * @param props - The properties passed to the component (see SelectInputProps for details)
 * @returns JSX that provides dropdowns for dataset selection and visualization format, and a submit button.
 */
export function SelectInput(props: SelectInputProps) {
  /**
   * Handles the submission of the selected dataset and visualization format.
   * It fetches data from the server and updates the history and visualization state.
   * @param dataset - The selected dataset name.
   * @param visualization - The selected visualization format.
   */
  function handleSubmit(): void {
    props.history.length = 0; // Reset history again
    fetch("http://localhost:3231/requirements")
      .then((response) => response.json())
      .then((json) => {
        if (json.response_type !== "Success") {
          props.setMessageString("Data could not be viewed");
        } else {
          props.setHistory(json.data);
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
          paddingRight: "20px"
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
