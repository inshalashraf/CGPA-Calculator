
GradeHub is a desktop CGPA (Cumulative Grade Point Average) management application written in Java. It provides a Swing-based GUI for managing student records, calculating CGPA, and storing data in a MySQL database. The project uses Maven for build and dependency management.

## Features
- Manage student records (add, edit, delete)
- Compute semester and cumulative CGPA
- Simple role-based faculty access (configured via properties)
- Modern Swing look-and-feel (FlatLaf) and responsive layouts (MigLayout)

## Quick facts / tech
- Language: Java 17
- Build tool: Maven
- UI: Java Swing (FlatLaf + MigLayout)
- Database: MySQL (via JDBC)

## Repository layout

- `src/main/java` — application source code
- `src/main/resources` — configuration templates and resource files
- `lib/` — included third-party JARs required at build/runtime (example: TimingFramework)
- `pom.xml` — Maven project descriptor

## Prerequisites

Before you build or run GradeHub, you should have:

- Java 17 (JDK) installed and JAVA_HOME set
- Maven 3.6+ or the included Maven wrapper (`mvnw` / `mvnw.cmd`)
- MySQL server (or compatible) with a database created for the app

On Windows PowerShell the following commands check versions:

```powershell
java -version
mvn -v
```

## Important: configure resource files (DON'T skip)

This project includes example configuration files in `src/main/resources` that MUST be copied and filled with your real credentials before building or running the application. Do NOT commit real credentials to version control.

Files to copy and configure:

- `src/main/resources/db.properties.example` -> `src/main/resources/db.properties`
	- Contains: `db.url`, `db.user`, `db.password`
	- Example contents:
		```text
		db.url=jdbc:mysql://127.0.0.1:3306/cgpa_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
		db.user=YOUR_DB_USER
		db.password=YOUR_DB_PASSWORD
		```

- `src/main/resources/faculties.properties.example` -> `src/main/resources/faculties.properties`
	- Contains local faculty usernames and passwords used by the app for simple access control
	- Example format:
		```text
		username1=teacher1
		password1=example_password

		username2=teacher2
		password2=example_password
		```

How to copy and fill (PowerShell):

```powershell
Copy-Item -Path src/main/resources/db.properties.example -Destination src/main/resources/db.properties
notepad src/main/resources/db.properties

Copy-Item -Path src/main/resources/faculties.properties.example -Destination src/main/resources/faculties.properties
notepad src/main/resources/faculties.properties
```

Replace `YOUR_DB_USER` and `YOUR_DB_PASSWORD` with your actual database user and password and save the files. If you use a remote DB, ensure `db.url` points to the correct host and port, and that the DB accepts remote connections.

Security note: keep these files out of version control. Consider adding them to `.gitignore` if you create them locally.

## Database setup

1. Create a database for GradeHub, for example `cgpa_db`.
2. Run any provided SQL schema or create the required tables manually. (If the repo contains `check_table.sql` or other SQL files, inspect them in the repository's SQL or `src` folders.)

Example MySQL commands (run in MySQL shell or client):

```sql
CREATE DATABASE cgpa_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- then create tables as required by the app
```

If your application includes automatic schema creation or migrations, check the code under `src/main/java/com/cgpa/database` or `backend` for details.

### Using the provided SQL schema

This repository includes a ready-to-run schema file at `sql/schema.sql` that will create the `cgpa_db` database and the `students` and `subjects` tables used by the application.

Run the schema from the MySQL command-line client (replace USER with your DB user):

```powershell
mysql -u USER -p < sql\schema.sql
```

Or, connect to MySQL and source the file (useful when you need to run multiple statements interactively):

```sql
-- from the mysql> prompt
SOURCE /full/path/to/GradeHub/sql/schema.sql;
```

After creating the database and tables, update `src/main/resources/db.properties` to point to the new database (for example `db.url=jdbc:mysql://127.0.0.1:3306/cgpa_db`, and set `db.user` and `db.password`).

If you prefer a dedicated DB user, create one and grant minimal privileges:

```sql
CREATE USER 'gradehub_user'@'localhost' IDENTIFIED BY 'strong_password';
GRANT SELECT, INSERT, UPDATE, DELETE ON cgpa_db.* TO 'gradehub_user'@'localhost';
FLUSH PRIVILEGES;
```

Now your application should be able to connect using the credentials set in `db.properties`.

## Build and run

From the repository root on Windows (PowerShell):

Build the project (using the Maven wrapper if available):

```powershell
# Use the wrapper (if present) otherwise fall back to system mvn
if (Test-Path .\mvnw.cmd) { .\mvnw.cmd clean package } else { mvn clean package }
```

After a successful build, run the application. The `pom.xml` configures `com.cgpa.app.Main` as the main class. If the build creates a runnable JAR under `target/`, run it with:

```powershell
java -jar target\cgpa-calculator-1.0.0.jar
```

Alternatively, run directly with Maven (exec plugin):

```powershell
if (Test-Path .\mvnw.cmd) { .\mvnw.cmd exec:java } else { mvn exec:java }
```

Notes:
- If the project is split across modules, check the relevant module's README or `pom.xml` and run its build separately.
- Ensure the `lib/TimingFramework-0.55.jar` exists and is readable; the `pom.xml` references it with a system scope.

Optional: build UI assets on Windows

There is a helper batch script in the repository root to build or prepare the UI assets on Windows. From PowerShell run:

```powershell
if (Test-Path .\compile_new_ui.bat) { .\compile_new_ui.bat } else { Write-Host "compile_new_ui.bat not found in repository root" }
```

This script is provided for convenience and may generate UI resources or copy files used by the application. Run it before packaging if your build depends on those generated UI files.

## Development

- Open the project in your IDE (IntelliJ IDEA, Eclipse). Set the project JDK to Java 17.
- Mark `src/main/resources` as resources so the properties files are on the classpath at runtime.

## Troubleshooting

- Common error: "Communications link failure" or unable to connect to DB — verify `db.properties` values, that MySQL is running, and firewall settings.
- If the app can't find `faculties.properties`, make sure you copied the `.example` file and it is on the classpath.
- Missing local JAR errors — ensure `lib/TimingFramework-0.55.jar` exists. If missing, either obtain the JAR and add it to `lib/` or remove the system-scoped dependency and add a proper Maven artifact if available.

## Contributing

Contributions are welcome. Typical contributions include bug fixes, documentation improvements, and tests. Please open issues or pull requests on the repository.

When opening a PR:

- Keep secrets out of commits
- Add tests for new functionality when possible
- Describe any DB schema changes and include SQL or migration scripts

## License

This repository contains a [LICENSE](LICENSE) file — please review it for licensing details.

## Credits

This project is a combination of original work by the repository owner and UI assets inspired from an external open-source dashboard.

- UI / Dashboard: Adapted from "java-ui-dashboard-010" by DJ-Raven — https://github.com/DJ-Raven/java-ui-dashboard-010
	- Please review the upstream repository and its license before redistributing UI assets.

- Project author and maintainer: [rishit-exe](https://github.com/rishit-exe/rishit-exe/tree/main) (repository owner) — responsible for the backend, database integration, CGPA logic, and overall project coordination.

Built with Java, Swing, FlatLaf, MigLayout, and MySQL.
----

 
