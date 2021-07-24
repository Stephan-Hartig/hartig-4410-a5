package ucf.assignments.warehouse;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {
   
   
   private static Stream<Arguments> params_clone() {
      return Stream.of(
         Arguments.of(new Item("", "", "")),
         Arguments.of(new Item("value", "", "")),
         Arguments.of(new Item("", "serial", "")),
         Arguments.of(new Item("", "", "name")),
         Arguments.of(new Item("", "", "")),
         Arguments.of(new Item("value", "serial", "name"))
      );
   }
   
   @ParameterizedTest
   @MethodSource("params_clone")
   void test_clone(Item item) {
      
      Item cloned = item.copy();
      
      /* Assert field equality. */
      assertEquals(item.getValue(), cloned.getValue());
      assertEquals(item.getSerial(), cloned.getSerial());
      assertEquals(item.getName(), cloned.getName());
      
      /* Assert reference inequality. */
      assertNotSame(item, cloned);
   }
   
   private static Stream<Arguments> params_validity() {
      return Stream.of(
         Arguments.of(
            true,
            true, true, true,
            new Item("100.00", "1234567890", "name")
         ),
         Arguments.of(
            false,
            false, false, false,
            new Item("", "", "")
         ),
         Arguments.of(
            false,
            true, true, false,
            new Item("100", "abc1234xyz", "1")
         ),
         Arguments.of(
            false,
            false, false, true,
            new Item("$100.00", "123456789", "1a")
         ),
         Arguments.of(
            false,
            true, false, false,
            new Item("00.00", "12345 6789", "MA$H")
         ),
         Arguments.of(
            false,
            false, false, true,
            new Item("10.0", "#123456789", "na e")
         )
      );
   }
   
   @ParameterizedTest
   @MethodSource("params_validity")
   void test_validity(
         boolean isValidItem,
         boolean isValidValue,
         boolean isValidSerial,
         boolean isValidName,
         Item item) {
      assertEquals(isValidItem,     item.isValidItem());
      assertEquals(isValidValue,    item.isValidValue());
      assertEquals(isValidSerial,   item.isValidSerial());
      assertEquals(isValidName,     item.isValidName());
   }
}
