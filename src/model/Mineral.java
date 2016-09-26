package model;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Mineral implements Comparable<Mineral> {

	private final StringProperty NAME;
	private final ArrayList<StringProperty> STREAK;
	private final ArrayList<StringProperty> COLOR;
	private final ArrayList<StringProperty> LUSTER;
	private final ArrayList<StringProperty> DIAPHANEITY;
	private final BooleanProperty FRACTURE;
	private final IntegerProperty CLEAVAGE;
	private final FloatProperty HARDLOW;
	private final FloatProperty HARDHIGH;
	private final FloatProperty SPECGRAVLOW;
	private final FloatProperty SPECGRAVHIGH;
	private final ArrayList<StringProperty> OTHER;
	
	public Mineral(
			String name,
			ArrayList<String> colors,
			ArrayList<String> streaks,
			ArrayList<String> lusters, 
			ArrayList<String> diaphaneities,  
			ArrayList<String> others,
			Boolean fracture, 
			Integer cleavage, 
			Float hardLow, 
			Float hardHigh,
			Float specGravLow, 
			Float specGravHigh) {
		this.NAME = new SimpleStringProperty(name);
		
		this.COLOR = new ArrayList<StringProperty>();
		for (String color : colors) {
			COLOR.add(new SimpleStringProperty(color));
		}
		
		this.STREAK = new ArrayList<StringProperty>();
		for (String streak : streaks) {
			STREAK.add(new SimpleStringProperty(streak));
		}
		
		this.LUSTER = new ArrayList<StringProperty>();
		for (String luster : lusters) {
			LUSTER.add(new SimpleStringProperty(luster));
		}

		this.DIAPHANEITY = new ArrayList<StringProperty>();
		for (String diaphaneity : diaphaneities) {
			DIAPHANEITY.add(new SimpleStringProperty(diaphaneity));
		}
		
		this.OTHER = new ArrayList<StringProperty>();		
		for (String other : others) {
			OTHER.add(new SimpleStringProperty(other));
		}
		
		this.FRACTURE = new SimpleBooleanProperty(fracture);
		this.CLEAVAGE = new SimpleIntegerProperty(cleavage);
		
		this.HARDLOW = new SimpleFloatProperty(hardLow);
		this.HARDHIGH = new SimpleFloatProperty(hardHigh);
		
		this.SPECGRAVLOW = new SimpleFloatProperty(specGravLow);
		this.SPECGRAVHIGH = new SimpleFloatProperty(specGravHigh);
		
	}
	
	public int compareTo(Mineral other) {
		return this.getName().compareTo(other.getName());
	}
	
	public StringProperty nameProperty() {
		return NAME;
	}
	public void setName(String name) {
		this.NAME.set(name);
	}
	public String getName() {
		return NAME.get();
	}
	
	public ArrayList<StringProperty> colorProperty() {
		return COLOR;
	}
	public void setColor(String... colors) {
		this.COLOR.clear();
		for (String color : colors) {
			this.COLOR.add(new SimpleStringProperty(color));
		}
	}
	public String getColor() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < COLOR.size(); i++) {
			sb.append(COLOR.get(i).get());
			if (i + 1 < COLOR.size()) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	
	public ArrayList<StringProperty> streakProperty() {
		return STREAK;
	}
	public void setStreak(String... streaks) {
		this.STREAK.clear();
		for (String streak : streaks) {
			this.STREAK.add(new SimpleStringProperty(streak));
		}
	}
	public String getStreak() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < STREAK.size(); i++) {
			sb.append(STREAK.get(i).get());
			if (i + 1 < STREAK.size()) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	public ArrayList<StringProperty> lusterProperty() {
		return LUSTER;
	}
	public void setLuster(String... lusters) {
		this.LUSTER.clear();
		for (String luster : lusters) {
			this.LUSTER.add(new SimpleStringProperty(luster));
		}
	}
	public String getLuster() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < LUSTER.size(); i++) {
			sb.append(LUSTER.get(i).get());
			if (i + 1 < LUSTER.size()) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	
	public ArrayList<StringProperty> diaphaneityProperty() {
		return DIAPHANEITY;
	}
	public void setDiaphaneity(String... diaphaneities) {
		this.DIAPHANEITY.clear();
		for (String diaphaneity : diaphaneities) {
			this.DIAPHANEITY.add(new SimpleStringProperty(diaphaneity));
		}
	}
	public String getDiaphaneity() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < DIAPHANEITY.size(); i++) {
			sb.append(DIAPHANEITY.get(i).get());
			if (i + 1 < DIAPHANEITY.size()) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	
	public ArrayList<StringProperty> otherProperty() {
		return OTHER;
	}
	public void setOther(String... others) {
		this.OTHER.clear();
		for (String other : others) {
			this.OTHER.add(new SimpleStringProperty(other));
		}
	}
	public String getOther() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < OTHER.size(); i++) {
			sb.append(OTHER.get(i).get());
			if (i + 1 < OTHER.size()) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	
	public BooleanProperty fractureProperty() {
		return FRACTURE;
	}
	public void setFracture(Boolean fracture) {
		this.FRACTURE.set(fracture);
	}
	public String getFracture() {
		if (FRACTURE.get()) {
			return "Displays fracture";
		} 
		else {
			return "Displays cleavage";
		}
	}
	
	public IntegerProperty cleavageProperty() {
		return CLEAVAGE;
	}
	public void setCleavage(Integer cleavage) {
		this.CLEAVAGE.set(cleavage);
	}
	public String getCleavage() {
		return CLEAVAGE.get() + " planes";
	}
	
	public FloatProperty hardLowProperty() {
		return HARDLOW;
	}
	public void setHardLow(Float hardLow) {
		this.HARDLOW.set(hardLow);
	}
	public FloatProperty hardHighProperty() {
		return HARDHIGH;
	}
	public void setHardHigh(Float hardHigh) {
		this.HARDHIGH.set(hardHigh);
	}
	public String getHardness() {
		return HARDLOW.get() + " - " + HARDHIGH.get();
	}
	
	public FloatProperty specGravLowProperty() {
		return SPECGRAVLOW;
	}
	public FloatProperty specGravHighProperty() {
		return SPECGRAVHIGH;
	}
	public void setSpecGravLow(Float specGravLow) {
		this.SPECGRAVLOW.set(specGravLow);
	}
	public void setSpecGravHigh(Float specGravHigh) {
		this.SPECGRAVHIGH.set(specGravHigh);
	}
	public String getSpecGrav() {
		return SPECGRAVLOW.get() + " - " + SPECGRAVHIGH.get();
	}
}
