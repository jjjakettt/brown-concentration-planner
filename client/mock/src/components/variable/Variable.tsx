import { useState } from "react";
import "../../styles/main.css";
import { VariableInput } from "./variableInput";
import { VariableHistory } from "./VariableHistory";

/**
 * A histEntry interface to structure each single output stored in the main output area
 *
 * @params
 * data: the result of running the command; can be string or 2D array holding string
 */
export interface histEntry {
  data: string;
}

/**
 * A modal tab representing a note
 *
 * @params
 * id: an unique note id
 * title: the title of the note
 * content: main content of a note
 */
export interface Tab {
  id: number;
  title: string;
  content: string;
}

/**
 * Builds a Select component object that provides a dropdown to view current datasets available
 *
 * @returns A JSX element that includes a dropdown, after selection, display the dataset in tabular form
 *
 */
export function Variable() {
  const [history, setHistory] = useState<string>("");
  const [format, setFormat] = useState<string>("");

  return (
    <div className="min-h-[95vh] relative">
      <div className="w-full" style={{ width: "100%" }}>
        <div className="select-container" aria-label="Select container">
        <VariableInput history={history} setHistory={setHistory} format={format} setFormat={setFormat} />
        <pre>
            <VariableHistory history={history} format={format} />
          </pre>
        </div>

      </div>
    </div>
  );
}