package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import application.helpers.UndoRedo;
import application.helpers.XMLParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
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
	public UndoRedo undoRedo;
	
	public void displayBeforeChange (File passedFile) throws IOException {
		
		parser=new XMLParser();
		undoRedo=new UndoRedo();
		String fileContent=parser.readFileContent(passedFile.toPath().toString());
		String fileContentAfterDetection=parser.xmlCorrection(fileContent).get(1);
//		System.out.println(fileContentAfterDetection);
		parser.parseXMLFile(fileContent);
		
		preChange.setText(fileContent);
	}
	
	public void fixErrors(ActionEvent e){
		afterChange.setText(parser.xmlCorrection(preChange.getText()).get(1));
//		afterChange.appendText("fixErrors \n");
	}
	
	public void checkErrors(ActionEvent e){
		afterChange.setText(parser.xmlCorrection(preChange.getText()).get(0));
//		afterChange.appendText("checkErrors \n");
	}
	
	public void XMLFormat(ActionEvent e){
		afterChange.setText(parser.formatXml());
//		afterChange.appendText("XMLFormat \n");
	}
	
	public void convertToJason(ActionEvent e){
		afterChange.setText(parser.formatJSON( parser.createJson(), 2));
//		afterChange.appendText("convertToJason \n");
	}
	
	public void compress(ActionEvent e){
		
		afterChange.setText(parser.compressFile(preChange.getText()));
//		afterChange.appendText("compress \n");
	}
	
	public void undo(ActionEvent e){
		preChange.setText(undoRedo.undo(preChange.getText()));
//		afterChange.appendText("undo \n");
	}
	
	public void redo(ActionEvent e){
		preChange.setText(undoRedo.redo(preChange.getText()));
//		afterChange.appendText("redo \n");
	}
	
	public void apply(ActionEvent e){
		undoRedo.saveState(preChange.getText());
		preChange.setText(afterChange.getText());
//		afterChange.appendText("apply \n");
	}
	
	public void save(ActionEvent e){
		 FileChooser fileChooser = new FileChooser();
		 File selectedFile = fileChooser.showSaveDialog(null);
		 if(selectedFile==null)return;
		 parser.saveFile(preChange.getText(), selectedFile.getPath()) ;
//		afterChange.appendText("save \n");
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
