package com.pread.yoursoulmate.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.pread.yoursoulmate.R;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final View fingerprintView = findViewById(R.id.tv_fingerprint_shadow);
		final Animation scanAnimation = AnimationUtils.loadAnimation(this, R.anim.fingerprint_scan);
		
		scanAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(intent);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
		});

		/*
        AnimationDrawable aniDrawable = (AnimationDrawable) fingerprintView.getBackground();
        final FrameAnimationController aniController = new FrameAnimationController(aniDrawable) {
            @Override
            public void animationCallback() {
                Log.e("aniCallback", "callback");
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        };
*/
		
        fingerprintView.setOnTouchListener(new View.OnTouchListener() {
               @SuppressLint("ClickableViewAccessibility") 
               @Override
               public boolean onTouch(View v, MotionEvent event) {
                   int action = event.getAction();

                   switch (action) {
                       case MotionEvent.ACTION_DOWN:
                    	   fingerprintView.startAnimation(scanAnimation);
                   		
//                           aniController.startCallbackTimer();
//                           aniController.start();
                           break;
                       case MotionEvent.ACTION_UP:
                    	   fingerprintView.startAnimation(scanAnimation);
                    	   fingerprintView.clearAnimation();
                      		
//                           aniController.stopCallbackTimer();
//                           aniController.reset();
                           break;
                       default:
                           break;
                   }
                   return true;
               }
           }
        );
		
	}
}
