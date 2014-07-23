package com.example.calculateway;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.Time;

public class MainActivity extends Activity implements LocationListener, SensorEventListener{
	Location location = new Location("Asia/Tokyo");
	Location oldLocation;
	Location saveLocation;
	Time time = new Time("Asia/Tokyo");
	Time oldTime;
	Time saveTime;
	float[] result = new float[3];

	 //方位センサー関係
	private SensorManager manager;
	private boolean isSensorResisted = false;
	Bitmap needleImg;

	//SurfaceViewへの描画関係
	SurfaceView surface;
	private CompassHandler compassHandler;

	@Override
	protected void onStart(){
		super.onStart();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		

		LocationManager mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = mLocationManager.getBestProvider(criteria, true);
		mLocationManager.requestLocationUpdates(provider, 0, 0, this);		
		//For compass
		surface = (SurfaceView)findViewById(R.id.compass);
	}

	@Override
	 protected void onResume() {
	  super.onResume();

	  //センサーの起動
	  if (manager == null) {
	   manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	  }
	  List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ORIENTATION);
	  if (sensors.size() > 0) {
	   isSensorResisted = manager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_FASTEST);
	   
	   //針の画像をロード
	   needleImg = BitmapFactory.decodeResource(getResources(), R.drawable.needle);
	   
	   
	   //Handlerを起動
	   compassHandler = new CompassHandler();
	   compassHandler.SetMe(new WeakReference<MainActivity>(this));
	   compassHandler.setAlive();
	  } else {
	   //方位磁石センサーがなければメッセージを表示
	   Toast.makeText(getApplicationContext(), "No Orientaion Sensor!", Toast.LENGTH_LONG).show();
	  }
	 }
	@Override
	 protected void onPause() {
	  super.onPause();

	  //センサーの終了
	  if (isSensorResisted) {
	   manager.unregisterListener(this);
	   isSensorResisted = false;
	   
	   //ループフラグ・オフ
	   compassHandler.unsetAlive();
	   
	   //Handlerにメッセージが残っていれば削除
	   compassHandler.removeMessages(CompassHandler.WHAT);
	   
	   //針画像の解放
	   needleImg.recycle();
	  }
	 }

	
	public void startCalc(View view){

			time.setToNow();
			oldTime = new Time(time);
			oldLocation = new Location(location);			
			TextView dateText = (TextView)findViewById(R.id.date_id);
			TextView tv_lat = (TextView)findViewById(R.id.Latitude);
			TextView tv_lng = (TextView)findViewById(R.id.Longitude);

			String date = "スタート時刻: " + oldTime.hour + "時" + oldTime.minute + "分" + oldTime.second + "秒";
			dateText.setText(date);			
			tv_lat.setText("スタート緯度: "+oldLocation.getLatitude());
			tv_lng.setText("スタート経度: "+oldLocation.getLongitude());
	}
	
	public void goalCalc(View view){
		TextView newDateText = (TextView)findViewById(R.id.newDate_id);
		TextView lat = (TextView)findViewById(R.id.newLatitude);
		TextView lng = (TextView)findViewById(R.id.newLongitude);
		TextView distanceText = (TextView)findViewById(R.id.distance_id);
		TextView timeText = (TextView)findViewById(R.id.costTime_id);
		TextView velocityText = (TextView)findViewById(R.id.velocity_id);
		TextView tv_lat = (TextView)findViewById(R.id.Latitude);

		if(oldTime != null && oldLocation != null){
			time.setToNow();

			String newDate = "ゴール時刻: " + time.hour + "時" + time.minute + "分" + time.second + "秒";
			newDateText.setText(newDate);
			lat.setText("ゴール緯度: "+location.getLatitude());
			lng.setText("ゴール経度: "+location.getLongitude());

			distanceText.setText(calcDistance(location, oldLocation));
			timeText.setText(calcTime(time, oldTime));
			velocityText.setText(calcVelocity(result, time, oldTime));
		} else {
			tv_lat.setText("スタートしてください");
		}
	}

	public void saveTown(View view){
		saveLocation = new Location(location);
		saveTime = new Time(time);
	}
	public void loadTown(View view){
		TextView loadDateText = (TextView)findViewById(R.id.saveDate_id);
		TextView loadlat = (TextView)findViewById(R.id.saveLatitude);
		TextView lng = (TextView)findViewById(R.id.saveLongitude);

		if(saveTime != null && saveLocation != null){
			String saveDate = "保存した時刻: " + saveTime.hour + "時" + saveTime.minute + "分" + saveTime.second + "秒";
			loadDateText.setText(saveDate);
			loadlat.setText("保存した緯度: "+saveLocation.getLatitude());
			lng.setText("保存した経度: "+saveLocation.getLongitude());
		} else {
			loadDateText.setText("何も保存されていません");
		}
	}
	
	private String calcDistance(Location location, Location oldlocation){
		Location.distanceBetween(oldlocation.getLatitude(),
				oldlocation.getLongitude(), 
				location.getLatitude(),
				location.getLongitude(),
				result);

		return "移動距離: " + result[0] + "m";
	}
	
	private String calcTime(Time time, Time oldtime){
		int cHour, cMinute, cSecond;
		long mills = time.normalize(false) - oldTime.normalize(false);
		cHour = (int) (mills / (60 * 60 * 1000));		
		cMinute = (int) (mills % (60 * 60 * 1000) / (60 * 1000));		
		cSecond = (int) (mills % (60 * 1000) / 1000);
		return "所要時間: " + cHour + "時間" + cMinute + "分" + cSecond + "秒";
	}
	
	private String calcVelocity(float[] distance, Time time, Time oldtime){
		long mills = time.normalize(false) - oldTime.normalize(false);
		float vel = distance[0] / mills * 3600;
		return "平均速度: " + vel + "km/h";
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub		
		this.location = new Location(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	 public void onSensorChanged(SensorEvent event) {
	  switch (event.sensor.getType()) {
	  case Sensor.TYPE_ORIENTATION:
	   //北向きをゼロ度とする方位の角度
	   compassHandler.setOrientation(event.values[0]);
	   
	   //Handlerで針画像の描画
	   compassHandler.sendEmptyMessage(CompassHandler.WHAT);
	   break;
	  }
	 }

	 @Override
	 public void onAccuracyChanged(Sensor sensor, int accuracy) {
	 }
}