package com.ant.liao;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

public class GifAnimation {

	private GifReDraw draw = null;
	private boolean pause = false;

	// max sleep time avoid long delay
	private final int MAXD_DELAY = 200;
	// total delay
	private int currentDelay = 0;

	private Handler handler = new Handler(Looper.getMainLooper());
	private AnimationRunAble animation = new AnimationRunAble();

	public GifAnimation() {
	}

	public void setRedraw(GifReDraw v) {
		draw = v;
	}

	public void pauseAnimation() {
		synchronized (animation) {
			handler.removeCallbacks(animation);
			pause = true;
		}
	}

	public void restartAnimation() {
		synchronized (animation) {
			handler.post(animation);
			pause = false;
		}
	}

	public void stopAnimation() {
		pauseAnimation();
	}

	public void runAnimation() {
		pause = false;
		handler.post(animation);
	}

	public void destroy() {
		stopAnimation();
		draw = null;
	}

	private class AnimationRunAble implements Runnable {
		public void run() {
			if (currentDelay > 0) {
				currentDelay -= MAXD_DELAY;
				if (pause == false) {
					if (currentDelay > MAXD_DELAY) {
						currentDelay -= MAXD_DELAY;
						SystemClock.sleep(MAXD_DELAY);
					} else {
						SystemClock.sleep(currentDelay);
					}
					synchronized (animation) {
						if (pause == false)
							handler.post(animation);
					}
				}
			} else {
				int delay = draw.reDraw();
				if (pause == false) {
					if (delay > MAXD_DELAY) {
						currentDelay = delay - MAXD_DELAY;
						SystemClock.sleep(MAXD_DELAY);
					} else {
						SystemClock.sleep(delay);
					}
					synchronized (animation) {
						if (pause == false)
							handler.post(animation);
					}
				}
			}
		}
	}
}
