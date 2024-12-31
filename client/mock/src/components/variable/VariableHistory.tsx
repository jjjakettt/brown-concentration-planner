import "../../styles/main.css";
import { histEntry } from "./Variable";

interface VariableHistoryProps {
  history: string;
  format: string;
}

/**
 * The SelectHistory component displays a table based on the user's selection from the Select component. 
 * It takes a history prop (a string representing the selected table name), 
 * retrieves the corresponding data using getTable, and renders the table if the data is available. 
 * If no selection is made or no data is found for the selected table, it shows an appropriate message: 
 * `Please select a table to view` 
 * `No data available for the selected history`.
 * The component adjusts its font size and layout based on the screen width to ensure responsive design.
 */
export function VariableHistory(props: VariableHistoryProps) {
  const key = props.history;
  console.log(key);
  const format = props.format;

  if (!key) {
    return <div aria-label="variable message default">Please input a valid variable, state, and county.</div>;
  }

  return (
    <div className="variable-history" >
        <h3>Result: {key}</h3>

    </div>
  );
}
