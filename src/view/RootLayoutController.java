package view;

import java.io.File;

import application.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class RootLayoutController {
	
	private MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	private void handleSave() {
		File mineralFile = mainApp.getMineralFilePath();
		if (mineralFile != null) {
			mainApp.saveMineralData(mineralFile);
		}
		else {
			handleSaveAs();
		}
	}
	
	@FXML
	private void handleSaveAs() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
		
		if (file != null) {
			if (!file.getPath().endsWith(".csv")) {
				file = new File(file.getPath() + ".csv");
			}
			if (mainApp.saveMineralData(file)) {
				mainApp.setMineralFilePath(file);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Save Successful");
				alert.setHeaderText(null);
				alert.setContentText("Mineral Data was successfully saved to file");
				
				alert.showAndWait();
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Save Unsuccessful");
				alert.setHeaderText(null);
				alert.setContentText("Mineral Data was not able to be saved to " + file.getPath());
				alert.showAndWait();
			}
		}
	}

	@FXML
	private void handleOpen() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());	
	
		if (file != null) {
			mainApp.readMineralList(file);
			mainApp.setMineralFilePath(file);
		}
	}
	
	@FXML
	private void handleLoadDefaultMineral() {
		mainApp.readMineralList(new File(new File("").getAbsolutePath() + "/src/data/Minerals.csv"));
		mainApp.setMineralFilePath(new File(new File("").getAbsolutePath() + "/src/data/Minerals.csv"));
	}
	
	@FXML
	private void handleAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Mineral Search App");
		alert.setHeaderText("About");
		alert.setContentText("Author: Kyle Moulton\nGithub: https://github.com/kylemoulton");
		
		alert.showAndWait();
	}
	
	@FXML
	private void handleExit() {
		System.exit(0);
	}
}
