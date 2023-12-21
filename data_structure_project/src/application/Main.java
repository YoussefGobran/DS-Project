package application;

import application.helpers.XMLParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			XMLParser xmlParser=new XMLParser();
			
			String fileContent=xmlParser.readFileContent("data_structure_project\\src\\application\\helpers\\assets\\sample.xml");

			xmlParser.parseXMLFile(fileContent);
			System.out.println(xmlParser.rootNode);
			// String res=xmlParser.cleanFileContent(fileContent);
			// xmlParser.saveFile(res, "data_structure_project\\src\\application\\helpers\\assets\\sample_after_compression.xml");
			// System.out.println(res);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
