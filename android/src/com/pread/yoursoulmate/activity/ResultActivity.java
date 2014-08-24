package com.pread.yoursoulmate.activity;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pread.yoursoulmate.GlobalData;
import com.pread.yoursoulmate.R;

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

    public String getResult() {
        GlobalData gd = (GlobalData) getApplicationContext();
        List<String> resultList = gd.getResultList();

        Random random = new Random(System.currentTimeMillis());
        int index = random.nextInt(resultList.size());

        return resultList.get(index);
    }
}
