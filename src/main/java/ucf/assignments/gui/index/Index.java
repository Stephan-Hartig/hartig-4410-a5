package ucf.assignments.gui.index;

/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 Stephan Hartig
 */

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import ucf.assignments.gui.Component;
import ucf.assignments.gui.addItemWidget.AddItemWidget;
import ucf.assignments.warehouse.Item;
import ucf.assignments.warehouse.Warehouse;

import java.io.File;

public class Index extends Component {
   private final String fxml_AddItemWidget = "/ucf/assignments/gui/addItemWidget/AddItemWidget.fxml";
   
   @FXML @Getter BorderPane root;
   @FXML TextField searchbar;
   @FXML TableView table;
   @FXML HBox footer;
   
   private Warehouse warehouse;
   private String query;
   private Stage stage;
   
   public void init(Stage stage) {
      this.stage = stage;
      ((AddItemWidget) this.addChild(this.footer, fxml_AddItemWidget)).init(this);
      
      this.query = "";
      
      /* Probably should've used ObservableList but whatevs. */
      this.warehouse = new Warehouse();
   
      TableColumn valueColumn = new TableColumn("Value");
      TableColumn serialColumn = new TableColumn("Serial Number");
      TableColumn nameColumn = new TableColumn("Name");
      
      valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
      serialColumn.setCellValueFactory(new PropertyValueFactory<>("serial"));
      nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
      
      this.table.getColumns().addAll(
         valueColumn,
         serialColumn,
         nameColumn
      );
      
      this.table.setEditable(true);
   }
   
   private void refreshDisplay() {
      this.table.getItems().clear();
      for (Item item : this.warehouse.snapshot()) {
         if (this.displayPredicate(item))
            this.table.getItems().add(item);
      }
   }
   
   public void add(Item item) {
      if (this.warehouse.add(item))
         this.refreshDisplay();
   }
   
   public boolean has(Item item) {
      return this.warehouse.has(item);
   }
   
   public void remove(Item item) {
      if (this.warehouse.remove(item))
         this.refreshDisplay();
   }
   
   public void update(Item from, Item to) {
      if (this.warehouse.update(from, to))
         this.refreshDisplay();
   }
   
   private boolean displayPredicate(Item item) {
      return
         item
            .getName()
            .startsWith(query) ||
         item
            .getSerial()
            .startsWith(query);
   }
   
   
   /*--------------------------------------------------------------------------
    * Menubar.
    *------------------------------------------------------------------------*/
   
   public void menu_new() {
      this.warehouse = new Warehouse();
      this.refreshDisplay();
   }
   
   public void menu_open() {
      File file = this.chooseFileLoad();
      
      if (file != null)
         this.warehouse.load(file);
   }
   
   public void menu_save() {
      if (!this.warehouse.save())
         this.menu_saveAs();
   }
   
   public void menu_saveAs() {
      this.warehouse.saveAs(this.chooseFileSave());
   }
   
   public void menu_revertToSaved() {
      this.warehouse.refresh();
   }
   
   public void menu_quit() {
      System.exit(0);
   }
   
   public void menu_deleteSelected() {
      Item item = (Item) this.table.getSelectionModel().getSelectedItem();
      this.remove(item);
   }
   
   public void menu_deleteAll() {
      this.warehouse.clear();
      this.refreshDisplay();
   }
   
   public void menu_search() {
      this.searchbar.requestFocus();
      this.searchbar.selectPositionCaret(this.searchbar.getText().length());
   }
   
   public void menu_help() {
      System.out.println("for help read the readme on github :kekw:");
   }
   
   private File chooseFileLoad() {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open an inventory.");
      FileChooser.ExtensionFilter tsvFilter = new FileChooser.ExtensionFilter("TSV files (*.txt)", "*.txt");
      FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
      FileChooser.ExtensionFilter htmlFilter = new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");
      fileChooser.getExtensionFilters().add(tsvFilter);
      fileChooser.getExtensionFilters().add(jsonFilter);
      fileChooser.getExtensionFilters().add(htmlFilter);
   
      return fileChooser.showOpenDialog(this.stage);
   }
   
   private File chooseFileSave() {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save inventory to file.");
      FileChooser.ExtensionFilter tsvFilter = new FileChooser.ExtensionFilter("TSV files (*.txt)", "*.txt");
      FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
      FileChooser.ExtensionFilter htmlFilter = new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");
      fileChooser.getExtensionFilters().add(tsvFilter);
      fileChooser.getExtensionFilters().add(jsonFilter);
      fileChooser.getExtensionFilters().add(htmlFilter);
   
      return fileChooser.showSaveDialog(this.stage);
   }
   
   
   
   /*------------------------------------------------------------------------*
    * Search bar.
    *------------------------------------------------------------------------*/
   
   public void search() {
      this.query = this.searchbar.getText();
      this.refreshDisplay();
   }
}
