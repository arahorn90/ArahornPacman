package ua.arahorn.arahornspacman;

import ua.arahorn.arahornspacman.gameelements.BackgroundElement;
import ua.arahorn.arahornspacman.gameelements.PacmanControl;
import ua.arahorn.arahornspacman.gameelements.PacmanElement;
import ua.arahorn.arahornspacman.gameelements.PacmanElement.PacmanMove;
import ua.arahorn.arahornspacman.gameelements.RedAlien;
import ua.arahorn.arahornspacman.gameelements.TextElement;
import ua.arahorn.arahornspacman.gameelements.WallElement;
import ua.arahorn.arahornspacman.utils.Constants;

public class GameEngine {

	private TextElement smallTextElement;
	private BackgroundElement backgroundElement;
	private WallElement wallElement;
	private PacmanElement pacmanElement;
	private PacmanControl pacmanControl;
	private RedAlien redAlien;
//	private int elementSizeOnSurface;
	private int width;
	private int height;

	public static GameEngine getInstanceEngine() {
		return new GameEngine();
	}

	public GameEngine() {

	}

	public void updateEngine() {
		if(pacmanElement != null && pacmanControl != null)
			pacmanElement.updatePacman(pacmanControl.getX(), pacmanControl.getY());
		if(redAlien != null)
			redAlien.updateRedAlien(pacmanElement.getPacmanPosition()[1], pacmanElement.getPacmanPosition()[0]);

	}

	public void checkContains(float x, float y) {
		if (pacmanControl.getRect().contains(x, height - y)) {
			pacmanControl.setControlActive(true);
			pacmanControl.setX(x);
			pacmanControl.setY(height - y);
		}

	}

	public void checkMove(float x, float y) {
		if (pacmanControl != null && pacmanControl.getControlActive()) {
			float offsetX = x - (width - 100 - Constants.CONTROL_SIZE / 2);
			float offsetY = y - (height - 100 - Constants.CONTROL_SIZE / 2);
			if (Math.abs(offsetX) > Math.abs(offsetY)) {
				if (offsetX > 0) {
					pacmanControl.setX(pacmanControl.getStableX() + Constants.MAX_CONTROL_DELTA);
				} else if (-offsetX > 0) {
					pacmanControl.setX(pacmanControl.getStableX() - Constants.MAX_CONTROL_DELTA);
				} else {
					pacmanControl.setX(x);
				}
				pacmanControl.setY(pacmanControl.getStableY());
			} else {
				if (offsetY > 0) {
					pacmanControl.setY(pacmanControl.getStableY() - Constants.MAX_CONTROL_DELTA);
				} else if (-offsetY > 0) {
					pacmanControl.setY(pacmanControl.getStableY() + Constants.MAX_CONTROL_DELTA);
				} else {
					pacmanControl.setY(y);
				}
				pacmanControl.setX(pacmanControl.getStableX());
			}
		}
	}

	public void stopMoving() {
		if (pacmanElement != null) {
			pacmanElement.setMove(PacmanMove.STOP);
		}
		if (pacmanControl != null) {
			pacmanControl.setControlActive(false);
		}
	}

	public void setAttributes(int elementSizeOnSurface, int width, int height) {
//		this.elementSizeOnSurface = elementSizeOnSurface;
		this.width = width;
		this.height = height;
		GameActivity.pacmanStep = elementSizeOnSurface/8;
		GameActivity.redAlienStep = elementSizeOnSurface/12;

	}

	public TextElement getSmallTextElement() {
		return smallTextElement;
	}

	public void setSmallTextElement(TextElement smallTextElement) {
		this.smallTextElement = smallTextElement;
	}

	public BackgroundElement getBackgroundElement() {
		return backgroundElement;
	}

	public void setBackgroundElement(BackgroundElement backgroundElement) {
		this.backgroundElement = backgroundElement;
	}

	public WallElement getWallElement() {
		return wallElement;
	}

	public void setWallElement(WallElement wallElement) {
		this.wallElement = wallElement;
	}

	public PacmanElement getPacmanElement() {
		return pacmanElement;
	}

	public void setPacmanElement(PacmanElement pacmanElement) {
		this.pacmanElement = pacmanElement;
	}

	public PacmanControl getPacmanControl() {
		return pacmanControl;
	}

	public void setPacmanControl(PacmanControl pacmanControl) {
		this.pacmanControl = pacmanControl;
	}

	public RedAlien getRedAlien() {
		return redAlien;
	}

	public void setRedAlien(RedAlien redAlien) {
		this.redAlien = redAlien;
	}

}