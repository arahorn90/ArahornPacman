package ua.arahorn.arahornspacman.gameelements;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.RectF;

public class PacmanControl {
	
	private RectF mRect;
	private boolean active;

//	protected int mTexture [];	
	
	private float stableX;
	private float stableY;
	private float x;
	private float y;
	private int sizeInSurface;
	
	private FloatBuffer vertexBuffer;
	private float vertices[] = { 
								0.0f, 0.0f, 0.0f, 
								0.0f, 1.0f, 0.0f, 	
								1.0f, 0.0f, 0.0f, 	
								1.0f, 1.0f, 0.0f 	
												};
		
	public PacmanControl(int sizeInSurface, int stableX, int stableY) {

//		this.mTexture = mTexture;
		this.sizeInSurface = sizeInSurface;
		this.stableX = stableX;
		this.stableY = stableY;
		this.x = stableX;
		this.y = stableY;
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}

	public void draw(GL10 gl) {
		
		gl.glLoadIdentity();

		gl.glTranslatef(x, y, 0f);
		
		gl.glScalef(sizeInSurface, sizeInSurface, 0);
		
		
		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	public RectF getRect(){
		return mRect;
	}
	
	public void setRect(float left, float top, float right, float bottom ){
		if(mRect == null)
			mRect = new RectF(left, top, right, bottom);
		else
			mRect.set(left, top, right, bottom);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getStableX() {
		return stableX;
	}
	
	public float getStableY() {
		return stableY;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setControlActive(boolean active) {
		if(!active) {
			x = stableX;
			y = stableY;
		}
		this.active = active;
	}
	
	public boolean getControlActive() {
		return active;
	}
	
}