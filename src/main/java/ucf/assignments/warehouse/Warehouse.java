package ucf.assignments.warehouse;

/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 Stephan Hartig
 */

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Warehouse {
   @Getter private File lastFile;
   private Inventory inventory;
   
   public Warehouse() {
      this.lastFile = null;
      this.inventory = new Inventory();
   }
   
   /*-------------------------------------------------------------------------*
    * Public methods.
    *-------------------------------------------------------------------------*/
   
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Warehouse warehouse = (Warehouse) o;
      return inventory.equals(warehouse.inventory);
   }
   
   @Override
   public int hashCode() {
      return Objects.hash(inventory);
   }
   
   public boolean add(Item item) {
      return this.inventory.add(item);
   }
   
   public boolean has(Item item) {
      return this.inventory.has(item.getSerial());
   }
   
   public boolean remove(Item item) {
      return this.inventory.remove(item);
   }
   
   public boolean update(Item from, Item to) {
      if (!this.remove(from))
         return false;
      
      return this.add(to);
   }
   
   public void clear() {
      this.inventory = new Inventory();
   }
   
   public int size() {
      return this.inventory.size();
   }
   
   public Iterable<Item> snapshot() {
      return this.inventory.snapshot();
   }
   
   public boolean saveAs(File file) {
      if (this.saveFromFile(file))
         this.lastFile = file;
      else
         return false;
      
      return true;
   }
   
   public boolean save() {
      return this.saveFromFile(this.lastFile);
   }
   
   public boolean refresh() {
      return this.loadFromFile(this.lastFile);
   }
   
   public boolean load(File file) {
      return this.loadFromFile(file);
   }
   
   /*-------------------------------------------------------------------------*
    * Private methods.
    *-------------------------------------------------------------------------*/
   private boolean loadFromFile(File file) {
      try {
         LoadingDock loadingDock = new LoadingDock();
      
         Inventory loading = new Inventory();
         for (Item item : loadingDock.load(file))
            loading.add(item);
      
         this.inventory = loading;
         this.lastFile = file;
      
         return true;
      } catch (IOException | NoSuchFieldException e) {
         return false;
      }
   }
   
   private boolean saveFromFile(File file) {
      try {
         LoadingDock loadingDock = new LoadingDock();
      
         loadingDock.save(file, inventory.snapshot());
      
         return true;
      }
      catch (IOException e) {
         return false;
      }
   }
}
