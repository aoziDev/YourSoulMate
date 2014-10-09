package com.pread.yoursoulmate.activity;

import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pread.yoursoulmate.GlobalData;
import com.pread.yoursoulmate.R;

public class MainActivity extends Activity {
	private AdView m_bannerAdView;
	
	private Vibrator m_vibe;
	private boolean m_isComplete;

	private TextView m_resultView;
	private View m_was;
	private View m_facebookPosting;
	private View m_kakaostoryPosting;

	private GlobalData gd;
	private Dialog exitDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		loadAdView();

		setFingerprintScanEvent();
		setPostingEvent();
		
		createExitDialog();
	}  


	private void init() {
		m_isComplete = true;
		m_vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		initStatusStr();

		m_bannerAdView = (AdView)findViewById(R.id.bannerAdView);
		
		m_resultView = (TextView)findViewById(R.id.tv_result);
		m_was = findViewById(R.id.tv_was);
		m_facebookPosting = findViewById(R.id.btn_post_facebook);
		m_kakaostoryPosting = findViewById(R.id.btn_post_kakaostory);

		gd = (GlobalData) getApplicationContext();
	}

	private void initStatusStr() {
		setStatusStr("지문을 대세요");
	}

	private void setStatusStr(String str) {
		TextView statusView = (TextView)findViewById(R.id.tv_status);
		statusView.setText(str);
	}

	private void loadAdView() {
		m_bannerAdView.loadAd(new AdRequest.Builder().build());
	}

	private void setFingerprintScanEvent() {
		final ImageView fingerprint = (ImageView)findViewById(R.id.iv_fingerprint);
		final Animation alphaAnim = AnimationUtils.loadAnimation(fingerprint.getContext(), R.anim.fingerprint_change_alpha);

		final View scannerBoard = findViewById(R.id.iv_fingerprint_scanner_board);
		final View fingerprintScreen = findViewById(R.id.fingerprint_screen);
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
				showResult();
			}
		});

		scannerBoard.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility") 
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();

				switch (action) {
				case MotionEvent.ACTION_DOWN:
					if (!m_isComplete) {
						break;
					}

					clearResult();
					m_vibe.vibrate(20);     
					setStatusStr("스캔 중..");

					setPostingVisible(View.INVISIBLE);

					fingerprint.setVisibility(View.VISIBLE);
					fingerprint.startAnimation(alphaAnim);

					fingerprintScreen.startAnimation(scanAnimation);

					fingerprintScanbar.setVisibility(View.VISIBLE);
					fingerprintScanbar.startAnimation(scanAnimation);

					break;

				case MotionEvent.ACTION_UP:
					boolean isScanComplete = fingerprintScanbar.getVisibility() == View.INVISIBLE;
					if (!isScanComplete) {
						initStatusStr();
					}

					fingerprint.setVisibility(isScanComplete ? View.VISIBLE : View.INVISIBLE);
					fingerprint.startAnimation(scanAnimation);
					fingerprint.clearAnimation();

					fingerprintScreen.startAnimation(scanAnimation);
					fingerprintScreen.clearAnimation();

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

	private void clearResult() {
		m_resultView.setText("");
	}

	private void setPostingVisible(int visible) {
		m_was.setVisibility(visible);
		m_facebookPosting.setVisibility(visible);
		m_kakaostoryPosting.setVisibility(visible);
	}

	private void showResult() {
		String result = getResult();
		m_resultView.setText(result);
		gd.setResultStr(result);

		Animation alphaAnim = AnimationUtils.loadAnimation(m_resultView.getContext(), R.anim.fingerprint_change_alpha);
		m_resultView.startAnimation(alphaAnim);
		alphaAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				setStatusStr("분석 중..");
				m_isComplete = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				setStatusStr("분석 완료");
				m_isComplete = true;

				setPostingVisible(View.VISIBLE);
			}
		});
	}

	private String getResult() {
		List<String> resultList = gd.getResultList();

		Random random = new Random(System.currentTimeMillis());
		int index = random.nextInt(resultList.size());

		return resultList.get(index);
	}

	private void setPostingEvent() {
		findViewById(R.id.btn_post_kakaostory).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, KakaoStoryLoginActivity.class));
			}
		});

		findViewById(R.id.btn_post_facebook).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, FacebookPostingActivity.class));
			}
		});
	}
	
	@SuppressLint("InflateParams") 
	private void createExitDialog() {
		View innerView = getLayoutInflater().inflate(R.layout.layout_exit_dialog, null);

		exitDialog = new Dialog(this);
		exitDialog.setContentView(innerView);
		exitDialog.setTitle("종료하시겠습니까?");
		exitDialog.setCanceledOnTouchOutside(false);
		exitDialog.setCancelable(true);
		
		LayoutParams params = exitDialog.getWindow().getAttributes();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.WRAP_CONTENT;
		exitDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

		AdView dialogAdView = (AdView)exitDialog.findViewById(R.id.dialogAdView);
		dialogAdView.loadAd(new AdRequest.Builder().build());
		
		exitDialog.findViewById(R.id.btn_exit_app).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveTaskToBack(true);
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		
		exitDialog.findViewById(R.id.btn_exit_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		exitDialog.show();
	}
	
	@Override
	public void onPause() {
		if (m_bannerAdView != null) {
			m_bannerAdView.pause();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (m_bannerAdView != null) {
			m_bannerAdView.resume();
		}
	}

	@Override
	public void onDestroy() {
		if (m_bannerAdView != null) {
			m_bannerAdView.destroy();
		}
		super.onDestroy();
	}
}