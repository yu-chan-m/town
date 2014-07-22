package com.example.calculateway;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class Town extends Activity implements LocationListener{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.town);
		
		LocationManager mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = mLocationManager.getBestProvider(criteria, true);
		TextView tv_provider = (TextView)findViewById(R.id.Provider);
		tv_provider.setText("Provider: "+provider);
		mLocationManager.requestLocationUpdates(provider, 0, 0, this);
			
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		TextView tv_lat = (TextView)findViewById(R.id.Latitude);
		tv_lat.setText("Latitude: "+location.getLatitude());

		TextView tv_lng = (TextView)findViewById(R.id.Longitude);
		tv_lng.setText("Longitude: "+location.getLongitude());

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