package application;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MenuModelTest {
	
	MenuModel dbMod= new MenuModel();
	/**
	 * Plan:
	 * Debug insert button bug 
	 * Problem: 
	 * Inserting valid types but invalid pop displays anyway  
	 * 
	 * attempted to create hand-rolled mock objects to test the insert function in databse Model
	 * difficulties were understanding the state of the objects. 
	 *  
	 * */
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testInsert() {
		
		WelcomeModel wMod = new WelcomeModel();		
		wMod.databaseDriver("oracle");		
		//WelcomeModel Mock
		MenuModel dbMod = new MenuModel();
		//DatabaseModel Mock
		Controller conTest = new Controller();
		//Controller Mock
		
		String []exampleLoginInfo = new String[5];
		exampleLoginInfo[0] = "bonnet19.cs.qc.edu";
		exampleLoginInfo[1] = "1521";
		exampleLoginInfo[2] = "dminor";
		exampleLoginInfo[3] = "lackner";
		exampleLoginInfo[4] = "guest";
		try{
			boolean isConnected = wMod.connectingToDatabase("Oracle", exampleLoginInfo);
		}catch(SQLException e){
			System.out.println("SQLException from connect");
		}

		//information to be inserted
		String []exampleInsertData = new String[4];
		exampleInsertData[0] = "Lin";
		exampleInsertData[1] = "25";
		exampleInsertData[2] = "Tennis";
		exampleInsertData[3] = "Red";
		thrown.expect(SQLException.class);
		boolean testInsertion = false;
		String[] testColName = new String[3];
		String[] testColType = new String[3];
		try {
			testInsertion = dbMod.insertData(exampleInsertData);
		} catch (SQLException e) {
			System.out.println("SQLException");
		}
		
		System.out.println(""+ testInsertion);
		
	}

}
