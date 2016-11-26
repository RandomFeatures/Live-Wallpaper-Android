package rfi2d.engine.live;

import rfi2d.engine.live.types.AniType;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;

public class AniSprite {

	private float m_ElapsedTime;
	private Bitmap m_Animation;
	private final Rect[] m_KeyFrames;
	private final float m_FrameDuration;
	private int m_CurrentFrame = -1;
	private AniType m_AniType = AniType.FORWARD;
	private boolean m_Forward = true;
	private boolean m_Playing = false;
	private boolean m_Visible = true;
	private Paint m_Paint = null;
	private boolean m_FadingIn;
	private boolean m_FadingOut;
	private boolean m_Drawable = true;
	private boolean m_Tinted = false;
	public int height;
	public int width;
	private static final int FADE_MILLISECONDS = 3000; // 3 second fade effect
	private static final int FADE_STEP = 120; // 120ms refresh
	// Calculate our alpha step from our fade parameters
	private static final int ALPHA_STEP = 255 / (FADE_MILLISECONDS / FADE_STEP);
	// Need to keep track of the current alpha value
	private int m_currentAlpha = 255;

	/**
	 * Constructor, storing the frame duration and key frames.
	 * 
	 * @param frameDuration
	 *            the time between frames in seconds.
	 * @param keyFrames
	 *            the {@link TextureRegion}s representing the frames.
	 */
	public AniSprite(Bitmap theBitmap, float frameDuration, int Width, int Height, AniType anitype, Rect... keyFrames) {
		this.m_Animation = theBitmap;
		this.m_FrameDuration = frameDuration;
		this.m_KeyFrames = keyFrames;
		this.m_AniType = anitype;
		this.height = Height;
		this.width = Width;
		this.m_Paint = new Paint();
	}

	public AniSprite SetTint(int tint) {
		m_Drawable = false;

		m_Tinted = true;
		m_Paint = new Paint(tint);
		ColorFilter filter = new LightingColorFilter(tint, 1);
		m_Paint.setColorFilter(filter);

		m_Drawable = true;
		return this;
	}

	public AniSprite ClearTint() {
		m_Drawable = false;

		m_Tinted = false;
		this.m_Paint = new Paint();

		m_Drawable = true;
		return this;
	}

	public boolean isTinted() {
		return m_Tinted;
	}

	public void dispose() {
		m_Animation = null;
	}

	public int getCurrentFrame() {
		return m_CurrentFrame;
	}

	// set animation to a specific frame
	public AniSprite setFrame(int value) {
		if (value <= m_KeyFrames.length - 1)
			m_CurrentFrame = value;
		return this;
	}

	// play the animation from a specific frame
	public AniSprite play(int from) {
		m_Playing = true;
		m_Visible = true;
		setFrame(from);
		return this;
	}

	// stop all animaiton
	public AniSprite stop() {
		m_Playing = false;
		return this;
	}

	// Hide the image
	public AniSprite hide() {
		m_Visible = false;
		return this;
	}

	// show the image
	public AniSprite show() {
		m_Visible = false;
		return this;
	}

	// set the animation type
	public AniSprite setAniType(AniType value) {
		this.m_AniType = value;
		return this;
	}

	public void update(float deltaTime) {
		if (!m_Playing)
			return;

		m_ElapsedTime += deltaTime;

		if (m_ElapsedTime >= m_FrameDuration) {
			m_ElapsedTime = 0;
			// Animation type
			switch (m_AniType) {
			case FORWARD:
				if (m_CurrentFrame < m_KeyFrames.length - 1)
					m_CurrentFrame++;
				break;
			case BACK:
				if (m_CurrentFrame > 0)
					m_CurrentFrame--;
				break;
			case LOOP:
				if (m_CurrentFrame == m_KeyFrames.length - 1)
					m_CurrentFrame = 0;
				else
					m_CurrentFrame++;
				break;
			case BACKLOOP:
				if (m_CurrentFrame == 0)
					m_CurrentFrame = m_KeyFrames.length - 1;
				else
					m_CurrentFrame--;
				break;
			case PINGPONG:
				if (m_Forward) {
					if (m_CurrentFrame == m_KeyFrames.length - 1) {
						// at the end of the line so stop and back up
						m_Forward = false;
						m_CurrentFrame--;
					} else
						m_CurrentFrame++;
				} else if (m_CurrentFrame == 0) {
					// at tehe begining so start forward
					m_Forward = true;
					m_CurrentFrame++;
				} else
					m_CurrentFrame--;
				break;

			}

		}

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

	}

	// draw normal image at x,y
	public void draw(Canvas canvas, int x, int y) {
		if (m_CurrentFrame > -1 && m_Visible && m_Drawable) {
			Rect dest = new Rect(x, y, x + width, y + height);
			canvas.drawBitmap(m_Animation, m_KeyFrames[m_CurrentFrame], dest, m_Paint);
		}

	}

	// stretch image starting at x,y
	public void draw(Canvas canvas, int x, int y, int width, int height) {
		if (m_CurrentFrame > -1 && m_Visible && m_Drawable) {
			Rect dest = new Rect(x, y, x + width, y + height);
			canvas.drawBitmap(m_Animation, m_KeyFrames[m_CurrentFrame], dest, m_Paint);
		}
	}

	// define image rect to draw in
	public void draw(Canvas canvas, Rect dest) {
		if (m_CurrentFrame > -1 && m_Visible && m_Drawable)
			canvas.drawBitmap(m_Animation, m_KeyFrames[m_CurrentFrame], dest, m_Paint);

	}

	public AniSprite fadeIn() {
		m_FadingIn = true;
		return this;

	}

	public AniSprite fadeOut() {
		m_FadingOut = true;
		return this;
	}

}
