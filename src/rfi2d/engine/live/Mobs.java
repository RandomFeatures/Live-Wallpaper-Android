package rfi2d.engine.live;

import rfi2d.engine.live.utils.Const;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import aurelienribon.tweenengine.Tweenable;

public class Mobs extends RenderBase implements Tweenable {

	public float posX = 0;
	public float posY = 0;
	private float m_Size = 1;
	private AniSprite m_Sprite = null;
	private Bitmap m_Image = null;
	private int m_ImageType = 0;
	private boolean m_Visible = true;
	private Rect rect = null;
	private Rect dest = null;
	private Paint m_Paint = null;
	public int width = 0;
	public int height = 0;
	private static final int FADE_MILLISECONDS = 3000; // 3 second fade effect
	private static final int FADE_STEP = 120; // 120ms refresh
	private boolean m_Drawable = true;
	// Calculate our alpha step from our fade parameters
	private static final int ALPHA_STEP = 255 / (FADE_MILLISECONDS / FADE_STEP);
	// Need to keep track of the current alpha value
	private int m_currentAlpha = 255;
	private boolean m_Tinted = false;
	private boolean m_FadingOut = false;
	private boolean m_FadingIn = false;

	public Mobs(LiveBase game, AniSprite sprite) {
		super(game);
		m_Sprite = sprite;
		m_Sprite.play(0);
		m_ImageType = 0;
		height = sprite.height;
		width = sprite.width;
		dest = new Rect(Math.round(posX), Math.round(posY), (int) ((posX + width) * m_Size), (int) ((posY + height) * m_Size));

	}

	public Mobs(LiveBase game, Bitmap bmp, Rect src) {
		super(game);
		m_Image = bmp;
		m_ImageType = 1;
		rect = src;
		height = src.height();
		width = src.width();
		m_Paint = new Paint();
		dest = new Rect(Math.round(posX), Math.round(posY), (int) ((posX + width) * m_Size), (int) ((posY + height) * m_Size));
	}

	public Mobs show() {
		m_Visible = true;
		return this;
	}

	public Mobs hide() {
		m_Visible = false;
		return this;
	}

	public Mobs fadeOut() {
		if (m_ImageType == 0)
			m_Sprite.fadeOut();
		else
			m_FadingOut = true;
		return this;
	}

	public Mobs fadeIn() {
		m_Visible = true;
		if (m_ImageType == 0)
			m_Sprite.fadeIn();
		else
			m_FadingIn = true;
		return this;
	}

	public Mobs SetTint(int tint) {
		m_Drawable = false;
		m_Tinted = true;
		if (m_ImageType == 0)
			m_Sprite.SetTint(tint);
		else {
			m_Paint = new Paint(tint);
			ColorFilter filter = new LightingColorFilter(tint, 1);
			m_Paint.setColorFilter(filter);
		}
		m_Drawable = true;
		return this;
	}

	public Mobs ClearTint() {
		m_Drawable = false;
		m_Tinted = false;
		if (m_ImageType == 0)
			m_Sprite.ClearTint();
		else {
			m_Paint = new Paint();
		}
		m_Drawable = true;
		return this;
	}

	public boolean isTinted() {
		boolean rtn = false;

		if (m_ImageType == 0)
			rtn = m_Sprite.isTinted();
		else
			rtn = m_Tinted;
		return rtn;
	}

	public boolean isVisibe() {
		return m_Visible;
	}

	public Mobs setPos(float x, float y, float z) {
		posX = x;
		posY = y;
		m_Size = z;
		if (dest != null) {
			dest.set(Math.round(posX), Math.round(posY), (int) ((posX + width) * m_Size), (int) ((posY + height) * m_Size));
		} else
			dest = new Rect(Math.round(posX), Math.round(posY), (int) ((posX + width) * m_Size), (int) ((posY + height) * m_Size));
		return this;
	}

	@Override
	public int getTweenValues(int tweenType, float[] returnValues) {
		switch (tweenType) {
		case Const.TWEEN_TYPE_X:
			returnValues[0] = posX;
			return 1;
		case Const.TWEEN_TYPE_Y:
			returnValues[0] = posY;
			return 1;
		case Const.TWEEN_TYPE_Z:
			returnValues[0] = m_Size;
			return 1;
		case Const.TWEEN_TYPE_XY:
			returnValues[0] = posX;
			returnValues[1] = posY;
			return 2;
		case Const.TWEEN_TYPE_XYZ:
			returnValues[0] = posX;
			returnValues[1] = posY;
			returnValues[2] = m_Size;
			return 3;
		default:
			assert false;
			return 0;
		}
	}

	@Override
	public void onTweenUpdated(int tweenType, float[] newValues) {
		switch (tweenType) {
		case Const.TWEEN_TYPE_X:
			posX = newValues[0];
			break;
		case Const.TWEEN_TYPE_Y:
			posY = newValues[1];
			break;
		case Const.TWEEN_TYPE_Z:
			m_Size = newValues[2];
			break;
		case Const.TWEEN_TYPE_XY:
			posX = newValues[0];
			posY = newValues[1];
			break;
		case Const.TWEEN_TYPE_XYZ:
			posX = newValues[0];
			posY = newValues[1];
			m_Size = newValues[2];
			break;
		default:
			assert false;
			break;
		}
		if (dest != null) {
			dest.set(Math.round(posX), Math.round(posY), (int) ((posX + width) * m_Size), (int) ((posY + height) * m_Size));
		} else
			dest = new Rect(Math.round(posX), Math.round(posY), (int) ((posX + width) * m_Size), (int) ((posY + height) * m_Size));

	}

	@Override
	public void dispose() {
		if (m_ImageType == 0) {
			m_Sprite.dispose();
			m_Sprite = null;
		} else {
			m_Image.recycle();
			m_Image = null;
		}
	}

	@Override
	public void render(Canvas canvas, float deltaTime) {
		if (m_Visible && m_Drawable) {
			if (m_ImageType == 0)
				m_Sprite.draw(canvas, dest);
			else {

				canvas.drawBitmap(m_Image, rect, dest, m_Paint);

			}
		}
	}

	@Override
	public void update(float deltaTime) {
		if (m_currentAlpha < 0 && m_FadingOut) {
			m_FadingOut = false;
			m_Visible = false;
			m_currentAlpha = 255;
		} else if (m_currentAlpha > 255 && m_FadingIn) {
			m_FadingIn = false;
			m_currentAlpha = 255;
		} else if (m_currentAlpha > 0 && m_FadingOut) {
			m_Paint.setAlpha(m_currentAlpha);
			m_currentAlpha -= ALPHA_STEP;
		} else if (m_currentAlpha < 255 && m_FadingIn) {
			m_Paint.setAlpha(m_currentAlpha);
			m_currentAlpha += ALPHA_STEP;
		}

		if (m_Visible) {
			if (m_ImageType == 0)
				m_Sprite.update(deltaTime);
		}
	}

}
