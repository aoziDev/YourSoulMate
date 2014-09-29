package com.pread.yoursoulmate.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.pread.yoursoulmate.GlobalData;
import com.pread.yoursoulmate.R;
import com.pread.yoursoulmate.io.FileIO;

public class IntroActivity extends Activity {
    private TextView loading;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        init();

        fileLoading();
        startMainActivity();
	}

	private void init() {
		loading = (TextView) findViewById(R.id.tv_intro_loading); 
		Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
		loading.startAnimation(blinkAnimation);
	}

	private void fileLoading() {
        FileIO fileIO = new FileIO();
        fileIO.read(getResources().openRawResource(R.raw.data));
        List<String> resultList = fileIO.getResult();

        GlobalData gd = (GlobalData)getApplicationContext();
        gd.setResultList(resultList);
    }
	
	private void startMainActivity() {
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable()
		{
			@Override     
			public void run() {
				Intent intent = new Intent(IntroActivity.this, MainActivity.class);
				startActivity(intent);
				
				loading.clearAnimation();

				finish();
			}
		}, 3000);
	}
}
