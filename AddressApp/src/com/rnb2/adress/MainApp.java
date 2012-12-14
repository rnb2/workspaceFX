package com.rnb2.adress;

import java.io.IOException;

import com.rnb2.model.Person;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private static final String string_resource = "/com/rnb2/view/";
	private static final String title = "Тест JavaFX ";
	private Stage primaryStage;
	private BorderPane rootPane;
	
	private ObservableList<Person> personList = FXCollections.observableArrayList();
	

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(title);
		
		try {
	          // Load the root layout from the fxml file
	          FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(string_resource + "RootLayout.fxml"));
	          rootPane = (BorderPane) loader.load();
	          Scene scene = new Scene(rootPane);
	          primaryStage.setScene(scene);
	          primaryStage.show();
	      } catch (IOException e) {
	          // Exception gets thrown if the fxml file could not be loaded
	          e.printStackTrace();
	      }
		
		showPersonDetail();
		
		initPersonList();
		
	}
	
	
	private void initPersonList() {
		// TODO Auto-generated method stub
		personList.add(new Person("Вася","Пупкин"));
		personList.add(new Person("Петя","Ленын"));
		personList.add(new Person("Ёся","Сидоров"));
		
	}


	public void showPersonDetail(){
		try {
	          // Load the root layout from the fxml file
	          FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(string_resource + "PersonOverView.fxml"));
	          AnchorPane anchorPane = (AnchorPane) loader.load();
	          rootPane.setCenter(anchorPane);
	        
	      } catch (IOException e) {
	          // Exception gets thrown if the fxml file could not be loaded
	          e.printStackTrace();
	      }
	}


	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public ObservableList<Person> getPersonList() {
		return personList;
	}


	public static void main(String[] args) {
		launch(args);
	}
}
