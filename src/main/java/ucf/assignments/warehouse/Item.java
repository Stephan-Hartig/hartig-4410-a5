package ucf.assignments.warehouse;

import lombok.Getter;

import java.util.Objects;
import java.util.regex.Pattern;

public class Item {
   @Getter public String value;
   @Getter public String serial;
   @Getter public String name;
   
   @SuppressWarnings("unused")
   public Item() { }
   
   public Item(String value, String serial, String name) {
      this.value = value;
      this.serial = serial;
      this.name = name;
   }
   
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Item item = (Item) o;
      return value.equals(item.value) && serial.equals(item.serial) && name.equals(item.name);
   }
   
   @Override
   public int hashCode() {
      return Objects.hash(value, serial, name);
   }
   
   public Item copy() {
      return new Item(this.value, this.serial, this.name);
   }
   
   public boolean isValidValue() {
      return
         /* Number: 1234 */
         Pattern
            .compile("^\\d+$")
            .matcher(this.value)
            .matches() ||
         /* Number with 2 decimals: 1234.00 */
         Pattern
            .compile("^\\d+\\.\\d{2}$")
            .matcher(this.value)
            .matches();
   }
   
   public boolean isValidSerial() {
      return
         /* Ten alphanumeric digits. */
         Pattern
            .compile("^[a-zA-Z0-9]{10}$")
            .matcher(this.serial)
            .matches();
   }
   
   public boolean isValidName() {
      return
         Pattern
            .compile("^[a-zA-Z0-9 ]{2,256}$")
            .matcher(this.name)
            .matches();
   }
   
   public boolean isValidItem() {
      return
         this.isValidValue() &&
         this.isValidSerial() &&
         this.isValidName();
   }
}
