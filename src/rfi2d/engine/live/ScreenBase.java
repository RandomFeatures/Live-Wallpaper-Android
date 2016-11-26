package rfi2d.engine.live;

import android.graphics.Canvas;

public abstract class ScreenBase extends InputAdapter {

	protected final LiveBase m_Game;
	protected boolean m_Dying = false;
	protected boolean m_Paused = false;
	protected final GUIManager m_guimanager;
	protected boolean AcceptInput = true;

	@Override
	protected void finalize() throws Throwable {
		try {
			m_guimanager.dispose();
		} finally {
			super.finalize();
		}
	}

	public ScreenBase(final LiveBase game) {
		this.m_Game = game;
		m_guimanager = new GUIManager(game);
		AcceptInput = m_Game.AcceptInput;
		if (!m_Game.AcceptInput)
			m_guimanager.DisableAll();

	}

	public void AddControl(final ControlBase control) {
		m_guimanager.AddControl(control);
	}

	public GUIManager getGUIManager() {
		return m_guimanager;
	}

	public void EnableInput(boolean value) {
		AcceptInput = value;
		if (value)
			m_guimanager.EnableAll();
		else
			m_guimanager.DisableAll();
	}

	public boolean keyDown(int keycode) {
		return m_guimanager.keyDown(keycode);
	}

	public boolean keyTyped(char character) {
		return m_guimanager.keyTyped(character);
	}

	public boolean keyUp(int keycode) {
		return m_guimanager.keyUp(keycode);
	}

	public boolean scrolled(int amount) {
		return m_guimanager.scrolled(amount);
	}

	public boolean touchDown(int x, int y, int pointer, int button) {
		return m_guimanager.touchDown(x, y, pointer, button);
	}

	public boolean touchDragged(int x, int y, int pointer) {
		return m_guimanager.touchDragged(x, y, pointer);
	}

	public boolean touchMoved(int x, int y) {
		return m_guimanager.touchMoved(x, y);
	}

	public boolean touchUp(int x, int y, int pointer, int button) {
		return m_guimanager.touchUp(x, y, pointer, button);
	}

	public abstract void surfaceChange(int w, int h);

	public abstract void init();

	public abstract void update(float deltaTime);

	public abstract void render(Canvas canvas, float deltaTime);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();

	public abstract void loadSettings();

}
