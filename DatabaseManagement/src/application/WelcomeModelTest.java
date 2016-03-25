package application;

import static org.junit.Assert.*;
import java.sql.SQLException;
import java.sql.Connection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
/*Plan
 * Test the WelcomeModel class
 * Find out what are the limitations of this class
 * 
 * Note many methods will be given specific information to help ensure fewer errors
 */
public class WelcomeModelTest {
/*Plan:
 * See if the databaseDriver method will return true if the driver is installed
 * the input can be spelled right but different input is lowercase or capital both mysql or oracle
 */
	@Test
	public void test() {
		WelcomeModel wMod = new WelcomeModel();
		assertTrue(wMod.databaseDriver("oracle"));
	}

	@Test
	public void test2() {
		WelcomeModel wMod = new WelcomeModel();
		assertTrue(wMod.databaseDriver("oRaCle"));
	}
	
	@Test
	public void test3() {
		WelcomeModel wMod = new WelcomeModel();
		assertTrue(wMod.databaseDriver("mysql"));
	}
	@Test
	public void test4() {
		WelcomeModel wMod = new WelcomeModel();
		assertTrue(wMod.databaseDriver("mYSql"));
	}
	
	//Driver tests passes with different capital of oracle and mysql  
	//information will be sent from 
	
	/*
	 * Plan: 
	 * test to see if login with right credentials right throw an exception if no good 
	 */
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Test
	public void test5() throws SQLException{
		
		WelcomeModel wMod = new WelcomeModel();
		String []exampleLoginInfo = new String[5];
		//correct data
		exampleLoginInfo[0] = "bonnet19.cs.qc.edu";
		exampleLoginInfo[1] = "1521";
		exampleLoginInfo[2] = "dminor";
		exampleLoginInfo[3] = "lackner";
		exampleLoginInfo[4] = "guest";
		
		assertTrue(wMod.connectingToDatabase("Oracle", exampleLoginInfo));

	}
	//only connect to data base that allows access
	
	@Test
	public void test6() throws SQLException{
		thrown.expect(SQLException.class);
		WelcomeModel wMod = new WelcomeModel();
		String []exampleLoginInfo = new String[5];
		//incorrect data
		exampleLoginInfo[0] = "bonnet19.com";
		exampleLoginInfo[1] = "2015";
		exampleLoginInfo[2] = "someSchema";
		exampleLoginInfo[3] = "user";
		exampleLoginInfo[4] = "pass";
		
		assertTrue(wMod.connectingToDatabase("Oracle", exampleLoginInfo));

	}
	
/*
 * Plan:
 * Test user login, with real or fake account and assertTrue when credentials are true. 
 *	should fail with invalid input  
 */
	//mySQL 
	@Test 
	public void test7() throws SQLException{
		//thrown.expect(SQLException.class);
		WelcomeModel wMod = new WelcomeModel();
		String []exampleLoginInfo = new String[5];
		//correct data
		exampleLoginInfo[0] = "db4free.net";
		exampleLoginInfo[1] = "3306";
		exampleLoginInfo[2] = "csci370";
		exampleLoginInfo[3] = "bonsypikachu370";
		exampleLoginInfo[4] = "pikachu";
		assertTrue(wMod.connectingToDatabase("mySQL", exampleLoginInfo));
	}
	
	@Test 
	public void test8() throws SQLException{
		//thrown.expect(SQLException.class);
		WelcomeModel wMod = new WelcomeModel();
		String []exampleLoginInfo = new String[5];
		//incorrect data
		exampleLoginInfo[0] = "fakedb.com";
		exampleLoginInfo[1] = "3236";
		exampleLoginInfo[2] = "exampleSchema";
		exampleLoginInfo[3] = "exampleuser";
		exampleLoginInfo[4] = "examplepassword";
		assertFalse(wMod.connectingToDatabase("mySQL", exampleLoginInfo));
	}
	
}
