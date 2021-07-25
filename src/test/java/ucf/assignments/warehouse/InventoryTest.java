package ucf.assignments.warehouse;

/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 Stephan Hartig
 */

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
   
   private static Inventory inventoryFromStrings(String ... fields) {
      Inventory inventory = new Inventory();
      for (String field : fields)
         inventory.add(new Item(field, field, field));
      return inventory;
   }
   
   private static Iterable<Item> itemsFromStrings(String ... fields) {
      List<Item> items = new ArrayList<>();
      for (String field : fields)
         items.add(new Item(field, field, field));
      return items;
   }
   
   @SuppressWarnings("unused")
   private static Stream<Arguments> params_snapshot() {
      return Stream.of(
         Arguments.of(
            itemsFromStrings("one", "two", "three"),
            inventoryFromStrings("one", "two", "three")
         )
      );
   }
   
   @ParameterizedTest
   @MethodSource("params_snapshot")
   void test_snapshot(Iterable<Item> expected, Inventory inventory) {
   
      /* Assert that both the snapshot and items are equal by items. */
      List<Item> expectedList = new ArrayList<>();
      List<Item> inventoryList = new ArrayList<>();
      
      expected.iterator().forEachRemaining(expectedList::add);
      inventory.snapshot().iterator().forEachRemaining(inventoryList::add);
      
      expectedList.sort(Comparator.comparing(Item::getSerial));
      inventoryList.sort(Comparator.comparing(Item::getSerial));
      
      assertEquals(expectedList.size(), inventoryList.size());
      
      for (int i = 0; i < expectedList.size(); i++) {
         assertEquals(expectedList.get(i).getValue(), inventoryList.get(i).getValue());
         assertEquals(expectedList.get(i).getSerial(), inventoryList.get(i).getSerial());
         assertEquals(expectedList.get(i).getName(), inventoryList.get(i).getName());
      }
      
      /*
       * We don't need to assert that `inventory.snapshot()` is immutable,
       * because there is no way to edit `Item` objects and `Iterator<>`s
       * are equally immutable.
       */
      assertTrue(true);
   }
   
   @SuppressWarnings("unused")
   private static Stream<Arguments> params_add() {
      return Stream.of(
         /* NOTE: Inventory ONLY cares if the serial is a dupe and DOES NOT care if `Item.isValid()`. */
         Arguments.of(1, itemsFromStrings("one")),
         Arguments.of(3, itemsFromStrings("one", "two", "three")),
         Arguments.of(3, itemsFromStrings("1!", "2@", "3#")),
         Arguments.of(2, itemsFromStrings("one", "one", "three")),
         Arguments.of(3, itemsFromStrings("one", "ONE", "three")),
         Arguments.of(2, itemsFromStrings("one", "two", "one")),
         Arguments.of(3, itemsFromStrings("one", "two", "one", "four")),
         Arguments.of(2, itemsFromStrings("one", "two", "one", "one"))
      );
   }
   
   
   @ParameterizedTest
   @MethodSource("params_add")
   void test_add(int expectedSize, Iterable<Item> items) {
      Inventory inventory = new Inventory();
      
      assertEquals(0, inventory.size());
      
      for (Item item : items)
         inventory.add(item);
      
      assertEquals(expectedSize, inventory.size());
   }
   
   @SuppressWarnings("unused")
   private static Stream<Arguments> params_remove() {
      return Stream.of(
         /* NOTE: Inventory ONLY cares about the serial when it comes to removing. */
         Arguments.of(
            2,
            inventoryFromStrings("one", "two", "three"),
            new Item("one", "one", "one")
         ),
         Arguments.of(
            2,
            inventoryFromStrings("one", "two", "three"),
            new Item("", "one", "")
         ),
         Arguments.of(
            3,
            inventoryFromStrings("one", "two", "three"),
            new Item("one", "", "one")
         )
      );
   }
   
   @ParameterizedTest
   @MethodSource("params_remove")
   void test_remove(int expectedSize, Inventory inventory, Item toRemove) {
      inventory.remove(toRemove);
      
      assertEquals(expectedSize, inventory.size());
      
      assertFalse(inventory.has(toRemove.getSerial()));
   }
   
   @SuppressWarnings("unused")
   private static Stream<Arguments> params_has() {
      return Stream.of(
         Arguments.of(
            true,
            inventoryFromStrings("one", "two", "three"),
            "one"
         ),
         Arguments.of(
            false,
            inventoryFromStrings("one", "two", "1234567890"),
            "123456789x"
         ),
         Arguments.of(
            false,
            inventoryFromStrings("one", "two", "three"),
            "four"
         )
      );
   }
   
   @ParameterizedTest
   @MethodSource("params_has")
   void test_has(boolean has, Inventory inventory, String serial) {
      assertEquals(has, inventory.has(serial));
   }
}
