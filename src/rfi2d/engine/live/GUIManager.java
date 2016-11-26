package rfi2d.engine.live;

import rfi2d.engine.live.utils.Array;

public class GUIManager extends InputAdapter {

	private final Array<ControlBase> ControlList;
	private boolean m_AcceptInput = true;
	protected final LiveBase m_Game;

	public GUIManager(final LiveBase game) {
		ControlList = new Array<ControlBase>();
		m_Game = game;
	}

	public GUIManager AddControl(ControlBase control) {
		ControlList.add(control);
		return this;
	}

	public void dispose() {
		for (ControlBase control : ControlList) {
			control.dispose();
		}
		ControlList.clear();
	}

	public void render(float deltaTime) {
		for (ControlBase control : ControlList) {
			if (control.m_Visible)
				control.render(deltaTime);
		}
	}

	public void update(float deltaTime) {
		for (ControlBase control : ControlList) {
			if (control.m_Enabled && control.m_Visible)
				control.update(deltaTime);
		}
	}

	public void EnableAll() {
		m_AcceptInput = true;
	}

	public void DisableAll() {
		m_AcceptInput = false;
	}

	@Override
	public boolean keyDown(int keycode) {

		if (m_AcceptInput)
			for (ControlBase control : ControlList) {
				if (control.m_Enabled && control.m_Visible)
					if (control.keyDown(keycode))
						return true;
			}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		if (m_AcceptInput)
			for (ControlBase control : ControlList) {
				if (control.m_Enabled && control.m_Visible)
					if (control.keyTyped(character))
						return true;
			}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (m_AcceptInput)
			for (ControlBase control : ControlList) {
				if (control.m_Enabled && control.m_Visible)
					if (control.keyUp(keycode))
						return true;
			}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if (m_AcceptInput)
			for (ControlBase control : ControlList) {
				if (control.m_Enabled && control.m_Visible)
					if (control.scrolled(amount))
						return true;
			}
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (m_AcceptInput) {
			int _X = x - m_Game.ScreenX;
			int _Y = y - m_Game.ScreenTopY;
			for (ControlBase control : ControlList) {
				if (control.m_Enabled && control.m_Visible)
					if (control.touchDown(_X, _Y, pointer, button))
						return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if (m_AcceptInput) {
			int _X = x - m_Game.ScreenX;
			int _Y = y - m_Game.ScreenTopY;
			for (ControlBase control : ControlList) {
				if (control.m_Enabled && control.m_Visible)
					if (control.touchDragged(_X, _Y, pointer))
						return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		if (m_AcceptInput) {
			int _X = x - m_Game.ScreenX;
			int _Y = y - m_Game.ScreenTopY;
			for (ControlBase control : ControlList) {
				if (control.m_Enabled && control.m_Visible)
					if (control.touchMoved(_X, _Y))
						return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (m_AcceptInput) {
			int _X = x - m_Game.ScreenX;
			int _Y = y - m_Game.ScreenTopY;
			for (ControlBase control : ControlList) {
				if (control.m_Enabled && control.m_Visible)
					if (control.touchUp(_X, _Y, pointer, button))
						return true;
			}
		}
		return false;
	}

}
