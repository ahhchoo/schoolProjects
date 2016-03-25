package application;
import java.sql.*;

// databaseCommands extends databaseConnection because databaseCommands requires the connection.
public class MenuModel extends WelcomeModel {	
	/** the methods here are meant for the test purpose
	//public int returnNumberOfColumns(){
	//return 3;
	//}
	//public String[] returnColumnType(){
	//String []testColType = new String[3];
	//testColType[0] = "String";
	//testColType[1] = "String";
	//testColType[2] = "String";
	//return testColType;
	//}
	//public String[] returnColumnNames(){
	//String []testColName = new String[3];
	//testColName[0] = "Name";
	//testColName[1] = "Hobby";
	//testColName[2] = "Location";
	//return testColName;
	//}
	*/

	public boolean doesTableExist() throws SQLException {
		boolean tableExist = false;
		DatabaseMetaData md = (DatabaseMetaData) connect.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		while (rs.next()) {
			if(rs.getString(3).toUpperCase().equals("PIKACHUTABLE")) {
				tableExist = true;
				break;
			} else {
				tableExist = false;
			}
		}
		return tableExist;
	}
	// Creating the table based on the number of columns, the type of columns, and number of columns.
	public boolean createTable(String[] typeOfColumns, String[] nameOfColumns) throws SQLException {
		// Boolean value to check if table creation was successful. Requested by Alison.
		boolean successfullyUpdatedTable = false;
		Statement stmt = null;
		String stmtQuery = "CREATE TABLE pikachuTable (";
		for(int i = 0; i < typeOfColumns.length-1; i++) {
			// User wants integer column.
			if(typeOfColumns[i].toUpperCase().equals("NUMBER")) {
				stmtQuery +=  nameOfColumns[i] + " INTEGER, ";
				// User wants string(characters) column.
			} else {
				stmtQuery +=  nameOfColumns[i] + " VARCHAR(200), ";
			}
		}
		// Doing the last value outside of loop to prevent mysql syntax error. ( , )
		if(typeOfColumns[typeOfColumns.length-1].toUpperCase().equals("NUMBER")) {
			stmtQuery += nameOfColumns[typeOfColumns.length-1] + " INTEGER)";
		} else {
			stmtQuery += nameOfColumns[typeOfColumns.length-1] +" VARCHAR(200))";				
		}
		try{
			stmt = connect.createStatement();
			stmt.executeUpdate(stmtQuery);
			stmt.close();
			successfullyUpdatedTable = true;
		}catch(Exception e){
			
		}
		return successfullyUpdatedTable;
	}
	

	// Function that is responsible for the inserting data into the table.
	public boolean insertData(String[] dataToBeInserted) throws SQLException{
		// Boolean value to check if inserting data was successful. Requested by Alison.
		boolean successfullyInsertedData = false;
		
		// Getting the names of the columns first.
        String[] myColumnName = returnColumnNames();
        String[] myColumnType = returnColumnType();

        Statement stmt = null;
        String stmtQuery = "INSERT INTO pikachuTable  (";
        for(int i = 0; i < myColumnName.length-1; i++) {
        	stmtQuery += myColumnName[i] + ", ";
        }
        stmtQuery += myColumnName[myColumnName.length-1] + ") VALUES(";

        // Need to check which values are integers, and parse those values because certain columns only take integers values.
        for(int i = 0; i < dataToBeInserted.length-1; i++) {
        	if(myColumnType[i] == "NUMBER") {
        		int parsedTempInt = Integer.parseInt(dataToBeInserted[i]);
        		stmtQuery += parsedTempInt + ",";
        	} else {
        		stmtQuery += "'";
        		stmtQuery += dataToBeInserted[i] + "',";
        	}
        }
        if(myColumnType[dataToBeInserted.length-1] == "NUMBER") {
        	int parsedTempInt = Integer.parseInt(dataToBeInserted[dataToBeInserted.length-1]);
        	stmtQuery += parsedTempInt;
        	stmtQuery += ")";
        } else {
        	stmtQuery += "'";
        	stmtQuery += dataToBeInserted[dataToBeInserted.length-1] + "'";
        	stmtQuery += ")";
        }
        stmt = connect.createStatement();
        stmt.executeUpdate(stmtQuery);
        stmt.close();
        successfullyInsertedData = true;
            
        return successfullyInsertedData;
	}
	
