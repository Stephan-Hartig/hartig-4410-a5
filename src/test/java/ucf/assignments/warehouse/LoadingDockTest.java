package ucf.assignments.warehouse;

/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 Stephan Hartig
 */

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LoadingDockTest {
   
   @Test
   void test_() throws NoSuchFieldException {
      Inventory inventory = inventoryFromStrings("one", "two", "three");
      
      LoadingDock loadingDock = new LoadingDock();
      String serialized = loadingDock.itemsToString(inventory.snapshot(), LoadingDock.DataType.HTML);
      
      Inventory processed = new Inventory();
      
      for (Item item : loadingDock.itemsFromString(serialized, LoadingDock.DataType.HTML))
         processed.add(item);
      
      assertEquals(inventory, processed);
   }
   
   private static Inventory inventoryFromStrings(String ... fields) {
      Inventory inventory = new Inventory();
      for (String field : fields)
         inventory.add(new Item("VALUE", field, "NAME"));
      return inventory;
   }
   
   @SuppressWarnings("unused")
   private static Stream<Arguments> params_itemsFromString() {
      return Stream.of(
         /* TSV. */
         Arguments.of(
            inventoryFromStrings(),
            "" +
               "value\tserial\tname\n",
            LoadingDock.DataType.TSV
         ),
         Arguments.of(
            inventoryFromStrings("one", "two", "three"),
            "" +
               "value\tserial\tname\n" +
               "VALUE\tone\tNAME\n" +
               "VALUE\ttwo\tNAME\n" +
               "VALUE\tthree\tNAME\n",
            LoadingDock.DataType.TSV
         ),
         Arguments.of(
            inventoryFromStrings("one", "two", "three"),
            "" +
               "serial\tname\tvalue\n" +
               "one\tNAME\tVALUE\n" +
               "two\tNAME\tVALUE\n" +
               "three\tNAME\tVALUE",
            LoadingDock.DataType.TSV
         ),
         Arguments.of(
            inventoryFromStrings("one", "two"),
            "" +
               "value\tserial\tname\n" +
               "VALUE\tone\tNAME\n" +
               "VALUE\ttwo\tNAME\n" +
               "VALUE\tone\tNAME\n",
            LoadingDock.DataType.TSV
         ),
         /* JSON. */
         Arguments.of(
            inventoryFromStrings(),
            "[]",
            LoadingDock.DataType.JSON
         ),
         Arguments.of(
            inventoryFromStrings("one", "two", "three"),
            "[" +
               "{ \"value\": \"VALUE\", \"name\": \"NAME\", \"serial\": \"one\" }," +
               "{ \"value\": \"VALUE\", \"name\": \"NAME\", \"serial\": \"two\" }," +
               "{ \"value\": \"VALUE\", \"name\": \"NAME\", \"serial\": \"three\" }" +
            "]",
            LoadingDock.DataType.JSON
         ),
         Arguments.of(
            inventoryFromStrings("one", "two"),
            "[" +
               "{ \"value\": \"VALUE\", \"name\": \"NAME\", \"serial\": \"one\" }," +
               "{ \"value\": \"VALUE\", \"name\": \"NAME\", \"serial\": \"two\" }," +
               "{ \"value\": \"VALUE\", \"name\": \"NAME\", \"serial\": \"one\" }" +
            "]",
            LoadingDock.DataType.JSON
         ),
         /* HTML. */
         Arguments.of(
            inventoryFromStrings(),
            "<html>" +
               "<body>" +
                  "<table id='inventory'>" +
                     "<thead>" +
                        "<tr><th>value</th><th>serial</th><th>name</th></tr>" +
                     "</thead>" +
                     "<tbody></tbody>" +
                  "</table>" +
               "</body>" +
            "</html>",
            LoadingDock.DataType.HTML
         ),
         Arguments.of(
            inventoryFromStrings("one", "two"),
            "<html>" +
               "<body>" +
                  "<table id='inventory'>" +
                     "<thead>" +
                        "<tr><th>value</th><th>serial</th><th>name</th></tr>" +
                     "</thead>" +
                     "<tbody>" +
                        "<tr><td>VALUE</td><td>one</td><td>NAME</td></tr>" +
                        "<tr><td>VALUE</td><td>two</td><td>NAME</td></tr>" +
                     "</tbody>" +
                  "</table>" +
               "</body>" +
            "</html>",
            LoadingDock.DataType.HTML
         ),
         Arguments.of(
            inventoryFromStrings("one", "two"),
            "<html>" +
               "<body>" +
                  "<table id='inventory'>" +
                     "<thead>" +
                        "<tr><th>name</th><th>value</th><th>serial</th></tr>" +
                     "</thead>" +
                     "<tbody>" +
                        "<tr><td>NAME</td><td>VALUE</td><td>one</td></tr>" +
                        "<tr><td>NAME</td><td>VALUE</td><td>two</td></tr>" +
                        "<tr><td>NAME</td><td>VALUE</td><td>one</td></tr>" +
                     "</tbody>" +
                  "</table>" +
               "</body>" +
            "</html>",
            LoadingDock.DataType.HTML
         )
      );
   }
   
   @ParameterizedTest
   @MethodSource("params_itemsFromString")
   void test_itemsFromString(Inventory expected, String input, LoadingDock.DataType dataType) throws NoSuchFieldException {
      LoadingDock loadingDock = new LoadingDock();
      
      Inventory inventory = new Inventory();
      
      for (Item item : loadingDock.itemsFromString(input, dataType))
         inventory.add(item);
   
      assertEquals(expected, inventory);
   }
   
   
   @SuppressWarnings("unused")
   private static Stream<Arguments> params_itemsToString() {
      return Stream.of(
         /* TSV. */
         Arguments.of(
            inventoryFromStrings(),
            LoadingDock.DataType.TSV
         ),
         Arguments.of(
            inventoryFromStrings("one", "two"),
            LoadingDock.DataType.TSV
         ),
         Arguments.of(
            inventoryFromStrings("one", "two", "three"),
            LoadingDock.DataType.TSV
         ),
         /* JSON. */
         Arguments.of(
            inventoryFromStrings(),
            LoadingDock.DataType.JSON
         ),
         Arguments.of(
            inventoryFromStrings("one", "two"),
            LoadingDock.DataType.JSON
         ),
         Arguments.of(
            inventoryFromStrings("one", "two", "three"),
            LoadingDock.DataType.JSON
         ),
         /* HTML. */
         Arguments.of(
            inventoryFromStrings(),
            LoadingDock.DataType.JSON
         ),
         Arguments.of(
            inventoryFromStrings("one", "two"),
            LoadingDock.DataType.HTML
         ),
         Arguments.of(
            inventoryFromStrings("one", "two", "three"),
            LoadingDock.DataType.HTML
         )
      );
   }
   
   @ParameterizedTest
   @MethodSource("params_itemsToString")
   void test_itemsToString(Inventory inventory, LoadingDock.DataType dataType) throws NoSuchFieldException {
      LoadingDock loadingDock = new LoadingDock();
      
      /* Serialize `inventory` to a string. */
      String serialized = loadingDock.itemsToString(inventory.snapshot(), dataType);
      
      /*
       * We only care if the result is parseable into an inventory equal
       * to the one we started with. The actual output text for the
       * JSON/TSV/HTML might vary between compiles.
       */
      Inventory processed = new Inventory();
   
      for (Item item : loadingDock.itemsFromString(serialized, dataType))
         processed.add(item);
      
      assertEquals(inventory, processed);
   }
   
   
   @SuppressWarnings("unused")
   private static Stream<Arguments> params_getFileType() {
      return Stream.of(
         /* TSV. */
         Arguments.of("foo.txt", LoadingDock.DataType.TSV),
         Arguments.of("foo.bar.txt", LoadingDock.DataType.TSV),
         Arguments.of("foo.tsv", LoadingDock.DataType.TSV),
         /* JSON. */
         Arguments.of("foo.json", LoadingDock.DataType.JSON),
         Arguments.of("foo.html.json", LoadingDock.DataType.JSON),
         Arguments.of("./foo.json", LoadingDock.DataType.JSON),
         /* HTML. */
         Arguments.of("foo.html", LoadingDock.DataType.HTML),
         Arguments.of("~/foobar/foo.html", LoadingDock.DataType.HTML),
         Arguments.of("/absolute/foo.html", LoadingDock.DataType.HTML),
         /* null. */
         Arguments.of("", null),
         Arguments.of("foo", null),
         Arguments.of("foo.txt.bar", null),
         Arguments.of("foo.TXT", null),
         Arguments.of("foo.txt.", null)
      );
   }
   
   @ParameterizedTest
   @MethodSource("params_getFileType")
   void test_getFileType(String filepath, LoadingDock.DataType expected) {
      LoadingDock loadingDock = new LoadingDock();
      
      assertEquals(expected, loadingDock.getFileType(new File(filepath)));
   }
}
