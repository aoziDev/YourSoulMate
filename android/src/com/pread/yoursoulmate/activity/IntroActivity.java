package com.pread.yoursoulmate.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pread.yoursoulmate.GlobalData;
import com.pread.yoursoulmate.R;
import com.pread.yoursoulmate.io.FileIO;

public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        loading();

        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    private void loading() {
        Log.e("loading", "111");
        FileIO fileIO = new FileIO();
        fileIO.read(getResources().openRawResource(R.raw.data));
        List<String> resultList = fileIO.getResult();
        Log.e("loading", "222");

        GlobalData gd = (GlobalData)getApplicationContext();
        gd.setResultList(resultList);

        Log.d("result", resultList.size()+"");
    }
}
