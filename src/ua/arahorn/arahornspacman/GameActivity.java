package ua.arahorn.arahornspacman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import ua.arahorn.arahornspacman.view.PacmanGLSurfaceView;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GameActivity extends Activity {
	
	public enum GameState {
		FIRST_SCREEN,
		SECOND_SCREEN,
		MENU_SCREEN
	}

	private PacmanGLSurfaceView glSurface;
	private GameEngine gameEngine;
	private GameRenderer gameRenderer;
	
	public static int[][] maskOfMapFile;
	public static int mapWidth;
	public static int mapHeight;
	public static int pacmanStep = 3;
	public static int redAlienStep = 2;
	public static GameState gameState = GameState.FIRST_SCREEN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loadMap();
		
		boolean isGameNew = false;
		if(gameEngine == null) {
			gameEngine = GameEngine.getInstanceEngine(this);
			gameState = GameState.FIRST_SCREEN;
			isGameNew = true;
		}
		gameRenderer = new GameRenderer(this, gameEngine, isGameNew);
		glSurface = new PacmanGLSurfaceView(this, gameEngine);
		glSurface.setRenderer(gameRenderer);

		setContentView(glSurface);
	}
	
	public void refreshGame() {
		if(glSurface != null) {
			glSurface.stopGameEngine();
			loadMap();
			
			gameEngine = GameEngine.getInstanceEngine(this);
			gameRenderer.gameNew(gameEngine);
			gameState = GameState.FIRST_SCREEN;
			glSurface.setNewGameEngine(gameEngine);
		} else {
			finish();
			Log.e("", "finish()");
		}
	}
	
	private void loadMap() {
		BufferedReader in = null;
		try {
			
			in = new BufferedReader(new InputStreamReader(getAssets().open("map_level_3_size.txt"), "UTF-8"));

			for (int i = 0;; i++) {
				String line = in.readLine();
				if (line == null)
					break;
				if (i == 0) {
					mapHeight = Integer.parseInt(line);
				} else if (i == 1) {
					mapWidth = Integer.parseInt(line);
				}
			}
			maskOfMapFile = new int[mapHeight][mapWidth];
			
			in = new BufferedReader(new InputStreamReader(getAssets().open("map_level_3.txt"), "UTF-8"));

			for (int i = 0;; i++) {
				String line = in.readLine();
				if (line == null)
					break;
				byte[] lineBytes = line.getBytes();
				for (int j = 0; j < lineBytes.length; j++) {
					maskOfMapFile[i][j] = Integer.parseInt(Character.toString(line.charAt(j)));
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		glSurface.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurface.onPause();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			gameEngine.checkContains(event.getX(), event.getY());
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			gameEngine.checkMove(event.getX(), event.getY());
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			gameEngine.stopMoving();
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onBackPressed() {
		if(gameState !=gameState.MENU_SCREEN)
			gameState = GameState.MENU_SCREEN;
		else
			super.onBackPressed();
	}

}
