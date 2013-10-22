package ua.arahorn.arahornspacman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import ua.arahorn.arahornspacman.view.PacmanGLSurfaceView;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

public class GameActivity extends Activity implements SensorEventListener{
	
	public enum GameState {
		GAME_START,
		FIRST_SCREEN,
		SECOND_SCREEN,
		MENU_SCREEN
	}

	private PacmanGLSurfaceView glSurface;
	private GameEngine gameEngine;
	private GameRenderer gameRenderer;
	private SensorManager sensorManager;
	private Sensor accelerometerSensor,magneticFieldSensor;
	private boolean isDefaultOrientationLand = false; 
	
	public static int[][] maskOfMapFile;
	public static int mapWidth;
	public static int mapHeight;
	public static int pacmanStep = 3;
	public static int redAlienStep = 2;
	public static int [] pacmanStartPosition = new int [2];
	public static int [] redAlienStartPosition = new int [2];
	public static GameState gameState = GameState.GAME_START;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loadMap();
		isDefaultOrientationLand = isLandscapeOrientation(this);
		boolean isGameNew = false;
		if(gameEngine == null) {
			gameEngine = GameEngine.getInstanceEngine(this);
			gameState = GameState.GAME_START;
			isGameNew = true;
		}
		gameRenderer = new GameRenderer(this, gameEngine, isGameNew);
		glSurface = new PacmanGLSurfaceView(this, gameEngine);
		glSurface.setRenderer(gameRenderer);
		
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
      
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
      	if(sensors.size() > 0){
      		for (Sensor sensor : sensors) {
      		  	switch(sensor.getType()){
      		       	case Sensor.TYPE_ACCELEROMETER:
      		       		if(accelerometerSensor == null) accelerometerSensor = sensor;
      		        	break;
      		        case Sensor.TYPE_MAGNETIC_FIELD:
      		        	if(magneticFieldSensor == null) magneticFieldSensor = sensor;
      		        	break;
      		        default:
      		        	break;
      		    } 
           	}
      	} 

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
		}
	}
	
	private void loadMap() {
		BufferedReader in = null;
		try {
			
			in = new BufferedReader(new InputStreamReader(getAssets().open("map_level_3_param.txt"), "UTF-8"));

			for (int i = 0;; i++) {
				String line = in.readLine();
				if (line == null)
					break;
				if (i == 0) {
					mapHeight = Integer.parseInt(line);
				} else if (i == 1) {
					mapWidth = Integer.parseInt(line);
				} else if (i == 2) {
					pacmanStartPosition[0] = Integer.parseInt(line);
				} else if (i == 3) {
					pacmanStartPosition[1] = Integer.parseInt(line);
				} else if (i == 4) {
					redAlienStartPosition[0] = Integer.parseInt(line);
				} else if (i == 5) {
					redAlienStartPosition[1] = Integer.parseInt(line);
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
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		}
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_GAME);
		glSurface.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurface.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			gameEngine.checkContains(event.getX(), event.getY());
		}
//		if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			gameEngine.checkMove(event.getX(), event.getY());
//		}
//		if (event.getAction() == MotionEvent.ACTION_UP) {
//			gameEngine.stopMoving();
//		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onBackPressed() {
		if(!gameState.equals(GameState.MENU_SCREEN) && !gameState.equals(GameState.GAME_START))
			gameState = GameState.MENU_SCREEN;
		else
			super.onBackPressed();
	}
	
	public static int getDeviceDefaultOrientation(Activity activity) {

		WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);

		Configuration config = activity.getResources().getConfiguration();

		int rotation = windowManager.getDefaultDisplay().getRotation();

		if (((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) && config.orientation == Configuration.ORIENTATION_LANDSCAPE)
				|| ((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) && config.orientation == Configuration.ORIENTATION_PORTRAIT)) {
			return Configuration.ORIENTATION_LANDSCAPE;
		} else
			return Configuration.ORIENTATION_PORTRAIT;
	}

	public static boolean isLandscapeOrientation(Activity activity) {
		if (getDeviceDefaultOrientation(activity) == Configuration.ORIENTATION_LANDSCAPE)
			return true;
		else 
			return false;
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		float [] values = event.values;
		switch(event.sensor.getType()){
			case Sensor.TYPE_ACCELEROMETER:{
				float x = 0.0f, y = 0.0f;
				if(isDefaultOrientationLand){
					x=-values[0];
					y=-values[1];
				}
				else {
					x = values[1];
					y = -values[0];
				}

				
				gameEngine.checkMove(x, y);
//				float xx=50-(int)(x*5-0.5);
//				float yy=50-(int)(y*5-0.5);
			} break;
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor paramSensor, int paramInt) {
	
	}

}
