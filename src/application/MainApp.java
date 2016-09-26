package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Mineral;
import view.AddMineralController;
import view.EditMineralController;
import view.MineralOverviewController;
import view.MineralSearchController;
import view.RootLayoutController;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private File defaultMineralFile = new File(new File("").getAbsolutePath() + "/src/data/Minerals.csv");
	
	private ObservableList<Mineral> mineralData = FXCollections.observableArrayList();
	private ObservableMap<String, HashMap<String, Integer>> searchResultCounts = FXCollections.observableHashMap();
	
	private StringProperty currentSearch = new SimpleStringProperty("");

	public MainApp() {
		searchResultCounts.put("Name", new HashMap<String, Integer>());
		searchResultCounts.put("Color", new HashMap<String, Integer>());
		searchResultCounts.put("Streak", new HashMap<String, Integer>());
		searchResultCounts.put("Luster", new HashMap<String, Integer>());
		searchResultCounts.put("Diaphaneity", new HashMap<String, Integer>());
		searchResultCounts.put("Fracture", new HashMap<String, Integer>());
		searchResultCounts.put("Cleavage", new HashMap<String, Integer>());
		searchResultCounts.put("Other Property", new HashMap<String, Integer>());
		
		readMineralList(defaultMineralFile);
		Collections.sort(mineralData);
		updateMineralProperties();
	}

	public File getMineralFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		}
		else {
			return null;
		}
	}

	public void setMineralFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());
			
			primaryStage.setTitle("Mineral Search App - " + file.getName());
		}
		else {
			prefs.remove("filePath");
			
			primaryStage.setTitle("Mineral Search App");
		}
	}
	
	public ObservableList<Mineral> getMineralData() {
		return mineralData;
	}
	
	public StringProperty currentSearchProperty() {
		return currentSearch;
	}

	public String getCurrentSearch() {
		return currentSearch.get();
	}
	
	public void resetCurrentSearchProperty() {
		currentSearch.set("");
	}
	
	public ObservableMap<String, HashMap<String, Integer>> getSearchResultCounts() {
		return searchResultCounts;
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Mineral Search App");
		
		initRootLayout();
		showMineralOverview();
	}
	
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/RootLayout.fxml"));
			rootLayout = (BorderPane)loader.load();
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);;
			primaryStage.show();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showMineralOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/MineralOverview.fxml"));
			AnchorPane mineralOverview = (AnchorPane)loader.load();
			rootLayout.setCenter(mineralOverview);
			
			MineralOverviewController controller = loader.getController();
			controller.setMainApp(this);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean showMineralSearch() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/MineralSearch.fxml"));
			AnchorPane mineralSearchOverview = (AnchorPane)loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Mineral Search");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(mineralSearchOverview);
			dialogStage.setScene(scene);
			
			MineralSearchController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			dialogStage.showAndWait();
			
			return controller.isOkClicked();
		}	catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean showAddMineral() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/AddMineral.fxml"));
			AnchorPane newMineralOverview = (AnchorPane)loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add New Mineral");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(newMineralOverview);
			dialogStage.setScene(scene);
			
			AddMineralController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			dialogStage.showAndWait();
			
			return controller.isOkClicked();
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showEditMineral(Mineral mineral) {
		try {
			EditMineralController controller = new EditMineralController(mineral);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/EditMineral.fxml"));
			loader.setController(controller);
			AnchorPane editMineralOverview = (AnchorPane)loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Mineral");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(editMineralOverview);
			dialogStage.setScene(scene);
			controller.setDialogStage(dialogStage);
			
			dialogStage.showAndWait();
			
			return controller.isOkClicked();
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void readMineralList(File file) {
		mineralData.clear();
		
		BufferedReader br;
		String line;
		try {
			br = new BufferedReader(new FileReader(file.getPath()));
			line = br.readLine();

			while ((line = br.readLine()) != null) {
				mineralData.add(createNewMineral(line));
			}
		} catch (FileNotFoundException e1) {
			System.out.println("Could not open file, " + file.getPath());
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(mineralData);
	}
	
	private Mineral createNewMineral(String line) {
		
		String[] values = line.split(",");
		
		// Read in Name
		String name = values[0];
		
		//Read in Colors
		ArrayList<String> colors = new ArrayList<String>();
		for (int i = 1; i < 7; i++) {
			if (!values[i].isEmpty()) {
				colors.add(values[i]);
			}
		}
		
		// Read in Streaks
		ArrayList<String> streaks = new ArrayList<String>();
		for (int i = 7; i < 13; i++) {
			if (!values[i].isEmpty()) {
				streaks.add(values[i]);
			}
		}
		
		//Read in Luster
		ArrayList<String> lusters = new ArrayList<String>();
		for (int i = 13; i < 16; i++) {
			if (!values[i].isEmpty()) {
				lusters.add(values[i]);
			}
		}
		
		//Read in Diaphaneity
		ArrayList<String> diaphaneities = new ArrayList<String>();
		for (int i = 16; i < 18; i++) {
			if (!values[i].isEmpty()) {
				diaphaneities.add(values[i]);
			}
		}
				
		//Read in Other
		ArrayList<String> others = new ArrayList<String>();
		for (int i = 18; i < 20; i++) {
			if (!values[i].isEmpty()) {
				others.add(values[i]);
			}
		}
		
		//Read in Fracture
		Boolean fracture = Boolean.parseBoolean(values[20]);
				
		//Read in Cleavage
		Integer cleavage = Integer.parseInt(values[21]);
				
		//Read in Hardness
		Float hardLow = Float.parseFloat(values[22]);
		Float hardHigh = Float.parseFloat(values[23]);
		
		//Read in Specific Gravity
		Float specGravLow = Float.parseFloat(values[24]);
		Float specGravHigh = Float.parseFloat(values[25]);
		
		return new Mineral(name, colors, streaks, lusters, diaphaneities, others, fracture, cleavage, hardLow, hardHigh, specGravLow, specGravHigh);
	}
	
	public boolean saveMineralData(File file) {
		BufferedWriter bw = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write("Name,Color1,Color2,Color3,Color4,Color5,Color6,Streak1,Streak2,Streak3,Streak4,Streak5,Streak6,Luster1,Luster2,Luster3,Diaphaneity1,Diaphaneity2,Other1,Other2,Fracture,Cleavage,HardLow,HardHigh,SpecGravLow,SpecGravHigh");
			bw.newLine();
			
			for (Mineral mineral : mineralData) {
				bw.write(mineral.getName() + ",");

				for (int i = 0; i < 6; i++) {
					if (i < mineral.colorProperty().size()) {
						bw.write(mineral.colorProperty().get(i).get() + ",");
					}
					else {
						bw.write(",");
					}
				}
				
				for (int i = 0; i < 6; i++) {
					if (i < mineral.streakProperty().size()) {
						bw.write(mineral.streakProperty().get(i).get() + ",");
					}
					else {
						bw.write(",");
					}
				}
				
				for (int i = 0; i < 3; i++) {
					if (i < mineral.lusterProperty().size()) {
						bw.write(mineral.lusterProperty().get(i).get() + ",");
					}
					else {
						bw.write(",");
					}
				}
				
				for (int i = 0; i < 2; i++) {
					if (i < mineral.diaphaneityProperty().size()) {
						bw.write(mineral.diaphaneityProperty().get(i).get() + ",");
					}
					else {
						bw.write(",");
					}
				}
				
				for (int i = 0; i < 2; i++) {
					if (i < mineral.otherProperty().size()) {
						bw.write(mineral.otherProperty().get(i).get() + ",");
					}
					else {
						bw.write(",");
					}
				}
				
				bw.write(mineral.fractureProperty().getValue().toString() + ",");
				
				bw.write(mineral.cleavageProperty().getValue().toString() + ",");
				
				bw.write(mineral.hardLowProperty().getValue().toString() + ",");
				bw.write(mineral.hardHighProperty().getValue().toString() + ",");
				
				bw.write(mineral.specGravLowProperty().getValue().toString() + ",");
				bw.write(mineral.specGravHighProperty().getValue().toString());
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (Exception e) {
				System.out.println("Error closing BufferedWriter");
				return false;
			}
		}
		return true;

	}
	
	public void updateMineralProperties() {
		searchResultCounts.get("Name").clear();
		searchResultCounts.get("Color").clear();
		searchResultCounts.get("Streak").clear();
		searchResultCounts.get("Luster").clear();
		searchResultCounts.get("Diaphaneity").clear();
		searchResultCounts.get("Fracture").clear();
		searchResultCounts.get("Cleavage").clear();
		searchResultCounts.get("Other Property").clear();
		
		for (Mineral mineral : mineralData) {
			searchResultCounts.get("Name").put(mineral.getName(), 1);
			
			for (StringProperty color : mineral.colorProperty()) {
				if (searchResultCounts.get("Color").get(color.get()) != null) {
					searchResultCounts.get("Color").put(color.get(), searchResultCounts.get("Color").get(color.get()) + 1);
				}
				else {
					searchResultCounts.get("Color").put(color.get(), 1);
				}
			}
			
			for (StringProperty streak : mineral.streakProperty()) {
				if (searchResultCounts.get("Streak").get(streak.get()) != null) {
					searchResultCounts.get("Streak").put(streak.get(), searchResultCounts.get("Streak").get(streak.get()) + 1);
				}
				else {
					searchResultCounts.get("Streak").put(streak.get(), 1);
				}
			}
			
			for (StringProperty luster : mineral.lusterProperty()) {
				if (searchResultCounts.get("Luster").get(luster.get()) != null) {
					searchResultCounts.get("Luster").put(luster.get(), searchResultCounts.get("Luster").get(luster.get()) + 1);
				}
				else {
					searchResultCounts.get("Luster").put(luster.get(), 1);
				}
			}
			
			for (StringProperty diaphaneity : mineral.diaphaneityProperty()) {
				if (searchResultCounts.get("Diaphaneity").get(diaphaneity.get()) != null) {
					searchResultCounts.get("Diaphaneity").put(diaphaneity.get(), searchResultCounts.get("Diaphaneity").get(diaphaneity.get()) + 1);
				}
				else {
					searchResultCounts.get("Diaphaneity").put(diaphaneity.get(), 1);
				}
			}
			
			if (searchResultCounts.get("Fracture").get(mineral.getFracture()) != null) {
				searchResultCounts.get("Fracture").put(mineral.getFracture(), searchResultCounts.get("Fracture").get(mineral.getFracture()) + 1);
			}
			else {
				searchResultCounts.get("Fracture").put(mineral.getFracture(), 1);
			}
			
			if (searchResultCounts.get("Cleavage").get(mineral.cleavageProperty().getValue().toString()) != null) {
				searchResultCounts.get("Cleavage").put(mineral.cleavageProperty().getValue().toString(), searchResultCounts.get("Cleavage").get(mineral.cleavageProperty().getValue().toString()) + 1);
			}
			else {
				searchResultCounts.get("Cleavage").put(mineral.cleavageProperty().getValue().toString(), 1);
			}
			
			for (StringProperty other : mineral.otherProperty()) {
				if (searchResultCounts.get("Other Property").get(other.get()) != null) {
					searchResultCounts.get("Other Property").put(other.get(), searchResultCounts.get("Other Property").get(other.get()) + 1);
				}
				else {
					searchResultCounts.get("Other Property").put(other.get(), 1);
				}
			}
		}
	}
	
	public void processNewMineral(Mineral newMineral) {
		mineralData.add(newMineral);
		updateMineralProperties();
	}
	
	public void deleteMineral(Mineral mineral) {
		mineralData.remove(mineral);
	}
	
	public void searchMinerals(String searchValue, String searchType) {
		Iterator<Mineral> i = mineralData.iterator();
		
		switch (searchType) {
		case "Name":
			while (i.hasNext()) {
				Mineral mineralToTest = i.next();
				if (!mineralToTest.getName().toLowerCase().contains(searchValue.toLowerCase())) {
					i.remove();
				}
			}
			break;
		case "Color":
			while (i.hasNext()) {
				Mineral mineralToTest = i.next();
				if (!mineralToTest.getColor().contains(searchValue.toLowerCase())) {
					i.remove();
				}
			}
			break;
		case "Streak":
			while (i.hasNext()) {
				Mineral mineralToTest = i.next();
				if (!mineralToTest.getStreak().contains(searchValue.toLowerCase())) {
					i.remove();
				}
			}
			break;
		case "Luster":
			while (i.hasNext()) {
				Mineral mineralToTest = i.next();
				if (!mineralToTest.getLuster().contains(searchValue.toLowerCase())) {
					i.remove();
				}
			}
			break;
		case "Diaphaneity":
			while (i.hasNext()) {
				Mineral mineralToTest = i.next();
				if (!mineralToTest.getDiaphaneity().contains(searchValue.toLowerCase())) {
					i.remove();
				}
			}
			break;
		case "Other Property":
			while (i.hasNext()) {
				Mineral mineralToTest = i.next();
				if (!mineralToTest.getOther().contains(searchValue.toLowerCase())) {
					i.remove();
				}
			}
			break;
		case "Fracture":
			Boolean fracture = false;
			if (searchValue.equals("Displays fracture")) {
				fracture = true;
			}
			while (i.hasNext()) {
				Mineral mineralToTest = i.next();
				if (mineralToTest.fractureProperty().get() != fracture) {
					i.remove();
				}
			}
			break;
		case "Cleavage":
			while (i.hasNext()) {
				Mineral mineralToTest = i.next();
				if (mineralToTest.cleavageProperty().get() != Integer.parseInt(searchValue)) {
					i.remove();
				}
			}
			break;
		case "Hardness":
			while (i.hasNext()) {
				Mineral mineralToTest = i.next();
				if (mineralToTest.hardLowProperty().get() > Float.parseFloat(searchValue)
						|| mineralToTest.hardHighProperty().get() < Float.parseFloat(searchValue)) {
					i.remove();
				}
			}
			break;
		case "Specific Gravity":
			while (i.hasNext()) {
				Mineral mineralToTest = i.next();
				if (mineralToTest.specGravLowProperty().get() > Float.parseFloat(searchValue)
						|| mineralToTest.specGravHighProperty().get() < Float.parseFloat(searchValue)) {
					i.remove();
				}
			}
			break;
		default:
			break;
		}
		
		if (currentSearch.get().isEmpty()) {
			currentSearch.set("Showing results for " + searchType + ": '" + searchValue + "'");
		}
		else {
			currentSearch.set(currentSearch.get() + ", " + searchType + ": '" + searchValue + "'");
		}
		updateMineralProperties();
	}	
}



