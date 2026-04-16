# Football App

A web application for managing football match results, built with Jakarta EE (Servlets), JPA/EclipseLink for persistence, and an embedded Apache Derby database.

---

## Features

- Add match results (two teams + goals scored)
- Browse the full history of recorded matches
- View team statistics (wins and total matches played)
- Automatically load initial data from `matches.txt` on server startup
- Remember the last entered Team A name via a browser cookie

---

## Technology Stack

| Layer | Technology |
|---|---|
| Runtime | Jakarta EE (Servlet API) |
| Persistence | JPA 3.0 (EclipseLink) |
| Database | Apache Derby (embedded) |
| Build | Maven |
| Utilities | Lombok |

---

## Project Structure

```
src/
├── pl/polsl/model/
│   ├── Model.java            # Facade — business logic, bridges servlets and DB
│   ├── BaseModel.java        # Base class with in-memory match list
│   ├── MatchRecord.java      # Immutable record representing a single match
│   ├── MatchEntity.java      # JPA entity mapped to the database
│   ├── TournamentEntity.java # JPA entity — groups matches into a tournament
│   ├── DbManager.java        # Singleton — manages EntityManagerFactory
│   └── package-info.java     # Package-level Javadoc
├── servlets/
│   ├── InitServlet.java      # Loads matches.txt on startup (loadOnStartup = 1)
│   ├── MatchServlet.java     # GET/POST /match — add match, show history
│   ├── MatchesServlet.java   # GET /matches — simple match table
│   ├── StatsServlet.java     # GET /stats — team statistics
│   └── NewServlet.java       # Auto-generated template servlet
└── webapp/
    ├── index.html            # Main menu
    ├── add_match.html        # Add match form
    └── show_matches.html     # Match list template
resources/
├── META-INF/persistence.xml  # JPA configuration (Derby, EclipseLink)
└── db.properties             # DB settings: persistence unit name, default tournament
WEB-INF/
└── matches.txt               # Initial match data loaded on startup
```

---

## Configuration

### `db.properties`
Located on the classpath. Required keys:

```properties
db.pu.name=FootballPU
db.default.tournament=Default Tournament
```

### `persistence.xml`
Configures the `FootballPU` persistence unit using an embedded Derby database (`footballDB`). The schema is **dropped and recreated on every startup** (`drop-and-create`), so all data is reset each time the server restarts.

> To persist data across restarts, change `javax.persistence.schema-generation.database.action` to `create` or `none` in `persistence.xml`.

---

## URL Endpoints

| URL | Method | Description |
|---|---|---|
| `/index.html` | GET | Main menu |
| `/match` | GET | Show match form and match history |
| `/match` | POST | Submit a new match result |
| `/matches` | GET | Plain match table |
| `/stats` | GET | Team statistics |
| `/add_match.html` | GET | Static add-match form |

---

## Initial Data (`matches.txt`)

Place the file in `WEB-INF/matches.txt`. Each line represents one match in the following format:

```
TeamA TeamB goalsA goalsB
```

Example:
```
Poland France 1 1
Germany Brazil 2 3
Spain Italy 0 0
```

Lines with invalid format or negative goal values are skipped with a warning logged to stderr.

---

## Data Model

### `MatchRecord` (record)
An immutable value object used in the view layer. Provides `getSummary()` (e.g. `"Poland wins"` or `"Draw"`) and `getResultEnum()` returning `TEAM_A_WIN`, `TEAM_B_WIN`, or `DRAW`.

### `MatchEntity` / `TournamentEntity` (JPA entities)
Persisted to Derby. Each match belongs to a single tournament. `DbManager` automatically creates the default tournament (configured in `db.properties`) if it does not exist yet.

---

## Running the Application

1. Build the project with Maven:
   ```bash
   mvn clean package
   ```
2. Deploy the generated `.war` file to a Jakarta EE-compatible server (e.g. Apache Tomcat 10+, GlassFish, Payara).
3. Access the app at:
   ```
   http://localhost:8080/<context-root>/
   ```

---

## Author

Adam Bronikowski
