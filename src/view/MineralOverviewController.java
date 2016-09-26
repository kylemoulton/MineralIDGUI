package view;

import java.util.Optional;

import application.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Mineral;

public class MineralOverviewController {

	@FXML
	private TableView<Mineral> mineralTable;
	@FXML
	private TableColumn<Mineral, String> mineralNameColumn;

	@FXML
	private Label mineralNameLabel;
	@FXML
	private Label colorLabel;
	@FXML
	private Label streakLabel;
	@FXML
	private Label lusterLabel;
	@FXML
	private Label diaphaneityLabel;
	@FXML
	private Label fractureLabel;
	@FXML
	private Label cleavageLabel;
	@FXML
	private Label hardnessLabel;	
	@FXML
	private Label specGravLabel;
	@FXML
	private Label otherLabel;
	@FXML
	private Label currentSearchLabel;

	private MainApp mainApp;

	public MineralOverviewController() {
	}

	@FXML
	private void initialize() {
		mineralNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		showMineralDetails(null);
		currentSearchLabel.setText("");
		mineralTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showMineralDetails(newValue));
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		mineralTable.setItems(mainApp.getMineralData());
	}

	private void showMineralDetails(Mineral mineral) {
		if (mineral != null) {
			mineralNameLabel.setText(mineral.getName());
			colorLabel.setText(mineral.getColor());
			streakLabel.setText(mineral.getStreak());
			lusterLabel.setText(mineral.getLuster());
			diaphaneityLabel.setText(mineral.getDiaphaneity());
			fractureLabel.setText(mineral.getFracture());
			cleavageLabel.setText(mineral.getCleavage());
			hardnessLabel.setText(mineral.getHardness());
			specGravLabel.setText(mineral.getSpecGrav());
			otherLabel.setText(mineral.getOther());
			
		} else {
			mineralNameLabel.setText("");
			colorLabel.setText("");
			streakLabel.setText("");
			lusterLabel.setText("");
			diaphaneityLabel.setText("");
			fractureLabel.setText("");
			cleavageLabel.setText("");
			hardnessLabel.setText("");
			specGravLabel.setText("");
			otherLabel.setText("");
		}
	}
	
	@FXML
	private void handleMineralSearch() {
		mainApp.showMineralSearch();
		currentSearchLabel.textProperty().bind(mainApp.currentSearchProperty());
	}

	@FXML
	private void handleResetSearch() {
		mainApp.readMineralList(mainApp.getMineralFilePath());
		mainApp.resetCurrentSearchProperty();
		mainApp.updateMineralProperties();
	}
	
	@FXML
	private void handleAddMineral() {
		mainApp.showAddMineral();
	}
	
	@FXML
	public void handleEditMineral() {
		if (mineralTable.getSelectionModel().getSelectedItem() != null) {
			mainApp.showEditMineral(mineralTable.getSelectionModel().getSelectedItem());
		}
	}

	@FXML
	private void handleDeleteMineral() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Delete");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete " + mineralTable.getSelectionModel().getSelectedItem().getName() + "?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			mainApp.deleteMineral(mineralTable.getSelectionModel().getSelectedItem());
		}
	}
	
	
}
