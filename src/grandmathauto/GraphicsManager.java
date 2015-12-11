package grandmathauto;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;

public class GraphicsManager {
   static public Image image, currentSprite, playerImage, backgroundImage,
         debugDot, leftArrow, rightArrow, checkMark, checkBox, coneImage,
         connect1, connect2, connect3;

   private Main main;

   private String[] menuOptions = { "Grand Math Auto", "Play", "Options",
         "High Scores", "Credits", "Quit" };
   private String[] optOptions = { "Sound Effects", "Background Music",
         "Back" };
   private String[] levelOptions = { "Select Skill Level", "Addition",
         "Subtraction", "Multiplication", "Division", "Start Game" };
   private String[] names = { "Matthew Molloy", "Jennifer Tang", "Ricky Yu" };

   public GraphicsManager(Main main) {
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
      connect1 = main.getImage(main.getBase(), "data/connect1.png");
      connect2 = main.getImage(main.getBase(), "data/connect2.png");
      connect3 = main.getImage(main.getBase(), "data/connect3.png");
   }

   public void paint(Graphics g) throws InterruptedException {
      int x = 0, y = 0, index = 0;
      Dimension d = main.getSize();
      FontMetrics fm;
      Game game = main.getGame();

      // Fonts, Title & Options
      Font font1 = new Font("Helvetica", Font.BOLD, 75);
      Font font2 = new Font("Helvetica", Font.BOLD, 50);

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

         System.out.println(game.isSoundOn());
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

      case SCORES:
         g.setFont(font1);
         g.setColor(Color.white);

         fm = g.getFontMetrics();
         x = (d.width / 2) - (fm.stringWidth(menuOptions[3]) / 2);
         y = 100;

         g.drawString(menuOptions[3], x, y);

         // DATABASE
         break;

      case CREDITS:
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

      case CONNECTION:
         g.setFont(font1);
         g.setColor(Color.white);

         fm = g.getFontMetrics();
         x = (d.width / 2) - (fm.stringWidth(menuOptions[0]) / 2);
         y = 100;

         g.drawString(menuOptions[0], x, y);
         
         x = (d.width / 2) - (connect1.getWidth(main)/2);
         y = (d.height / 2) - fm.getHeight();
         
         g.drawImage(connect1, x, y, main);
         //Thread.sleep(20);
         g.drawImage(connect2, x, y, main);
         //Thread.sleep(20);
         g.drawImage(connect3, x, y, main);
         //Thread.sleep(20);
         break;

      case SKILL_LEVEL:
         g.setFont(font1);
         g.setColor(Color.white);

         fm = g.getFontMetrics();
         x = (d.width / 2) - (fm.stringWidth(levelOptions[0]) / 2);
         y = 100;

         g.drawString(levelOptions[0], x, y);
         break;

      default:
         break;

      }
   }
}
