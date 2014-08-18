package com.pread.yoursoulmate.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pread.yoursoulmate.GlobalData;
import com.pread.yoursoulmate.R;
import com.pread.yoursoulmate.io.FileIO;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends ActionBarActivity {

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.intro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
