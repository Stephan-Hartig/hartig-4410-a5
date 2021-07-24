package ucf.assignments.warehouse;

import lombok.Getter;

import java.util.regex.Pattern;

public class Item {
   @Getter public String value;
   @Getter public String serial;
   @Getter public String name;
   
   public Item(String value, String serial, String name) {
      this.value = value;
      this.serial = serial;
      this.name = name;
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
