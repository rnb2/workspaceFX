package com.rnb2.adress;

import com.rnb2.model.Person;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PersonEditDialogController {
	
	@FXML
	private TextField firstName;
	
	@FXML
	private TextField lastName;
	
	@FXML
	private TextField street;
	
	private Stage dialogeStage;
	private Person person;
	private boolean isOkClicked  = Boolean.FALSE;

	public PersonEditDialogController() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	private void initialize(){
		
	}
	
	@FXML
	private void okAction(){
		isOkClicked = true;
		
		person.setFirstName(firstName.getText());
		person.setLastName(lastName.getText());
		person.setStreet(street.getText());
		
		dialogeStage.close();
	}
	
	@FXML
	private void cancelAction(){
		dialogeStage.close();
	}

	public boolean isOkClicked() {
		return isOkClicked;
	}

	public void setDialogStage(Stage primaryStage) {
		this.dialogeStage = primaryStage;
	}

	public void setPerson(Person p) {
		this.person = p;
		
		firstName.setText(p.getFirstName());
		lastName.setText(p.getLastName());
		street.setText(p.getStreet());
	}
	
	
}
