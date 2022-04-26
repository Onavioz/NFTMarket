package application;	
import javafx.application.Application;
import javafx.stage.Stage;
import service.GUIControlService;

public class Main extends Application {
	GUIControlService guiControl=GUIControlService.getInstance();
	@Override
	public void start(Stage primaryStage) {
			guiControl.setStage(primaryStage);
			guiControl.openMainPage();

	}
	public static void main(String[] args) {
		launch(args);
	}
}
