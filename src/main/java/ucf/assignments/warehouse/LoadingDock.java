package ucf.assignments.warehouse;

/*
 * UCF COP3330 Summer 2021 Assignment 5 Solution
 * Copyright 2021 Stephan Hartig
 */

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ucf.assignments.fs.FileIO;
import ucf.assignments.tsv.Tsv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadingDock {
   
   public enum DataType {
      TSV,
      JSON,
      HTML
   }
   
   /*-------------------------------------------------------------------------*
    * Public methods.
    *-------------------------------------------------------------------------*/
   
   public DataType getFileType(File file) {
      if (file.getName().length() == 0)
         return null;
   
      if (file.getName().charAt(file.getName().length() - 1) == '.')
         return null;
      
      String[] split = file.getName().split("\\.");
      
      return switch (split[split.length - 1]) {
         case "txt", "tsv" -> DataType.TSV;
         case "json" -> DataType.JSON;
         case "html" -> DataType.HTML;
         default -> null;
      };
   }
   
   public Iterable<Item> itemsFromString(String data, DataType dataType) throws NoSuchFieldException {
      return switch (dataType) {
         case TSV -> fromTsv(data);
         case JSON -> fromJson(data);
         case HTML -> fromHtml(data);
      };
   }
   
   public String itemsToString(Iterable<Item> items, DataType dataType) {
      return switch (dataType) {
         case TSV -> toTsv(items);
         case JSON -> toJson(items);
         case HTML -> toHtml(items);
      };
   }
   
   public Iterable<Item> load(File file) throws NoSuchFieldException, IOException {
      String data = FileIO.slurp(file.getPath());
      DataType dataType = this.getFileType(file);
      
      if (dataType == null)
         throw new IOException("File type could not be resolved.");
      
      return this.itemsFromString(
         data,
         dataType
      );
   }
   
   public void save(File file, Iterable<Item> items) throws IOException {
      DataType dataType = this.getFileType(file);
      
      if (dataType == null)
         throw new IOException("File type could not be resolved.");
      
      FileIO.spit(
         file.getPath(),
         this.itemsToString(items, dataType)
      );
   }

   /*-------------------------------------------------------------------------*
    * Private methods.
    *-------------------------------------------------------------------------*/
   
   private Iterable<Item> fromTsv(String data) throws NoSuchFieldException {
      Tsv tsv = new Tsv();
      return Arrays.asList(tsv.fromTsv(data, Item.class));
   }
   
   private Iterable<Item> fromJson(String data) {
      Gson gson = new Gson();
      return Arrays.asList(gson.fromJson(data, Item[].class));
   }
   
   private Iterable<Item> fromHtml(String data) throws NoSuchFieldException {
      /* Some hard-coded laziness (don't change the definition of class `Item`), but otherwise should work fine. */
      
      
      List<Item> items = new ArrayList<>();
      
      Document doc = Jsoup.parse(data);
      Element inventory = doc.selectFirst("#inventory");
      Element tableHead = inventory.selectFirst("thead");
      Element tableBody = inventory.selectFirst("tbody");
      
      /* Figure out which column in the HTML table is which field in clsas `Item`. */
      int valueIdx = -1;
      int serialIdx = -1;
      int nameIdx = -1;
      
      int i = 0;
      for (Element field : tableHead.selectFirst("tr").select("th")) {
         switch (field.html()) {
            case "value" -> valueIdx = i;
            case "serial" -> serialIdx = i;
            case "name" -> nameIdx = i;
            default -> throw new NoSuchFieldException("So such field in Items.");
         }
         i++;
      }
      
      if (valueIdx < 0 || serialIdx < 0 || nameIdx < 0)
         throw new NoSuchFieldException("Missing a field.");
      
      /* Parse one `Item` instance per row of the HTML table. */
      for (Element tr : tableBody.select("tr")) {
         Elements cells = tr.select("td");
         
         String value, serial, name;
         value =  cells.get(valueIdx).html();
         serial = cells.get(serialIdx).html();
         name =   cells.get(nameIdx).html();
         
         items.add(new Item(value, serial, name));
      }
      
      return items;
   }
   
   private String toTsv(Iterable<Item> items) {
      Tsv tsv = new Tsv();
      
      List<Item> list = new ArrayList<>();
      items.iterator().forEachRemaining(list::add);
      
      return tsv.toTsv(list.toArray(new Item[0]));
   }
   
   private String toJson(Iterable<Item> items) {
      Gson gson = new Gson();
      
      List<Item> list = new ArrayList<>();
      items.iterator().forEachRemaining(list::add);
      
      return gson.toJson(list);
   }
   
   private String toHtml(Iterable<Item> items) {
      String docTemplate = "" +
         "<!doctype html>" +
         "<html>" +
            "<head>" +
               "<meta charset='utf-8'>" +
            "</head>" +
            "<body>" +
               "<table id='inventory'>" +
                  "<thead></thead>" +
                  "<tbody></tbody>" +
               "</table>" +
            "</body>" +
         "</html>";
      
      String tableHeaderFmt = "" +
         "<tr>" +
            "<th>%s</th>" +
            "<th>%s</th>" +
            "<th>%s</th>" +
         "</tr>";
      
      String tableRowFmt = "" +
         "<tr>" +
            "<td>%s</td>" +
            "<td>%s</td>" +
            "<td>%s</td>" +
         "</tr>";
   
      Document doc = Jsoup.parse(docTemplate);
      Element table = doc.selectFirst("#inventory");
      Element tableHead = table.selectFirst("thead");
      Element tableBody = table.selectFirst("tbody");
      
      tableHead.html(String.format(tableHeaderFmt, "value", "serial", "name"));
      
      StringBuilder tbody = new StringBuilder();
      for (Item item : items) {
         tbody.append(String.format(
            tableRowFmt,
            item.getValue(),
            item.getSerial(),
            item.getName()
         ));
      }
      tableBody.html(tbody.toString());
      
      return doc.toString();
   }
}
