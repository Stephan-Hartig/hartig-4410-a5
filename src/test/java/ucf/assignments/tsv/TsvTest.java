package ucf.assignments.tsv;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class TsvTest {
   
   @Test
   void test_toTsv() throws IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException {
      Tsv tsv = new Tsv();
   
      Point[] original = new Point[] {
         new Point("0", "0"),
         new Point("11", "11"),
         new Point("222", "222")
      };
      
      Point[] processed = tsv.fromTsv(tsv.toTsv(original), Point.class);
   
      for (int i = 0; i < original.length; i++)
         assertEquals(processed[i], original[i]);
   }
   
   
   @Test
   void test_fromTsv() throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException {
      Tsv tsv = new Tsv();
   
      Point[] original = new Point[] {
         new Point("0", "0"),
         new Point("11", "11"),
         new Point("222", "222")
      };
      
      String pointsTsv = "x\ty\n" +
         "0\t0\n" +
         "11\t11\n" +
         "222\t222\n";
      
      Point[] parsedPoints = tsv.fromTsv(pointsTsv, Point.class);
      
      for (int i = 0; i < original.length; i++)
         assertEquals(parsedPoints[i], original[i]);
      
   }
   
}
