package grandmathauto;

public class Car {
	static final int width = 38;
	static final int height = 64;

	private int positionX = 0, positionY = 0;
	private int speedX = 6, speedY = 6;

	private boolean movingLeft = false, movingRight = false, movingUp = false, movingDown = false;

	public Car(int x, int y) {
		positionX = x;
		positionY = y;
	}

	public void update() {
		/* Handle Movement */
		if (positionX > Background.roadBarrierLeft && movingLeft) { // Move left
			positionX -= speedX;
		}
		if (positionX + width < Background.roadBarrierRight && movingRight) { // Move right
			positionX += speedX;
		}
		if (positionY > 0 && movingUp) { // Move up
			positionY -= speedY;
		}
		if (positionY + height < Main.windowHeight && movingDown) { // Move down
			positionY += speedY;
		}
		
		/* Create Obstacles */
	}

	public void moveLeft() {
		movingLeft = true;
		movingRight = false;
	}

	public void moveRight() {
		movingRight = true;
		movingLeft = false;
	}

	public void moveUp() {
		movingUp = true;
		movingDown = false;
	}

	public void moveDown() {
		movingDown = true;
		movingUp = false;
	}

	public void stopLeft() {
		movingLeft = false;
	}

	public void stopRight() {
		movingRight = false;
	}

	public void stopUp() {
		movingUp = false;
	}

	public void stopDown() {
		movingDown = false;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int centerX) {
		this.positionX = centerX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int centerY) {
		this.positionY = centerY;
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
