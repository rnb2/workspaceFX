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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private static final String string_resource = "/com/rnb2/view/";
	private static final String title = "Тест JavaFX ";
	private Stage primaryStage;
	private BorderPane rootPane;
	
	private ObservableList<Person> personList = FXCollections.observableArrayList();
	

	public MainApp() {
		initPersonList();
	}
	
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
	
		
	}
	
	
	private void initPersonList() {
		personList.add(new Person("Вася","Пупкин", "Киевская"));
		personList.add(new Person("Петя","Ленын","Московская"));
		personList.add(new Person("Ёся","Сидоров", "Любая"));
		
	}


	public void showPersonDetail(){
		try {
	          // Load the root layout from the fxml file
	          FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(string_resource + "PersonOverView.fxml"));
	          AnchorPane anchorPane = (AnchorPane) loader.load();
	          rootPane.setCenter(anchorPane);
	          
	          
	          PersonOverviewController overviewController = loader.getController();
	          overviewController.setMainApp(this);
	          
	      } catch (IOException e) {
	          // Exception gets thrown if the fxml file could not be loaded
	          e.printStackTrace();
	      }
	}
	
	public boolean showPersonEdit(Person p){
		 
         try {
        	FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(string_resource + "PersonEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			PersonEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPerson(p);
			
			dialogStage.showAndWait();
			return controller.isOkClicked();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 
	         return false;	
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
