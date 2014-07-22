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
	Location location;
	Location oldLocation;
	Time time = new Time("Asia/Tokyo");
	Time oldTime;

	@Override
	protected void onStart(){
		super.onStart();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
	}
	
	public void startCalc(View view){
		float[] result = new float[3];
		if(oldLocation == null){	//初めの1回
			time.setToNow();
			oldTime = time;
			TextView dateText = (TextView)findViewById(R.id.date_id);
			String date = "現在の時刻: " + time.hour + "時" + time.minute + "分" + time.second + "秒";
			dateText.setText(date);

			LocationManager mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			String provider = mLocationManager.getBestProvider(criteria, true);
			mLocationManager.requestLocationUpdates(provider, 0, 0, this);		
			oldLocation = location;			
			
		} else {					//それ以降
			time.setToNow();
			oldTime = time;
			TextView dateText = (TextView)findViewById(R.id.newDate_id);
			String date = "現在の時刻: " + time.hour + "時" + time.minute + "分" + time.second + "秒";
			dateText.setText(date);
			
			TextView tv_lat = (TextView)findViewById(R.id.newLatitude);
			tv_lat.setText("現在の緯度: "+location.getLatitude());

			TextView tv_lng = (TextView)findViewById(R.id.newLongitude);
			tv_lng.setText("現在の経度: "+location.getLongitude());

			Location.distanceBetween(oldLocation.getLatitude(),
			oldLocation.getLongitude(), 
			location.getLatitude(),
			location.getLongitude(),
			result);
			oldLocation = location;

			
			TextView distanceText = (TextView)findViewById(R.id.distance_id);
			String distance = "移動距離: " + result[0] + "m";
			distanceText.setText(distance);

		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		TextView tv_lat = (TextView)findViewById(R.id.Latitude);
		tv_lat.setText("現在の緯度: "+location.getLatitude());

		TextView tv_lng = (TextView)findViewById(R.id.Longitude);
		tv_lng.setText("現在の経度: "+location.getLongitude());
		
//		this.location = location;
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