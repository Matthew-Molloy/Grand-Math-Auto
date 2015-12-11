package grandmathauto;

import grandmathauto.Game.STATE;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;

public class Main extends Applet implements KeyListener, MouseListener {
	static final int windowWidth = 800;
	static final int windowHeight = 480;

	private Image image;
	private URL base;
	private Graphics second;
	private Color bg = new Color(87, 172, 242);
	static public GraphicsManager graphicsManager;

	private Game game;

	/**
	 * Called after applet loaded into system
	 */
	@Override
	public void init() {
		/* Initialize application window */
		setSize(windowWidth, windowHeight);
		setBackground(bg);
		setFocusable(true);
		addKeyListener(this);
		addMouseListener(this);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Grand Math Auto");

		/* Initialize game bitmaps */
		try {
			base = getDocumentBase();
		} catch (Exception e) {
			e.printStackTrace();
		}

		graphicsManager = new GraphicsManager(this);
		game = new Game(this);
	}

	/**
	 * Called at start of applet execution
	 */
	@Override
	public void start() {
		/* Start game */
		Thread thread = new Thread(game);
		thread.start();
	}

	/**
	 * Updates graphics window. Utilizes double buffering
	 */
	@Override
	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}

		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);

		g.drawImage(image, 0, 0, this);
	}

	/**
	 * Paints graphics window
	 */
	@Override
	public void paint(Graphics g) {
		graphicsManager.paint(g);

	}

	/**
	 * Handles key press events
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int index;
		
		if (e.getKeyCode() == KeyEvent.VK_M) {
			game.setFirstRun(true);
			game.setState(STATE.MAIN);
		}

		if (game.getState() == STATE.MAIN) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				index = game.getMainIndex();
				index--;
				game.setMainIndex(index);
				break;
			case KeyEvent.VK_DOWN:
				index = game.getMainIndex();
				index++;
				game.setMainIndex(index);
				break;
			case KeyEvent.VK_ENTER:
				index = game.getMainIndex();
				game.setState(game.getMainArr(index));
				break;
			case KeyEvent.VK_SPACE:
				index = game.getMainIndex();
				game.setState(game.getMainArr(index));
				break;
			}
		}

		if (game.getState() == STATE.SKILL_LEVEL) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				index = game.getSkillIndex();
				index--;
				game.setSkillIndex(index);
				break;
			case KeyEvent.VK_DOWN:
				index = game.getSkillIndex();
				index++;
				game.setSkillIndex(index);
				break;
			case KeyEvent.VK_ENTER:
				index = game.getSkillIndex();
				game.setLevel();
				break;
			case KeyEvent.VK_SPACE:
				index = game.getSkillIndex();
				game.setLevel();
				break;
			case KeyEvent.VK_ESCAPE:
				game.setState(STATE.MAIN);
				break;
			}
		}
		
		if (game.getState() == STATE.GAME) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				game.getPlayer().moveUp();
				break;
			case KeyEvent.VK_DOWN:
				game.getPlayer().moveDown();
				break;
			case KeyEvent.VK_LEFT:
				game.getPlayer().moveLeft();
				break;
			case KeyEvent.VK_RIGHT:
				game.getPlayer().moveRight();
				break;
			case KeyEvent.VK_SPACE:
				break;
			}
		}

		if (game.getState() == STATE.OPTIONS) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				index = game.getOptionIndex();
				index--;
				game.setOptionIndex(index);
				break;
			case KeyEvent.VK_DOWN:
				index = game.getOptionIndex();
				index++;
				game.setOptionIndex(index);
				break;
			case KeyEvent.VK_ENTER:
				index = game.getOptionIndex();
				game.setOptions();
				System.out.println("moo");
				break;
			case KeyEvent.VK_SPACE:
				index = game.getOptionIndex();
				System.out.println("moo");
				game.setOptions();
			case KeyEvent.VK_ESCAPE:
				game.setState(STATE.MAIN);
				break;
			}
		}
		
		if(game.getState() == STATE.SCORES)
		{
			if(game.isScoreDisplay())
			{
				game.setScoreDisplay(false);
			}
			else
			{
				game.setState(STATE.MAIN);
			}
		}
		
		if(game.getState() == STATE.CREDITS)
		{
			if(game.isCreditDisplay())
			{
				game.setCreditDisplay(false);
			}
			else
			{
				game.setState(STATE.MAIN);
			}
		}
	}

	/**
	 * Handles key release events
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (game.getState() == STATE.GAME) {
			char c = e.getKeyChar();
			if (game.mathProblemActive == true && c >= '0' && c <= '9') {
				if( c == game.result.get(game.mathIndex)) {
					if( game.result.size() - 1 == game.mathIndex ) {
						game.problem = "Correct!";
						game.mathSchemeTracker = 180;
						if (Game.speed > Game.minSpeed) {
							Game.speed -= 2;
						}
						game.result = new ArrayList<>();
						game.mathProblemActive = false;
						System.out.println("Correct!");
					}
					else {
						game.mathIndex++;
					}
				}
				else {
					game.problem = "Oops!";
					game.mathProblemActive = false;
					game.result = new ArrayList<>();
					game.mathSchemeTracker = 240;
					Game.speed++;
				}
			}
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				game.getPlayer().stopUp();
				break;
			case KeyEvent.VK_DOWN:
				game.getPlayer().stopDown();
				break;
			case KeyEvent.VK_LEFT:
				game.getPlayer().stopLeft();
				break;
			case KeyEvent.VK_RIGHT:
				game.getPlayer().stopRight();
				break;
			case KeyEvent.VK_SPACE:
				break;
			}
		}
	}

	/**
	 * Handles key types. Called if key stroke would generate a Unicode
	 * character
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public URL getBase() {
		return base;
	}

	public Game getGame() {
		return game;
	}

	/**
	 * Called at end of applet execution
	 */
	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}

	/**
	 * Destroy any resources that has allocated
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("(" + e.getX() + ", " + e.getY() + ")");
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
