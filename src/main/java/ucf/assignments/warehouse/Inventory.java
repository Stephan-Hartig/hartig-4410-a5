package ucf.assignments.warehouse;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
   private Map<String, Item> items;
   
   public Inventory() {
      this.items = new HashMap<>();
   }
   
   public boolean add(Item item) {
      if (this.has(item.serial))
         return false;
      
      this.items.put(item.serial, item);
      return true;
   }
   
   public boolean remove(Item item) {
      return this.items.remove(item.getSerial()) == null;
   }
   
   public int size() {
      return this.items.size();
   }
   
   public boolean has(String serial) {
      return this.items.containsKey(serial);
   }
   
   public Iterable<Item> snapshot() {
      return () -> items.values().stream().map(Item::copy).iterator();
   }
}
