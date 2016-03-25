package application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;

/**
 * AlertBox class to manage configuration of notifications
 * @author alisoncheu
 *
 */
public class AlertBox {
	protected static String resolution;

	public AlertBox(){}
	
	
    public void display(String title, String message) {
        final Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        Label pika = makePikachu();
        
        label.setText(message);
        Button closeButton = new Button("Close this window");
        closeButton.setOnAction(new EventHandler<ActionEvent>(){
        	 public void handle(ActionEvent e) {
        		 window.close();
        	 }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(pika,label, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
    
    public static String followUp(String title, String message) {
        final Stage window = new Stage();
        
        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Yes");
        closeButton.setOnAction(new EventHandler<ActionEvent>(){
        	 public void handle(ActionEvent e) {
        		 resolution = "Yes";
        		 window.close();
        	 }
        });
        
        Button notCloseButton = new Button ("No");
        notCloseButton.setOnAction(new EventHandler<ActionEvent>(){
       	 public void handle(ActionEvent e) {
       		 resolution = "No";
       		 window.close();
       	 }
       });
        
        VBox layout = new VBox(10);
        Label pikachu = new Label("Bye");
        pikachu = makePikachu();
        layout.getChildren().addAll(pikachu, label, closeButton, notCloseButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
		return resolution;
    }
    
    // Team mascot printout
    public static Label makePikachu(){
    	Label pikachu = new Label(
    					"ix.\t\t\t\t     _.;\n"
    					+" xxx.\t\t\t\t ,,xxx\n"
    					+" ~xxxx,\t\t      .;xxxxx'\n"
    					+"  ~xxxxx,\t\t    ,;xxxxxx'\n"
    					+"   ~x    ,---''''''''---,;  xxx'\n"
    					+"     :\t\t\t                '\n"
    					+"      i'\t\t               ,^'\n"
    					+"     |    -----     -----    |\n"
    	    		 	+"     |  \t                     |\n"
    					+"     |----       -         ---|\n"
    					+"     |     |      (_^_)     |     |\n"
    	    			+"      ----       _        --- :'\n"
    	    		    +"      '-..______________..- '\n"
    	    			
        );
    	return pikachu; 
    }
}
