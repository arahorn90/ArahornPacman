package ua.arahorn.arahornspacman.gameelements;

import javax.microedition.khronos.opengles.GL10;

import ua.arahorn.arahornspacman.utils.Constants;

public class WallElement extends BaseElement {

	public WallElement(int[] mTexture, int elementSize, int sizeInSurface) {
		super(mTexture, mTexture[Constants.TEXTURE_NUMBER_MAIN_ATLAS], elementSize, sizeInSurface);
	}

	public void draw(GL10 gl, int positionX, int positionY, int numberInAtlas) {
		super.draw(gl, positionX, positionY, numberInAtlas, Constants.ROTATION_0);
	}

}