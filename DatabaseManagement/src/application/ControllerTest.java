package application;

import static org.junit.Assert.*;

import java.awt.Event;

import org.junit.Test;
/**
 * Plan: 
 * Run multiple tests on methods within the controller class
 * and see what are the limitations. 
 * 
 * */


public class ControllerTest {
	Controller conTest = new Controller();
	//controller instance
	javafx.event.Event testEvent = new javafx.event.Event(null);

	/**
	 *Plan: 
	 *test the handleChoiceBoxAction method. 
	 *Find if this method is automatically 
	 *setting the textfield in the Welcome frame or not.
	 */
	@Test
	public void test() {
		
		conTest.handleChoiceBoxAction(testEvent);
		
	}

}
