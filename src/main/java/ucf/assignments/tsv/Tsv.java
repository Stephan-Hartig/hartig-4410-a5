package ucf.assignments.tsv;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tsv {
   /** Works like Gson's json serializer. All fields must be of type String, however. */
   public <T> String toTsv(T[] objs) throws IllegalAccessException {
      StringBuilder sb = new StringBuilder();
      
      Class<?> c = objs[0].getClass();
      
      Field[] fields = c.getDeclaredFields();
      /* Remove `.this`. */
      fields = (Field[]) Arrays
         .stream(fields)
         .filter(field -> !field.getName().equals("this$0"))
         .toList()
         .toArray(new Field[0]);
      
      for (Field field : fields)
         field.setAccessible(true);
      
      
      /* The header of the TSV represents the names of the class fields. */
      sb.append(fields[0].getName());
      for (int i = 1; i < fields.length; i++) {
         sb.append("\t");
         sb.append(fields[i].getName());
      }
      
      sb.append("\n");
      
      /* One instance per row, and one field per column. */
      for (T obj : objs) {
         sb.append(fields[0].get(obj).toString());
         for (int i = 1; i < fields.length; i++) {
            sb.append("\t");
            sb.append(fields[i].get(obj).toString());
         }
         
         sb.append("\n");
      }
      
      return sb.toString();
   }
   
   /** Works like Gson's json deserializer. All fields must be of type String, however. */
   public <T> T[] fromTsv(String tsv, Class<T> classOfT) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException {
      List<T> objs = new ArrayList<>();
      
      try {
         /* One `T obj` per row of the TSV. */
         List<String> table = new ArrayList<>(Arrays.asList(tsv.split("\n")));
         
         /* The header of the TSV represents the names of the class fields. */
         String[] fieldNames = table.remove(0).split("\t");
         Field[] fields = new Field[fieldNames.length];
         
         /* Reflect fields from the `fieldNames` strings. */
         for (int i = 0; i < fieldNames.length; i++) {
            fields[i] = classOfT.getDeclaredField(fieldNames[i]);
            fields[i].setAccessible(true);
         }
   
         /* For each row of data of the TSV create a new instance and inject the TSV columns into the corresponding field. */
         for (String row : table) {
            if (row.isBlank())
               continue;
            
            String[] cells = row.split("\t");
            T reflected = classOfT.getDeclaredConstructor().newInstance();
      
            for (int i = 0; i < fieldNames.length; i++)
               fields[i].set(reflected, cells[i]);
      
            objs.add(reflected);
         }
         
      }
      catch (IllegalAccessException e) {
         /* This exception should never be thrown, in practice. */
         System.err.println("This should be impossible!");
         e.printStackTrace();
      }
      
      T[] result = (T[]) Array.newInstance(classOfT, objs.size());
      for (int i = 0; i < objs.size(); i++)
         result[i] = objs.get(i);
      
      return result;
   }
}
