package ua.arahorn.arahornspacman.gameelements;

import javax.microedition.khronos.opengles.GL10;

import ua.arahorn.arahornspacman.GameActivity;
import ua.arahorn.arahornspacman.utils.Constants;
import ua.arahorn.arahornspacman.utils.PathFinder;
import ua.arahorn.arahornspacman.utils.PathFinder.Point;

public class RedAlien extends BaseElement {

	public enum RedAlienRotation {
		RIGHT, LEFT, TOP, BOTTOM
	}

	public enum RedAlienMove {
		MOVE, STOP
	}

	private long startTime = 0;
	private RedAlienRotation rotation = RedAlienRotation.LEFT;
	private RedAlienMove move = RedAlienMove.STOP;
	private int width;
	private int height;

	private int[] redAlienPosition;
	private int[] redAlienSurfaceCoordinate = { 0, 0 };

	public RedAlien(int[] mTexture, int elementSize, int sizeInSurface, long startTime, int width, int height) {
		super(mTexture, mTexture[Constants.TEXTURE_NUMBER_MAIN_ATLAS], elementSize, sizeInSurface);
		this.startTime = startTime;
		this.width = width;
		this.height = height;
	}

	public void draw(GL10 gl, int numberInAtlas) {
		long time = (System.currentTimeMillis() - startTime) % 1000;
		int n = 0;
		if (time >= 0 && time <= 125)
			n = 0;
		else if (time > 125 && time <= 250)
			n = 1;
		else if (time > 250 && time <= 375)
			n = 2;
		else if (time > 375 && time <= 500)
			n = 3;
		else if (time > 500 && time <= 625)
			n = 4;
		else if (time > 625 && time <= 750)
			n = 5;
		else if (time > 750 && time <= 875)
			n = 6;
		else if (time > 875 && time <= 1000)
			n = 7;
		super.draw(gl, redAlienSurfaceCoordinate[1], redAlienSurfaceCoordinate[0] - sizeInSurface, numberInAtlas + n,
				Constants.ROTATION_0);
	}

	public void updateRedAlien(int pacmanPositionX, int pacmanPostionY) {
		int startX = sizeInSurface * redAlienPosition[1];
		int startY = height - sizeInSurface * redAlienPosition[0];

		calcRedAlienMove(pacmanPositionX, pacmanPostionY);

		switch (getMove()) {
		case MOVE:
			redAlienMove(startX, startY);
			break;
		case STOP:
			redAlienStop(startX, startY);
			break;
		default:
			break;
		}
	}

	private void redAlienMove(int startX, int startY) {
		boolean nextSquareMove = true;
		switch (getRotation()) {
		case LEFT:

			if (redAlienSurfaceCoordinate[1] - GameActivity.redAlienStep < startX) {
				if (GameActivity.maskOfMapFile[redAlienPosition[0]][redAlienPosition[1] - 1] != 0) {
					redAlienPosition[1]--;
				} else {
					nextSquareMove = false;
				}
			}
			if (nextSquareMove) {
				redAlienSurfaceCoordinate[1] -= GameActivity.redAlienStep;
			}
			break;
		case BOTTOM:
			if (redAlienSurfaceCoordinate[0] - GameActivity.redAlienStep < startY) {
				if (GameActivity.maskOfMapFile[redAlienPosition[0] + 1][redAlienPosition[1]] != 0) {
					redAlienPosition[0]++;
				} else {
					nextSquareMove = false;
				}
			}
			if (nextSquareMove) {
				redAlienSurfaceCoordinate[0] -= GameActivity.redAlienStep;
			}
			break;
		case RIGHT:
			if (redAlienSurfaceCoordinate[1] + GameActivity.redAlienStep > startX) {
				if (GameActivity.maskOfMapFile[redAlienPosition[0]][redAlienPosition[1] + 1] != 0) {
					redAlienPosition[1]++;
				} else {
					nextSquareMove = false;
				}
			}
			if (nextSquareMove) {
				redAlienSurfaceCoordinate[1] += GameActivity.redAlienStep;
			}
			break;
		case TOP:
			if (redAlienSurfaceCoordinate[0] + GameActivity.redAlienStep > startY) {
				if (GameActivity.maskOfMapFile[redAlienPosition[0] - 1][redAlienPosition[1]] != 0) {
					redAlienPosition[0]--;
				} else {
					nextSquareMove = false;
				}
			}
			if (nextSquareMove) {
				redAlienSurfaceCoordinate[0] += GameActivity.redAlienStep;
			}
			break;

		default:
			break;
		}

	}

