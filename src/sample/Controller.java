package sample;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Controller class for handling the Weather Application UI and logic.
 */
public class Controller {

    /**
     * Root VBox layout for the application UI.
     */
    @FXML
    private VBox root;

    /**
     * TextField for the user to input the city name.
     */
    @FXML
    private TextField cityField;

    /**
     * Button to trigger the weather search.
     */
    @FXML
    private Button searchButton;

    /**
     * Label to display the weather description.
     */
    @FXML
    private Label weatherLabel;

    /**
     * Label to display the temperature.
     */
    @FXML
    private Label temperatureLabel;

    /**
     * Label to display the "feels like" temperature.
     */
    @FXML
    private Label feelsLikeLabel;

    /**
     * Label to display the UV index.
     */
    @FXML
    private Label uvIndexLabel;

    /**
     * Label to display visibility information.
     */
    @FXML
    private Label visibilityLabel;

    /**
     * Label to display wind speed.
     */
    @FXML
    private Label windLabel;

    /**
     * Label to display wind direction.
     */
    @FXML
    private Label windDirectionLabel;

    /**
     * Label to display cloud cover percentage.
     */
    @FXML
    private Label cloudLabel;

    /**
     * ImageView to display the weather condition icon.
     */
    @FXML
    private ImageView conditionImage;

    /**
     * API key for accessing the Weather API.
     */
    private static final String WEATHER_API_KEY = "d3f64b06ffba4e5eb73150849241312";

    /**
     * API key for accessing the Unsplash API.
     */
    private static final String UNSPLASH_API_KEY = "547ePh_rhPxzkOfI0ohokosj82gkICc1ICMkYHks_DA";

    /**
     * Base URL for the Weather API.
     */
    private static final String WEATHER_API_URL = "http://api.weatherapi.com/v1/current.json?key=%s&q=%s";

    /**
     * Base URL for the Unsplash API.
     */
    private static final String UNSPLASH_API_URL = "https://api.unsplash.com/search/photos?query=%s&client_id=%s";

    /**
     * Fetches and displays weather data based on the user's input city.
     */
    @FXML
    private void getWeather() {
        String city = cityField.getText().trim();

        try {
            String weatherResponse = fetchWeatherData(city);
            if (weatherResponse != null) {
                WeatherData data = parseWeatherData(weatherResponse);
                updateUI(data);
                setBackgroundImage(city); // Set background image
            } else {
                showError("City not found!");
            }
        } catch (Exception e) {
            showError("Error fetching weather data!");
        }
    }

