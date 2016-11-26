package rfi2d.engine.live;

import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public abstract class MyWallPaperService extends WallpaperService {

	protected class MyWallpaperEngine extends Engine {
		private final LiveInterface GameListner;
		private final Handler handler = new Handler();
		private SurfaceHolder holder;
		private final Runnable drawRunner = new Runnable() {
			@Override
			public void run() {
				draw();
			}

		};

		// private Paint paint = new Paint();
		private boolean visible = true;

		// private int maxNumber;
		// private boolean touchEnabled;

		public MyWallpaperEngine(LiveInterface listner) {
			GameListner = listner;
			// SharedPreferences prefs =
			// PreferenceManager.getDefaultSharedPreferences(MyWallpaperService.this);
			// maxNumber = Integer.valueOf(prefs.getString("numberOfCircles",
			// "4"));
			// touchEnabled = prefs.getBoolean("touch", false);

			// paint.setAntiAlias(true);
			// paint.setColor(Color.WHITE);
			// paint.setStyle(Paint.Style.STROKE);
			// paint.setStrokeJoin(Paint.Join.ROUND);
			// paint.setStrokeWidth(10f);
			handler.post(drawRunner);

		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			// TODO Auto-generated method stub
			super.onCreate(surfaceHolder);
			holder = surfaceHolder;
			GameListner.init();
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			this.visible = visible;
			if (visible) {
				handler.post(drawRunner);
				GameListner.resume();
			} else {
				handler.removeCallbacks(drawRunner);
				GameListner.pause();
			}
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			this.visible = false;
			handler.removeCallbacks(drawRunner);
			// GameListner.dispose();
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			this.holder = holder;
			// int desiredWidth = getDesiredMinimumWidth();
			// int desiredHeight = getDesiredMinimumHeight();

			// if(desiredWidth <= 0) desiredWidth = width;
			// if(desiredHeight <= 0) desiredHeight = height;
			// GameListner.surfacechange(desiredWidth, desiredHeight);
			GameListner.surfacechange(width, height);

		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			super.onTouchEvent(event);
			GameListner.touch(event);
		}

		private void draw() {
			Canvas canvas = null;
			this.holder = this.getSurfaceHolder();
			if (visible) {
				try {
					canvas = holder.lockCanvas();
					if (canvas != null) {
						GameListner.render(canvas);
					}
				} finally {
					if (canvas != null && holder != null)
						holder.unlockCanvasAndPost(canvas);
				}
				handler.removeCallbacks(drawRunner);

				handler.postDelayed(drawRunner, 21);
			}
		}
	}

}
