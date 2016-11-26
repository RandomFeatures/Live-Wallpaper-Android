package rfi2d.engine.live;

import java.util.ArrayList;

import rfi2d.engine.live.math.WindowedMean;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

public abstract class LiveBase implements LiveInterface {

	private ArrayList<ScreenBase> screens;
	public int Width = 480;
	public int Height = 800;

	public int HalfWidth = 240;
	public int HalfHeight = 400;

	public boolean AcceptInput = true;
	public ScreenBase CurrentScreen;
	public int ScreenX = 0;
	public int ScreenY = 0;
	public int ScreenTopY;
	protected boolean m_Dying = false;

	private long lastFrameTime = System.nanoTime();
	private float deltaTime = 0;
	private long frameStart = System.nanoTime();
	private int frames = 0;
	private int fps;
	private WindowedMean mean = new WindowedMean(5);
	public Context context;

	public LiveBase(Context context) {
		super();
		this.context = context;
	}

	public abstract void logEvent(String eventID);

	public abstract ScreenBase getStartScreen();

	public abstract void exitLive();

	public abstract boolean loadAssets();

	public void enableInput(boolean value) {
		AcceptInput = value;
		if (CurrentScreen != null)
			CurrentScreen.EnableInput(value);
	}

	public float getDeltaTime() {
		return mean.getMean() == 0 ? deltaTime : mean.getMean();
	}

	public int getFramesPerSecond() {
		return fps;
	}

	public void pushScreen(ScreenBase newscreen) {
		screens.add(0, newscreen);
		screens.get(0).init();
		CurrentScreen = screens.get(0);
	}

	public void popScreen() {
		if (screens.size() > 0) {
			screens.get(0).dispose();
			screens.remove(0);
			if (screens.size() > 0) {
				screens.get(0).init();
				CurrentScreen = screens.get(0);
			} else
				CurrentScreen = null;
		}
	}

	public void popScreens(int count) {
		for (int i = 0; i < count; i++)
			if (screens.size() > 0) {
				screens.get(0).dispose();
				screens.remove(0);
			}

		if (screens.size() > 0) {
			screens.get(0).init();
			CurrentScreen = screens.get(0);
		} else
			CurrentScreen = null;
	}

	public void popAll() {
		while (screens.size() > 0) {
			screens.get(0).dispose();
			screens.remove(0);
			CurrentScreen = null;
		}
		screens.clear();
	}

	public void loadSettings() {
		if (screens.size() > 0)
			if (screens.get(0) != null)
				screens.get(0).loadSettings();
	}

	@Override
	public void resume() {
		// don't track time between frames while suspeded
		long time = System.nanoTime();
		lastFrameTime = time;

		if (screens.size() > 0)
			if (screens.get(0) != null)
				screens.get(0).resume();
	}

	@Override
	public void init() {
		screens = new ArrayList<ScreenBase>();
		screens.add(0, getStartScreen());
		if (screens.get(0) != null)
			screens.get(0).init();
	}

	@Override
	public void render(Canvas canvas) {
		if (m_Dying)
			return;
		long time = System.nanoTime();
		deltaTime = (time - lastFrameTime) / 1000000000.0f;
		lastFrameTime = time;
		mean.addValue(deltaTime);

		if (screens.size() > 0) {
			if (screens.get(0) != null) {
				float delta = getDeltaTime();
				screens.get(0).update(delta);
				canvas.drawColor(Color.BLACK);// clear screen
				screens.get(0).render(canvas, delta);
			}
		}

		if (time - frameStart > 1000000000) {
			fps = frames;
			frames = 0;
			frameStart = time;
		}
		frames++;
	}

	@Override
	public void surfacechange(int width, int height) {
		this.Width = width;
		this.Height = height;
		this.HalfWidth = width / 2;
		this.HalfHeight = height / 2;

		if (screens.size() > 0)
			if (screens.get(0) != null)
				screens.get(0).surfaceChange(width, height);
	}

	@Override
	public void pause() {
		if (screens.size() > 0)
			if (screens.get(0) != null)
				screens.get(0).pause();
	}

	public void dispose() {
		m_Dying = true;

		while (screens.size() > 0) {
			if (screens.get(0) != null) {
				screens.get(0).dispose();
				screens.remove(0);
			}
		}
		screens.clear();
	}
}