	// Function that is responsible for updating data on the table.
	public boolean updateTableData(String[] columnOfUpdatedData, String[] updatedData, String[] columnOfNotChangedData, String[] dataNotChanged) throws SQLException {
		// Boolean value to check if update was successfully. Requested by Alison.
		boolean successfullyUpdated = false;
		Statement stmt = null;
        
        try {
            String stmtQuery = "UPDATE pikachuTable SET ";
            for(int i = 0; i < columnOfUpdatedData.length-1; i++) {
            	stmtQuery += columnOfUpdatedData[i] + " = '" + updatedData[i] + "', ";
            }
            stmtQuery += columnOfUpdatedData[columnOfUpdatedData.length-1] + " = '" + updatedData[updatedData.length-1] + "'";
            stmtQuery += " WHERE ";
            for(int i = 0; i < columnOfNotChangedData.length-1; i++) {
            	stmtQuery += columnOfNotChangedData[i] + " = '" + dataNotChanged[i] + "' AND ";
            }
            stmtQuery += columnOfNotChangedData[columnOfNotChangedData.length-1] + " = '" + dataNotChanged[dataNotChanged.length-1] + "'";
            
            stmt = connect.createStatement();
            stmt.executeUpdate(stmtQuery);
            stmt.close();
            successfullyUpdated = true;
        } catch (Exception e) {
        
        }
	
		return successfullyUpdated;
	}
	
	// Function that is responsible for deleting a specific entry in the table.
	public boolean deleteData(String[] dataToBeDeleted) throws SQLException {
		boolean successfullyDeleted = false;
		String[] columnNames = returnColumnNames();		
		
		Statement stmt = null;

		String stmtQuery = "DELETE FROM pikachuTable WHERE ";
		for(int i = 0; i < columnNames.length-1; i++) {
			stmtQuery += columnNames[i] + " = '" + dataToBeDeleted[i] + "' AND ";  
		}
		stmtQuery += columnNames[columnNames.length-1] + " = '" + dataToBeDeleted[dataToBeDeleted.length-1] + "'";

		stmt = connect.createStatement();
		stmt.executeUpdate(stmtQuery);
		stmt.close();
		successfullyDeleted = true;
		
		return successfullyDeleted;
	}

