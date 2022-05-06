package application;

import java.io.IOException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Collection;
import model.CollectionFactory;
import service.GUIControlService;
import service.ServiceFacade;

public class Main extends Application {
	GUIControlService guiControl = GUIControlService.getInstance();
	

	@Override
	public void start(Stage primaryStage) throws IOException {
		ServiceFacade serviceFacade = new ServiceFacade();
		serviceFacade.InitializeCollection();
		guiControl.setStage(primaryStage);
		guiControl.openMainPage();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
