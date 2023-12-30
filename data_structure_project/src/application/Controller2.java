package application;

import java.util.List;

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
		parser.parseXMLToUsers();
		parser.buildAdjanceyListUsers();
		result.setText("");
		if(!searchField.getText().trim().isEmpty()) {
			List<String> postOutput=parser.postSearch(searchField.getText());	
			result.appendText("Posts Found:\n");
			result.appendText(postOutput.toString()+'\n');
		}
		Integer mostFollowerUserID=parser.userWithMostFollowers();
		result.appendText("User ID With Most Followers: ");
		result.appendText(mostFollowerUserID.toString()+'\n');
		
		Integer mostConnectedToUserID=parser.userWithMostConnectedTo();
		result.appendText("User ID With Most Connected: ");
		result.appendText(mostConnectedToUserID.toString()+'\n');
		
		if(!userMutualID1.getText().trim().isEmpty()&&
			!userMutualID2.getText().trim().isEmpty()&&
			XMLParser.isNumeric(userMutualID1.getText())&&
			XMLParser.isNumeric(userMutualID2.getText())) {	
		
			List<Integer>mutualUsersIDs=parser.mutualFollowers(Integer.parseInt( userMutualID1.getText()), Integer.parseInt( userMutualID2.getText()));
			result.appendText("Mutual User IDs: ");
			result.appendText(mutualUsersIDs.toString()+'\n');
			
		}
		if(!userSuggestID.getText().trim().isEmpty()&&XMLParser.isNumeric(userSuggestID.getText())) {			
			List<Integer>suggestUsersIDs=parser.suggestUser(Integer.parseInt(userSuggestID.getText()));
			result.appendText("Suggested User IDs: ");
			result.appendText(suggestUsersIDs.toString()+'\n');
		}

	}

}
