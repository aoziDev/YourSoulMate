package com.pread.yoursoulmate.activity;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.pread.yoursoulmate.GlobalData;
import com.pread.yoursoulmate.R;

public class ResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        setButtonEvent();
        
        String resultStr = getResult();
        showResult(resultStr);
        
        GlobalData gd = (GlobalData)getApplicationContext();
        gd.setResultStr(resultStr);
    }
    
    private void setButtonEvent() {
    	findViewById(R.id.btn_again).setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			finish();
    		}
    	});
    	
    	findViewById(R.id.btn_post_kakaostory).setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			Intent intent = new Intent(ResultActivity.this, KakaoStoryLoginActivity.class);
    			startActivity(intent);
    		}
    	});
    	
    	findViewById(R.id.btn_post_facebook).setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			startActivity(new Intent(ResultActivity.this, FacebookPostingActivity.class));
    		}
    	});
    }
    
    private String getResult() {
    	GlobalData gd = (GlobalData) getApplicationContext();
    	List<String> resultList = gd.getResultList();
    	
    	Random random = new Random(System.currentTimeMillis());
    	int index = random.nextInt(resultList.size());
    	
    	return resultList.get(index);
    }

	private void showResult(String resultStr) {
		String result = getResult();
        TextView resultTextView = (TextView)findViewById(R.id.tv_result);
        resultTextView.setText(result);
	}
	
}