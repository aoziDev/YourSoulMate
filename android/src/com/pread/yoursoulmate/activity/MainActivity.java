package com.pread.yoursoulmate.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pread.yoursoulmate.R;

public class MainActivity extends Activity {
	private AdView mAdView;
	
	private void loadAdView() {
		AdRequest adRequest = new AdRequest.Builder()
		.build();
		
		mAdView = (AdView)findViewById(R.id.adView);
		mAdView.loadAd(adRequest);
	}
	
	private void init() {
		setStatusStr("지문을 대세용");
	}
	
	private void setStatusStr(String str) {
		TextView statusView = (TextView)findViewById(R.id.tv_status);
		statusView.setText(str);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loadAdView();
		init();
		
		final ImageView fingerprint = (ImageView)findViewById(R.id.iv_fingerprint);
		final Animation alphaAnim = AnimationUtils.loadAnimation(fingerprint.getContext(), R.anim.fingerprint_change_alpha);
		
		final View board = findViewById(R.id.iv_fingerprint_scanner_board);
		final View screen = findViewById(R.id.fingerprint_screen);
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
				setStatusStr("스캔 완료");
			}
		});

		board.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility") 
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();

				switch (action) {
				case MotionEvent.ACTION_DOWN:
					setStatusStr("스캔 중..");
					fingerprint.setVisibility(View.VISIBLE);
					fingerprint.startAnimation(alphaAnim);
					
					screen.startAnimation(scanAnimation);

					fingerprintScanbar.setVisibility(View.VISIBLE);
					fingerprintScanbar.startAnimation(scanAnimation);
					
					break;
					
				case MotionEvent.ACTION_UP:
					boolean isScanComplete = fingerprintScanbar.getVisibility() == View.INVISIBLE;
					if (!isScanComplete) {
						init();
					}
					
					fingerprint.setVisibility(isScanComplete ? View.VISIBLE : View.INVISIBLE);
					fingerprint.startAnimation(scanAnimation);
					fingerprint.clearAnimation();
					
					screen.startAnimation(scanAnimation);
					screen.clearAnimation();

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
	
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}