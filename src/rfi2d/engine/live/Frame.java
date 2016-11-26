package rfi2d.engine.live;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;

public class Frame {
	public int FrameWidth;
	public int FrameHeight;
	private Bitmap m_Image;
	private boolean m_Visible = true;
	private Rect m_Scr;
	private Paint m_Paint = null;

	public Frame(Bitmap theBitmap, int Width, int Height) {
		this.m_Image = theBitmap;
		this.FrameHeight = Height;
		this.FrameWidth = Width;

		m_Scr = new Rect(0, 0, Width, Height);
	}

	public Frame SetTint(int tint) {
		m_Paint = new Paint(tint);
		ColorFilter filter = new LightingColorFilter(tint, 1);
		m_Paint.setColorFilter(filter);

		return this;
	}

	public Frame ClearTint() {
		m_Paint = null;
		return this;
	}

	public boolean isTinted() {
		return m_Paint != null;
	}

	public void dispose() {
		this.m_Image.recycle();
		this.m_Image = null;

	}

	public void draw(Canvas canvas, int x, int y) {
		if (m_Visible) {

			canvas.drawBitmap(m_Image, x, y, m_Paint);
		}

	}

	public void draw(Canvas canvas, int x, int y, int width, int height) {
		if (m_Visible) {
			Rect dest = new Rect(x, y, x + width, y + height);
			canvas.drawBitmap(m_Image, m_Scr, dest, m_Paint);
		}

	}

	public void update(float deltaTime) {
		// TODO Auto-generated method stub

	}
}
