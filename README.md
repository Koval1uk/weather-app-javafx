# Weather App

This is a JavaFX-based Weather Application that fetches weather data for a given city using the Weather API and displays relevant weather information. Additionally, it fetches background images from Unsplash API based on the city name.

## Features
- Input city name to get current weather information.
- Displays weather details including temperature, humidity, wind speed, UV index, visibility, and cloud cover.
- Fetches a background image of the city using the Unsplash API.

## Prerequisites
Before running the project, ensure you have the following installed:

1. **Java JDK 21.0.2**
2. **JavaFX SDK 23.0.1**
3. Required libraries to download:
   - `jackson-annotations-2.18.0`
   - `jackson-core-2.2.3`
   - `jackson-databind-2.1.3`

## Setting Up the Project

### Cloning the Repository
To download the project from GitHub:

```bash
git clone <repository-url>
cd <repository-directory>
```

### Configuring the Project in IntelliJ IDEA
1. **Import the Project:**
   - Open IntelliJ IDEA.
   - Select **File > Open...** and navigate to the project's directory.

2. **Configure JavaFX SDK:**
   - Go to **File > Project Structure > Libraries > Add**.
   - Select the JavaFX SDK directory (e.g., `C:\Program Files\Java\javafx-sdk-23.0.1\lib`).

3. **Add VM Options:**
   - Go to **Run > Edit Configurations**.
   - Under **VM options**, add:
     ```
     --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx.controls,javafx.fxml
     ```

4. **Add External Libraries:**
   - Go to **File > Project Structure > Libraries**.
   - Add the following JAR files to the project:
     - `jackson-annotations-2.18.0.jar`
     - `jackson-core-2.2.3.jar`
     - `jackson-databind-2.1.3.jar`

### Running the Project
1. Open `Main.java` in IntelliJ IDEA.
2. Right-click on the file and select **Run 'Main.main()'**.

## Usage
- Enter the name of a city in the text field.
- Click on the **Get Weather** button to fetch and display the weather information.
- View the fetched data and background image of the city.

## APIs Used
1. **Weather API:**
   - URL: `http://api.weatherapi.com/v1/current.json`
   - Requires an API key (`WEATHER_API_KEY`).

2. **Unsplash API:**
   - URL: `https://api.unsplash.com/search/photos`
   - Requires an API key (`UNSPLASH_API_KEY`).

## Troubleshooting
- **JavaFX Runtime Components Missing:** Ensure the correct `--module-path` and `--add-modules` VM options are added.
- **Class Not Found Errors:** Verify that all required libraries are added to the project.

## License
This project is licensed under the MIT License.
