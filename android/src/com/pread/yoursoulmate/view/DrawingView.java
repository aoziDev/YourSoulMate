package com.pread.yoursoulmate.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DrawingView extends View {
	private Bitmap image;
	private float x;
	private float y;
	
	public DrawingView(Context context) {
		super(context);
	}
	
	public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setImageResource(int imageResource) {
		image = BitmapFactory.decodeResource(getContext().getResources(), imageResource);
	}
	
	public void setPosistion(float x, float y) {
		if (image == null) {
			return;
		}
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		this.x = x - (w/2);
		this.y = y - (h/2);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.e("onDraw", "call");
		if (image != null) {
			canvas.drawBitmap(image, x, y, null);
			
			Log.e("draw X", x+"");
			Log.e("draw Y", y+"");
		} else {
			Log.e("onDraw", "image is null");
			
		}
	}
}
