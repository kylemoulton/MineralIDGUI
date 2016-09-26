package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import application.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Mineral;

public class AddMineralController {

	private Stage dialogStage;
	private MainApp mainApp;
	
	@FXML
	private TextField mineralNameTextField;
	@FXML
	private TextField colorTextField;
	@FXML
	private TextField streakTextField;
	@FXML
	private TextField lusterTextField;
	@FXML
	private TextField diaphaneityTextField;
	@FXML
	private ComboBox<String> fractureComboBox;
	@FXML
	private ComboBox<String> cleavageComboBox;
	@FXML
	private TextField hardLowTextField;
	@FXML
	private TextField hardHighTextField;
	@FXML
	private TextField specGravLowTextField;
	@FXML
	private TextField specGravHighTextField;
	@FXML
	private TextField otherTextField;
	
	private boolean okClicked = false;

	@FXML
	private void initialize() {
		fractureComboBox.getItems().addAll("displays fracture", "displays cleavage");
		cleavageComboBox.getItems().addAll("1", "2", "3", "4", "6");
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleFractureSelected () {
		if (!fractureComboBox.getSelectionModel().isEmpty()) {
			if (fractureComboBox.getValue().equals("displays fracture")) {
				cleavageComboBox.getItems().clear();
				cleavageComboBox.getItems().add("0");
				cleavageComboBox.getSelectionModel().select("0");
			}
			else {
				cleavageComboBox.getItems().clear();
				cleavageComboBox.getItems().addAll("1", "2", "3", "4", "6");
			}	
		}
	}

	// Should mineral be created in mainApp to keep with MVC?
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			
			Mineral newMineral = new Mineral(
					mineralNameTextField.getText(),
					new ArrayList<String>(Arrays.asList(colorTextField.getText().split("\\s*(\\s|,|=>)\\s*"))),
					new ArrayList<String>(Arrays.asList(streakTextField.getText().split("\\s*(\\s|,|=>)\\s*"))),
					new ArrayList<String>(Arrays.asList(lusterTextField.getText().split("\\s*(\\s|,|=>)\\s*"))),
					new ArrayList<String>(Arrays.asList(diaphaneityTextField.getText().split("\\s*(\\s|,|=>)\\s*"))),
					new ArrayList<String>(Arrays.asList(otherTextField.getText().split("\\s*(\\s|,|=>)\\s*"))),
					fractureComboBox.getSelectionModel().isSelected(0),
					Integer.parseInt(cleavageComboBox.getValue()),
					Float.parseFloat(hardLowTextField.getText()),
					Float.parseFloat(hardHighTextField.getText()),
					Float.parseFloat(specGravLowTextField.getText()),
					Float.parseFloat(specGravHighTextField.getText()));
			
			mainApp.processNewMineral(newMineral);
			
			okClicked = true;
			dialogStage.close();
		}
	}
	
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private boolean isInputValid() {
		if (!mineralNameTextField.getText().isEmpty() 
				&& !colorTextField.getText().isEmpty()
				&& !streakTextField.getText().isEmpty() 
				&& !lusterTextField.getText().isEmpty()
				&& !diaphaneityTextField.getText().isEmpty()
				&& fractureComboBox.getValue() != null
				&& cleavageComboBox.getValue() != null
				&& !hardLowTextField.getText().isEmpty() 
				&& !hardHighTextField.getText().isEmpty()
				&& !specGravLowTextField.getText().isEmpty() 
				&& !specGravHighTextField.getText().isEmpty()) {

			if (!Pattern.matches("^[ A-z]+$", mineralNameTextField.getText())) {
				invalidInput("Name");
				return false;
			} 
			else if (!Pattern.matches("^[ A-z,]+$", colorTextField.getText())) {
				invalidInput("Color");
				return false;
			} 
			else if (!Pattern.matches("^[ A-z,]+$", streakTextField.getText())) {
				invalidInput("Streak");
				return false;
			} 
			else if (!Pattern.matches("^[ A-z,]+$", lusterTextField.getText())) {
				invalidInput("Luster");
				return false;
			} 
			else if (!Pattern.matches("^[ A-z,]+$", diaphaneityTextField.getText())) {
				invalidInput("Diaphaneity");
				return false;
			} 
			else if (fractureComboBox.getSelectionModel().isEmpty()) {
				invalidInput("Fracture");
				return false;
			} 
			else if (cleavageComboBox.getSelectionModel().isEmpty()) {
				invalidInput("Cleavage");
				return false;
			}
			else if (!otherTextField.getText().isEmpty()) {
				if (!Pattern.matches("^[ A-z,]+$", otherTextField.getText())) {
					invalidInput("Other Property");
					return false;
				}
			}
			try {
				Float hardLow = Float.parseFloat(hardLowTextField.getText());
				Float hardHigh = Float.parseFloat(hardHighTextField.getText());
				if (hardLow < 0 || hardLow > 10 || hardHigh < 0 || hardHigh > 10) {
					invalidInput("Hardness");
					return false;
				}
				else if (hardLow > hardHigh) {
					invalidInput("Switched Hardness");
					return false;
				}
			}
			catch (NumberFormatException e) {
				invalidInput("Hardness");
				return false;
			}
			try {
				Float specGravLow = Float.parseFloat(specGravLowTextField.getText());
				Float specGravHigh = Float.parseFloat(specGravHighTextField.getText());
				if (specGravLow < 0 || specGravLow > 10 || specGravHigh < 0 || specGravHigh > 10) {
					invalidInput("Specific Gravity");
					return false;
				}
				else if (specGravLow > specGravHigh) {
					invalidInput("Switched SpecGrav");
					return false;
				}
			}
			catch (NumberFormatException e) {
				invalidInput("Specific Gravity");
				return false;
			}
			return true;
		}
		else {
			invalidInput("None");
			return false;
		}
	}

	private void invalidInput(String inputField) {
		Alert alert = new Alert(AlertType.ERROR);
		
		if (!inputField.equals("Hardness") && !inputField.equals("Specific Gravity") && !inputField.equals("None") 
				&& !inputField.equals("Switched Hardness") && !inputField.equals("Switched SpecGrav")) {
			alert.setTitle("Invalid " + inputField + " field Error");
			alert.setHeaderText("Please enter a valid " + inputField + " field value.");
			alert.setContentText(inputField + " field may not contain non-alphabetic characters besides commas to separate values");
			
		} else if (inputField.equals("Hardness") || inputField.equals("Specific Gravity")){
			alert.setTitle("Invalid " + inputField + " field Error");
			alert.setHeaderText("Please enter a valid " + inputField + " field value.");
			alert.setContentText("Hardness/Specific gravity values must be float values between 0 and 10.");
		
		}
		else if (inputField.equals("Switched Hardness")) {
			alert.setTitle("Switched/Incorrect Hardness Values");
			alert.setHeaderText("Lower hardness bound is greater than higher bound");
			alert.setContentText("Hardness low must be less than hardness high");
		}
		else if (inputField.equals("Switched SpecGrav")) {
			alert.setTitle("Switched/Incorrect Specific Gravity Values");
			alert.setHeaderText("Lower specific gravity bound is greater than higher bound");
			alert.setContentText("Specific gravity low must be less than Specific gravity high");
		}
		else {
			alert.setTitle("Missing Field Error");
			alert.setHeaderText("All required fields must be completed");
			alert.setContentText("All fields must be completed except the optional field 'Other Properties'");
		}
		alert.showAndWait();
	}
}
