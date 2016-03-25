package application;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.SQLException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller {

	@FXML private SplitPane splitPane; 
	//Variables for Logging in 
	@FXML private Text failTarget, successTarget, missingTarget; 
	@FXML private TextField tfUrl,tfPort, tfSchema, tfUser;
	@FXML private PasswordField tfPass; 
	@FXML private ChoiceBox<String> choiceDB;
	@FXML private Button btnSubmit;         
	private static WelcomeModel welcome = null; 
	private static MenuModel db = null; 
	private String loginType; 
	private String[] credentials; 

	//Variables for MenuView
	@FXML private Button btnLogout;
	protected GridPane insertDelete, update, search, displayGrid,create; 
	private ScrollPane displayScroll; 
	@FXML private Pane menuPane;
	@FXML private TabPane tabPane;
	private Tab tabWelcome, tabCreate, tabInsertDelete, tabSearch, tabUpdate, tabDisplay; 

	//Choice Login
	@FXML private void handleChoiceBoxAction(Event event){
		choiceDB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, 
					String old_val, String new_val) {
				tfUser.setText("");
				tfPass.setText("");
				if("Oracle".contains(new_val)){
					loginType = "Oracle";
					tfUrl.setText("bonnet19.cs.qc.edu");
					tfPort.setText("1521");
					tfSchema.setText("dminor");
				}else{//MySQL
					loginType = "MySQL";
					tfUrl.setText("db4free.net");
					tfPort.setText("3306");
					tfSchema.setText("csci370");
				}
			}
		});
	}

	//Function to login 
	@FXML private void handleLogin() throws SQLException {
		credentials = new String[]{tfUrl.getText(),tfPort.getText(),tfSchema.getText(), tfUser.getText(), tfPass.getText()};
		boolean fill = checkInput(credentials); 
		boolean validPort = validPort(Integer.parseInt(tfPort.getText()));

		if(fill && validPort){
			clearText();
			successTarget.setText("Attempting login...");
			welcome = new WelcomeModel();

			boolean work = false;
			try{
				welcome.databaseDriver(loginType);
			}catch(Exception e){
				clearText();
				failTarget.setText("Unable to locate " + loginType + " driver.");
			}
			try{
				work = welcome.connectingToDatabase(loginType, credentials);
				db = new MenuModel();
			}catch(Exception e){
				clearText();
				failTarget.setText("Invalid login credentials to " + loginType + " database.");
			}

			if(work){
				Stage stage; 
				Parent root = null;

				//get reference to the button's stage         
				stage=(Stage) btnSubmit.getScene().getWindow();
				//load up OTHER FXML document
				try {
					root = FXMLLoader.load(getClass().getResource("MenuView.fxml"));
				} catch (IOException e) {
					AlertBox load= new AlertBox();
					load.display("Loading View Fail","Retrieval of the Menu display failed.");
				}
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
				
				if(!db.doesTableExist()){
					AlertBox error = new AlertBox();
					error.display("Table Not Created", "Your table hasn't been creatd. Please create table first");
				}else; 
				
			}else{
				clearText();
				failTarget.setText("Unable to connect to " + loginType + " database.");
			}
		}else{
			clearText();
			missingTarget.setText("One or more inputs are missing.\nNote: Check for valid port?");
		}

	}

	@FXML private void handleQuitButtonAction(ActionEvent event){
		AlertBox quit = new AlertBox();
		String decision = quit.followUp("Quit", "Are you sure you want to quit?\n");
	 		if(decision.equals("Yes")){
	 			System.exit(1);
	 		}else{
	 			//do nothing	
	 		}
	}

	private boolean validPort(int num){
		if(num < 1 || num > 65535){
			return false; 
		} else return true; 
	}

	//check if User has everything inserted
	private boolean checkInput(String[] cred){
		for(int i =0; i < cred.length;i++){
			if(cred[i].equals("") || cred[i].equals(null)){
				return false; 
			}
		}
		return true; 
	}

	private void clearText(){
		failTarget.setText("");
		missingTarget.setText("");
		successTarget.setText("");
	}
	
