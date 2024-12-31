import { Dispatch, SetStateAction, KeyboardEvent } from "react";
import { useState, useEffect } from "react";

/**
 * An interface for logged-in state for mock.
 *
 * @params
 * isLoggedIn: true if the user is logged in, false otherwise
 * setIsLoggedIn: to update the state of isLoggedIn
 */
interface loginProps {
  isLoggedIn: boolean;
  setIsLoggedIn: Dispatch<SetStateAction<boolean>>;
}

/**
 * Builds a component that manages the login button and end-user's logged-in state.
 *
 * @param props to access logged-in state (see interface loginProps for more details)
 * @returns JSX to let user know they can sign out if they are logged in
 *  or log-in if they are not logged in
 */
export function LoginButton(props: loginProps) {
  /**
   * Function to manage authentication;
   *  if the user is logged in, the user's log-in state will update to not logged in
   *  if the user is not logged in, the user's log-in state will update to logged in
   *
   * @returns whether they are logged in or not
   */
  const [password, setPassword] = useState("");
  const [placeHolder, setPlaceHolder] = useState("Enter password");

  const authenticate = () => {
    const pass = "aaa";
    if (password === pass) {
      const newValue = !props.isLoggedIn;
      props.setIsLoggedIn(newValue);
      return newValue;
    } else if (password === "") {
      setPlaceHolder("Password field is empty.");
    } else {
      setPassword("");
      setPlaceHolder("Password is incorrect.");
    }
  };

  const handleSignOut = () => {
    setPassword("");
    authenticate();
  };

  const handleKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter" && !props.isLoggedIn) {
      // Only authenticate if the user is not logged in
      authenticate();
    }
  };

  if (props.isLoggedIn) {
    return (
      <button aria-label="Sign Out" onClick={handleSignOut}>
        Sign out
      </button>
    );
  } else {
    return (
      <span>
        <input
          type="password"
          aria-label="password"
          placeholder={placeHolder}
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          onKeyDown={handleKeyDown} // Attach the event handler here
          style={{
            borderRadius: "8px",
            border: "1px solid transparent",
            padding: "0.6em 1.2em",
            fontSize: "1em",
            fontWeight: "500",
            fontFamily: "inherit",
            backgroundColor: "#1a1a1a",
            color: "white",
            marginRight: "10px",
            cursor: "text",
            boxSizing: "border-box",
          }}
        />
        <button aria-label="Login" onClick={authenticate}>
          Login
        </button>
      </span>
    );
  }
}