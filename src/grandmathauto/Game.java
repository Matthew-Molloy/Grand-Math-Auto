package grandmathauto;

import java.util.ArrayList;
import java.util.Random;

public class Game implements Runnable {
   final int FRAMES_PER_SECOND = 60;
   final int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;
   
   static int speed = 0;
   static int minSpeed = 0;

   private Main main;
   private Car player;
   private Background bg1, bg2;

   private boolean addition = false, subtraction = false,
         multiplication = false;

   private boolean soundOn = true;
   private boolean bgmOn = true;

   private boolean scoreDisplay = false;
   private boolean creditDisplay = false;
   private boolean optionsDisplay = false;
   private boolean skillDisplay = false;

   private boolean firstRun = true;
   private boolean running = true;

   private int obstacleScheme = 0; // used to determine what obstacle pattern
   // to generate in handleObstacles()
   private int obstacleSchemeTracker = 0; // used to track what step in the
   // scheme is currently needed
   private int timeBetweenObstacles = 0;

   // math stuff
   public int mathScheme = 0;
   public int mathSchemeTracker = 0;
   public boolean mathProblemActive = false;
   public boolean correct = false;
   public int mathIndex = 0;
   public String problem;
   public ArrayList<Character> result = new ArrayList<>();

   private long nextGameTick = 0;
   private long elapsedTicks = 0;
   private long sleepTime = 0;

   // Server stuff
   private Server server;
   private int dataCheck = 5;
   private boolean serverCheck = true;
   
   // Sensor stuff
   private boolean menuCheck = true;

   public boolean badSkillSelect = false;
   
   public ArrayList<ConeObstacle> obstacleList = new ArrayList<>();

   public enum STATE {
      MAIN, GAME, GAMEOVER, OPTIONS, SCORES, CREDITS, CONNECTION, SKILL_LEVEL, QUIT
   };

   private STATE[] mainArr = { STATE.SKILL_LEVEL, STATE.OPTIONS, STATE.SCORES,
         STATE.CREDITS, STATE.QUIT };

   private int[] optionArr = { 0, 1, 2 }, skillArr = { 0, 1, 2, 3 };

   private int mainIndex = 0, optionIndex = 0, skillIndex = 0;

   private STATE State = STATE.CONNECTION;
   
   private HighscoreManager highScores = new HighscoreManager();

   public Game(Main main) {
      this.main = main;
   }

