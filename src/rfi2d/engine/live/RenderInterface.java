package rfi2d.engine.live;

import android.graphics.Canvas;

public interface RenderInterface {
	public void dispose();

	public void update(final float deltaTime);

	public void render(Canvas canvas, final float deltaTime);
}
