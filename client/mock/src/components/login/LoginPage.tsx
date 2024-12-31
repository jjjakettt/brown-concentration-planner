import React from 'react';
import { SignInButton } from "@clerk/clerk-react";
import "../../styles/login.css";

const LoginPage = () => {
  return (
    <div style={{
      minHeight: '100vh',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      padding: '20px',
      backgroundColor: 'rgb(151, 132, 123)',
      position: 'relative',
      overflow: 'hidden'
    }}>
      {/* Coffee steam animation */}
      <div style={{
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        zIndex: 0
      }}>
        <div className="steam-container">
          <div className="steam steam1"></div>
          <div className="steam steam2"></div>
          <div className="steam steam3"></div>
          <div className="steam steam4"></div>
        </div>
      </div>

      {/* Main content */}
      <div style={{
        textAlign: 'center',
        position: 'relative',
        zIndex: 1,
        backgroundColor: 'rgba(58, 30, 26, 0.9)', 
        padding: '40px',
        borderRadius: '15px',
        boxShadow: '0 10px 25px rgba(0, 0, 0, 0.2)',
        maxWidth: '600px',
        width: '100%'
      }}>
        <h1 style={{
          color: 'rgb(151, 132, 123)',
          fontSize: '2.5rem',
          marginBottom: '20px',
          animation: 'fadeIn 1.5s ease-out'
        }}>
          Welcome to Brown Concentration Planner
        </h1>
        
        <p style={{
          color: 'rgb(151, 132, 123)',
          fontSize: '1.2rem',
          marginBottom: '30px',
          animation: 'slideUp 1.5s ease-out'
        }}>
          Please sign in with your Brown Account to start planning your academic journey
        </p>

        <div style={{
          animation: 'fadeIn 2s ease-out'
        }}>
        <SignInButton>
        <button 
            className="sign-in-button"  // Move hover styles to CSS
            style={{
            backgroundColor: 'rgb(58, 30, 26)',
            color: 'rgb(151, 132, 123)',
            padding: '12px 24px',
            border: 'none',
            borderRadius: '8px',
            fontSize: '1.1rem',
            cursor: 'pointer',
            transition: 'all 0.3s ease'
            }}
        >
            Sign In with Brown Account
        </button>
        </SignInButton>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;