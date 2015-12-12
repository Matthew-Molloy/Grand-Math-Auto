package grandmathauto;

import java.io.Serializable;


// Score class that stores a name and score
public class Score implements Serializable {

   private static final long serialVersionUID = 1L;
   private int score;
   private String name;
   
   // Constructor
   public Score(String name, int score) {
      this.score = score;
      this.name = name;
   }

   // Getters
   public int getScore() {
      return score;
   }

   public String getName() {
      return name;
   }

}