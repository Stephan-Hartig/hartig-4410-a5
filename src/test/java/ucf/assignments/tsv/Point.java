package ucf.assignments.tsv;

import lombok.Getter;

import java.util.Objects;

public class Point {
   @Getter private String x;
   @Getter private String y;
   
   public Point() { }
   public Point(String x, String y) {
      this.x = x;
      this.y = y;
   }
   
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Point point = (Point) o;
      return x.equals(point.x) && y.equals(point.y);
   }
   
   @Override
   public int hashCode() {
      return Objects.hash(x, y);
   }
}
