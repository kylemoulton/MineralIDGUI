package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import application.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MineralSearchController {

	private Stage dialogStage;
	private MainApp mainApp;

	@FXML
	public ComboBox<String> propertyNameComboBox;

	@FXML
	public ComboBox<String> propertyValueComboBox;

	@FXML
	private TextField searchValue;

	@FXML
	private Label resultCountLabel;
	
	private boolean okClicked = false;

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void initialize() {
		propertyNameComboBox.getItems().addAll("Name", "Color", "Streak", "Fracture", "Cleavage", "Luster",
				"Diaphaneity", "Hardness", "Specific Gravity", "Other Property");

		propertyValueComboBox.setDisable(true);
		propertyValueComboBox.setVisible(false);
		searchValue.setVisible(false);
		searchValue.setDisable(true);

		resultCountLabel.setText("");
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleNameComboBox() {
		propertyValueComboBox.setVisible(false);
		propertyValueComboBox.setDisable(true);
		
		searchValue.setVisible(false);
		searchValue.setDisable(true);

		propertyValueComboBox.getItems().clear();

		if (!propertyNameComboBox.getValue().equals("Hardness")
				&& !propertyNameComboBox.getValue().equals("Specific Gravity")) {
			Set<String> propertyValueSet = mainApp.getSearchResultCounts().get(propertyNameComboBox.getValue())
					.keySet();
			ArrayList<String> propertyValueList = new ArrayList<String>();

			for (String propertyValue : propertyValueSet) {
				propertyValueList.add(propertyValue);
			}
			Collections.sort(propertyValueList);

			propertyValueComboBox.getItems().addAll(propertyValueList);
			propertyValueComboBox.setVisible(true);
			propertyValueComboBox.setDisable(false);

		} else {
			searchValue.setVisible(true);
			searchValue.setDisable(false);
		}
	}

	@FXML
	private void handleValueClicked() {
		if (!propertyNameComboBox.getValue().equals("Hardness")
				&& !propertyNameComboBox.getValue().equals("Specific Gravity")) {
			if (mainApp.getSearchResultCounts().containsKey(propertyNameComboBox.getValue())) {
				if (mainApp.getSearchResultCounts().get(propertyNameComboBox.getValue())
						.containsKey(propertyValueComboBox.getValue())) {
					resultCountLabel.setText(mainApp.getSearchResultCounts().get(propertyNameComboBox.getValue())
							.get(propertyValueComboBox.getValue()).toString() + " result(s) found for "
							+ propertyNameComboBox.getValue() + " : " + propertyValueComboBox.getValue());
				}
			}
		}
	}

	@FXML
	private void handleOk() {
		boolean isValid = isInputValid();
		if (isValid && !propertyNameComboBox.getValue().equals("Hardness") && !propertyNameComboBox.getValue().equals("Specific Gravity")) {
			mainApp.searchMinerals(propertyValueComboBox.getValue(), propertyNameComboBox.getValue());
			
			okClicked = true;
			dialogStage.close();
		}
		else if (isValid) {
			mainApp.searchMinerals(searchValue.getText(), propertyNameComboBox.getValue());
			
			okClicked = true;
			dialogStage.close();
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public boolean isInputValid() {
		if (propertyNameComboBox.getSelectionModel().isEmpty()) {
			return false;
		}
		else if (propertyNameComboBox.getValue().equals("Hardness") || propertyNameComboBox.getValue().equals("Specific Gravity")) {
			if (!searchValue.getText().isEmpty()) {
				try {
					Float value = Float.parseFloat(searchValue.getText());
					if (value <= 10 && value > 0) {
						return true;
					}
				} catch (NumberFormatException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Invalid " + propertyNameComboBox.getValue() + " field Error");
					alert.setContentText(propertyNameComboBox.getValue() + " field values must be float values between 0 and 10");
					alert.showAndWait();
					return false;
				}
			}
			else {
				return false;
			}
		}
		else if (!propertyValueComboBox.getSelectionModel().isEmpty()) {
			return true;
		}
		return false;
	}
}
