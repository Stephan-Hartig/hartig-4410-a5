package ucf.assignments.gui;

/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 Stephan Hartig
 */

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

abstract public class Component {
   
   /* Theoretically we can use relative paths for `fxmlFilename`. */
   public Component addChild(Pane parent, String fxmlFilename) {
      try {
         FXMLLoader fxmlLoader = new FXMLLoader();
         Parent child = fxmlLoader.load(this.getClass().getResource(fxmlFilename).openStream());
         parent.getChildren().add(child);
         return fxmlLoader.getController();
      } catch (IOException | NullPointerException e) {
         /* In practice, the FXML file and its controller should always exist, so no need to rethrow. */
         e.printStackTrace();
         System.exit(1);
         return null;
      }
   }
   
   abstract public Node getRoot();
}
