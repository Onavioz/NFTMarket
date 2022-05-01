package service;

import java.util.Optional;
import resources.Constants;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GUIControlService {
	public static GUIControlService instance = new GUIControlService();
	private Object viewModel;
	private Stage primaryStage;
	public static boolean msgResult; // Checked whether yes or no pressed.

	private GUIControlService() {
	}

	public static GUIControlService getInstance() {
		return instance;
	}

	public void setController(Object viewModel) {
		this.viewModel = viewModel;
	}

	public Object getController() {
		return viewModel;
	}

	public void setStage(Stage stage) {
		primaryStage = stage;
	}

	public Stage getStage() {
		return primaryStage;
	}

	public void openMainPage() {
		try {

			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource(Constants.SampleView));
			Scene scene = new Scene(root, 990, 600);
			scene.getStylesheets().add(getClass().getResource(Constants.appCSS).toExternalForm());
			primaryStage.setTitle(Constants.NFTMarketTitle);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * method that displays an error message in an alert
	 *
	 * @param msg the message to be displayed
	 */
	public static void popUpError(String msg) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("");
			alert.setContentText(msg);
			alert.showAndWait();
		});
	}

	/*
	 * method that displays an error message in an alert and exits the client once
	 * the user exited the error
	 * 
	 * @param msg the message to be displayed
	 */
	public static void popUpErrorExitOnClick(String msg) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("");
			alert.setContentText(msg);
			alert.setOnCloseRequest(e -> {
				System.exit(0);
			});
			alert.showAndWait();
		});
	}

	public static void popUpMessage(String msg) {
		popUpMessage("Message", msg);
	}

	/*
	 * method that displays a message in an alert
	 * 
	 * @param title the title of the message
	 * 
	 * @param msg the message
	 */
	public static void popUpMessage(String title, String msg) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(title);
			alert.setHeaderText("");
			alert.setContentText(msg);
			alert.showAndWait();
		});
	}

	public static void popUpMessageYesNo(String title, String msg) {

		Alert alert = new Alert(AlertType.INFORMATION, msg, ButtonType.YES, ButtonType.NO);
		alert.setTitle(title);
		alert.setHeaderText("");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.YES) {
			msgResult = true;
		} else {
			msgResult = false;
		}
	}

}
