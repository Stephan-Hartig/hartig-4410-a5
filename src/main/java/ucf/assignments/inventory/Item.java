package ucf.assignments.inventory;

public class Item {
   public String value;
   public String serial;
   public String name;
   
   public Item(String value, String serial, String name) {
      this.value = value;
      this.serial = serial;
      this.name = name;
   }
   
   public Item copy() {
      return new Item(this.value, this.serial, this.name);
   }
}
