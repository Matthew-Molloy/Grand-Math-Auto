package grandmathauto;

import java.util.ArrayList;
import java.util.Random;

public class Game implements Runnable {
	final int FRAMES_PER_SECOND = 60;
	final int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;

	private Main main;
	private Car player;
	private Background bg1, bg2;
	
	private boolean addition = true, subtraction = true, multiplication = true, division = true;
	
	private boolean soundOn = true;
	private boolean bgmOn = true;
	
	private boolean scoreDisplay = false;
	private boolean creditDisplay = false;

	private boolean firstRun = true;
	private boolean running = true;

	private int obstacleScheme = 0; // used to determine what obstacle pattern
									// to generate in handleObstacles()
	private int obstacleSchemeTracker = 0; // used to track what step in the
											// scheme is currently needed
	private int timeBetweenObstacles = 0;

	private long nextGameTick = 0;
	private long elapsedTicks = 0;
	private long sleepTime = 0;
	
	public ArrayList<ConeObstacle> obstacleList = new ArrayList<>();

	public enum STATE {
		MAIN, GAME, GAMEOVER, OPTIONS, SCORES, CREDITS, CONNECTION, SKILL_LEVEL, QUIT
	};

	private STATE[] mainArr = { STATE.SKILL_LEVEL, STATE.OPTIONS, STATE.SCORES,
			STATE.CREDITS, STATE.QUIT };

	private int[] optionArr = { 0, 1, 2 }, skillArr = { 0, 1, 2, 3, 4 };

	private int mainIndex = 0, optionIndex = 0, skillIndex = 0;

	private STATE State = STATE.MAIN;

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
					timeBetweenObstacles = 180;
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
				bg1.update();
				bg2.update();
				handleObstacles();
				main.repaint();
				elapsedTicks++;

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
				break;

			// Select a skill level before playing game
			case SKILL_LEVEL:
				State = STATE.GAME;
				break;

			/* Close applet */
			case QUIT:
				running = false;
				System.exit(0);
				break;
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
				obstacleScheme = 1 + rand.nextInt(2);
				obstacleSchemeTracker = 180;
				System.out.println(obstacleScheme);
			}
			break;

		case 1: // 5 random cones
			if (obstacleSchemeTracker % 36 == 0) {
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
			if (obstacleSchemeTracker % 60 == 0) {
				int xPosition = rand.nextInt((Background.roadBarrierRight-(ConeObstacle.width * 5)) - Background.roadBarrierLeft) + Background.roadBarrierLeft;
				ConeObstacle cone = new ConeObstacle(xPosition, -ConeObstacle.height);
				ConeObstacle cone1 = new ConeObstacle(xPosition + ConeObstacle.width, -ConeObstacle.height);
				ConeObstacle cone2 = new ConeObstacle(xPosition + (2*ConeObstacle.width), -ConeObstacle.height);
				ConeObstacle cone3 = new ConeObstacle(xPosition + (3*ConeObstacle.width), -ConeObstacle.height);
				ConeObstacle cone4 = new ConeObstacle(xPosition + (4*ConeObstacle.width), -ConeObstacle.height);
				obstacleList.add(cone);
				obstacleList.add(cone1);
				obstacleList.add(cone2);
				obstacleList.add(cone3);
				obstacleList.add(cone4);
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
		if(State == STATE.SCORES)
		{
			scoreDisplay = true;
		}
		if(State == STATE.CREDITS)
		{
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
			skillIndex = 4;
		} else if (val > 4) {
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
	
	public void setOptions(){
		switch(optionIndex){
		
		// Sound
		case 0:
			if(soundOn)
			{
				soundOn = false;
			}
			else
			{
				soundOn = true;
			}
			break;
			
		// BGM	
		case 1:
			if(bgmOn)
			{
				bgmOn = false;
			}
			else
			{
				bgmOn = true;
			}
			break;
		
		// Back
		case 2:
			State = STATE.MAIN;
			break;
		}
	}
	
	public void setLevel(){
		switch(skillIndex){
		
		// Addition
		case 0:
			if(addition)
			{
				addition = false;
			}
			else
			{
				addition = true;
			}
			break;
		
		// Subtraction
		case 1:
			if(subtraction)
			{
				subtraction = false;
			}
			else
			{
				subtraction = true;
			}
			break;
		
		// Multiplication
		case 2:
			if(multiplication)
			{
				multiplication = false;
			}
			else
			{
				multiplication = true;
			}
			break;
		
		// Division	
		case 3:
			if(division)
			{
				division = false;
			}
			else
			{
				division = true;
			}
			break;
		
		// Start Game
		case 4:
			State = STATE.GAME;
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
}
