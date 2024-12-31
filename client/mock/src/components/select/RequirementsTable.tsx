import "../../styles/main.css";
import { Dispatch, SetStateAction } from "react";
import "chart.js/auto";

/**
 * Interface for the props that are passed into RequirementsTableProps.
 *
 * @params
 * requirements: an array holding concentration requirements.
 * messageString: displays error messages
 * setMessageString: updates error messages
 */
interface RequirementsTableProps {
  requirements: string[][];
  messageString: string;
  setMessageString: Dispatch<SetStateAction<string>>;
}

/**
 * Handles the generation of a table of concentration requirements using data previously fetched from a back-end server.
 *
 * @param props - The requirements entries (see SelectHistoryProps for more details)
 * @returns JSX that will print a tabular view of the passed-in data
 */
export function RequirementsTable(props: RequirementsTableProps) {
  /**
   * Creates a table from the provided dataset.
   * @param inputDataset - The requirements dataset to display in a table format.
   * @returns JSX for rendering the table.
   */
  function createTable(inputDataset: Array<Array<string>>) {
    const pastelColors = [
      "rgba(255, 255, 255, 0.5)",
      "rgba(255, 255, 255, 0.8)",
    ];

    const trimmedDataset = inputDataset.slice(
      0,
      inputDataset.findIndex((row) => row[0] === "Total Credits") + 1
    );

    const groups: {
      rows: { row: Array<string>; isNumber: boolean }[];
      color?: string;
    }[] = [];
    let currentGroup: {
      rows: { row: Array<string>; isNumber: boolean }[];
      color?: string;
    } | null = null;

    trimmedDataset.forEach((row) => {
      if (row[0] === "New Option") {
        if (currentGroup) {
          groups.push(currentGroup);
        }
        currentGroup = { rows: [] };
      } else {
        const rightCell = row[row.length - 1];
        const isSingleNumber = /^\d+$/.test(rightCell);

        if (isSingleNumber) {
          if (currentGroup) {
            groups.push(currentGroup);
            currentGroup = null;
          }
          groups.push({
            rows: [{ row, isNumber: true }],
            color: /^\d+$/.test(row[row.length - 1])
              ? "rgb(51, 30, 3)"
              : "rgb(58, 30, 26)",
          });
        } else if (currentGroup) {
          currentGroup.rows.push({ row, isNumber: false });
        }
      }
    });

    if (currentGroup) {
      groups.push(currentGroup);
    }

    groups.forEach((group) => {
      if (group.color !== "rgb(51, 30, 3)") {
        group.rows.sort((a, b) =>
          a.row[0].toString().localeCompare(b.row[0].toString())
        );
      }
    });

    const sortedGroups: typeof groups = [];
    let pastelSegment: typeof groups = [];

    groups.forEach((group) => {
      if (group.color === "rgb(51, 30, 3)") {
        if (pastelSegment.length > 0) {
          pastelSegment.sort((a, b) => {
            const firstRowA = a.rows[0]?.row[0]?.toString() || "";
            const firstRowB = b.rows[0]?.row[0]?.toString() || "";
            return firstRowA.localeCompare(firstRowB);
          });
          sortedGroups.push(...pastelSegment);
          pastelSegment = [];
        }
        sortedGroups.push(group);
      } else {
        pastelSegment.push(group);
      }
    });

    if (pastelSegment.length > 0) {
      pastelSegment.sort((a, b) => {
        const firstRowA = a.rows[0]?.row[0]?.toString() || "";
        const firstRowB = b.rows[0]?.row[0]?.toString() || "";
        return firstRowA.localeCompare(firstRowB);
      });
      sortedGroups.push(...pastelSegment);
    }

    let pastelColorIndex = 0;
    sortedGroups.forEach((group) => {
      if (group.color !== "rgb(51, 30, 3)") {
        group.color = pastelColors[pastelColorIndex];
        pastelColorIndex = (pastelColorIndex + 1) % pastelColors.length;
      }
    });

    return (
      <div
        style={{ overflowX: "auto", overflowY: "auto", marginBottom: "24px" }}
      >
        <table
          className="table"
          aria-label="Data table"
          style={{
            width: "100%",
            borderCollapse: "collapse",
            borderColor: "transparent",
            fontSize: "9px",
          }}
        >
          <tbody>
            {sortedGroups.flatMap((group, groupIndex) =>
              group.rows.map(({ row, isNumber }, rowIndex) => (
                <tr
                  key={`${groupIndex}-${rowIndex}`}
                  style={{
                    backgroundColor: isNumber ? "rgb(58, 30, 26)" : group.color,
                    fontWeight: isNumber ? "bold" : "normal",
                    fontSize: isNumber ? "15px" : "14px",
                    color:
                      group.color === "rgb(51, 30, 3)" ? "#dfd9d7" : "inherit",
                  }}
                >
                  {isNumber ? (
                    <td
                      colSpan={row.length}
                      style={{
                        textAlign: "left",
                        padding: "8px",
                        border: "1px solid rgb(151, 132, 123)",
                      }}
                    >
                      <div
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                        }}
                      >
                        <span>{row[0]}</span>
                        <span style={{ textAlign: "right" }}>
                          {row[row.length - 1]}
                        </span>
                      </div>
                    </td>
                  ) : (
                    row.map((cell, cellIndex) => (
                      <td
                        key={cellIndex}
                        style={{
                          border: "1px solid rgb(151, 132, 123)",
                          padding: "8px",
                        }}
                      >
                        {cell}
                      </td>
                    ))
                  )}
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    );
  }
  return (
    <div
      className="select-history"
      aria-label="Select history visualization"
      style={{ paddingLeft: "20px", paddingRight: "20px" }}
    >
      {props.requirements.length !== 0 ? (
        <div>{createTable(props.requirements)}</div>
      ) : null}
    </div>
  );
}
