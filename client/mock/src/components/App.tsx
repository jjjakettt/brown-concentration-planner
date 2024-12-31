import { useState } from "react";
import "../styles/App.css";
import React, { useEffect } from "react";
import { CoursePlanner } from "./course/CoursePlanner";
import { SelectElements } from "./select/SelectElements";
import { RequirementsTable } from "./select/RequirementsTable";
import LoginPage from "./login/LoginPage";

import {
  SignedIn,
  SignedOut,
  SignInButton,
  SignOutButton,
  useUser,
  useClerk,
} from "@clerk/clerk-react";

/**
 * This is the highest level of Mock which builds the component APP;
 *
 * @return JSX of the entire mock
 *  Note: if the user is loggedIn, the main interactive screen will show,
 *  else it will stay at the screen prompting for log in
 */
function App({}: { children: React.ReactNode; modal: React.ReactNode }) {
  /**
   * A state tracker for if the user is logged in and
   *  a function to update the logged-in state
   */
  const { user } = useUser();
  const { signOut } = useClerk();
  const [history, setHistory] = useState<string[][]>([]);
  const [messageString, setMessageString] = useState<string>("");
  const [showCourseBank, setShowCourseBank] = useState(false);

  // Check if the user has a Brown University email after they sign in
  useEffect(() => {
    if (user) {
      const hasBrownEmail = user.emailAddresses.some((email) =>
        email.emailAddress.endsWith("@brown.edu")
      );

      // If user does not have a Brown email, sign them out
      if (!hasBrownEmail) {
        alert("Access restricted to users with a @brown.edu email address.");
        signOut();
      }
    }
  }, [user, signOut]);

  // clears the cache after logout, this ensures that the User has upto date courses and prerequisites (jjjakettt)
  const clearCacheAfterLogout = () => {
    localStorage.removeItem("courseBankData");
    localStorage.removeItem("courseBankTimestamp");
  };

  return (
    <div className="App">
      <SignedOut>
        <LoginPage />
      </SignedOut>
      {/* Display main content only if signed in */}
      <SignedIn>
        <div className="masthead">
          <div className="wrap">
            <a href="http://www.brown.edu" title="Brown University Homepage">
              <img src="images/brown-logo.png" />
            </a>
            BROWN UNIVERSITY
          </div>
          {/* Sign-in and Sign-out controls */}
          <div className="button-container">
            <SignedIn>
              <SignOutButton>
                <button
                  onClick={clearCacheAfterLogout}
                  style={{
                    backgroundColor: "rgb(58, 30, 26)",
                    color: "#d9d4d3",
                  }}
                >
                  Sign Out
                </button>
              </SignOutButton>
            </SignedIn>
          </div>
        </div>
        <h1 aria-label="Mock Header"></h1>
        <div className="content" style={{ marginTop: "20px" }}>
          <SelectElements
            concentrationRequirements={history}
            setConcentrationRequirements={setHistory}
            setMessageString={setMessageString}
            messageString={messageString}
            showCourseBank={showCourseBank}
            setShowCourseBank={setShowCourseBank}
          />
          <div aria-live="polite" aria-atomic="true">
            {messageString}
          </div>
          <div className="side-by-side">
            {history.length > 0 && (
              <div
                className="select-container"
                aria-label="Select container for datasets and history"
                style={{
                  width: "5%", // Only visible when history is empty
                  transition: "width 0.3s ease", // Smooth transition
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                }}
              >
                <RequirementsTable
                  requirements={history}
                  setMessageString={setMessageString}
                  messageString={messageString}
                />
              </div>
            )}
            <CoursePlanner
              setShowCourseBank={setShowCourseBank}
              showCourseBank={showCourseBank}
            />
          </div>
        </div>
      </SignedIn>
    </div>
  );
}
export default App;
