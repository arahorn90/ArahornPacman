package ua.arahorn.arahornspacman.gameelements;

import javax.microedition.khronos.opengles.GL10;

import ua.arahorn.arahornspacman.GameActivity;
import ua.arahorn.arahornspacman.utils.Constants;

public class PacmanElement extends BaseElement {

	private enum PacmanRotation {
		RIGHT, LEFT, TOP, BOTTOM
	}

	public enum PacmanMove {
		MOVE, STOP
	}

	private long startTime = 0;
	private PacmanRotation rotation = PacmanRotation.LEFT;
	private PacmanMove move = PacmanMove.STOP;
	private int width;
	private int height;

	private int[] pacmanPosition;
	private int[] pacmanSurfaceCoordinate = { 0, 0 };

	public PacmanElement(int[] mTexture, int elementSize, int sizeInSurface, long startTime, int width, int height) {
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
		super.draw(gl, pacmanSurfaceCoordinate[1], pacmanSurfaceCoordinate[0] - sizeInSurface, numberInAtlas + n,
				getRotationByEnum());
	}

	private int getRotationByEnum() {
		switch (this.rotation) {
		case LEFT:
			return Constants.ROTATION_0;
		case BOTTOM:
			return Constants.ROTATION_90;
		case RIGHT:
			return Constants.ROTATION_180;
		case TOP:
			return Constants.ROTATION_270;

		default:
			return Constants.ROTATION_0;
		}
	}

	public void updatePacman(float controlX, float controlY) {
		int startX = sizeInSurface * pacmanPosition[1];
		int startY = height - sizeInSurface * pacmanPosition[0];

		for (int i = 0; i < GameActivity.mapHeight; i++) {
			for (int j = 0; j < GameActivity.mapWidth; j++) {
				if (GameActivity.maskOfMapFile[i][j] == 2 && i == pacmanPosition[0] && j == pacmanPosition[1]) {
					GameActivity.maskOfMapFile[i][j] = 1;
				}
			}
		}

		calcPacmanMove(controlX, controlY);

		switch (getMove()) {
		case MOVE:
			pacmanMove(startX, startY);
			break;
		case STOP:
			pacmanStop(startX, startY);
			break;
		default:
			break;
		}
	}

	private void pacmanMove(int startX, int startY) {
		boolean nextSquareMove = true;
		switch (getRotation()) {
		case LEFT:

			if (pacmanSurfaceCoordinate[1] - GameActivity.pacmanStep < startX) {
				if (GameActivity.maskOfMapFile[pacmanPosition[0]][pacmanPosition[1] - 1] != 0) {
					pacmanPosition[1]--;
				} else {
					nextSquareMove = false;
				}
			}
			if (nextSquareMove) {
				pacmanSurfaceCoordinate[1] -= GameActivity.pacmanStep;
			}
			break;
		case BOTTOM:
			if (pacmanSurfaceCoordinate[0] - GameActivity.pacmanStep < startY) {
				if (GameActivity.maskOfMapFile[pacmanPosition[0] + 1][pacmanPosition[1]] != 0) {
					pacmanPosition[0]++;
				} else {
					nextSquareMove = false;
				}
			}
			if (nextSquareMove) {
				pacmanSurfaceCoordinate[0] -= GameActivity.pacmanStep;
			}
			break;
		case RIGHT:
			if (pacmanSurfaceCoordinate[1] + GameActivity.pacmanStep > startX) {
				if (GameActivity.maskOfMapFile[pacmanPosition[0]][pacmanPosition[1] + 1] != 0) {
					pacmanPosition[1]++;
				} else {
					nextSquareMove = false;
				}
			}
			if (nextSquareMove) {
				pacmanSurfaceCoordinate[1] += GameActivity.pacmanStep;
			}
			break;
		case TOP:
			if (pacmanSurfaceCoordinate[0] + GameActivity.pacmanStep > startY) {
				if (GameActivity.maskOfMapFile[pacmanPosition[0] - 1][pacmanPosition[1]] != 0) {
					pacmanPosition[0]--;
				} else {
					nextSquareMove = false;
				}
			}
			if (nextSquareMove) {
				pacmanSurfaceCoordinate[0] += GameActivity.pacmanStep;
			}
			break;

		default:
			break;
		}

	}

