package com.example.calculateway;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;

//SurfaceViewを描画するHandler
public class CompassHandler extends Handler{
	  static final int WHAT = 1;
	  
	  private SurfaceView surface;
	  private Bitmap needleImg;
	  private float centerX;
	  private float centerY;
	  private float drawX;
	  private float drawY;
	  private boolean isFast = true;

      private float orientation;
      private static boolean isAlive;
	  
	  public void SetMe(WeakReference<MainActivity> me) {
	   this.surface = me.get().surface;
	   this.needleImg = me.get().needleImg;
	  }
	  
	  @Override
	  public void handleMessage(Message msg) {
	   removeMessages(WHAT);
	   
	   if ( ! isAlive){
	    //フラグオフなら終了
	    return;
	   }

	   Canvas canvas = surface.getHolder().lockCanvas();
	   if (canvas == null) {
	    //SurfaceViewの準備が終わってなければ時間を空けてリトライ
	    sendEmptyMessageDelayed(WHAT, 10);
	    return;
	   }

	   //最初の1回だけの処理
	   if (isFast) {
	    //中心座標を計算
	    centerX = surface.getWidth() / 2;
	    centerY = surface.getHeight() / 2;
	    drawX = centerX - needleImg.getWidth() / 2;
	    drawY = centerY - needleImg.getHeight() / 2;
	    isFast = false;
	   }
	   
	   //背景を白く塗りつぶし
	   canvas.drawColor(Color.WHITE);

	   //矢印を方角に向かって描画
	   canvas.save();
	   canvas.rotate(-orientation, centerX, centerY);
	   canvas.drawBitmap(needleImg, drawX, drawY, null);
	   canvas.restore();

	   surface.getHolder().unlockCanvasAndPost(canvas);
   }

	public void setAlive() {
		isAlive = true;
	}

	public void unsetAlive() {
		isAlive = false;
		
	}

	public void setOrientation(float orientation) {
		this.orientation = orientation;
		
	}
}
