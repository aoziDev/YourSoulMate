package com.pread.yoursoulmate.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.pread.yoursoulmate.R;
import com.pread.yoursoulmate.view.DrawingView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final DrawingView fingerprint = new DrawingView(this);

		final View fingerprintShadow = findViewById(R.id.tv_fingerprint_shadow);
		final View fingerprintScanbar = findViewById(R.id.tv_fingerprint_scanbar); 

		final Animation scanAnimation = AnimationUtils.loadAnimation(this, R.anim.fingerprint_scan);

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
					Log.e("x", event.getX()+"");
					Log.e("y", event.getY()+"");


					fingerprint.setImageResource(R.drawable.fingerprint);
					fingerprint.setPosistion(event.getX(), event.getY());
					fingerprint.invalidate();

					fingerprintShadow.startAnimation(scanAnimation);

					fingerprintScanbar.setVisibility(View.VISIBLE);
					fingerprintScanbar.startAnimation(scanAnimation);

					break;
				case MotionEvent.ACTION_UP:
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
}