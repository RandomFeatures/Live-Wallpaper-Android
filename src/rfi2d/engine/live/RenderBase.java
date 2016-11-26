package rfi2d.engine.live;

import rfi2d.engine.live.LiveBase;
import rfi2d.engine.live.RenderInterface;

public abstract class RenderBase implements RenderInterface {

	protected final LiveBase m_Game;

	public RenderBase(final LiveBase game) {
		m_Game = game;
	}

}
