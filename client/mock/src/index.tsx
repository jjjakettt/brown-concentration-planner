import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from "./components/App"
import './styles/index.css'
import { ClerkProvider } from "@clerk/clerk-react";

// Import your publishable key
const PUBLISHABLE_KEY = import.meta.env.VITE_CLERK_PUBLISHABLE_KEY
//const PUBLISHABLE_KEY = 'pk_test_bG9naWNhbC1wdWctMzkuY2xlcmsuYWNjb3VudHMuZGV2JA'


if (!PUBLISHABLE_KEY) {
  throw new Error('Missing Publishable Key')
}


createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ClerkProvider publishableKey={PUBLISHABLE_KEY} afterSignOutUrl="/">
      <App />
    </ClerkProvider>
  </StrictMode>,
)
