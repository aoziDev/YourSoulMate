package com.pread.yoursoulmate.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

public class DrawingView extends View {
	private Bitmap image;
	private float x;
	private float y;
	
	public DrawingView(Context context) {
		super(context);
		//setFocusable(true);
	}
	
	public void setImageResource(int imageResource) {
		image = BitmapFactory.decodeResource(getContext().getResources(), imageResource);
	}
	
	public void setPosistion(float x, float y) {
		this.x = x;
		this.y = y;
		
		Log.e("setPosition X", x+"");
		Log.e("setPosition Y", y+"");
		
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		if (image != null) {
			canvas.drawBitmap(image, x, y, null);
			
			Log.e("draw X", x+"");
			Log.e("draw Y", y+"");
		} else {
			Log.e("onDraw", "image is null");
			
		}
	}
}