	// Function that is responsible for the selecting the specific data that the user wants.
	public String[][] selectData(String[] columnsToLook, String[] valuesOfColumns, String[] namesOfColumnsToPrint) throws SQLException{
		int numberOfColumns = namesOfColumnsToPrint.length;
		int numberOfMaxRowsPossible = returnNumberOfRows();
		// Array must be at most able to hold all the rows, and all the columns.
		String[][] resultAfterSelect = new String[numberOfMaxRowsPossible][numberOfColumns];
		PreparedStatement pstmt;
		
		try {
			String stmtQuery = "SELECT ";
			for(int i = 0; i < namesOfColumnsToPrint.length-1; i++) {
				stmtQuery += namesOfColumnsToPrint[i] + ", ";
				}
			stmtQuery += namesOfColumnsToPrint[namesOfColumnsToPrint.length-1] + " FROM pikachuTable WHERE ";
			for(int i = 0; i < columnsToLook.length-1; i++) {
				stmtQuery += columnsToLook[i] + " = '" + valuesOfColumns[i] + "' AND ";
				}
				stmtQuery += columnsToLook[columnsToLook.length-1] + " = '" + valuesOfColumns[valuesOfColumns.length-1] + "'";

			// Getting the data, and saving it in a 2-D array.
			pstmt = connect.prepareStatement(stmtQuery);
			ResultSet result= pstmt.executeQuery();
			int k = 0;
			while(result.next()) {
				int j = 0;
				for(int i = 1; i <= numberOfColumns;i ++) {
					resultAfterSelect[k][j++] = result.getString(i);
					}
				k++;
				}
			result.close();
			pstmt.close();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		return resultAfterSelect;
	}

	// Function that is responsible for displaying the current data on the table.
	public String[][] displayCurrentTable() throws SQLException {
		int numberOfColumns = returnNumberOfColumns();
		int numberOfRows = returnNumberOfRows();
		String[][] currentTable = new String[numberOfRows][numberOfColumns];
        PreparedStatement pstmt;
        
        try {
            String stmtQuery = "SELECT * FROM pikachuTable";
            // Getting the data, and saving it in a 2-D array.
            pstmt = connect.prepareStatement(stmtQuery);
            ResultSet result= pstmt.executeQuery();
            int k = 0;
            while(result.next()) {
            	int j = 0;
            	for(int i = 1; i <= numberOfColumns;i ++) {
            		currentTable[k][j++] = result.getString(i);
            	}
            	k++;
            }
            result.close();
            pstmt.close();
        	} catch (Exception e) {
			System.err.println(e.getMessage());
		}
        
		return currentTable;
	}
	
    // Function that is responsible for deleting table.
    public boolean deleteTable() throws SQLException {
    	boolean successfullyDeleted = false;
        Statement stmt = null;

        String stmtQuery = "DROP TABLE pikachuTable";
        stmt = connect.createStatement();
        stmt.executeUpdate(stmtQuery);
        stmt.close();
        successfullyDeleted = true;

        return successfullyDeleted;
    }
	
	// Function that is responsible for getting the number of columns.
	// Needed when selecting data.
	public int returnNumberOfColumns() throws SQLException {
		
        Statement statement = connect.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM pikachuTable");
        ResultSetMetaData metadata = results.getMetaData();
        // Getting the number of columns to make appropriate size array.
        int columnCount = metadata.getColumnCount();
		return columnCount;
	}
	
	// Function that is responsible for getting the data type of the columns.
	// Needed when inserting data into the table.
	public String[] returnColumnType() throws SQLException {	
        // Getting the number of columns to make appropriate size array.
        int columnCount = returnNumberOfColumns();
        
        Statement statement = connect.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM pikachuTable");
        ResultSetMetaData metadata = results.getMetaData();
        String[] myColumnType = new String[columnCount];
        // Saving the column types into an array for easier accessibility.
        int j=0;
        for (int i = 1; i <= columnCount; i++) {
        	myColumnType[j++] = metadata.getColumnTypeName(i);
        }
		return myColumnType;
	}
	
	// Function that is responsible for getting the name of the columns.
	// Needed when asking user to enter data for specific columns.
    public String[] returnColumnNames() throws SQLException{
        // Getting the number of columns to make appropriate size array.
        int columnCount = returnNumberOfColumns();
        
        Statement statement = connect.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM pikachuTable");
        ResultSetMetaData metadata = results.getMetaData();
        String[] myColumnName = new String[columnCount];
        // Saving the column names into an array for easier accessibility.
        int j=0;
        for (int i = 1; i <= columnCount; i++) {
            myColumnName[j++] = metadata.getColumnName(i);
        }
                
        return myColumnName;
    }
	
    // Function that is responsible for getting the number of rows on the table.
    // Needed for creating an array with number of rows for SELECT.
    public int returnNumberOfRows() {
    	int numberOfRows = 0;

		try {
            Statement statement = connect.createStatement();
            ResultSet rs = statement.executeQuery("SELECT count(*) from pikachuTable");
            rs.next();
            int count = rs.getInt(1);
            
            numberOfRows = count;
		} catch (Exception e) {
			System.err.println("Error in getting number of rows.");
		}
		
    	return numberOfRows;
    }
}
