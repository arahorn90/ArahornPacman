package ua.arahorn.arahornspacman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import ua.arahorn.arahornspacman.view.PacmanGLSurfaceView;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class GameActivity extends Activity {

	private PacmanGLSurfaceView glSurface;
	private GameEngine gameEngine;
	public static int[][] maskOfMapFile;
	public static int mapWidth;
	public static int mapHeight;
	public static int pacmanStep = 3;
	public static int redAlienStep = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(getAssets().open("map_level_3_size.txt"), "UTF-8"));
			
			for (int i = 0;; i++) {
				String line = in.readLine();
				if (line == null)
					break;
				if(i == 0) {
					mapHeight = Integer.parseInt(line);
				} else if(i == 1){
					mapWidth = Integer.parseInt(line);
				}
			}
			maskOfMapFile = new int[mapHeight][mapWidth];
			in = new BufferedReader(new InputStreamReader(getAssets().open("map_level_3.txt"), "UTF-8"));

			for (int i = 0;; i++) {
				String line = in.readLine();
				if (line == null)
					break;
//				mapHeight = i + 1;
				byte[] lineBytes = line.getBytes();
				for (int j = 0; j < lineBytes.length; j++) {
					maskOfMapFile[i][j] = Integer.parseInt(Character.toString(line.charAt(j)));
				}
//				Log.e("", "pacman_map " + maskOfMapFile[i][0] + maskOfMapFile[i][1] + maskOfMapFile[i][2]
//						+ maskOfMapFile[i][3] + maskOfMapFile[i][4] + maskOfMapFile[i][5] + maskOfMapFile[i][6]
//						+ maskOfMapFile[i][7] + maskOfMapFile[i][8] + maskOfMapFile[i][9] + maskOfMapFile[i][10]
//						+ maskOfMapFile[i][11] + maskOfMapFile[i][12] + maskOfMapFile[i][13] + maskOfMapFile[i][14]
//						+ maskOfMapFile[i][15] + maskOfMapFile[i][16] + maskOfMapFile[i][17] + maskOfMapFile[i][18]
//						+ maskOfMapFile[i][19]);
//				Log.e("", "pacman_map " + line);
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
		gameEngine = GameEngine.getInstanceEngine();
		glSurface = new PacmanGLSurfaceView(this, gameEngine);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		glSurface.setRenderer(new GameRenderer(this, gameEngine));

		setContentView(glSurface);
	}

	@Override
	protected void onResume() {
		super.onResume();
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

}
