package grandmathauto;

public class ConeObstacle {
	static final int width = 40;
	static final int height = 40;

	private int positionX = 0, positionY = 0;
	private int speedX = 5, speedY = 5;

	public ConeObstacle(int x, int y) {
		positionX = x;
		positionY = y;
	}

	public void update() {
		speedY = Game.speed;
		this.positionY += speedY; // Move cone downward
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
}
