package grandmathauto;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GraphicsManager {
   static public Image image, currentSprite, playerImage, backgroundImage,
         debugDot, leftArrow, rightArrow, checkMark, checkBox, coneImage,
         connect;

   private Main main;

   private String[] menuOptions = { "Grand Math Auto", "Play", "Options",
         "High Scores", "Credits", "Quit" };
   private String[] optOptions = { "Sound Effects", "Background Music",
         "Back" };
   private String[] levelOptions = { "Select Skill Level", "Addition",
         "Subtraction", "Multiplication", "Start Game" };
   private String[] names = { "Matthew Molloy", "Jennifer Tang", "Ricky Yu" };
   private String[] gameOver = {"Game Over", "Score:", "Enter your name for score: "};
   private int size;
   boolean displayed = false;

   // Constructor
   public GraphicsManager(Main main) throws ClassNotFoundException {

      this.main = main;

      /* Import sprites into application */
      playerImage = main.getImage(main.getBase(), "data/player_car.png");
      currentSprite = playerImage;
      backgroundImage = main.getImage(main.getBase(),
            "data/road_background.png");
      debugDot = main.getImage(main.getBase(), "data/dot.png");
      leftArrow = main.getImage(main.getBase(), "data/left.png");
      rightArrow = main.getImage(main.getBase(), "data/right.png");
      checkMark = main.getImage(main.getBase(), "data/check.png");
      checkBox = main.getImage(main.getBase(), "data/box.png");
      coneImage = main.getImage(main.getBase(), "data/cone.png");
      connect = main.getImage(main.getBase(), "data/connecting.png");

   }

   public void paint(Graphics g) {
      int x = 0, y = 0, index = 0;
      Dimension d = main.getSize();
      FontMetrics fm;
      Game game = main.getGame();

      // Fonts, Title & Options
      Font font1 = new Font("Helvetica", Font.BOLD, 75);
      Font font2 = new Font("Helvetica", Font.BOLD, 50);
      Font font3 = new Font("Helvetica", Font.BOLD, 25);

      switch (game.getState()) {
      /* In main menu */
      case MAIN:
         // Display Title
         g.setFont(font1);
         g.setColor(Color.white);

         fm = g.getFontMetrics();
         x = (d.width / 2) - (fm.stringWidth(menuOptions[0]) / 2);
         y = 100;

         g.drawString(menuOptions[0], x, y);

         // Display options
         g.setFont(font2);
         fm = g.getFontMetrics();

         for (index = 0; index <= 4; index++) {
            y = 200 + (60 * index);
            x = (d.width / 2) - (fm.stringWidth(menuOptions[index + 1]) / 2);

            g.drawString(menuOptions[index + 1], x, y);
         }

         x = (d.width / 2);

         // Move selection arrow
         switch (game.getMainIndex()) {
         case 0:
            g.drawImage(rightArrow, x - fm.stringWidth(menuOptions[1]), 167,
                  main);
            g.drawImage(leftArrow,
                  x + (fm.stringWidth(menuOptions[1]) / 2) + 10, 167, main);
            break;

         case 1:
            g.drawImage(rightArrow,
                  x - (fm.stringWidth(menuOptions[2]) / 2) - 50, 227, main);
            g.drawImage(leftArrow,
                  x + (fm.stringWidth(menuOptions[2]) / 2) + 10, 227, main);
            break;

         case 2:
            g.drawImage(rightArrow,
                  x - (fm.stringWidth(menuOptions[3]) / 2) - 50, 287, main);
            g.drawImage(leftArrow,
                  x + (fm.stringWidth(menuOptions[3]) / 2) + 10, 287, main);
            break;

         case 3:
            g.drawImage(rightArrow,
                  x - (fm.stringWidth(menuOptions[4]) / 2) - 50, 347, main);
            g.drawImage(leftArrow,
                  x + (fm.stringWidth(menuOptions[4]) / 2) + 10, 347, main);
            break;

         case 4:
            g.drawImage(rightArrow, x - fm.stringWidth(menuOptions[5]), 407,
                  main);
            g.drawImage(leftArrow,
                  x + (fm.stringWidth(menuOptions[5]) / 2) + 10, 407, main);
            break;
         }
         break;

      /* In game state */
      case GAME:
         if (!game.getFirstRun()) {
            g.drawImage(backgroundImage, game.getBg1().getBgX(),
                  game.getBg1().getBgY(), main);
            g.drawImage(backgroundImage, game.getBg2().getBgX(),
                  game.getBg2().getBgY(), main);
            g.drawImage(currentSprite, game.getPlayer().getPositionX(),
                  game.getPlayer().getPositionY(), main);

            if (game.problem != null) {
               g.drawString(game.problem, 600, 40);
            }
            
            // draw score
            g.drawString("Score: " + Integer.toString(game.tempScore), 600, 80);

            for (Iterator<ConeObstacle> iterator = game.obstacleList
                  .iterator(); iterator.hasNext();) {
               ConeObstacle cone = iterator.next();
               if (cone.getPositionY() > Main.windowHeight
                     + ConeObstacle.height) {
                  iterator.remove();
               } else {
                  g.drawImage(coneImage, cone.getPositionX(),
                        cone.getPositionY(), main);
               }
            }
         }
         break;

      // In options state
      case OPTIONS:
         // Display Title
         g.setFont(font1);
         g.setColor(Color.white);

         fm = g.getFontMetrics();
         x = (d.width / 2) - (fm.stringWidth(menuOptions[2]) / 2);
         y = 100;

         g.drawString(menuOptions[2], x, y);

         // Display options
         g.setFont(font2);
         fm = g.getFontMetrics();

         for (index = 0; index < 2; index++) {
            y = 250 + (60 * index);

            x = (d.width / 2) - 300;

            g.drawString(optOptions[index], x, y);
         }

         // Draw check marks
         x = (d.width / 2) - (fm.stringWidth(optOptions[2]) / 2);
         g.drawString(optOptions[2], x, 370);

         if (game.isSoundOn()) {
            g.drawImage(checkMark, 600, 218, main);
         }

         else {
            g.drawImage(checkBox, 600, 218, main);
         }

         if (game.isBgmOn()) {
            g.drawImage(checkMark, 600, 278, main);
         }

         else {
            g.drawImage(checkBox, 600, 278, main);
         }

         // Move selection arrow
         switch (game.getOptionIndex()) {
         case 0:
            g.drawImage(leftArrow, 650, 218, main);
            break;

         case 1:
            g.drawImage(leftArrow, 650, 278, main);
            break;

         case 2:
            g.drawImage(leftArrow, 468, 335, main);
            break;
         }
         break;

      // In score state
      case SCORES:
         // Title
         g.setFont(font1);
         g.setColor(Color.white);

         fm = g.getFontMetrics();
         x = (d.width / 2) - (fm.stringWidth(menuOptions[3]) / 2);
         y = 100;

         g.drawString(menuOptions[3], x, y);

         // Display for scores
         x = (d.width/2);
         
         g.setFont(font2);
         fm = g.getFontMetrics();
         
         x = 200;
         y = 200;

         if(!displayed)
         {
         game.getHighScores().getHighscoreString(g, x, y);
         }
         else
         {
        	 displayed = true;
         }
         break;
         
      // In credits state
      case CREDITS:
         // Title
         g.setFont(font1);
         g.setColor(Color.white);

         fm = g.getFontMetrics();
         x = (d.width / 2) - (fm.stringWidth(menuOptions[4]) / 2);
         y = 100;

         g.drawString(menuOptions[4], x, y);

         // Display options
         g.setFont(font2);
         fm = g.getFontMetrics();

         for (index = 0; index < 3; index++) {
            y = 250 + (60 * index);
            x = (d.width / 2) - 300;

            g.drawString(names[index], x, y);
         }
         break;
      
      // In connection state
      case CONNECTION:
         // Title
         g.setFont(font1);
         g.setColor(Color.white);

         fm = g.getFontMetrics();
         x = (d.width / 2) - (fm.stringWidth(menuOptions[0]) / 2);
         y = 100;

         g.drawString(menuOptions[0], x, y);

         // Display connecting image
         x = (d.width / 2) - (connect.getWidth(main) / 2);
         y = (d.height / 2) - fm.getHeight();

         g.drawImage(connect, x, y, main);

         break;

      // In skill level state
      case SKILL_LEVEL:
         // Title
         g.setFont(font1);
         g.setColor(Color.white);

         fm = g.getFontMetrics();
         x = (d.width / 2) - (fm.stringWidth(levelOptions[0]) / 2);
         y = 100;

         g.drawString(levelOptions[0], x, y);

         // Display level options
         g.setFont(font2);
         fm = g.getFontMetrics();

         for (index = 1; index < 5; index++) {
            y = 150 + (60 * index);
            x = (d.width / 2) - 250;

            g.drawString(levelOptions[index], x, y);

         }
         
         if (game.badSkillSelect == true) {
        	 g.drawString("You must select a skill level.", 60, 450);
         }

         if (game.isAddition()) {
            g.drawImage(checkMark, 580, 175, main);
         }

         else {
            g.drawImage(checkBox, 580, 175, main);
         }

         if (game.isSubtraction()) {
            g.drawImage(checkMark, 580, 235, main);
         }

         else {
            g.drawImage(checkBox, 580, 235, main);
         }
         
         if (game.isMultiplcation()) {
            g.drawImage(checkMark, 580, 295, main);
         }

         else {
            g.drawImage(checkBox, 580, 295, main);
         }
         
         // Move selection arrow
         switch (game.getSkillIndex()) {
         case 0:
            g.drawImage(leftArrow, 630, 175, main);
            break;

         case 1:
            g.drawImage(leftArrow, 630, 235, main);
            break;

         case 2:
            g.drawImage(leftArrow, 630, 295, main);
            break;
            
         case 3:
            g.drawImage(leftArrow, 435, 360, main);
         }
         break;
         
      // In game over state
      case GAMEOVER:
         // Title
          g.setFont(font1);
          g.setColor(Color.white);

          fm = g.getFontMetrics();
          x = (d.width / 2) - (fm.stringWidth(gameOver[0]) / 2);
          y = 100;

          g.drawString(gameOver[0], x, y);
          
          // Display text for name and scores
          g.setFont(font2);
    	  
          g.drawString(gameOver[1], x - 150, 200);
          g.drawString(Integer.toString(game.score), x, 260);
          g.drawString(gameOver[2], x - 150, 320);
          g.drawString(game.playerName, x , 380);
          break;

      // In instructions state
      case INSTRUCTIONS:
         // Display title
    	  g.setFont(font1);
          g.setColor(Color.white);
          
          fm = g.getFontMetrics();
          x = (d.width / 2) - (fm.stringWidth(levelOptions[0]) / 2) + 80;
          y = 75;
    	  
          g.drawString("Instructions", x, y);
          
          g.setFont(font3);
          g.setColor(Color.white);
          x += 5;
          
          // Display instructions
          y += 100;
          g.drawString("Use enter to select a menu option", x, y);
          y += 50;
          g.drawString("Rotate your wrist to steer your car", x, y);
          y += 50;
          g.drawString("Solve the math problems by typing", x, y);
          y += 25;
          g.drawString("the answer in order to slow the game down", x, y);
          y += 50;
          g.drawString("Use the arrow keys to navigate the menus", x, y);
          y += 50;
          g.drawString("Press enter to go to the main menu", x, y);
          y += 50;
    	  break;
      default:
         break;

      }
   }
}
