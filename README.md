# JSON to CSV Converter

This project is a simple Java application that converts JSON files to CSV format. It provides a menu-driven interface for easy usage and supports both command-line and IDE-based execution.

## Features

- Read JSON files
- Transform Json String to CSV format
- Menu-driven user interface
- Modular code structure (Menu, CsvHandler, JsonHandler)

## Project Structure

```
src/
	main/
		java/
			org/
				arkn37/
					Main.java
					service/
						CsvHandler.java
						JsonHandler.java
						Menu.java
	test/
		java/
			org/
				arkn37/
					service/
						CsvHandlerTest.java
						JsonHandlerTest.java
```

## Prerequisites

- Java 17 or higher
- Gradle (wrapper included)

## How to Run (Any IDE)

1. **Import Project**: Open your IDE (IntelliJ IDEA, Eclipse, VS Code, etc.) and import the project as a Gradle project.
2. **Build Project**: Use the IDE's build/run feature to compile the project.
3. **Run Main Class**: Execute `org.arkn37.Main` to start the application.

## Usage

Follow the on-screen menu to select JSON files and convert them to CSV. Output files will be saved in the specified location.

### Examples

#### Example 1: Read a JSON file

Suppose your JSON file contains:

```json
[
  { "name": "Alice", "age": 30 },
  { "name": "Bob", "age": 25 }
]
```

1. Run the application (via IDE or CLI).
2. When prompted, enter the path to your JSON file, e.g.:
   ```
   Enter the path to the JSON file:
   C:\Users\YourName\Documents\input.json
   ```
3. The application will process the file and prompt it, e.g.:
   ```
   [{"name": "Alice", "age": 30 },{ "name": "Bob","age": 25 }]
   ```

#### Example 2: JSON String to CSV

Suppose a JSON String:

```
[{"name":"Alice","age":30},{"name":"Bob","age":25}]
```

The resulting CSV will be:

```csv
name,age
Alice,30
Bob,25
```

#### Example 3: Error Handling

If you enter an invalid file path or malformed JSON, the application will display an error message and prompt you to try again.

## Testing

Run tests using Gradle:

```powershell
./gradlew test
```

## License

This project is licensed under the MIT License.
