package com.pread.yoursoulmate.ani;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;

public abstract class FrameAnimationController {
    AnimationDrawable aniDrawable;
    Handler handler = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run() {
            stop();
            animationCallback();
        }
    };

    public FrameAnimationController(AnimationDrawable aniDrawable) {
        this.aniDrawable = aniDrawable;
    }

    public void startCallbackTimer() {
        handler.postDelayed(run, getTotalDelay());
    }

    public void stopCallbackTimer() {
        handler.removeCallbacks(run);
    }

    private int getTotalDelay() {
        int frameCount = aniDrawable.getNumberOfFrames();
        int totDelay = 0;
        for (int i = 0; i < frameCount; i++) {
            totDelay += aniDrawable.getDuration(i);
        }

        Log.d("totDelay", totDelay+"");
        return totDelay;
    }

    public void start() {
        aniDrawable.start();
    }

    public void stop() {
        aniDrawable.stop();
    }

    public void reset() {
        stop();
        start();
        stop();
    }

    public abstract void animationCallback();
}

