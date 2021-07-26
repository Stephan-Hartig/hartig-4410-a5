package ucf.assignments.gui.addItemWidget;

/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 Stephan Hartig
 */

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import lombok.Getter;
import ucf.assignments.gui.Component;
import ucf.assignments.gui.index.Index;
import ucf.assignments.warehouse.Item;

public class AddItemWidget extends Component {
   
   @FXML @Getter HBox root;
   @FXML TextField value;
   @FXML TextField serial;
   @FXML TextField name;
   
   private Index parent;

   public void init(Index parent) {
      this.parent = parent;
   }
   
   public void add() {
      String valueStr = this.value.getText();
      String serialStr = this.serial.getText();
      String nameStr = this.name.getText();
      
      Item item = new Item(valueStr, serialStr, nameStr);
      
      StringBuilder errMsg = new StringBuilder();
      boolean allValid = true;
      if (!item.isValidValue()) {
         errMsg.append("Value must be a number with zero or two decimals: 100 or 100.00\n");
         allValid = false;
      }
      if (!item.isValidSerial()) {
         errMsg.append("Serial number must be 10 alphanumeric digits.\n");
         allValid = false;
      }
      if (!item.isValidName()) {
         errMsg.append("Name must be 2-256 alphanumeric digits or spaces.\n");
         allValid = false;
      }
      
      if (!allValid) {
         Alert alert = new Alert(Alert.AlertType.ERROR, errMsg.toString(), ButtonType.OK);
         alert.showAndWait();
         return;
      }
      
      this.parent.add(item);
      
      /* Clear the widget. */
      this.value.setText("");
      this.serial.setText("");
      this.name.setText("");
   }
   
   public void add_keyPressed(KeyEvent event) {
      if (event.getCode().equals(KeyCode.ENTER))
         this.add();
   }
}
