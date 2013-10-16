package ua.arahorn.arahornspacman;

import ua.arahorn.arahornspacman.GameActivity.GameState;
import ua.arahorn.arahornspacman.gameelements.BackgroundElement;
import ua.arahorn.arahornspacman.gameelements.PacmanControl;
import ua.arahorn.arahornspacman.gameelements.PacmanElement;
import ua.arahorn.arahornspacman.gameelements.PacmanElement.PacmanMove;
import ua.arahorn.arahornspacman.gameelements.RedAlien;
import ua.arahorn.arahornspacman.gameelements.TextElement;
import ua.arahorn.arahornspacman.gameelements.WallElement;
import ua.arahorn.arahornspacman.utils.Constants;
import android.app.Activity;

public class GameEngine {

	private TextElement smallTextElement;
	private BackgroundElement backgroundElement;
	private WallElement wallElement;
	private PacmanElement pacmanElement;
	private PacmanControl pacmanControl;
	private RedAlien redAlien;
	private int elementSizeOnSurface;
	private int smallTextSizeOnSurface;
	private int width;
	private int height;
	private Activity activity;

	public static GameEngine getInstanceEngine(Activity activity) {
		return new GameEngine(activity);
	}

	public GameEngine(Activity activity) {
		this.activity = activity;
	}

	public void updateEngine() {
		if(GameActivity.gameState.equals(GameState.FIRST_SCREEN)) {
			if (pacmanElement != null && pacmanControl != null)
				pacmanElement.updatePacman(pacmanControl.getX(), pacmanControl.getY());
			if (redAlien != null)
				redAlien.updateRedAlien(pacmanElement.getPacmanPosition()[1], pacmanElement.getPacmanPosition()[0]);
			if (pacmanElement != null && redAlien != null) {
				if ((pacmanElement.getPacmanSurfaceCoordinate()[1] < redAlien.getRedAlienSurfaceCoordinate()[1] + elementSizeOnSurface * 2 / 3
						&& pacmanElement.getPacmanSurfaceCoordinate()[1] > redAlien.getRedAlienSurfaceCoordinate()[1] - elementSizeOnSurface * 2 / 3)
						&& (pacmanElement.getPacmanSurfaceCoordinate()[0] < redAlien.getRedAlienSurfaceCoordinate()[0] + elementSizeOnSurface * 2 / 3
								&& pacmanElement.getPacmanSurfaceCoordinate()[0] > redAlien.getRedAlienSurfaceCoordinate()[0] - elementSizeOnSurface * 2 / 3)) {
					GameActivity.gameState = GameState.MENU_SCREEN;
				}
			}
		}

	}

	public void checkContains(float x, float y) {
		if(GameActivity.gameState.equals(GameState.MENU_SCREEN)) {
			if(x > width / 2 - smallTextSizeOnSurface*3 && y < height / 2 
					&& x < width / 2 + smallTextSizeOnSurface*3 && y > height / 2 - smallTextSizeOnSurface) {
				GameActivity.gameState = GameState.FIRST_SCREEN;
				return;
			}else if(x > width / 2 - smallTextSizeOnSurface*4 && y < height / 2 + smallTextSizeOnSurface
					&& x < width / 2 + smallTextSizeOnSurface*4 && y > height / 2) {
				((GameActivity) activity).refreshGame();
				return;
			} else if(x > width / 2 - smallTextSizeOnSurface*2 && y < height / 2 + smallTextSizeOnSurface * 2
				&& x < width / 2 + smallTextSizeOnSurface*2 && y > height / 2 + smallTextSizeOnSurface) {
			activity.finish();
			return;
		}
		}
		if(GameActivity.gameState.equals(GameState.FIRST_SCREEN)) {
			if(x > width - elementSizeOnSurface*5 && y < elementSizeOnSurface) {
				if(GameActivity.gameState.equals(GameState.FIRST_SCREEN)) {
					GameActivity.gameState = GameState.MENU_SCREEN;
				} else {
					GameActivity.gameState = GameState.FIRST_SCREEN;
				}
				return;
			}
			if (pacmanControl.getRect().contains(x, height - y)) {
				pacmanControl.setControlActive(true);
				pacmanControl.setX(x);
				pacmanControl.setY(height - y);
			}
		}

	}

	public void checkMove(float x, float y) {
		if(GameActivity.gameState.equals(GameState.FIRST_SCREEN)) {
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
	}

	public void stopMoving() {
		if (pacmanElement != null) {
			pacmanElement.setMove(PacmanMove.STOP);
		}
		if (pacmanControl != null) {
			pacmanControl.setControlActive(false);
		}
	}

	public void setAttributes(int elementSizeOnSurface, int smallTextSizeOnSurface, int width, int height) {
		this.elementSizeOnSurface = elementSizeOnSurface;
		this.smallTextSizeOnSurface = smallTextSizeOnSurface;
		this.width = width;
		this.height = height;
		GameActivity.pacmanStep = elementSizeOnSurface / 8;
		GameActivity.redAlienStep = elementSizeOnSurface / 12;

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