	private void redAlienStop(int startX, int startY) {
		switch (getRotation()) {
		case LEFT:
			if (redAlienSurfaceCoordinate[1] > startX) {
				if (redAlienSurfaceCoordinate[1] < startX + GameActivity.redAlienStep) {
					redAlienSurfaceCoordinate[1] -= redAlienSurfaceCoordinate[1] - startX;
				} else {
					redAlienSurfaceCoordinate[1] -= GameActivity.redAlienStep;
				}
			}
			break;
		case BOTTOM:
			if (redAlienSurfaceCoordinate[0] > startY) {
				if (redAlienSurfaceCoordinate[0] < startY + GameActivity.redAlienStep) {
					redAlienSurfaceCoordinate[0] -= redAlienSurfaceCoordinate[0] - startY;
				} else {
					redAlienSurfaceCoordinate[0] -= GameActivity.redAlienStep;
				}
			}
			break;
		case RIGHT:
			if (redAlienSurfaceCoordinate[1] < startX) {
				if (redAlienSurfaceCoordinate[1] > startX - GameActivity.redAlienStep) {
					redAlienSurfaceCoordinate[1] += startX - redAlienSurfaceCoordinate[1];
				} else {
					redAlienSurfaceCoordinate[1] += GameActivity.redAlienStep;
				}
			}
			break;
		case TOP:
			if (redAlienSurfaceCoordinate[0] < startY) {
				if (redAlienSurfaceCoordinate[0] > startY - GameActivity.redAlienStep) {
					redAlienSurfaceCoordinate[0] += startY - redAlienSurfaceCoordinate[0];
				} else {
					redAlienSurfaceCoordinate[0] += GameActivity.redAlienStep;
				}
			}
			break;

		default:
			break;
		}
	}

	private void calcRedAlienMove(int pacmanPositionX, int pacmanPostionY) {
		if (isMovingRedAlien()) {
			return;
		}
		PathFinder pathFinder = new PathFinder(GameActivity.maskOfMapFile);

		Point start = pathFinder.new Point(redAlienPosition[1], redAlienPosition[0]);

		Point end = pathFinder.new Point(pacmanPositionX, pacmanPostionY);

		Point[] path = pathFinder.find(start, end);
		if (path.length > 1) {
			if (path[1].getX() != path[0].getX()) {
				if (path[1].getX() > path[0].getX()) {
					setRotation(RedAlienRotation.RIGHT);
					setMove(RedAlienMove.MOVE);
				} else {
					setRotation(RedAlienRotation.LEFT);
					setMove(RedAlienMove.MOVE);
				}
			} else {
				if (path[1].getY() > path[0].getY()) {
					setRotation(RedAlienRotation.BOTTOM);
					setMove(RedAlienMove.MOVE);
				} else {
					setRotation(RedAlienRotation.TOP);
					setMove(RedAlienMove.MOVE);
				}

			}
		}
	}

	public boolean isMovingRedAlien() {
		int startX = sizeInSurface * redAlienPosition[1];
		int startY = height - sizeInSurface * redAlienPosition[0];
		switch (getRotation()) {
		case LEFT:
			if (redAlienSurfaceCoordinate[1] > startX) {
				setMove(RedAlienMove.STOP);
				return true;
			}
			break;
		case BOTTOM:
			if (redAlienSurfaceCoordinate[0] > startY) {
				setMove(RedAlienMove.STOP);
				return true;
			}
			break;
		case RIGHT:
			if (redAlienSurfaceCoordinate[1] < startX) {
				setMove(RedAlienMove.STOP);
				return true;
			}
			break;
		case TOP:
			if (redAlienSurfaceCoordinate[0] < startY) {
				setMove(RedAlienMove.STOP);
				return true;
			}
			break;

		default:
			break;
		}
		return false;
	}

	public int[] getRedAlienPosition() {
		return redAlienPosition;
	}

	public void setRedAlienPosition(int[] redAlienPosition) {
		this.redAlienPosition = redAlienPosition;
	}

	public int[] getRedAlienSurfaceCoordinate() {
		return redAlienSurfaceCoordinate;
	}

	public void setRedAlienSurfaceCoordinate(int[] redAlienSurfaceCoordinate) {
		this.redAlienSurfaceCoordinate = redAlienSurfaceCoordinate;
	}
	
	public void setRedAlienSurfaceCoordinateX(int setRedAlienSurfaceCoordinateX) {
		this.redAlienSurfaceCoordinate[1] = setRedAlienSurfaceCoordinateX;
	}

	public void setRedAlienSurfaceCoordinateY(int setRedAlienSurfaceCoordinateY) {
		this.redAlienSurfaceCoordinate[0] = setRedAlienSurfaceCoordinateY;
	}

	public void setRotation(RedAlienRotation rotation) {
		this.rotation = rotation;
	}

	public RedAlienRotation getRotation() {
		return rotation;
	}

	public void setMove(RedAlienMove move) {
		this.move = move;
	}

	public RedAlienMove getMove() {
		return move;
	}

}