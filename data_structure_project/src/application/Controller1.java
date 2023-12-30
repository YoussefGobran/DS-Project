package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import application.helpers.XMLParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Controller1 {
	@FXML
	private TextArea preChange;
	@FXML
	private TextArea afterChange;
	
	public XMLParser parser;
	
	File file;
	public void displayBeforeChange (File passedFile) throws IOException {
		file = passedFile;
		String fileContent = new String(Files.readAllBytes(file.toPath()));
		preChange.setText(fileContent);
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
	
	public void networkAnalysis(ActionEvent e){
		showNetworkAnalysis("Analysis Done");
	}
	
	private void showNetworkAnalysis(String analysis) {
		Stage stage = new Stage();
		TextArea formattedXmlTextArea = new TextArea();
		formattedXmlTextArea.setText(analysis);
		Scene scene = new Scene(formattedXmlTextArea, 400, 300);
		stage.setScene(scene);
		stage.setTitle("Network Analysis");
		stage.show();
	}
	
	public void showGraph(ActionEvent actionEvent) throws IOException {
		String xmlContent = new String(Files.readAllBytes(file.toPath()));
		parser = new XMLParser();
		parser.parseXMLFile(xmlContent);

		// Create and show graph
		GraphVisualization graphVisualization = new GraphVisualization(parser.rootNode);
		graphVisualization.showGraph();
	}

}
