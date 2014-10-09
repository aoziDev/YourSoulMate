package com.pread.yoursoulmate.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pread.yoursoulmate.GlobalData;
import com.pread.yoursoulmate.R;
import com.pread.yoursoulmate.io.FileIO;

public class IntroActivity extends Activity implements OnClickListener {
    private View m_loadingView;
	private View m_touchToScreenView;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayoutAndTouchEvent();
        
        startLoadingAnimation();
        fileLoading();
        stopLoadingAnimation();
       
        showTouchToScreen();
	}

	@SuppressLint("InflateParams") 
	private void setLayoutAndTouchEvent() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View introActivity = inflater.inflate(R.layout.activity_intro, null);
		setContentView(introActivity);
		introActivity.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		m_touchToScreenView.clearAnimation();
		m_touchToScreenView.setVisibility(View.INVISIBLE);
		
		startActivity(new Intent(IntroActivity.this, MainActivity.class));
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
		
		finish();
	}
	
	private void startLoadingAnimation() {
		m_loadingView = findViewById(R.id.tv_intro_loading); 
		Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
		m_loadingView.startAnimation(blinkAnimation);
	}

	private void fileLoading() {
        FileIO fileIO = new FileIO();
        fileIO.read(getResources().openRawResource(R.raw.data));
        List<String> resultList = fileIO.getResult();

        GlobalData gd = (GlobalData)getApplicationContext();
        gd.setResultList(resultList);
    }
	
	private void stopLoadingAnimation() {
		m_loadingView.clearAnimation();
		m_loadingView.setVisibility(View.INVISIBLE);
	}

	private void showTouchToScreen() {
		m_touchToScreenView = findViewById(R.id.tv_touch_to_screen);
		Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
		
		m_touchToScreenView.setVisibility(View.VISIBLE);
		m_touchToScreenView.startAnimation(blinkAnimation);
	}
}
