package com.example.calculateway;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class CompassView extends View {
	Paint paint = new Paint();

	public CompassView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onDraw(Canvas c){
		super.onDraw(c);
		paint.setColor(0XFFFF0000);
		c.drawRect(100f, 200f, 300f, 400f, paint);
	}

}
