import React from 'react';

export default function LoadingSpinner() {
    return (
      <div style={{
        position: 'relative',
        minHeight: '80vh',
        backgroundColor: '#beb3ad',  // Changed to match the page background color
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        margin: '20px',
        borderRadius: '4px',  // add slight rounding to match other components
      }}>
        <div style={{
          width: '64px',
          height: '64px',
          border: '4px solid rgb(58, 30, 26)',
          borderTopColor: 'transparent',
          borderRadius: '50%',
          animation: 'spin 1s linear infinite',
        }} />
        <p style={{
          marginTop: '16px',
          color: 'rgb(58, 30, 26)',
          fontSize: '18px',
        }}>
          Loading Course Bank...
        </p>
        <style>
          {`
            @keyframes spin {
              to {
                transform: rotate(360deg);
              }
            }
          `}
        </style>
      </div>
    );
  }