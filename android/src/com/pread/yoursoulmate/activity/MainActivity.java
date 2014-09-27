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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pread.yoursoulmate.R;
import com.pread.yoursoulmate.view.DrawingView;

public class MainActivity extends Activity {
	AdView mAdView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		AdRequest adRequest = new AdRequest.Builder()
        .build();

		mAdView = (AdView)findViewById(R.id.adView);
		mAdView.loadAd(adRequest);
		mAdView.bringToFront();
		
		final DrawingView fingerprint = (DrawingView)findViewById(R.id.dv_fingerprint);
		fingerprint.setImageResource(R.drawable.q);
		
		final View fingerprintShadow = findViewById(R.id.tv_fingerprint_shadow);
		final View fingerprintScanbar = findViewById(R.id.tv_fingerprint_scanbar); 

		final Animation scanAnimation = AnimationUtils.loadAnimation(this, R.anim.fingerprint_scan);

		final View title = findViewById(R.id.tv_input_fingerprint);
		title.bringToFront();
		
		scanAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				fingerprintScanbar.setVisibility(View.INVISIBLE);

				Intent intent = new Intent(MainActivity.this, ResultActivity.class);
				startActivity(intent);
			}
		});

		fingerprintShadow.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility") 
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();

				switch (action) {
				case MotionEvent.ACTION_DOWN:
					fingerprintShadow.startAnimation(scanAnimation);

					fingerprintScanbar.setVisibility(View.VISIBLE);
					fingerprintScanbar.startAnimation(scanAnimation);
				case MotionEvent.ACTION_MOVE:
					fingerprint.setVisibility(View.VISIBLE);
					fingerprint.setPosistion(event.getX(), event.getY() + title.getHeight());
					fingerprint.invalidate();
					
					break;
				case MotionEvent.ACTION_UP:

					fingerprint.setVisibility(View.INVISIBLE);
					fingerprintShadow.startAnimation(scanAnimation);
					fingerprintShadow.clearAnimation();

					fingerprintScanbar.setVisibility(View.INVISIBLE);
					fingerprintScanbar.startAnimation(scanAnimation);
					fingerprintScanbar.clearAnimation();

					break;
	
					
				default:
					break;
				}
				return true;
			}
		});
	}  
	
    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}