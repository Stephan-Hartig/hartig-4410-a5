package ucf.assignments;

/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 Stephan Hartig
 */


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
   public static void main(String[] args) {
      System.out.println("Running project.");
      launch(args);
   }
   
   @Override
   public void start(Stage primaryStage) throws Exception {
      String index = "/ucf/assignments/gui/index/Index.fxml";
      
      
      FXMLLoader fxmlLoader = new FXMLLoader();
      Parent root = fxmlLoader.load(getClass().getResource(index).openStream());
      primaryStage.setTitle("Inventory");
      primaryStage.setScene(new Scene(root, 640, 480));
      primaryStage.show();
      
      //((TodoApp) fxmlLoader.getController()).init(primaryStage);
   }
}
