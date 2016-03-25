package application;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
public class WelcomeModel{
	protected static java.sql.Connection connect;

	// Boolean class that installs the driver of the database.
	public boolean databaseDriver(String databaseChoice) {
		boolean driverInstalled = false;
		// Oracle Driver
		String oracleDriver = "oracle.jdbc.driver.OracleDriver";
		// mySQL Driver
		String mysqlDriver = "com.mysql.jdbc.Driver";

		// User opted to install oracle driver.
		if(databaseChoice.toUpperCase().equals("ORACLE")) {
			// Installing driver
			try {
				Class.forName(oracleDriver);
				driverInstalled = true;
			} catch (Exception e) {
				System.err.println("Invalid Oracle driver.");
			}
		// User opted to install mySQL driver.
		} else {
			// Installing driver
			try {
				Class.forName(mysqlDriver);
				driverInstalled = true;
			} catch (Exception e) {
				System.err.println("Invalid mySQL driver.");
			}
		}

		return driverInstalled;
	}

	public boolean connectingToDatabase(String databaseChoice, String[] tempArrayWithDatabaseCredentials) throws SQLException {
		boolean connectedToDatabase = false;
		String fullUrl;

		// User opted to connect to the oracle database.
		if(databaseChoice.toUpperCase().equals("ORACLE")) {
			fullUrl = "jbc:oracle:thin:@" + tempArrayWithDatabaseCredentials[0] + ":" + tempArrayWithDatabaseCredentials[1] + ":" + tempArrayWithDatabaseCredentials[2];

			connect = DriverManager.getConnection(fullUrl, tempArrayWithDatabaseCredentials[3], tempArrayWithDatabaseCredentials[4]);
			connectedToDatabase = true;
			System.out.println(fullUrl);
		// User opted to connect to mySQL database.
		} else {
			fullUrl = "jdbc:mysql://" + tempArrayWithDatabaseCredentials[0] + ":" + tempArrayWithDatabaseCredentials[1] + "/" + tempArrayWithDatabaseCredentials[2];

			connect = DriverManager.getConnection(fullUrl, tempArrayWithDatabaseCredentials[3], tempArrayWithDatabaseCredentials[4]);
			connectedToDatabase = true;

		}

		return connectedToDatabase;
	}
	
	public java.sql.Connection passConnection() {
		return connect;
	}

    
    public void closeConnection() throws SQLException {
    	connect.close();
    }
}
