package ua.arahorn.arahornspacman;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import ua.arahorn.arahornspacman.gameelements.BackgroundElement;
import ua.arahorn.arahornspacman.gameelements.PacmanControl;
import ua.arahorn.arahornspacman.gameelements.PacmanElement;
import ua.arahorn.arahornspacman.gameelements.RedAlien;
import ua.arahorn.arahornspacman.gameelements.TextElement;
import ua.arahorn.arahornspacman.gameelements.WallElement;
import ua.arahorn.arahornspacman.utils.Constants;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

public class GameRenderer implements Renderer {
	

	// NEW VALUES
	private int mainAtlasTexture [] = new int[1];	
	private int enemyAtlasTexture [] = new int[1];	
	private Context mContext;
	private int elementSizeOnSurface;
	private int width;
	private int height;
	private GameEngine gameEngine;

	public GameRenderer(Context context, GameEngine gameEngine) {
	
		mContext = context;
		this.gameEngine = gameEngine;
		
	}
	
	public void loadGLTexture(GL10 gl, Context context) {

		AssetManager assetManager = mContext.getAssets();
		InputStream is = null;
		
		try {
			is = assetManager.open("atlas_text_game.png");
		
			Bitmap bit=BitmapFactory.decodeStream(is);

			gl.glGenTextures(1, mainAtlasTexture, 0);

			gl.glBindTexture(GL11.GL_TEXTURE_2D, mainAtlasTexture[0]);

			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST);
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
					GL11.GL_LINEAR);

			GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, bit, 0);
			bit.recycle();
			is.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		is = null;
		
		try {
			is = assetManager.open("atlas_game_enemy.png");
			
			Bitmap bit=BitmapFactory.decodeStream(is);
			
			gl.glGenTextures(1, enemyAtlasTexture, 0);
			
			gl.glBindTexture(GL11.GL_TEXTURE_2D, enemyAtlasTexture[0]);
			
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST);
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
					GL11.GL_LINEAR);
			
			GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, bit, 0);
			bit.recycle();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		loadGLTexture(gl, mContext);
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			display.getSize(size);
			elementSizeOnSurface = size.y / GameActivity.mapHeight;
			height = size.y;
			width = size.x;
			
	    }
		else {
			elementSizeOnSurface = display.getHeight();
			height = display.getHeight();
			width = display.getWidth();
		}
		gameEngine.setSmallTextElement(new TextElement(mainAtlasTexture, elementSizeOnSurface, Constants.ELEMENT_SIZE_IN_ATLAS));
		gameEngine.setBackgroundElement(new BackgroundElement(mainAtlasTexture, elementSizeOnSurface, Constants.ELEMENT_SIZE_IN_ATLAS));
		gameEngine.setWallElement(new WallElement(mainAtlasTexture, elementSizeOnSurface, Constants.ELEMENT_SIZE_IN_ATLAS));
		gameEngine.setPacmanElement(new PacmanElement(mainAtlasTexture, elementSizeOnSurface, Constants.ELEMENT_SIZE_IN_ATLAS, System.currentTimeMillis(), width, height));
		gameEngine.setPacmanControl(new PacmanControl(Constants.CONTROL_SIZE, width - 100 - Constants.CONTROL_SIZE/2, 100 - Constants.CONTROL_SIZE/2));
		gameEngine.setRedAlien(new RedAlien(enemyAtlasTexture, elementSizeOnSurface, Constants.ELEMENT_SIZE_IN_ATLAS, System.currentTimeMillis(), width, height));
				
		gameEngine.setAttributes(elementSizeOnSurface, width, height);
		
		gameEngine.getPacmanElement().setPacmanPosition(new int [] {1, 1});
		gameEngine.getPacmanElement().setPacmanSurfaceCoordinateX(elementSizeOnSurface * (int)(gameEngine.getPacmanElement().getPacmanPosition()[1]));
		gameEngine.getPacmanElement().setPacmanSurfaceCoordinateY(height - elementSizeOnSurface * (gameEngine.getPacmanElement().getPacmanPosition()[0]));
		
		gameEngine.getPacmanControl().setRect(width - 100 - Constants.CONTROL_SIZE/2, 100 - Constants.CONTROL_SIZE/2, width - 100 + Constants.CONTROL_SIZE/2, 100 + Constants.CONTROL_SIZE/2);
		
		gameEngine.getRedAlien().redAlienPosition = new int [] {6, 10};
		gameEngine.getRedAlien().redAlienSurfaceCoordinate[1] = elementSizeOnSurface * (int)(gameEngine.getRedAlien().redAlienPosition[1]);
		gameEngine.getRedAlien().redAlienSurfaceCoordinate[0] = height - elementSizeOnSurface * (gameEngine.getRedAlien().redAlienPosition[0]);
		
		gl.glEnable(GL11.GL_TEXTURE_2D);
		gl.glShadeModel(GL11.GL_SMOOTH); 			
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); 	
		gl.glClearDepthf(1.0f); 					
		gl.glEnable(GL11.GL_DEPTH_TEST); 			
		gl.glDepthFunc(GL11.GL_LEQUAL); 			
		
		gl.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); 
		
		
	}

	float angle;
	public void onDrawFrame(GL10 gl) {

		gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
		gl.glEnable(GL11.GL_BLEND);
		gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//		gl.glLoadIdentity();	
		for(int i = 0; i < GameActivity.mapHeight; i++) {
			int y = (int) (height - elementSizeOnSurface * (i + 1));
			for(int j = 0; j < GameActivity.mapWidth; j++) {
				int x = (int) (elementSizeOnSurface * j);
				if(GameActivity.maskOfMapFile[i][j] == 2) {
					gameEngine.getBackgroundElement().setCoin(true);
					gameEngine.getBackgroundElement().draw(gl, x, y);
				} else if(GameActivity.maskOfMapFile[i][j] == 1) {
					gameEngine.getBackgroundElement().setCoin(false);
					gameEngine.getBackgroundElement().draw(gl, x, y);
				}
				else if(GameActivity.maskOfMapFile[i][j] == 0)
					gameEngine.getWallElement().draw(gl, x, y, Constants.ELEMENT_WALL_NUMBER);
			}
		}

		gameEngine.getPacmanElement().draw(gl, Constants.ELEMENT_PACMAN_NUMBER);
		
		gameEngine.getRedAlien().draw(gl, Constants.ELEMENT_RED_ALIEN_NUMBER);

		gameEngine.getPacmanControl().draw(gl);
		
	}	
	
//	private String getTime(long timeStartGame) {
//		int allTime = ((int) (System.currentTimeMillis() - timeStartGame) / 1000);
//		int seconds = allTime % 60;
//		int minutes = allTime / 60;
//		StringBuilder stringBuilder = new StringBuilder((String) (minutes > 0 ? (minutes > 9 ? String.valueOf(minutes) : "0" + minutes) : "00"));
//		stringBuilder.append(":").append((String) (seconds > 0 ? (seconds > 9 ? String.valueOf(seconds) : "0" + seconds) : "00"));
//		return stringBuilder.toString();
//	}
	
//	private void drawText(GL10 gl, StringBuilder stringBuilder, int positionX, int positinY, int size){
//
//		for(int i = 0; i < stringBuilder.length() ; i++){
//			char symbol = stringBuilder.charAt(i);
//			if((int)symbol > 47 && (int)symbol < 91)
//				smallTextElement.draw(gl, positionX + 80 * (i), positinY, (int)symbol - 48);
//		}
//	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 	
			height = 1; 
		}

		gl.glViewport(0, 0, width, height); 
		gl.glMatrixMode(GL10.GL_PROJECTION); 
		gl.glLoadIdentity(); 

		gl.glOrthof(0, width, 0, height, 1.0f, -1.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 
		gl.glLoadIdentity(); 	
	
	}

}