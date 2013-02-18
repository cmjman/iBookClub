package com.shining.ibookclub;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.MapController;
import com.shining.ibookclub.bean.BookBean;


import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;

import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class PostBookActivity extends Activity {
	
	private BookBean bookBean;
	

	
	private GoogleMap gmap;
	
	
	
	private LocationManager locationManager;
	
	private LocationListener locationListener;
	
	private ImageButton button_getLocation;
	
	private double latitude;
	
	private double longitude;
	
	private String provider;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_book);
		
		bookBean=(BookBean)getIntent().getSerializableExtra("bean");
		
		 gmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragment_map)).getMap(); 
	
	   
	     locationListener=new MyLocationListener();
	     
	 	
		button_getLocation=(ImageButton)findViewById(R.id.button_getLocation);
			
		button_getLocation.setOnClickListener(new OnClickListener(){

		
			public void onClick(View arg0) {
				
				
				
	            
			        
			         Geocoder gc=new Geocoder(getBaseContext(),Locale.getDefault());  
			         
			         locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
			         
			         
			        provider=LocationManager.NETWORK_PROVIDER;
			         
			         Location location=locationManager.getLastKnownLocation(provider); 
			         
			         if(location == null){ 
			             locationManager.requestLocationUpdates(provider, 0, 0, locationListener );
			             location =locationManager.getLastKnownLocation(provider);
			        } 
			         
			         latitude=location.getLatitude();
			         longitude=location.getLongitude();
			       
			         
			         
			         System.out.println(latitude + " " +longitude);
			         
			         gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 15)); 
		                
					 gmap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Marker")); 

			}
			
		
		});
	}
	
	private String getGPSandNETWORK_Status(){
		
		boolean GPS_status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean NETWORK_status = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        String status = "";
        if(GPS_status){
         
        	status += "GPS 开启";
        }else{
         
        	status += "GPS 未开启";
        }
        if(NETWORK_status){
         
        	status += "NETWORK 开启";
        }else{
         
        	status += "NETWORK 未开启";
        }
        
        return status;
	}
	
	private class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
			 latitude = location.getLatitude();
			 longitude = location.getLongitude();
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_book, menu);
		return true;
	}
	
	protected void onResume() {
        super.onResume();
    
    
   
    //    locationManager.requestLocationUpdates(provider, 0, 0, locationListener );
       
     }

     @Override
     protected void onPause() {
       super.onPause();
   
       if (locationManager != null) {
    	   locationManager.removeUpdates(locationListener);
       }
  }



}
