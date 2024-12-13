package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class that serves as the entry point for the Weather App.
 * This class extends {@link Application} to set up and launch the JavaFX application.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by loading the FXML file and setting up the primary stage.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception If there is an error during FXML loading or stage setup.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Weather App");
        primaryStage.setScene(new Scene(loader.load(), 600, 500));
        primaryStage.show();
    }

    /**
     * The main method, the entry point for the application.
     * Launches the JavaFX application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
