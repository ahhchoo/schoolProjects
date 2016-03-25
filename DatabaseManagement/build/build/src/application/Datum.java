package application;

import javafx.beans.property.SimpleStringProperty;

public class Datum{

	private int maxCol; 
	private final SimpleStringProperty p1;
	private final SimpleStringProperty p2;
	private final SimpleStringProperty p3;
	private final SimpleStringProperty p4;
	private final SimpleStringProperty p5;
	private final SimpleStringProperty p6;

	public Datum(String string, String string2, String string3, String string4, String string5, String string6) {
		// TODO Auto-generated constructor stub
		p1 =  new SimpleStringProperty(string);
		p2 = new SimpleStringProperty(string2);
		p3 = new SimpleStringProperty(string3);
		p4 = new SimpleStringProperty(string4);
		p5 = new SimpleStringProperty(string5);
		p6 = new SimpleStringProperty(string6);
	}


}