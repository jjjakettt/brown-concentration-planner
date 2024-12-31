/**
 * A interface for an Error Message
 *
 * @params
 * history: the array storing all previous history entries
 * setHistory: function to add new history entry to history array
 */
interface Message {
    result: string;
    message: any;
    history: any;
    [key: string]: any;

  }


function isError(json: any): json is Message {
    return json.result === "error_internal" || json.result === "error_bad_request" || json.response_type === "error_datasource" || json.result === "error_datasource";
}

function isSuccess(json: any): json is Message {
    return json.result === "success" || json.response_type === "Success";
}

export function fetchVariable(link: string, variable: string) {
    return fetch(link)
    .then(response => response.json())
    .then(json => {
        
        if (isError(json)) {
            return {result: json.result, message: json.message, history: ""} as Message;
        }

        else if (isSuccess(json)) {
            const variable_result = json[variable];
            return {result: json.result, message: null, history:variable_result} as Message;
        }

        else {
            throw new Error("Unexpected response format");
        }
    })
    .catch((error) => {
      console.error("Error in fetch:", error);
    });
    
}

export async function fetchCSV(link: string, newEntry: string): Promise<Message> {
    try {
        const response = await fetch(link);
        const json = await response.json();
        console.log(json);
        console.log(link);

        if (isError(json)) {
            const empty2DArray: string[][] = [];
            console.log('in error block')
            return { result: "", message: json.error_code, history: empty2DArray } as Message;
        } else {
            // Proceed to the second fetch if there is no error
            console.log('in success block')
            const csvResponse = await fetch('http://localhost:3231/viewcsv');
            const csvJson = await csvResponse.json();
            const loaded_table = csvJson.data;
            return { result: newEntry, message: "", history: loaded_table } as Message;
        }
    } catch (error) {
        console.error("Error in fetch:", error);
        const empty2DArray: string[][] = [];
        return { result: "", message: "Fetch error", history: empty2DArray } as Message;
    }
}

export function fetchSearch(link: string) {

    return fetch(link)
      .then(response => response.json())
      .then(json => {
        if (json.response_type === "error_datasource" || !json.match || json.match.length === 0) {
          const emptyArray: string[][] = [];
          const error_message = "No data found for the given search term.";
          console.log(error_message);
          return { result: "", message: error_message, history: emptyArray } as Message;
        } else {
          const searchResults = json.match;
          return { result: "", message: null, history: searchResults } as Message;
        }
      })
      .catch((error) => {
        console.error("Error in fetch:", error);
      });
}