/**
 * 
 */
package com.rnb2.adress;

import com.rnb2.model.Person;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @author budukh-rn
 *
 */
public class PersonOverviewController {
	
	private static final String COL_2 = "lastName";

	private static final String COL_1 = "firstName";

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
	
	@FXML
	private Label streetLabel;

	
	private MainApp mainApp;
	
	
	/**
	 * 
	 */
	public PersonOverviewController() {
			
	}
	
	@FXML
	private void initialize(){
		
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>(COL_1));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>(COL_2));
		
		
		showPersonDetail(null);
		
		personTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Person>() {
			@Override
			public void changed(ObservableValue<? extends Person> observable,
					Person oldValue, Person newValue) {
				showPersonDetail(newValue);
			}
		});
	}
	
	@FXML
	private void showAddPerson(){
		Person p  = new Person();
		boolean isOkClick = mainApp.showPersonEdit(p);
		
		if(isOkClick){
			mainApp.getPersonList().add(p);
		}
	}
	
	@FXML
	private void showPersonEdit(){
		Person person = personTable.getSelectionModel().getSelectedItem();
		
		if (person == null) {
			Dialogs.showWarningDialog(mainApp.getPrimaryStage(), "Выберете запись", "Не выбрана запись");
			return;
		}
		
		boolean isOkClick = mainApp.showPersonEdit(person);
		
		if(isOkClick){
			refreshTable();
			showPersonDetail(person);
		}
		
	}
	
	public void deletePerson(){
		int index = personTable.getSelectionModel().getSelectedIndex();
		if(index >=0){
			personTable.getItems().remove(index);
		}else
			Dialogs.showWarningDialog(mainApp.getPrimaryStage(), "Выберете запись", "Не выбрана запись");
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		personTable.setItems(mainApp.getPersonList());
		System.out.println("sss " + mainApp.getPersonList().size());
	}
	

	
	private void refreshTable() {
		int index = personTable.getSelectionModel().getSelectedIndex();
		
		personTable.setItems(null);
		personTable.layout();
		personTable.setItems(mainApp.getPersonList());
		
		personTable.getSelectionModel().select(index);
	}

	private void showPersonDetail(Person person){
		if(person == null ){
			String nullString = "";
			firstNameLabel.setText(nullString);
			lastNameLabel.setText(nullString);
			streetLabel.setText(nullString);
		}else{	
			firstNameLabel.setText(person.getFirstName());
			lastNameLabel.setText(person.getLastName());
			streetLabel.setText(person.getStreet());
		}
	}
	

}