    /**
     * Fetches weather data for a given city from the Weather API.
     *
     * @param city The name of the city to fetch weather data for.
     * @return The JSON response as a string, or null if the city is not found.
     * @throws Exception If an error occurs while fetching the data.
     */
    private String fetchWeatherData(String city) throws Exception {
        String urlString = String.format(WEATHER_API_URL, WEATHER_API_KEY, city);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) { // HTTP OK
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            return response.toString();
        } else if (responseCode == 400 || responseCode == 404) {
            return null; // Invalid city or city not found
        } else {
            throw new Exception("Error: Response Code " + responseCode);
        }
    }

    /**
     * Parses the weather data JSON response into a WeatherData object.
     *
     * @param response The JSON response as a string.
     * @return A WeatherData object containing parsed data.
     * @throws Exception If an error occurs while parsing the data.
     */
    private WeatherData parseWeatherData(String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);

        WeatherData data = new WeatherData();
        data.setCity(root.get("location").get("name").asText());
        data.setTemperature(root.get("current").get("temp_c").asDouble());
        data.setFeelsLike(root.get("current").get("feelslike_c").asDouble());
        data.setWeather(root.get("current").get("condition").get("text").asText());
        data.setIconUrl("http:" + root.get("current").get("condition").get("icon").asText());
        data.setHumidity(root.get("current").get("humidity").asInt());
        data.setWindSpeed(root.get("current").get("wind_kph").asDouble());
        data.setWindDirection(root.get("current").get("wind_dir").asText());
        data.setUvIndex(root.get("current").get("uv").asDouble());
        data.setVisibility(root.get("current").get("vis_km").asDouble());
        data.setCloud(root.get("current").get("cloud").asInt());

        return data;
    }

    /**
     * Updates the UI with the fetched weather data.
     *
     * @param data The WeatherData object containing the weather information.
     */
    private void updateUI(WeatherData data) {
        weatherLabel.setText("Weather: " + data.getWeather());
        temperatureLabel.setText(String.format("Temperature: %.1f°C", data.getTemperature()));
        feelsLikeLabel.setText(String.format("Feels Like: %.1f°C", data.getFeelsLike()));
        uvIndexLabel.setText("UV Index: " + data.getUvIndex());
        visibilityLabel.setText("Visibility: " + data.getVisibility() + " km");
        windLabel.setText("Wind Speed: " + data.getWindSpeed() + " kph");
        windDirectionLabel.setText("Wind Direction: " + data.getWindDirection());
        cloudLabel.setText("Cloud Cover: " + data.getCloud() + "%");

        // Set condition icon
        conditionImage.setImage(new Image(data.getIconUrl()));
    }

    /**
     * Sets the background image for the application based on the city name.
     *
     * @param city The name of the city to fetch the background image for.
     * @throws Exception If an error occurs while fetching the image.
     */
    private void setBackgroundImage(String city) throws Exception {
        String urlString = String.format(UNSPLASH_API_URL, city, UNSPLASH_API_KEY);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) { // HTTP OK
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Parse the JSON response to get the image URL
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.toString());
            String imageUrl = rootNode.get("results").get(0).get("urls").get("regular").asText();

            // Set the background image for the VBox
            root.setStyle("-fx-background-image: url('" + imageUrl + "'); -fx-background-size: cover;");
        }
    }

    /**
     * Displays an error message in the UI and resets all weather-related fields to default values.
     *
     * @param message The error message to display.
     */
    private void showError(String message) {
        weatherLabel.setText("Weather: " + message);
        temperatureLabel.setText("Temperature: -");
        feelsLikeLabel.setText("Feels Like: -");
        uvIndexLabel.setText("UV Index: -");
        visibilityLabel.setText("Visibility: -");
        windLabel.setText("Wind Speed: -");
        windDirectionLabel.setText("Wind Direction: -");
        cloudLabel.setText("Cloud Cover: -");
        conditionImage.setImage(null);
    }

    /**
     * Represents the weather data for a specific location.
     */
    public static class WeatherData {
        /**
         * The city name.
         */
        private String city;

        /**
         * The temperature in Celsius.
         */
        private double temperature;

        /**
         * The "feels like" temperature in Celsius.
         */
        private double feelsLike;

        /**
         * The weather condition description.
         */
        private String weather;

        /**
         * The URL for the weather condition icon.
         */
        private String iconUrl;

        /**
         * The humidity percentage.
         */
        private int humidity;

        /**
         * The wind speed in kilometers per hour.
         */
        private double windSpeed;

        /**
         * The wind direction as a compass point (e.g., N, NW).
         */
        private String windDirection;

        /**
         * The UV index.
         */
        private double uvIndex;

        /**
         * The visibility distance in kilometers.
         */
        private double visibility;

        /**
         * The cloud cover percentage.
         */
        private int cloud;

        /**
         * Gets the cloud cover percentage.
         *
         * @return The cloud cover percentage.
         */
        public int getCloud() {
            return cloud;
        }

        /**
         * Sets the cloud cover percentage.
         *
         * @param cloud The cloud cover percentage to set.
         */
        public void setCloud(int cloud) {
            this.cloud = cloud;
        }

        /**
         * Gets the visibility distance in kilometers.
         *
         * @return The visibility distance.
         */
        public double getVisibility() {
            return visibility;
        }

        /**
         * Sets the visibility distance in kilometers.
         *
         * @param visibility The visibility distance to set.
         */
        public void setVisibility(double visibility) {
            this.visibility = visibility;
        }

        /**
         * Gets the UV index.
         *
         * @return The UV index.
         */
        public double getUvIndex() {
            return uvIndex;
        }

        /**
         * Sets the UV index.
         *
         * @param uvIndex The UV index to set.
         */
        public void setUvIndex(double uvIndex) {
            this.uvIndex = uvIndex;
        }

        /**
         * Gets the wind direction.
         *
         * @return The wind direction.
         */
        public String getWindDirection() {
            return windDirection;
        }

        /**
         * Sets the wind direction.
         *
         * @param windDirection The wind direction to set.
         */
        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        /**
         * Gets the wind speed in kilometers per hour.
         *
         * @return The wind speed.
         */
        public double getWindSpeed() {
            return windSpeed;
        }

        /**
         * Sets the wind speed in kilometers per hour.
         *
         * @param windSpeed The wind speed to set.
         */
        public void setWindSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
        }

        /**
         * Gets the humidity percentage.
         *
         * @return The humidity percentage.
         */
        public int getHumidity() {
            return humidity;
        }

        /**
         * Sets the humidity percentage.
         *
         * @param humidity The humidity percentage to set.
         */
        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        /**
         * Gets the weather condition icon URL.
         *
         * @return The weather condition icon URL.
         */
        public String getIconUrl() {
            return iconUrl;
        }

        /**
         * Sets the weather condition icon URL.
         *
         * @param iconUrl The weather condition icon URL to set.
         */
        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        /**
         * Gets the weather condition description.
         *
         * @return The weather condition description.
         */
        public String getWeather() {
            return weather;
        }

        /**
         * Sets the weather condition description.
         *
         * @param weather The weather condition description to set.
         */
        public void setWeather(String weather) {
            this.weather = weather;
        }

        /**
         * Gets the "feels like" temperature in Celsius.
         *
         * @return The "feels like" temperature.
         */
        public double getFeelsLike() {
            return feelsLike;
        }

        /**
         * Sets the "feels like" temperature in Celsius.
         *
         * @param feelsLike The "feels like" temperature to set.
         */
        public void setFeelsLike(double feelsLike) {
            this.feelsLike = feelsLike;
        }

        /**
         * Gets the temperature in Celsius.
         *
         * @return The temperature.
         */
        public double getTemperature() {
            return temperature;
        }

        /**
         * Sets the temperature in Celsius.
         *
         * @param temperature The temperature to set.
         */
        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        /**
         * Gets the city name.
         *
         * @return The city name.
         */
        public String getCity() {
            return city;
        }

        /**
         * Sets the city name.
         *
         * @param city The city name to set.
         */
        public void setCity(String city) {
            this.city = city;
        }
    }
}
