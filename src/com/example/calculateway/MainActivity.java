package com.example.calculateway;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.text.format.Time;

public class MainActivity extends Activity implements LocationListener{
	Location location = new Location("Asia/Tokyo");
	Location oldLocation;
	Location saveLocation;
	Time time = new Time("Asia/Tokyo");
	Time oldTime;
	Time saveTime;

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
	}
	
	public void startCalc(View view){

			time.setToNow();
			oldTime = new Time(time);
			oldLocation = new Location(location);			

			TextView dateText = (TextView)findViewById(R.id.date_id);
			String date = "スタート時刻: " + oldTime.hour + "時" + oldTime.minute + "分" + oldTime.second + "秒";
			dateText.setText(date);
			
			TextView tv_lat = (TextView)findViewById(R.id.Latitude);
			tv_lat.setText("スタート緯度: "+oldLocation.getLatitude());

			TextView tv_lng = (TextView)findViewById(R.id.Longitude);
			tv_lng.setText("スタート経度: "+oldLocation.getLongitude());
	}
	
	public void goalCalc(View view){
		float[] result = new float[3];

		if(oldTime != null && oldLocation != null){
			TextView dateText = (TextView)findViewById(R.id.date_id);
			String date = "スタート時刻: " + oldTime.hour + "時" + oldTime.minute + "分" + oldTime.second + "秒";
			dateText.setText(date);

			TextView oldLat = (TextView)findViewById(R.id.Latitude);
			oldLat.setText("スタート緯度: "+oldLocation.getLatitude());

			TextView oldLng = (TextView)findViewById(R.id.Longitude);
			oldLng.setText("スタート経度: "+oldLocation.getLongitude());

			time.setToNow();

			TextView newDateText = (TextView)findViewById(R.id.newDate_id);
			String newDate = "ゴール時刻: " + time.hour + "時" + time.minute + "分" + time.second + "秒";
			newDateText.setText(newDate);

			TextView lat = (TextView)findViewById(R.id.newLatitude);
			lat.setText("ゴール緯度: "+location.getLatitude());

			TextView lng = (TextView)findViewById(R.id.newLongitude);
			lng.setText("ゴール経度: "+location.getLongitude());

			Location.distanceBetween(oldLocation.getLatitude(),
					oldLocation.getLongitude(), 
					location.getLatitude(),
					location.getLongitude(),
					result);

			TextView distanceText = (TextView)findViewById(R.id.distance_id);
			String distance = "移動距離: " + result[0] + "m";
			distanceText.setText(distance);

			TextView timeText = (TextView)findViewById(R.id.costTime_id);
			timeText.setText(calcTime(time, oldTime));
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
	
	private String calcTime(Time time, Time oldtime){
		int cHour, cMinute, cSecond;
		cHour = time.hour - oldtime.hour;
		cMinute = time.minute - oldtime.minute;
		cSecond = time.second - oldtime.second;
		if(cSecond < 0){ cSecond += 60; cMinute -= 1; }
		if(cMinute < 0){ cMinute += 60; cHour -= 1; }
		return "所要時間: " + cHour + "時間" + cMinute + "分" + cSecond + "秒";
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
}