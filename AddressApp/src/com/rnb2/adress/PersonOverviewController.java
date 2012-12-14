/**
 * 
 */
package com.rnb2.adress;

import com.rnb2.model.Person;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @author budukh-rn
 *
 */
public class PersonOverviewController {
	
	@FXML
	private TableView<Person> personTable;
	
	@FXML
	private TableColumn<Person, String> firstNameColumn;
	
	@FXML
	private TableColumn<Person, String> lastNameColumn;
	
	@FXML
	private Label firstNameLabel;
	
	@FXML
	private Label lastNameLabel;

	
	private MainApp mainApp;
	
	
	/**
	 * 
	 */
	public PersonOverviewController() {
			
	}
	
	@FXML
	private void init(){
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		personTable.setItems(mainApp.getPersonList());
	}
	
	

}
