package com.pread.yoursoulmate.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pread.yoursoulmate.GlobalData;
import com.pread.yoursoulmate.R;

import java.util.List;
import java.util.Random;

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        String result = getResult();
        TextView resultTextView = (TextView)findViewById(R.id.tv_result);
        resultTextView.setText(result);

        findViewById(R.id.btn_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
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

    public String getResult() {
        GlobalData gd = (GlobalData) getApplicationContext();
        List<String> resultList = gd.getResultList();

        Random random = new Random(System.currentTimeMillis());
        int index = random.nextInt(resultList.size());

        return resultList.get(index);
    }
}
