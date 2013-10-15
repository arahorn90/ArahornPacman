package ua.arahorn.arahornspacman.view;

import ua.arahorn.arahornspacman.GameActivity;
import ua.arahorn.arahornspacman.GameActivity.GameState;
import ua.arahorn.arahornspacman.GameEngine;
import ua.arahorn.arahornspacman.utils.Constants;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class PacmanGLSurfaceView extends GLSurfaceView {

	private GameEngine gameEngine;
	private long lastTime = 0;
	private Thread engineThread;
	private boolean isAliveEngine = true;

	public PacmanGLSurfaceView(Context context, GameEngine gameEngine) {
		super(context);
		this.gameEngine = gameEngine;
		engineThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (isAliveEngine) {
					long currentTime = System.currentTimeMillis();
					if (currentTime - lastTime > Constants.DELTA_TIME_UPDATE_ENGINE) {
						PacmanGLSurfaceView.this.gameEngine.updateEngine();
						lastTime = currentTime;
					}
				}

			}
		});
		engineThread.start();
	}

	public PacmanGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
//		gameEngine = new GameEngine(this);
//		engineThread = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				while (true) {
//					long currentTime = System.currentTimeMillis();
//					if (currentTime - lastTime > Constants.DELTA_TIME_UPDATE_ENGINE) {
//						PacmanGLSurfaceView.this.gameEngine.updateEngine();
//						lastTime = currentTime;
//					}
//				}
//
//			}
//		});
//		engineThread.start();
	}

	@Override
	public void onPause() {
		if (engineThread != null && engineThread.isAlive()) {
			GameActivity.gameState = GameState.MENU_SCREEN;
		}
		super.onPause();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		if (engineThread != null && engineThread.isAlive()) {
			isAliveEngine = false;
			engineThread.interrupt();
			engineThread = null;
		}
		super.onDetachedFromWindow();
	}

}