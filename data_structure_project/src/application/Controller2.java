package application;

import application.helpers.XMLParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller2 {
	@FXML
	private TextField searchField;
	@FXML
	private TextField userMutualID1;
	@FXML
	private TextField userMutualID2;
	@FXML
	private TextField userSuggestID;
	@FXML
	private TextArea result;
	
	public XMLParser parser;
	public void analysisControl(XMLParser passedParser) {
		parser = passedParser;
	}
	
	public void search(ActionEvent e){
		result.appendText(searchField.getText());
		result.appendText(userMutualID1.getText());
		result.appendText(userMutualID2.getText());
		result.appendText(userSuggestID.getText());
	}

}
