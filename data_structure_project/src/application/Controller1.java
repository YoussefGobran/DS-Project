package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import application.helpers.XMLParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Controller1 {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private TextArea preChange;
	@FXML
	private TextArea afterChange;
	
	public XMLParser parser;

	public void displayBeforeChange (File passedFile) throws IOException {
		File file = passedFile;
		String fileContent = new String(Files.readAllBytes(file.toPath()));
		preChange.setText(fileContent);
		parser = new XMLParser();
		parser.parseXMLFile(fileContent);
	}
	
	public void fixErrors(ActionEvent e){
		afterChange.appendText("fixErrors \n");
	}
	
	public void checkErrors(ActionEvent e){
		afterChange.appendText("checkErrors \n");
	}
	
	public void XMLFormat(ActionEvent e){
		afterChange.appendText("XMLFormat \n");
	}
	
	public void convertToJason(ActionEvent e){
		afterChange.appendText("convertToJason \n");
	}
	
	public void compress(ActionEvent e){
		afterChange.appendText("compress \n");
	}
	
	public void undo(ActionEvent e){
		afterChange.appendText("undo \n");
	}
	
	public void redo(ActionEvent e){
		afterChange.appendText("redo \n");
	}
	
	public void apply(ActionEvent e){
		afterChange.appendText("apply \n");
	}
	
	public void save(ActionEvent e){
		afterChange.appendText("save \n");
	}
	
	public void networkAnalysis(ActionEvent event) throws IOException{
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Level2.fxml"));
		root = loader.load();
		
		Controller2 controller2 = loader.getController();
		
		controller2.analysisControl(parser);
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}
	
	
	
	public void showGraph(ActionEvent actionEvent) throws IOException {
		// Create and show graph
		GraphVisualization graphVisualization = new GraphVisualization(parser.rootNode);
		graphVisualization.showGraph();
	}

}
