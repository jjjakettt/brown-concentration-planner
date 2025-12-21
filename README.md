# Brown Concentration Planner

A full-stack web application designed to help Brown University students plan courses over four years to fulfill computer science concentration requirements.

## Features

- **Course Planning**: Drag-and-drop interface for planning courses across semesters
- **Concentration Requirements**: Browse and track CS concentration requirements
- **Prerequisites Checking**: Automatic validation of course prerequisites
- **Course Bank**: Searchable catalog of available CS courses
- **Data Persistence**: Firebase integration to save and sync planning data
- **Brown Authentication**: Secure login restricted to @brown.edu email addresses
- **Real-time Updates**: Live synchronization of course selections across sessions

## Technology Stack

**Frontend:**
- React 18 + TypeScript
- Vite (build tool)
- Clerk (authentication)
- Firebase (data storage)
- Chart.js (visualization)
- shadcn-ui (components)

**Backend:**
- Java 17
- Spark Framework (HTTP server)
- Moshi (JSON serialization)
- jsoup (web scraping)
- Maven (build management)

## Prerequisites

- Node.js 16+ and npm
- Java 17+
- Maven
- Firebase account (for data storage)
- Clerk account (for authentication)

## Installation

### 1. Clone the repository
```bash
git clone https://github.com/jjjakettt/brown-concentration-planner.git
cd brown-concentration-planner
```

### 2. Install root dependencies
```bash
npm install
```

### 3. Setup Frontend
```bash
cd client/mock
npm install
```

Create `.env.local` file in `client/mock/`:
```env
VITE_CLERK_PUBLISHABLE_KEY=your_clerk_publishable_key
VITE_FIREBASE_API_KEY=your_firebase_api_key
VITE_FIREBASE_AUTH_DOMAIN=your_firebase_auth_domain
VITE_FIREBASE_PROJECT_ID=your_firebase_project_id
VITE_FIREBASE_STORAGE_BUCKET=your_firebase_storage_bucket
VITE_FIREBASE_MESSAGING_SENDER_ID=your_firebase_messaging_sender_id
VITE_FIREBASE_APP_ID=your_firebase_app_id
```

### 4. Setup Backend
```bash
cd server
```

Place your `firebase_config.json` in `server/src/main/resources/`.

Build the project:
```bash
mvn clean install
```

## Running the Application

### Start the Backend (Port 3231)
```bash
cd server
mvn exec:java
```

### Start the Frontend (Port 8000)
```bash
cd client/mock
npm run dev
```

Access the application at `http://localhost:8000`

## Testing

### Frontend Tests
```bash
cd client/mock
npm run test
```

### E2E Tests (Playwright)
```bash
cd client/mock
npm run test:e2e
```

### Backend Tests
```bash
cd server
mvn test
```

## API Endpoints

- `GET /courses` - Fetch available CS courses
- `GET /requirements` - Fetch concentration requirements
- `GET /check-prerequisites?courseCode=CSCI0330` - Check prerequisites for a course

## Project Structure

```
brown-concentration-planner/
├── client/mock/          # React frontend
│   ├── src/
│   │   ├── components/   # React components
│   │   └── styles/       # CSS stylesheets
│   └── vite.config.ts
├── server/               # Java backend
│   ├── src/main/java/
│   │   ├── Server/       # HTTP handlers
│   │   └── WebScraper/   # Data scrapers
│   └── pom.xml
└── README.md
```

## Development Team

- Long (Jake) Tran - [@jjjakettt](https://github.com/jjjakettt)
- Caroline Cahill - [@caroline-cahill](https://github.com/caroline-cahill)
- William Tolmie - [@willtolmie](https://github.com/willtolmie)
- Tiffany Tran - [@tiffanytran1](https://github.com/tiffanytran1)

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit changes (`git commit -m 'Add your feature'`)
4. Push to branch (`git push origin feature/your-feature`)
5. Open a Pull Request

## License

This project is for educational purposes at Brown University.

## Architecture

For detailed technical architecture and design decisions, see [ARCHITECTURE.md](ARCHITECTURE.md).
