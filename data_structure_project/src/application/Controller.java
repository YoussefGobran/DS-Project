package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

public class Controller {
	@FXML
	private TextArea direction;
	@FXML
	private Button choose;
	
	final FileChooser fc = new FileChooser();
	public void multiFileChoosser(ActionEvent e){
		fc.setTitle("My File Chooser");
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.xml"));
		File file = fc.showOpenDialog(null);
		if(file==null)return;
		direction.appendText(file.getAbsolutePath()+ "\n");
	}
}
