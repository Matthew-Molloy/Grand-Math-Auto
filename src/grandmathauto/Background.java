package grandmathauto;

public class Background {
	static final int width = 800, height = 512;
	static final int roadBarrierLeft = 70;
	static final int roadBarrierRight = 530;

	private int bgX, bgY, speedY;

	public Background(int x, int y) {
		bgX = x;
		bgY = y;
		speedY = 5;
	}

	public void update() {
		bgY += speedY;

		if (bgY >= 512) {
			bgY -= 1024;
		}
	}

	public int getBgX() {
		return bgX;
	}

	public void setBgX(int bgX) {
		this.bgX = bgX;
	}

	public int getBgY() {
		return bgY;
	}

	public void setBgY(int bgY) {
		this.bgY = bgY;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public int getRoadBarrierLeft() {
		return roadBarrierLeft;
	}

	public int getRoadBarrierRight() {
		return roadBarrierRight;
	}
}
