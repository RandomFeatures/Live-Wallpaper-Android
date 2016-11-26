package rfi2d.engine.live;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface LiveInterface {
	public void dispose();

	public void init();

	public void render(Canvas canvas);

	public void surfacechange(int width, int height);

	public void touch(MotionEvent event);

	public void pause();

	public void resume();
}
