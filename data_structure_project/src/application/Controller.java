package application;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;

public class Controller {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	 
	
	@FXML
	private TextArea direction;
	//@FXML
	//public XMLParser parser;
	final FileChooser fc = new FileChooser();
	File file;
	public void multiFileChoosser(ActionEvent e){
		fc.setTitle("My File Chooser");
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.xml"));
		file = fc.showOpenDialog(null);
		if(file==null)return;
		direction.appendText(file.getAbsolutePath()+ "\n");
		// zinab code 
		/*try {
			String fileContent = new String(Files.readAllBytes(file.toPath()));
			direction.setText(fileContent);
		} catch (IOException ex) {
			ex.printStackTrace();
		}*/
	}
	// Formatting XML

	public void Proceed (ActionEvent event) throws IOException {
		// zinab code
		/*String xmlContent = direction.getText();
		parser = new XMLParser();
		parser.parseXMLFile(xmlContent);

		String formattedXml = formatXml(parser.rootNode);
		openFormattedXmlWindow(formattedXml);
		*/
		
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Level1.fxml"));
		root = loader.load();
		
		Controller1 controller1 = loader.getController();
		
		controller1.displayBeforeChange(file);
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
		//XMLParser parser = new XMLParser();
		//change.appendText(parser.readFileContent(direction.getText()));
	}
	
	
	
	// zinab code
/*  private String formatXml(XMLNode node) {
		StringBuilder formattedXml = new StringBuilder();
		formatNode(node, formattedXml, 0);
		return formattedXml.toString();
	}

	private void formatNode(XMLNode node, StringBuilder formattedXml, int indentationLevel) {
		formattedXml.append(getInd(indentationLevel)).append("<").append(node.getTagName()).append(">");

		if (node.getChildrenNodes() != null && !node.getChildrenNodes().isEmpty()) {
			formattedXml.append("\n");
			for (XMLNode child : node.getChildrenNodes()) {
				formatNode(child, formattedXml, indentationLevel + 1);
			}
			formattedXml.append(getInd(indentationLevel));
		} else if (node.getInnerText() != null && !node.getInnerText().trim().isEmpty()) {
			String trimmedInnerText = node.getInnerText().trim();
			if (trimmedInnerText.length() > 40) { // For name or ID
				formattedXml.append("\n").append(getInd(indentationLevel + 1)).append(trimmedInnerText).append("\n").append(getInd(indentationLevel));
			} else {
				formattedXml.append(trimmedInnerText);
			}
		}

		formattedXml.append("</").append(node.getTagName()).append(">\n");
	}


	private String getInd(int indentationLevel) {
		StringBuilder ind = new StringBuilder();
		for (int i = 0; i < indentationLevel; i++) {
			ind.append("    "); // Use 4 spaces for each level of indentation
		}
		return ind.toString();
	}


	private void openFormattedXmlWindow(String formattedXml) {
		Stage stage = new Stage();
		TextArea formattedXmlTextArea = new TextArea();
		formattedXmlTextArea.setText(formattedXml);
		Scene scene = new Scene(formattedXmlTextArea, 400, 300);
		stage.setScene(scene);
		stage.setTitle("Formatted XML");
		stage.show();
	}
*/
}
