import React, { Dispatch, SetStateAction, useState } from "react";

/**
 * An interface for FileInput.
 *
 * @params
 * addTableName: function to add a new table name to the dropdown options
 */
interface FileInputProps {
  addTableName: Dispatch<SetStateAction<string[]>>;
}

export function FileInput({ addTableName }: FileInputProps) {
  const [filename, setFilename] = useState("");

  const handleAddTable = () => {
    if (filename) {
      addTableName((prev) => [...prev, filename]);
      setFilename(""); // Clear the input after adding
    }
  };

  return (
    <div className="file-input-container">
      <input
        type="text"
        value={filename}
        onChange={(e) => setFilename(e.target.value)}
        placeholder="Enter new table name"
      />
      <button onClick={handleAddTable} aria-label="Add Table Name">
        Add Table
      </button>
    </div>
  );
}