@FXML private void handleCreateAction(){
	boolean exist = false;
	try {
		exist = db.doesTableExist();
	} catch (SQLException e1) {
		AlertBox error = new AlertBox();
		error.display("Error in Create Table", "Cannot determine if table exists or not.");
	}
	
	if(exist){
		AlertBox error = new AlertBox();
		error.display("Table Already Created", "Your table already exists. Create Function not available.");
	}else{
		try{	
			tabCreate = new Tab();
			create= new GridPane();

			create.setAlignment(Pos.CENTER);
			create.setHgap(5);
			create.setVgap(5);
			create.setPadding(new Insets(4, 4, 4, 4));

			Label createTable = new Label("You currently don't have a table in your schema\n Let's create a table.");
			create.add(createTable,0,0,1,1);
			Label howManyColumns = new Label("How many columns do you want?");
			create.add(howManyColumns, 0, 1);

			ChoiceBox<String> colNumbers = new ChoiceBox<String>();
			colNumbers.getItems().addAll("1","2","3","4","5","6");

			GridPane colGrid = new GridPane();
			colGrid.setAlignment(Pos.CENTER);
			colGrid.setHgap(4);
			colGrid.setVgap(4);
			colGrid.setPadding(new Insets(1, 1, 1, 1));

			TextField[] createColNames = new TextField[6];
			Label colCreateInstruction = new Label("Fill out the information.");
			create.add(colNumbers, 0, 2);
			colGrid.add(colCreateInstruction, 0,3);

			Label[] colLabel = new Label[6];
			int row =4;
			for(int i=0; i < 6; i++){
				colLabel[i] = new Label("Column " + (i+1));
				colLabel[i].setVisible(false);
				colGrid.add(colLabel[i], 0, row);
				createColNames[i] = new TextField();
				createColNames[i].setVisible(false);
				colGrid.add(createColNames[i],1,row);
				row++;
			}

			colNumbers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
			{
				public void changed(ObservableValue<? extends String> ov, 
						String old_val, String new_val) {

					int maxCol = Integer.parseInt(new_val);
					//set Visible
					ChoiceBox<String> colType0 = new ChoiceBox<String>();
					colType0.getItems().addAll("String", "Number");
					colType0.getSelectionModel().selectFirst();
					ChoiceBox<String> colType1 = new ChoiceBox<String>();
					colType1.getItems().addAll("String", "Number");
					colType1.getSelectionModel().selectFirst();
					ChoiceBox<String> colType2 = new ChoiceBox<String>();
					colType2.getItems().addAll("String", "Number");
					colType2.getSelectionModel().selectFirst();
					ChoiceBox<String> colType3 = new ChoiceBox<String>();
					colType3.getItems().addAll("String", "Number");
					colType3.getSelectionModel().selectFirst();
					ChoiceBox<String> colType4 = new ChoiceBox<String>();
					colType4.getItems().addAll("String", "Number");
					colType4.getSelectionModel().selectFirst();
					ChoiceBox<String> colType5 = new ChoiceBox<String>();
					colType5.getItems().addAll("String", "Number");
					colType5.getSelectionModel().selectFirst();

					int gridRow =4; 
					for(int i =0; i <maxCol; i++){

						colLabel[i].setVisible(true);
						createColNames[i].setVisible(true);
						if(i==0){
							colGrid.add(colType0, 2, gridRow);
							gridRow++;
						}else if(i==1){
							colGrid.add(colType1, 2, gridRow);
							gridRow++;
						}else if(i==2){
							colGrid.add(colType2, 2, gridRow);
							gridRow++;
						}else if(i==3){
							colGrid.add(colType3, 2, gridRow);
							gridRow++;
						}else if(i==4){
							colGrid.add(colType4, 2, gridRow);
							gridRow++;
						}else 
						{
							colGrid.add(colType5, 2, gridRow);
						}
					}

					Button finalizeCol = new Button("Submit");
					finalizeCol.setOnAction(new EventHandler<ActionEvent>(){

						@Override
						public void handle(ActionEvent event) {   
							String[] typesOfColumns = new String[maxCol];
							String[] namesOfColumns = new String[maxCol];

							for(int i =0; i < maxCol; i++){
								namesOfColumns[i] = createColNames[i].getText(); 
								if(i==0){
									typesOfColumns[i]=colType0.getSelectionModel().getSelectedItem();
								}else if(i==1){
									typesOfColumns[i]=colType1.getSelectionModel().getSelectedItem();
								}else if(i==2){
									typesOfColumns[i]=colType2.getSelectionModel().getSelectedItem();
								}else if(i==3){
									typesOfColumns[i]=colType3.getSelectionModel().getSelectedItem();
								}else if(i==4){
									typesOfColumns[i]=colType4.getSelectionModel().getSelectedItem();
								}else if(i==5){
									typesOfColumns[i]=colType5.getSelectionModel().getSelectedItem();	
								}else;     
							}

							boolean isFilledCol = checkInput(namesOfColumns);

							if(isFilledCol){
								boolean work = false;
								try {
									work = db.createTable(typesOfColumns, namesOfColumns);
								} catch (SQLException e) {
									AlertBox createFail = new AlertBox();
									createFail.display("Creation of Table Fail.","Another table already exists. Only one table is allowed.");
								}
								if(work){					
									AlertBox success = new AlertBox();
									success.display("Table created", "PikachuTable successfully created table.");
								}else;
							}else{//Not Filled
								AlertBox notReadyCol = new AlertBox();
								notReadyCol.display("Columns Not Ready","Not all columns have been named.");
							}            		      
						}

					});
					create.add(finalizeCol, 0, 9);
					create.add(colGrid, 0, 4);
				}
			});

			tabCreate.setContent(create);
			tabCreate.setText("Create");
			tabPane.getTabs().add(tabCreate);
			tabPane.getSelectionModel().select(tabCreate);

		}catch(Exception e){
			AlertBox noTable = new AlertBox();
			noTable.display("Error","PikachuTable not created. Create Table first.");
		}
	}
}
	
	private boolean isRightType(String[] arr){//VARCHAR //INTEGER
		String[] colType = null;
		boolean numExist = true; 
		try {
			colType = db.returnColumnType();
		} catch (SQLException e) {
			
		}
		for(int i =0; i <arr.length; i++){
			if(colType[i].equals("INT") || colType[i].equals("NUMBER") ){
				numExist = arr[i].matches("\\d+");
			}
		}
		return numExist; 
	}
	 
	//Insert and Delete Function 
	@FXML private void handleInsertDeleteAction(ActionEvent e) throws SQLException{
		String[] type = new String[2];
		try {
			type = db.returnColumnType();
		} catch (SQLException e3) {
			AlertBox error = new AlertBox();
			error.display("Error" , "Uknown Error");
		}

		Button whichAction = (Button)e.getSource();
		insertDelete = new GridPane();
		tabInsertDelete = new Tab();
		tabInsertDelete.setText(whichAction.getText());
		tabInsertDelete.setContent(insertDelete);
		tabPane.getTabs().add(tabInsertDelete);
		tabPane.getSelectionModel().select(tabInsertDelete);

		try {
			String[] colName = db.returnColumnNames();

			//generating insert items
			insertDelete.setAlignment(Pos.CENTER);
			insertDelete.setHgap(7);
			insertDelete.setVgap(7);
			insertDelete.setPadding(new Insets(10, 10, 10, 10));

			Text scenetitle = new Text("Fill in the following information:");
			scenetitle.setId("choice-text");
			insertDelete.add(scenetitle, 0, 0, 1, 1);

			TextField[] tfInsert = new TextField[colName.length];
			Label[] labelInsert = new Label[colName.length];
			for(int i = 0; i< colName.length; i++){
				labelInsert[i] = new Label(colName[i]);
				insertDelete.add(labelInsert[i],0,i+1);
				tfInsert[i] = new TextField();
				insertDelete.add(tfInsert[i], 1, i+1);
			}

			Button submit = new Button("Submit");
			insertDelete.add(submit,1, colName.length+1);
			submit.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event) {
					//gather TextField into String arr
					String[] arr= new String[colName.length];
					for(int i =0 ; i<colName.length; i++){
						arr[i] =tfInsert[i].getText();
					}

					boolean insertOrDelete = false; 
					boolean full = checkInput(arr);
					boolean right = isRightType(arr);
					
					if(full && right){
						try {
							if(whichAction.getText().equals("Insert")){
								insertOrDelete= db.insertData(arr);
							}else{
								insertOrDelete = db.deleteData(arr);
								System.out.println(insertOrDelete);
							}
						} catch (SQLException e) {
							AlertBox noInsertDelete = new AlertBox();
							noInsertDelete.display(whichAction.getText() + " Fail", "Unable to " +whichAction.getText() +". Please try again.");

						}
					}else{
						if(!full){
							AlertBox noInsertDelete = new AlertBox();
							noInsertDelete.display("Not enough data", "Unable to " +whichAction.getText() +". All inputs are required.");
						}else{
							AlertBox notRight= new AlertBox();
							notRight.display("Incorrect Data", "Unable to " +whichAction.getText() +".One of your inputs is not a valid type");
						}
					}
					if(insertOrDelete){
						AlertBox done = new AlertBox();
						done.display(whichAction.getText() + " Successful.",
								whichAction.getText()+" completed.");
					}else;

				}

			});

		} catch (SQLException e1) {
			AlertBox getColumnNames = new AlertBox();
			getColumnNames.display("Error","PikachuTable not created. Create Table first.");
		} catch(Exception e2){
			AlertBox noMore = new AlertBox();
			noMore.display("PikachuTable no longer exists.","PikachuTable was deleted. Create Table again");
		}

	}

	@FXML private void handleDisplayAllAction() throws SQLException{
		try{
			displayGrid = new GridPane();
			displayScroll = new ScrollPane();
			tabDisplay = new Tab();

			displayScroll.setContent(displayGrid);
			tabDisplay.setText("Display Of Table's Entire Content");
			tabDisplay.setContent(displayScroll);
			tabPane.getTabs().add(tabDisplay);
			tabPane.getSelectionModel().select(tabDisplay);

			displayGrid.setGridLinesVisible(true);
			displayGrid.setAlignment(Pos.CENTER);
			displayGrid.setHgap(5);
			displayGrid.setVgap(5);
			displayGrid.setPadding(new Insets(4, 4, 4, 4));
			displayGrid.setStyle("-fx-background-color: #FFFFFF;");

			//adding column rows to grid
			String[] colNames = db.returnColumnNames();
			Label name; 
			for(int top=0; top< colNames.length; top++){
				name = new Label(" "+ colNames[top]+ " ");
				name.setStyle("-fx-background-color:#FF7F50;");
				name.setTextFill(Color.web("#FFFFFF"));
				name.setFont(new Font("Arial", 15));
				displayGrid.add(name,top,0);
			}

			//adding all elements from database table onto grid
			int numOfItemsInTable = db.returnNumberOfColumns()*db.returnNumberOfRows();
			Label[] table = new Label[numOfItemsInTable];
			int item =0; 

			String[][] myData = db.displayCurrentTable();
			for(int row=0; row <myData.length; row++ ){
				for(int col =0; col<myData[row].length; col++){
					table[item]= new Label(" "+ myData[row][col] + " ");
					displayGrid.add(table[item], col, row+1);
					item++; 
				}
			}
		}catch(Exception e){
			AlertBox error = new AlertBox();
			error.display("Not created", "It seems like your table has not been created. Create table first.");
		}
	}

	@FXML private void handleSearchAction(){
		try{
			search = new GridPane();
			tabSearch = new Tab();
			tabSearch.setText("Search");
			tabPane.getTabs().add(tabSearch);
			tabSearch.setContent(search);
			tabPane.getSelectionModel().select(tabSearch);

			search.setAlignment(Pos.CENTER);
			search.setHgap(7);
			search.setVgap(7);
			search.setPadding(new Insets(10, 10, 10, 10));

			try {
				String[] colName = db.returnColumnNames();
				//Gathers information on what to search for
				Label firstInstruct = new Label("Select the columns that you want to search for");

				search.add(firstInstruct,0,0,colName.length,1);

				CheckBox[] cbGetInfo = new CheckBox[colName.length];

				for(int i =0; i< colName.length; i++){
					cbGetInfo[i] = new CheckBox(colName[i]);
					search.add(cbGetInfo[i], i, 1);
				}

				Label instruct = new Label("Select and Fill the columns you want to search by ");
				search.add(instruct,0,2,colName.length,1);

				CheckBox[] cbSearchBy = new CheckBox[colName.length];
				TextField[] tfSearch = new TextField[colName.length];
				Label[] labelSearch = new Label[colName.length];

				int row =3;
				for(int i = 0; i< colName.length; i++, row++){
					cbSearchBy[i] = new CheckBox();
					search.add(cbSearchBy[i],0,row);			

					labelSearch[i] = new Label(colName[i]);
					search.add(labelSearch[i],1,row);

					tfSearch[i] = new TextField();
					search.add(tfSearch[i], 2, row);

				}

				Button submitSearch = new Button("Submit");
				search.add(submitSearch,1, row );
				submitSearch.setOnAction(new EventHandler<ActionEvent>(){
					//filling values

					String[] colNamesReturn;

					@Override
					public void handle(ActionEvent event) {
						int count=0; 
						//get count for how many columns for getInfo
						for(int i =0; i < cbGetInfo.length; i++){

							if(cbGetInfo[i].isSelected()){
								count++; 
						
							}
						}
						colNamesReturn = new String[count];
						if(count >0){
							//determine which columns are GetInfo and stored into array
							for(int i =0, j=0; i <cbGetInfo.length;i++){
								if(cbGetInfo[i].isSelected()){
									colNamesReturn[j] =colName[i];
									 
									j++; 
								}
							}
						}else{
							AlertBox noColSearch = new AlertBox();
							noColSearch.display("No Column Selected","You didn't enter any columns to search for.");
						}

						//determine if anything selected
						boolean checkBoxSelect = false;
						boolean textFieldSelect = false;

						int counter =0; 
						for(int num =0; num<cbSearchBy.length; num++){
							if(cbSearchBy[num].isSelected()){

								if(!tfSearch[num].getText().isEmpty()){
									textFieldSelect = true;
									checkBoxSelect =true;
									counter++; 
								}						
							}						
						}	

						if((checkBoxSelect) && (textFieldSelect)){	
							String[] colNamesLookBy = new String[counter]; 
							String[] colValuesLookBy= new String[counter];	

							for(int i =0, j=0; i< colName.length; i++){
								if(cbSearchBy[i].isSelected()){
									colNamesLookBy[j]=colName[i];
									colValuesLookBy[j]=tfSearch[i].getText();
									j++;
								}else;
							}
							try {
								String[][] finds=db.selectData(colNamesLookBy, colValuesLookBy, colNamesReturn);
								//need to display					

								if(finds[0][0]==null){
									AlertBox nothing = new AlertBox();
									nothing.display("Found Nothing", "Search came up with nothing.");
								}else{
									
									//adding column rows to grid
									GridPane displayGrid = new GridPane();
									String[] colNames = db.returnColumnNames();
									Label name; 
									for(int top=0; top< colNames.length; top++){
										name = new Label(" "+ colNames[top]+ " ");
										name.setStyle("-fx-background-color:#FF7F50;");
										name.setTextFill(Color.web("#FFFFFF"));
										name.setFont(new Font("Arial", 15));
										displayGrid.add(name,top,0);
									}

									//adding all elements from database table onto grid 
									//setting values to one string
									String oneStringValues= "";
									String oneStringCol = "";
									for(int i =0; i<colValuesLookBy.length; i++){
										oneStringValues+= " " + colValuesLookBy[i];
									}
									
									for(int i=0; i<colNamesLookBy.length;i++){
										oneStringCol+="" + colNamesLookBy[i];
									}
									int numOfItemsInTable = finds.length*finds[0].length;
									Label[] table = new Label[numOfItemsInTable];
									int item =0; 

									for(int row=0; row <finds.length; row++ ){
										for(int col =0; col<finds[row].length; col++){
											table[item]= new Label(" "+ finds[row][col] + " ");
											displayGrid.add(table[item], col, row+1);
											item++; 
										}
										if(row==finds.length-1){
											Label result = new Label("These are the results after you searched for\n"
													+ "Values " +" set as " + oneStringValues +
													"\nBased on Columns " + oneStringCol);
											displayGrid.add(result, 0, row+2 );
										}
									}
									
									
	
									tabSearch.setText("Results Of Search");
									tabSearch.setContent(displayGrid);
									tabPane.getTabs().add(tabSearch);
									tabPane.getSelectionModel().select(tabSearch);

								}
							} catch (SQLException e) {
								AlertBox selectError = new AlertBox();
								selectError.display("Select Error", "Selection of Data created an error. Please try again");
							} catch(Exception e){
								AlertBox anotherError = new AlertBox();
								anotherError.display("Error has occured", "Unknown Error.");
							}

						}else{
							AlertBox nothing = new AlertBox();
							nothing.display("Need Input", "Search cannot be executed until fields and corresponding checkboxes are entered.");

						}				
					}

				});
			} catch (SQLException e) {
				AlertBox tableError = new AlertBox();
				tableError.display("Error","PikachuTable not created. Create Table first.");
			}	
		}catch(Exception e){
			AlertBox noTable = new AlertBox();
			noTable.display("Error","PikachuTable not created. Create Table first.");
		}
	}

	private String[] turnTableToArray(String[][] fullData){
		String[] oneRow = new String[fullData.length];
		for(int i =0; i <oneRow.length; i++){
			oneRow[i] = "";
		}
		for(int r =0; r< fullData.length;r++){
			for(int col =0; col<fullData[0].length; col++){
				if(col==fullData[0].length-1){
					oneRow[r]= oneRow[r] + fullData[r][col];
				}else{
					oneRow[r]= oneRow[r] + fullData[r][col];
					oneRow[r]+=",";
				}
			}
		}
		return oneRow;
	}
	//not sure 
	@FXML private void handleUpdateAction() {
		try{
			update = new GridPane();
			update.setAlignment(Pos.CENTER);
			update.setHgap(7);
			update.setVgap(7);
			update.setPadding(new Insets(10, 10, 10, 10));

			tabUpdate = new Tab();
			tabUpdate.setText("Update Database");
			tabUpdate.setContent(update);
			tabPane.getTabs().add(tabUpdate);
			tabPane.getSelectionModel().select(tabUpdate);

			try{
				String[][] fullData= null;
				try {
					fullData = db.displayCurrentTable();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String[] colName = db.returnColumnNames();

				//Gathers information on what to search for
				Label firstInstruct = new Label("Columns you want to search by:");

				update.add(firstInstruct,0,0,colName.length,1);

				CheckBox[] cbSearch= new CheckBox[colName.length];
				for(int i =0; i< colName.length; i++){
					cbSearch[i] = new CheckBox(colName[i]);
					update.add(cbSearch[i], i, 1);
				}

				Label secInstruct = new Label("Columns you want to update:");
				update.add(secInstruct,0,2,colName.length,1);

				CheckBox[] cbUpdate = new CheckBox[colName.length];
				for(int i =0; i< colName.length; i++){
					cbUpdate[i] = new CheckBox(colName[i]);
					update.add(cbUpdate[i], i, 3);
				}

				Button check = new Button("Submit");
				update.add(check, 0, 4);
				check.setOnAction(new EventHandler<ActionEvent>(){

					@Override
					public void handle(ActionEvent event) {
						//GridPane inner = new GridPane();
						//update.add(inner, 0, 5);
						//generating columns to search by
						Label thirdInstruct = new Label("Fill in the information you want to search by"); 		
						update.add(thirdInstruct,0,5,3,1);

						//get number of col search by
						int countSearch =0; 
						for(int i =0; i <cbSearch.length; i++){
							if(cbSearch[i].isSelected()){
								countSearch++;
							}else; 
						}
						final int counterForButton = countSearch;

						//storing columns Names Look
						String[] colNamesLook = new String[countSearch];
						for(int i =0,j=0; i < cbSearch.length; i++){
							if(cbSearch[i].isSelected()){
								colNamesLook[j]=cbSearch[i].getText();
								j++; 
							}else; 
						}
						//initialize TextFields to search by
						TextField[] tfSearchBy = new TextField[countSearch];
						int row =6; 
						for(int i =0,j=0; i<cbSearch.length;i++){
							if(cbSearch[i].isSelected()){
								Label col = new Label(colName[i]);
								update.add(col, 0, row);
								tfSearchBy[j] = new TextField();
								tfSearchBy[j].setPromptText("Required Input");
								update.add(tfSearchBy[j], 1,row);
								row++; 
								j++; 
							}
						}

						//convert to one string
						String one = "";
						for(int i =0; i < colNamesLook.length; i++){
							one = one + colNamesLook[i] + " ";
						}

						Label fourthInstruct = new Label("You will be updating any information in the database"
								+"\n that matches your input for column(s) " + one);
						fourthInstruct.setWrapText(true);
						update.add(fourthInstruct,0,row,3,1);
						row=row+2; 

						Label fifthInstruct = new Label("Fill in the new information ");
						update.add(fifthInstruct, 0, row,3,1);
						row= row+3; 

						//get number of col to update
						int countUpdate = 0; 
						for(int i =0; i<cbUpdate.length; i++){
							if(cbUpdate[i].isSelected()){
							
								countUpdate++;
							}else; 
						}

						//initialize TextFields to Update by
						TextField[] tfUpdateBy = new TextField[countUpdate];

						for(int i =0, j=0; i<colName.length;i++){
						
							if(cbUpdate[i].isSelected()){
							
								Label col = new Label(colName[i]);
								update.add(col, 0, row);
								tfUpdateBy[j] = new TextField();
								tfUpdateBy[j].setPromptText("Required Input");
								update.add(tfUpdateBy[j], 1,row);
								j++;
								row++; 
							}
						}

						Button searchAndUpdate = new Button("Update");
						update.add(searchAndUpdate, 0, row);
						searchAndUpdate.setOnAction(new EventHandler<ActionEvent>(){

							@Override
							public void handle(ActionEvent event) {

								//call select data first to get 2d array of all possible values
								//columns to Look At
								//variable -colNamesLook

								//values of those Columns - stored in tfSearchBy
								String[] valueOfCol = new String[tfSearchBy.length];

								for(int i=0; i< valueOfCol.length; i++){
									valueOfCol[i] = tfSearchBy[i].getText();
								}

								//names of Columns to print- everything
								String[] colNames;
								String[][] dataToCompare= new String[1][1]; 
								String[][] realData = new String[1][1];
								try {
									colNames = db.returnColumnNames();
									
									dataToCompare = db.selectData(colNamesLook, valueOfCol, colNames);
									
									int rowStop=0;  
									for(int i =0; i<dataToCompare.length;i++){
										for(int k=0; k<dataToCompare[0].length; k++){
											if(dataToCompare[i][k]==null){
												rowStop=i; 
												break;
											}
										}
										if(rowStop>0){
											break; 
										}
									}
									
									//reassign values
									realData = new String[rowStop][dataToCompare[0].length];
									for(int startRow = 0; startRow<realData.length; startRow++ ){
										for(int startCol=0; startCol<realData[startRow].length; startCol++){
											realData[startRow][startCol] = dataToCompare[startRow][startCol];
										}
									}
																	
								} catch (SQLException e) {
									AlertBox error = new AlertBox();
									error.display("Fail", "Failure to retrieve data. Values to be searched by may not exist");
								}

								//Determine Which Column(s) will be updated and store in array
								int countNum=0; 
								for(int i =0; i<colName.length;i++){
									if(cbUpdate[i].isSelected()){
										countNum++; 
									}
								}
								String[] colNamesUpdate = new String[countNum];
								for(int store =0,index=0; store<colName.length;store++){
									if(cbUpdate[store].isSelected()){
										colNamesUpdate[index]= colName[store];
									}
								}

								//Storing Column Names Not Selected into Array
								int totalCol=0;
								int notChanged=0; 
								try {
									totalCol = db.returnColumnType().length;
									notChanged=totalCol-counterForButton;
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								String[] colNotChangedName = new String[notChanged]; 
								int realCol =0; 
								for(int col = 0; col<realData[0].length; col++){
									if(!cbUpdate[col].isSelected()){
										colNotChangedName[realCol] = colName[col];
										realCol++; 
									}
								}
								
								//Storing ALL Values from Column's Not Selected into Array
								String[] valuesNotChanged = new String[realData.length*notChanged];
								int i=0; 
								for(int row =0; row<realData.length; row++){
									for(int col = 0; col<realData[0].length;col++ ){
										if(!cbUpdate[col].isSelected()){
											valuesNotChanged[i] = realData[row][col];
											i++;
										}
									}
								}

								//Storing Values from TextField UpdateBy into array
								String[] updatedData = new String[tfUpdateBy.length];
								for(int j =0; j < tfUpdateBy.length; j++){
									updatedData[j]=tfUpdateBy[j].getText();
									
								}

								//loop to update All matched rows
								int value = valuesNotChanged.length;
								int minus = notChanged;
							
								int shiftIndex = valuesNotChanged.length; 

								while(shiftIndex>=0){
									try {
										boolean work = db.updateTableData(colNamesUpdate, updatedData, colNotChangedName,valuesNotChanged);
										System.out.println(work);
									} catch (SQLException e) {
										AlertBox one = new AlertBox();
										one.display("Error", "Display Table Error.");
									}
									//shift by number of columns
									for(int t =0; t< shiftIndex ;t++ ){

										valuesNotChanged[t] = valuesNotChanged[t+notChanged];
									}
									shiftIndex = shiftIndex-notChanged; 

								}

								String[][] display = null; 
								try {
									display = db.displayCurrentTable();
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

						}); 

					}
				});

		}catch(SQLException e){	
			AlertBox tableError = new AlertBox();
			tableError.display("Error","PikachuTable not created. Create Table first.");	
		}
		}catch(Exception e){
			AlertBox tableError = new AlertBox();
			tableError.display("Error","PikachuTable not created. Create Table first.");	
		}
	}
	
    @FXML private void handleDeleteAllAction(){
    	boolean deleteTable = false; 
    	try{
    		deleteTable = db.deleteTable();
    		
    	}catch(SQLException e){
    		AlertBox notDeleted = new AlertBox();
    		notDeleted.display("Error","PikachuTable not created. Create Table first.");
    	}
    	if(deleteTable){
    		AlertBox deleted = new AlertBox();
    		deleted.display("Deleted Table", "Pikachu Table has been successfully deleted.");
    	};
    }
    
	//LOGOUT OF DATABASE
	@FXML private void handleLogoutAction(){
		try {
			welcome.closeConnection();
			System.exit(1);
		} catch (SQLException e) {
			AlertBox error = new AlertBox();
			error.display("Unable to Exit ", "Unable to close connection. Try another method?");
		}
	}
}