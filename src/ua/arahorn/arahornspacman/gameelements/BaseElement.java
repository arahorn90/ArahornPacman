package ua.arahorn.arahornspacman.gameelements;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.RectF;

public class BaseElement {

	protected RectF mRect;

	protected int mTexture[];

	protected float x;
	protected float y;
	protected int elementSize;
	protected int textureNumber;
	protected int sizeInSurface;

	private FloatBuffer vertexBuffer;
	private float vertices[] = { 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f };

	protected BaseElement(int[] mTexture, int textureNumber, int sizeInSurface, int elementSize) {

		this.mTexture = mTexture;
		this.elementSize = elementSize;
		this.textureNumber = textureNumber;
		this.sizeInSurface = sizeInSurface;

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}

	protected void draw(GL10 gl, int positionX, int positionY, int numberInAtlas, int rotate) {
		// gl.glMatrixMode(GL10.GL_MODELVIEW);
		// gl.glLoadIdentity();
		// gl.glTranslatef(100.0f, 0.0f, 0.0f);
		gl.glLoadIdentity();

		if (rotate == 0) {
			gl.glTranslatef(positionX, positionY, 0f);
		} else {

			gl.glTranslatef(sizeInSurface / 2, sizeInSurface / 2, 0.0f);
			gl.glTranslatef(positionX, positionY, 0.0f);
			gl.glRotatef(rotate, 0.0f, 0.0f, 1.0f);
			gl.glTranslatef(-sizeInSurface / 2, -sizeInSurface / 2, 0.0f);

		}

		gl.glScalef(sizeInSurface, sizeInSurface, 0);

		float texture[] = {
				// Mapping coordinates for the vertices
				(elementSize * (numberInAtlas % 8) + 2) / 512f,
				(elementSize * (numberInAtlas / 8) + elementSize - 2) / 512f, // top left (V2)
				(elementSize * (numberInAtlas % 8) + 2) / 512f,
				(elementSize * (numberInAtlas / 8) + 2) / 512f, // bottom left (V1)
				(elementSize * (numberInAtlas % 8) + elementSize - 2) / 512f,
				(elementSize * (numberInAtlas / 8) + elementSize - 2) / 512f, // top right (V4)
				(elementSize * (numberInAtlas % 8) + elementSize - 2) / 512f, (elementSize * (numberInAtlas / 8) + 2) / 512f // bottom
																																// right
																																// (V3)
		};

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		FloatBuffer textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[0]);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glFrontFace(GL10.GL_CW);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	public RectF getRect() {
		return mRect;
	}

	public void setRect(float left, float top, float right, float bottom) {
		if (mRect == null)
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

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

}