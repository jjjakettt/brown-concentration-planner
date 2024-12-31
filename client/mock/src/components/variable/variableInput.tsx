import { Dispatch, SetStateAction, useState } from "react";
import { fetchVariable } from "../../JsonNarrower";

/**
 * A interface for SelectInput.
 *
 * @params
 * history: the array storing all previous history entries
 * setHistory: function to add new history entry to history array
 */
interface VariableInputProps {
  history: string;
  format: string
  setHistory: Dispatch<SetStateAction<string>>;
  setFormat: Dispatch<SetStateAction<string>>;
}

/**
 * The SelectInput component allows users to choose a table from a dropdown menu and 
 * submit their selection to display its data. Everytime setHistory is called, 
 * the `Select` function re renders bevause the Hook is set there, later calling again the selectHistory function.
 */
export function VariableInput(props: VariableInputProps) {
  
  /**
   * Function that is called when a user click the submit button to display a new output
   *
   * @param file the file selected by the user
   */
  
  const [variableInput, setVariableInput] = useState("");
  const [stateInput, setStateInput] = useState("");
  const [countyInput, setCountyInput] = useState("");
  const [error, setError] = useState<string | null>(null);



  function handleVariable() {
    if (!variableInput.trim() || !stateInput.trim() || !countyInput.trim()) {
      setError("Missing required query parameters: variable, state, county");
      return;
    }
    async function variableHandler() {
      try {
          const result = await fetchVariable(
              `http://localhost:3230/variable?input_variable=${variableInput}&state=${encodeURIComponent(stateInput)}&county=${encodeURIComponent(countyInput)}`,
              variableInput
          );
          if (result) {
              console.log(result);
              props.setHistory(result.history);
              setError(result.message);
          }
      } catch (error) {
          console.error("Error:", error);
      }
  }
  variableHandler();
      
  }


  return (
    
    <div

    >
      <input
        type="text"
        value={variableInput}
        onChange={(e) => setVariableInput(e.target.value)}
        placeholder="Enter Variable Name"
        className="text-input"
        aria-label="variable-name-input"
      />
<input
        type="text"
        value={stateInput}
        onChange={(e) => setStateInput(e.target.value)}
        placeholder="Enter State"
        className="text-input"
        aria-label="state-input"
      />
      <input
        type="text"
        value={countyInput}
        onChange={(e) => setCountyInput(e.target.value)}
        placeholder="Enter County"
        className="text-input"
        aria-label="county-input"
      />
      <button
        className="variable-submit-button"
        onClick={handleVariable}
        aria-label="Submit Variable"
      >
        Submit Variable
      </button>
      <button
        className="variable-broadband-button"
        onClick={() => setVariableInput("S2802_C03_022E")}
        aria-label="Broadband Variable"
      >
        Broadband
      </button>

      {error && <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>}


    </div>
    
  );
}