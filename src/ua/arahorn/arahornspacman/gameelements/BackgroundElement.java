package ua.arahorn.arahornspacman.gameelements;

import javax.microedition.khronos.opengles.GL10;

import ua.arahorn.arahornspacman.utils.Constants;

public class BackgroundElement extends BaseElement{
	
	private CoinElement coinElement;
	private boolean hasCoin = false;
	
	public BackgroundElement(int [] mTexture, int elementSize, int sizeInSurface) {
		super(mTexture, mTexture[Constants.TEXTURE_NUMBER_MAIN_ATLAS], elementSize, sizeInSurface);
		coinElement = new CoinElement(mTexture, elementSize, sizeInSurface);
	}

	public void draw(GL10 gl, int positionX, int positionY) {
		super.draw(gl, positionX, positionY, Constants.ELEMENT_BACKGROUND_NUMBER, Constants.ROTATION_0);
		if(hasCoin) {
			coinElement.draw(gl, positionX, positionY, Constants.ELEMENT_COIN_NUMBER);
		}
	}
	
	class CoinElement extends BaseElement{

		public CoinElement(int [] mTexture, int elementSize, int sizeInSurface) {
			super(mTexture, mTexture[Constants.TEXTURE_NUMBER_MAIN_ATLAS], elementSize, sizeInSurface);
		}

		public void draw(GL10 gl, int positionX, int positionY, int numberInAtlas) {
			super.draw(gl, positionX, positionY, numberInAtlas, Constants.ROTATION_0);
		}
		
	}
	
	public void setCoin(boolean hasCoin) {
		this.hasCoin = hasCoin;
	}


	
}
