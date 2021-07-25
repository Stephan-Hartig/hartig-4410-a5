package ucf.assignments.warehouse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Inventory {
   private Map<String, Item> items;
   
   public Inventory() {
      this.items = new HashMap<>();
   }
   
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Inventory inventory = (Inventory) o;
      return items.equals(inventory.items);
   }
   
   @Override
   public int hashCode() {
      return Objects.hash(items);
   }
   
   public boolean add(Item item) {
      if (this.has(item.serial))
         return false;
      
      this.items.put(item.serial, item);
      return true;
   }
   
   public boolean remove(Item item) {
      return this.items.remove(item.getSerial()) != null;
   }
   
   public boolean update(Item from, Item to) {
      if (!this.remove(from))
         return false;
      
      return this.add(to);
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