   /**
    * Execute run when creating a new thread
    */
   @Override
   public void run() {
      while (running) {
         switch (State) {
         /* In main menu */
         case MAIN:
            main.repaint();
            checkSensorMenu();
            break;

         /* In game state */
         case GAME:
            /*
             * On the first tick of the game, initialize game objects and
             * variables
             */
            if (firstRun) {
               elapsedTicks = 0;
               obstacleScheme = 0;
               obstacleSchemeTracker = 0;
               mathSchemeTracker = 0;
               timeBetweenObstacles = 180;
               mathScheme = 0;
               mathSchemeTracker = 0;
               mathProblemActive = false;

               speed = 5;
               minSpeed = 5;

               correct = false;
               mathIndex = 0;
               result = new ArrayList<>();
               obstacleList = new ArrayList<>();
               bg1 = new Background(0, 0);
               bg2 = new Background(0, 512);
               player = new Car(3 * Main.windowWidth / 8,
                     Main.windowHeight - 100);
               nextGameTick = System.currentTimeMillis();
               firstRun = false;
            }

            /* Update game object physics and repaint scene */
            player.update();
            checkSensor();
            bg1.update();
            bg2.update();
            handleObstacles();
            handleMath();
            main.repaint();
            elapsedTicks++;

/* gradually increase difficulty */
if (elapsedTicks % 240 == 0) {
speed++;	
if (elapsedTicks % 1200 == 0) {
minSpeed++;
}
}

            /* Update 60 frames per second */
            nextGameTick += SKIP_TICKS;
            sleepTime = nextGameTick - System.currentTimeMillis();
            if (sleepTime >= 0) {
               try {
                  Thread.sleep(sleepTime);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }

            break;

         /* In game over state */
         case GAMEOVER:
System.out.println("Your score is " + (int)(elapsedTicks/60));
            main.repaint();
            break;

         case OPTIONS:
            main.repaint();
            break;

         case SCORES:
            main.repaint();
            break;

         case CREDITS:
            main.repaint();
            break;

         case CONNECTION:
            main.repaint();
            if (serverCheck) {
                // Start server thread to listen for sensor input
                server = new Server(this);
                new Thread(server).start();
                serverCheck = false;
             }
            break;

         // Select a skill level before playing game
         case SKILL_LEVEL:
            main.repaint();
            break;

         /* Close applet */
         case QUIT:
            running = false;
            System.exit(0);
            break;
         }
      }
   }

   public void checkSensor() {
      Float data = server.currentData;
      if (data != null) {
         // neutral movement
         if (data >= -dataCheck && data <= dataCheck) {
            player.stopLeft();
            player.stopRight();
         }
         // right movement
         else if (data > dataCheck) {
            player.moveRight();
         }
         // left movement
         else if (data < -dataCheck) {
            player.moveLeft();
         }
      }
   }

   public void checkSensorMenu() {
      Float data = server.currentData;
      if (data != null) {
         int index;
         // neutral movement
         if (data >= -dataCheck && data <= dataCheck) {
            menuCheck = true;
         }
         // right movement
         else if (data > dataCheck && menuCheck == true) {
            index = getMainIndex();
            index++;
            setMainIndex(index);
            menuCheck = false;
         }
         // left movement
         else if (data < -dataCheck && menuCheck == true) {
            index = getMainIndex();
            index--;
            setMainIndex(index);
            menuCheck = false;
         }
      }
   }

   /**
	 * Generates driving challenges for the player in the car portion of the
	 * game.
	 */
	private void handleObstacles() {
		Random rand = new Random();
		
		switch (obstacleScheme) {
		case 0: // No obstacles			
			/* Generate new obstacle */
			if (obstacleSchemeTracker < 0) {
				System.out.println("Speed/MinSpeed: " + Game.speed + "/" + Game.minSpeed);
				obstacleScheme = 1 + rand.nextInt(3);
				obstacleSchemeTracker = timeBetweenObstacles;
			}
			break;

		case 1: // 8 random cones
			if (obstacleSchemeTracker % (int)(timeBetweenObstacles / 8) == 0) {
				int xPosition = rand.nextInt((Background.roadBarrierRight-ConeObstacle.width) - Background.roadBarrierLeft) + Background.roadBarrierLeft;
				ConeObstacle cone = new ConeObstacle(xPosition, -ConeObstacle.height);
				obstacleList.add(cone);
			}

			if (obstacleSchemeTracker < 0) {
				obstacleScheme = 0;
				obstacleSchemeTracker = timeBetweenObstacles;
			}
			break;
			
		case 2: // 3 horizontal line of cones
			if (obstacleSchemeTracker % (int)(timeBetweenObstacles / 3) == 0) {
				int xPosition = rand.nextInt((Background.roadBarrierRight-(ConeObstacle.width * 4)) - Background.roadBarrierLeft) + Background.roadBarrierLeft;
				ConeObstacle cone = new ConeObstacle(xPosition, -ConeObstacle.height);
				ConeObstacle cone1 = new ConeObstacle(xPosition + ConeObstacle.width, -ConeObstacle.height);
				ConeObstacle cone2 = new ConeObstacle(xPosition + (2*ConeObstacle.width), -ConeObstacle.height);
				ConeObstacle cone3 = new ConeObstacle(xPosition + (3*ConeObstacle.width), -ConeObstacle.height);
				obstacleList.add(cone);
				obstacleList.add(cone1);
				obstacleList.add(cone2);
				obstacleList.add(cone3);
			}
			
			if (obstacleSchemeTracker < 0) {
				obstacleScheme = 0;
				obstacleSchemeTracker = timeBetweenObstacles;
			}
			break;
			
		case 3: // center opening
			if (obstacleSchemeTracker == (int)(timeBetweenObstacles / 2)) {
			int xPosition = Background.roadBarrierLeft;
			ConeObstacle cone = new ConeObstacle(xPosition, -ConeObstacle.height);
			ConeObstacle cone1 = new ConeObstacle(xPosition + ConeObstacle.width, -ConeObstacle.height);
			ConeObstacle cone2 = new ConeObstacle(xPosition + (2*ConeObstacle.width), -ConeObstacle.height);
			ConeObstacle cone7 = new ConeObstacle(xPosition + (3*ConeObstacle.width), -ConeObstacle.height);
			
			xPosition = Background.roadBarrierRight - (4*ConeObstacle.width);
			ConeObstacle cone3 = new ConeObstacle(xPosition, -ConeObstacle.height);
			ConeObstacle cone4 = new ConeObstacle(xPosition + ConeObstacle.width, -ConeObstacle.height);
			ConeObstacle cone5 = new ConeObstacle(xPosition + (2*ConeObstacle.width), -ConeObstacle.height);
			ConeObstacle cone6 = new ConeObstacle(xPosition + (3*ConeObstacle.width), -ConeObstacle.height);
			
			obstacleList.add(cone);
			obstacleList.add(cone1);
			obstacleList.add(cone2);
			obstacleList.add(cone3);
			obstacleList.add(cone4);
			obstacleList.add(cone5);
			obstacleList.add(cone6);
			obstacleList.add(cone7);
			} else if (obstacleSchemeTracker == 0 || obstacleSchemeTracker == timeBetweenObstacles - 1) {
				int xPosition = Background.roadBarrierLeft + (int)((Background.roadBarrierRight - Background.roadBarrierLeft)/2) - (2*ConeObstacle.width);
				ConeObstacle cone = new ConeObstacle(xPosition, -ConeObstacle.height);
				ConeObstacle cone1 = new ConeObstacle(xPosition + ConeObstacle.width, -ConeObstacle.height);
				ConeObstacle cone2 = new ConeObstacle(xPosition + (2*ConeObstacle.width), -ConeObstacle.height);
				ConeObstacle cone3 = new ConeObstacle(xPosition + (3*ConeObstacle.width), -ConeObstacle.height);
				
				obstacleList.add(cone);
				obstacleList.add(cone1);
				obstacleList.add(cone2);
				obstacleList.add(cone3);
			}
			
			if (obstacleSchemeTracker < 0) {
				obstacleScheme = 0;
				obstacleSchemeTracker = timeBetweenObstacles;
			}
		default:
			break;
		}
		
		/* Update cones */
		for (ConeObstacle cone: obstacleList) {
			/* Check Collisions */
			if (cone.getPositionX() < player.getPositionX() + Car.width &&
					cone.getPositionX() + ConeObstacle.width > player.getPositionX() &&
					cone.getPositionY() < player.getPositionY() + Car.height &&
					cone.getPositionY() + ConeObstacle.height > player.getPositionY()) {
				State = STATE.GAMEOVER;
			}
			
			cone.update();
		}
		
		obstacleSchemeTracker--;
	}
	
	private void handleMath() {
		Random rand = new Random();
		
		switch (mathScheme) {
		case 0:
			if (mathSchemeTracker < 0 && mathProblemActive == false) {
				mathScheme = 1;
				mathIndex = 0;
			}
			break;
			
		case 1:
			while (!mathProblemActive) {
				problem = "";
				String temp;
				int x, y, operator, r;
				x = 1 + rand.nextInt(8);
				y = 1 + rand.nextInt(8);
				operator = 1 + rand.nextInt(3);
				problem += (Integer.toString(x));
				switch(operator) {
				case 1:
					if (addition) {
						problem += (" + " + Integer.toString(y));
						r = x + y;
						temp = Integer.toString(r);
						for (int i = 0; i < temp.length(); i++) {
							result.add(temp.charAt(i));
						} 
						mathProblemActive = true;
					}
					break;
				case 2:
					if (subtraction) {
						while (x - y < 0) {
							problem = "";
							x = 1 + rand.nextInt(8);
							y = 1 + rand.nextInt(8);
							operator = 1 + rand.nextInt(3);
							problem += (Integer.toString(x));
						}
						problem += (" - " + Integer.toString(y));
						r = x - y;
						temp = Integer.toString(r);
						for (int i = 0; i < temp.length(); i++) {
							result.add(temp.charAt(i));
						} 
						mathProblemActive = true;
					}
					break;
				case 3:
					if (multiplication) {
						problem += (" * " + Integer.toString(y));
						r = x * y;
						temp = Integer.toString(r);
						for (int i = 0; i < temp.length(); i++) {
							result.add(temp.charAt(i));
						} 
						mathProblemActive = true;
					}
					break;
					
				default:
					break;
				}
			}			
			mathScheme = 0;
			break;
			
		default:
			break;
		}
		mathSchemeTracker -= 1;
	}

   public Boolean getFirstRun() {
      return firstRun;
   }

   public void setFirstRun(Boolean firstRun) {
      this.firstRun = firstRun;
   }

   public Background getBg1() {
      return bg1;
   }

   public Background getBg2() {
      return bg2;
   }

   public Car getPlayer() {
      return player;
   }

   public STATE getState() {
      return State;
   }

   public void setState(STATE newState) {
      State = newState;
      if (State == STATE.SCORES) {
         scoreDisplay = true;
      }
      if (State == STATE.CREDITS) {
         creditDisplay = true;
      }
   }

   public int getMainIndex() {
      return mainIndex;
   }

   public int getOptionIndex() {
      return optionIndex;
   }

   public int getSkillIndex() {
      return skillIndex;
   }

   public void setMainIndex(int val) {
      // Loops the main menu array
      if (val < 0) {
         mainIndex = 4;
      } else if (val > 4) {
         mainIndex = 0;
      } else {
         mainIndex = val;
      }
   }

   public void setOptionIndex(int val) {
      if (val < 0) {
         optionIndex = 2;
      } else if (val > 2) {
         optionIndex = 0;
      } else {
         optionIndex = val;
      }
   }

   public void setSkillIndex(int val) {
      if (val < 0) {
         skillIndex = 3;
      } else if (val > 3) {
         skillIndex = 0;
      } else {
         skillIndex = val;
      }
   }

   public STATE getMainArr(int index) {
      return mainArr[index];
   }

   public int getOptionArr(int index) {
      return optionArr[index];
   }

   public int getSkillArr(int index) {
      return skillArr[index];
   }

   public boolean isBgmOn() {
      return bgmOn;
   }

   public void setBgmOn(boolean bgmOn) {
      this.bgmOn = bgmOn;
   }

   public boolean isSoundOn() {
      return soundOn;
   }

   public void setSoundOn(boolean soundOn) {
      this.soundOn = soundOn;
   }

   public void setOptions() {
      switch (optionIndex) {

      // Sound
      case 0:
         if (soundOn && optionsDisplay) {
            soundOn = false;
         } else {
            soundOn = true;
         }
         break;

      // BGM
      case 1:
         if (bgmOn) {
            bgmOn = false;
         } else {
            bgmOn = true;
         }
         break;

      // Back
      case 2:
         optionsDisplay = false;
         optionIndex = 0;
         State = STATE.MAIN;
         break;
      }
   }

   public void setLevel() {
      switch (skillIndex) {

      // Addition
      case 0:
         if (addition && skillDisplay) {
            addition = false;
         } else {
            addition = true;
         }
         break;

      // Subtraction
      case 1:
         if (subtraction) {
            subtraction = false;
         } else {
            subtraction = true;
         }
         break;

      // Multiplication
      case 2:
         if (multiplication) {
            multiplication = false;
         } else {
            multiplication = true;
         }
         break;

      // Start Game
      case 3:
    	  if (addition || multiplication || subtraction) {
	         skillDisplay = false;
	         skillIndex = 0;
	         State = STATE.GAME;
    	  }
    	  else {
    		  badSkillSelect = true;
    	  }
    	  break;
      }
   }

   public boolean isScoreDisplay() {
      return scoreDisplay;
   }

   public void setScoreDisplay(boolean scoreDisplay) {
      this.scoreDisplay = scoreDisplay;
   }

   public boolean isCreditDisplay() {
      return creditDisplay;
   }

   public void setCreditDisplay(boolean creditDisplay) {
      this.creditDisplay = creditDisplay;
   }

   public boolean isOptionsDisplay() {
      return optionsDisplay;
   }

   public void setOptionsDisplay(boolean optionsDisplay) {
      this.optionsDisplay = optionsDisplay;
   }
   
   public boolean isSkillDisplay() {
      return skillDisplay;
   }

   public void setSkillDisplay(boolean skillDisplay) {
      this.skillDisplay = skillDisplay;
   }
   
   public boolean isAddition() {
      return addition;
   }

   public boolean isSubtraction() {
      return subtraction;
   }

   public boolean isMultiplcation() {
      return multiplication;
   }
   
}
