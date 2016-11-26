package rfi2d.engine.live;

import rfi2d.engine.live.InputAdapter;

import rfi2d.engine.live.LiveBase;
import rfi2d.engine.live.math.Rectangle;

public abstract class ControlBase extends InputAdapter {

	protected final LiveBase m_Game;
	protected final Rectangle m_Rect;
	protected float m_X;
	protected float m_Y;
	protected float m_Width;
	protected float m_Height;
	protected boolean m_Enabled = true;
	protected boolean m_Visible = true;

	public boolean isEnabled() {
		return m_Enabled;
	}

	public void setEnabled(boolean value) {
		m_Enabled = value;
	}

	public boolean isVisible() {
		return m_Visible;
	}

	public void setVisible(boolean value) {
		m_Visible = value;
	}

	public float getX() {
		return m_X;
	}

	public void setX(float value) {
		m_X = m_Game.ScreenX + value;
		m_Rect.setX(value);
	}

	public float getY() {
		return m_Y;
	}

	public void setY(float value) {
		m_Y = m_Game.ScreenY + value;
		m_Rect.setY(value);
	}

	public float getWidth() {
		return m_Width;
	}

	public void setWidth(float value) {
		// m_Game.viewportX
		m_Width = value;
		m_Rect.setWidth(value);
	}

	public float getHeight() {
		return m_Height;
	}

	public void setHeight(float value) {
		m_Height = value;
		m_Rect.setHeight(value);
	}

	public Rectangle getRect() {
		return m_Rect;
	}

	public ControlBase(final LiveBase game, Rectangle rect) {
		m_Game = game;
		m_Rect = rect;
		m_X = rect.x;
		m_Y = rect.y;
		m_Width = rect.width;
		m_Height = rect.height;
	}

	public abstract void dispose();

	public abstract void render(float deltaTime);

	public abstract void update(float deltaTime);
}
