package ucf.assignments.warehouse;

/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 Stephan Hartig
 */

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {
   @SuppressWarnings("unused")
   private static Stream<Arguments> params_file_io() {
      return Stream.of(
         Arguments.of()
      );
   }
   
   @ParameterizedTest
   @MethodSource("params_file_io")
   void test_file_io() {
      File tmp = new File("test-resources/tmp.txt");
      File nonexistent = new File("test-resources/nonexistent.txt");
      
      Warehouse warehouse = new Warehouse();
      warehouse.add(new Item("item-1-value", "item-1-serial", "item-1-name"));
      warehouse.add(new Item("item-2-value", "item-2-serial", "item-2-name"));
      int expectedSize = warehouse.size();
      
      /* `lastFile` is null to start off. */
      assertNull(warehouse.getLastFile());
      
      /* `saveAs()` updates `lastFile`. */
      warehouse.saveAs(tmp);
      
      assertEquals(tmp, warehouse.getLastFile());
      assertEquals(expectedSize, warehouse.size());
      
      /* `load()` does not update `lastFile` nor `inventory` on failure. */
      warehouse.load(nonexistent);
      
      assertEquals(tmp, warehouse.getLastFile());
      assertEquals(expectedSize, warehouse.size());
      
      /* `clear()` does not update `lastFile`. */
      warehouse.clear();
   
      assertEquals(tmp, warehouse.getLastFile());
      
      assertEquals(0, warehouse.size());
      warehouse.refresh();
      assertEquals(expectedSize, warehouse.size());
      
      
      /* Load from the file created by `warehouse` into a new Warehouse. */
      Warehouse other = new Warehouse();
      other.load(tmp);
      
      assertEquals(tmp, other.getLastFile());
      assertEquals(expectedSize, other.size());
   }
}
