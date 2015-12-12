package grandmathauto;

import java.util.*;
import java.awt.Graphics;
import java.io.*;

public class HighscoreManager {
   private ArrayList<Score> scores;

   // High score file
   private static final String HIGHSCORE_FILE = "scores.dat";

   // Input & output stream
   ObjectOutputStream outputStream = null;
   ObjectInputStream inputStream = null;

   // Constructor
   public HighscoreManager() {
      scores = new ArrayList<Score>();
   }

   // Return scores
   public ArrayList<Score> getScores() {
      loadScoreFile();
      sort();
      return scores;
   }

   // Sort scores from high to low
   private void sort() {
      ScoreComparator comparator = new ScoreComparator();
      Collections.sort(scores, comparator);
}

   // Add score
   public void addScore(String name, int score) {
      loadScoreFile();
      scores.add(new Score(name, score));
      updateScoreFile();
   }

   // Open score file
   public void loadScoreFile() {
      try {
         inputStream = new ObjectInputStream(
               new FileInputStream(HIGHSCORE_FILE));
         scores = (ArrayList<Score>) inputStream.readObject();
      } catch (FileNotFoundException e) {
      } catch (IOException e) {     
      } catch (ClassNotFoundException e) {
         
      } finally {
         try {
            if (outputStream != null) {
               outputStream.flush();
               outputStream.close();
            }
         } catch (IOException e) {
         }
      }
   }

   // Update score files with changes
   public void updateScoreFile() {
      try {
         outputStream = new ObjectOutputStream(
               new FileOutputStream(HIGHSCORE_FILE));
         outputStream.writeObject(scores);
      } catch (FileNotFoundException e) {
      } catch (IOException e) {
      } finally {
         try {
            if (outputStream != null) {
               outputStream.flush();
               outputStream.close();
            }
         } catch (IOException e) {
         }
      }
   }
   
   // Prints out each high score in the file to applet
   public void getHighscoreString(Graphics g, int xpos, int y) {
      String highscoreString = "";
      int max = 5;

      ArrayList<Score> scores;
      scores = getScores();

      int i = 0;
      int x = scores.size();
      if (x > max) {
          x = max;
      }
      while (i < x) {
          highscoreString += (i + 1) + "-" + scores.get(i).getName() + "-" + scores.get(i).getScore() + "\n";
          g.drawString(highscoreString, xpos, y+(60*i));
          i++;
          highscoreString = "";
      }
  
}
}