	private void pacmanStop(int startX, int startY) {
		switch (getRotation()) {
		case LEFT:
			if (pacmanSurfaceCoordinate[1] > startX) {
				if (pacmanSurfaceCoordinate[1] < startX + GameActivity.pacmanStep) {
					pacmanSurfaceCoordinate[1] -= pacmanSurfaceCoordinate[1] - startX;
				} else {
					pacmanSurfaceCoordinate[1] -= GameActivity.pacmanStep;
				}
			}
			break;
		case BOTTOM:
			if (pacmanSurfaceCoordinate[0] > startY) {
				if (pacmanSurfaceCoordinate[0] < startY + GameActivity.pacmanStep) {
					pacmanSurfaceCoordinate[0] -= pacmanSurfaceCoordinate[0] - startY;
				} else {
					pacmanSurfaceCoordinate[0] -= GameActivity.pacmanStep;
				}
			}
			break;
		case RIGHT:
			if (pacmanSurfaceCoordinate[1] < startX) {
				if (pacmanSurfaceCoordinate[1] > startX - GameActivity.pacmanStep) {
					pacmanSurfaceCoordinate[1] += startX - pacmanSurfaceCoordinate[1];
				} else {
					pacmanSurfaceCoordinate[1] += GameActivity.pacmanStep;
				}
			}
			break;
		case TOP:
			if (pacmanSurfaceCoordinate[0] < startY) {
				if (pacmanSurfaceCoordinate[0] > startY - GameActivity.pacmanStep) {
					pacmanSurfaceCoordinate[0] += startY - pacmanSurfaceCoordinate[0];
				} else {
					pacmanSurfaceCoordinate[0] += GameActivity.pacmanStep;
				}
			}
			break;

		default:
			break;
		}
	}

	private void calcPacmanMove(float controlX, float controlY) {
		if (isMovingPacman()) {
			return;
		}

		float offsetX = controlX - (width - 100 - Constants.CONTROL_SIZE / 2);
		float offsetY = controlY - (100 - Constants.CONTROL_SIZE / 2);
		if (Math.abs(offsetX) > Math.abs(offsetY)) {
			if (offsetX > 0) {
				setRotation(PacmanRotation.RIGHT);
				setMove(PacmanMove.MOVE);
			} else if (offsetX < 0) {
				setRotation(PacmanRotation.LEFT);
				setMove(PacmanMove.MOVE);
			} else {
				setMove(PacmanMove.STOP);
			}
		} else {
			if (offsetY > 0) {
				setRotation(PacmanRotation.TOP);
				setMove(PacmanMove.MOVE);
			} else if (offsetY < 0) {
				setRotation(PacmanRotation.BOTTOM);
				setMove(PacmanMove.MOVE);
			} else {
				setMove(PacmanMove.STOP);
			}
		}

	}

	private boolean isMovingPacman() {
		int startX = sizeInSurface * pacmanPosition[1];
		int startY = height - sizeInSurface * pacmanPosition[0];
		switch (getRotation()) {
		case LEFT:
			if (pacmanSurfaceCoordinate[1] > startX) {
				setMove(PacmanMove.STOP);
				return true;
			}
			break;
		case BOTTOM:
			if (pacmanSurfaceCoordinate[0] > startY) {
				setMove(PacmanMove.STOP);
				return true;
			}
			break;
		case RIGHT:
			if (pacmanSurfaceCoordinate[1] < startX) {
				setMove(PacmanMove.STOP);
				return true;
			}
			break;
		case TOP:
			if (pacmanSurfaceCoordinate[0] < startY) {
				setMove(PacmanMove.STOP);
				return true;
			}
			break;

		default:
			break;
		}
		return false;
	}

	public void setPacmanPosition(int[] pacmanPosition) {
		this.pacmanPosition = pacmanPosition;
	}

	public int[] getPacmanPosition() {
		return pacmanPosition;
	}

	public void setPacmanSurfaceCoordinateX(int setPacmanSurfaceCoordinateX) {
		this.pacmanSurfaceCoordinate[1] = setPacmanSurfaceCoordinateX;
	}

	public void setPacmanSurfaceCoordinateY(int setPacmanSurfaceCoordinateY) {
		this.pacmanSurfaceCoordinate[0] = setPacmanSurfaceCoordinateY;
	}

	public void setRotation(PacmanRotation rotation) {
		this.rotation = rotation;
	}

	public PacmanRotation getRotation() {
		return rotation;
	}

	public void setMove(PacmanMove move) {
		this.move = move;
	}

	public PacmanMove getMove() {
		return move;
	}

}