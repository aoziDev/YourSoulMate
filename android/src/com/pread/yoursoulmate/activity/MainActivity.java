package com.pread.yoursoulmate.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.pread.yoursoulmate.R;
import com.pread.yoursoulmate.ani.FrameAnimationController;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View fingerprintView = findViewById(R.id.iv_fingerprint);
        AnimationDrawable aniDrawable = (AnimationDrawable) fingerprintView.getBackground();
        final FrameAnimationController aniController = new FrameAnimationController(aniDrawable) {
            @Override
            public void animationCallback() {
                Log.e("aniCallback", "callback");
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        };

        fingerprintView.setOnTouchListener(new View.OnTouchListener() {
               @SuppressLint("ClickableViewAccessibility") 
               @Override
               public boolean onTouch(View v, MotionEvent event) {
                   int action = event.getAction();

                   switch (action) {
                       case MotionEvent.ACTION_DOWN:
                           aniController.startCallbackTimer();
                           aniController.start();
                           break;
                       case MotionEvent.ACTION_UP:
                           aniController.stopCallbackTimer();
                           aniController.reset();
                           break;
                       default:
                           break;
                   }
                   return true;
               }
           }
        );

    }
}
