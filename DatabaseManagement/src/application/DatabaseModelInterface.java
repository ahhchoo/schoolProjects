package application;

import java.sql.SQLException;

public interface DatabaseModelInterface {

	// Creating the table based on the number of columns, the type of columns, and number of columns.
	public boolean createTable(String[] typeOfColumns, String[] nameOfColumns) throws SQLException;

	// Function that is responsible for the inserting data into the table.
	public boolean insertData(String[] dataToBeInserted) throws SQLException;
	
	// Function that is responsible for updating data on the table.
	public boolean updateTableData(String[] columnOfUpdatedData, String[] updatedData, String[] columnOfNotChangedData, String[] dataNotChanged);
	
	// Function that is responsible for deleting a specific entry in the table.
	public boolean deleteData(String[] dataToBeDeleted) throws SQLException;
	
	// Function that is responsible for the selecting the specific data that the user wants.
	public String[][] selectData(String[] columnsToLook, String[] valuesOfColumns, String[] namesOfColumnsToPrint) throws SQLException;
	
	// Function that is responsible for displaying the current data on the table.
	public String[][] displayCurrentTable() throws SQLException;
		
	// Function that is responsible for deleting table.
    public boolean deleteTable() throws SQLException;
    
	// Function that is responsible for getting the number of columns.
	public int returnNumberOfColumns() throws SQLException;
	
	// Function that is responsible for getting the data type of the columns.
	public String[] returnColumnType() throws SQLException;
	
	// Function that is responsible for getting the name of the columns.
    public String[] returnColumnNames() throws SQLException;
    
    // Function that is responsible for getting the number of rows on the table.
    public int returnNumberOfRows();
    
    // Function that is responsible for checking if our table exist.
    public boolean doesTableExist() throws SQLException;
    
}
