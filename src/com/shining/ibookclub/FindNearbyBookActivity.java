package com.shining.ibookclub;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.Overlay;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shining.ibookclub.bean.BookBean;
import com.shining.ibookclub.support.BitmapCache;
import com.shining.ibookclub.support.HttpUtility;
import com.shining.ibookclub.support.LoginSingleton;

import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FindNearbyBookActivity extends Activity {
	

	
	private GoogleMap gmap;
	
	private LocationManager locationManager;
	
	private LocationListener locationListener;
	
	private double latitude;
	
	private double longitude;
	
	private String provider;
	
	private Bitmap bm;
	
	private ArrayList<BookBean> bookList=new ArrayList<BookBean>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_nearby_book);
		
		 gmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragment_map)).getMap(); 
		 
		 locationListener=new LocationListener(){
			 
			 public void onLocationChanged(Location location) {
			        longitude = location.getLongitude();
			        latitude = location.getLatitude();
			    }

		
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
		 };
		
		 Geocoder gc=new Geocoder(getBaseContext(),Locale.getDefault());  
	     locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
	     
	     
	          
	     if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
        	 provider=LocationManager.GPS_PROVIDER;
	     }
         else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        	 provider=LocationManager.NETWORK_PROVIDER;
         else
        	 Toast.makeText(getBaseContext(), "没有可用的位置数据，请打开GPS或者连接网络！", Toast.LENGTH_SHORT).show();
	    
        	 
	     /*
	        Criteria criteria = new Criteria();
	        criteria.setAccuracy(Criteria.ACCURACY_FINE); 
	        criteria.setAltitudeRequired(false);
	        criteria.setBearingRequired(false);
	        criteria.setCostAllowed(true);
	        criteria.setPowerRequirement(Criteria.POWER_LOW);
	        provider = locationManager.getBestProvider(criteria, true);
      */
         Location location=locationManager.getLastKnownLocation(provider); 
         
         locationManager.requestLocationUpdates(provider, 10000, 10, locationListener );
         location =locationManager.getLastKnownLocation(provider);
   
         if(location != null){ 
            
        	 latitude=location.getLatitude();//纬度
             longitude=location.getLongitude();//经度
         }else{
        	 locationManager.requestLocationUpdates(provider, 10000, 10, locationListener );
         }
         
        
      
         gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 15));  
	
         int radius =1000; 
  
         bm=getBitmap(radius);
         BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);
         gmap.addGroundOverlay(new GroundOverlayOptions().
        		            image(bmD).
        		            position(new  LatLng(latitude,longitude),radius,radius).
        		            transparency(0.4f));
		 
         gmap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Marker"));
         
         /*
         for(int i=0;i<5;i++){
         
        	 gmap.addMarker(new MarkerOptions().icon(getDummyIcon()).position(arg0));
         }
         
         MyItemizedOverlay itemizedOverlay=new MyItemizedOverlay(getAssets().list("images"));
         
         */
        
         GetNearbyTask getNearbyTask=new GetNearbyTask();
         getNearbyTask.execute((Void)null);
	}
	
	/*
	private BitmapDescriptor getDummyIcon(){
		
		return null;
		
	}*/
	
	 private Bitmap getBitmap(int radius) {
		
	      	Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2, Config.ALPHA_8);
	      	System.out.println("Bitmap Size:"+bitmap.getByteCount());
	      	
	        
	        SoftReference<Bitmap> softReference =new SoftReference<Bitmap>(bitmap);
	       
	        Canvas canvas = new Canvas(softReference.get());
	        
	        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	        
	        paint.setColor(0x110000FF);
	        paint.setStyle(Style.FILL);
			canvas.drawCircle(radius, radius, radius, paint);
	      
			paint.setColor(0xFF0000FF);
	        paint.setStyle(Style.STROKE);
	    	canvas.drawCircle(radius, radius,radius, paint);
	        return softReference.get();
	    }
	 
	public class GetNearbyTask extends AsyncTask<Void, Void, Boolean> {

		
		protected Boolean doInBackground(Void... arg0) {
			
			Boolean result=false;
			String httpUrl=iBookClub.SERVER_URL+"getNearby.action";
			
			if(LoginSingleton.isLoginSuccess()){
				
				
				List <NameValuePair> params = new ArrayList <NameValuePair>(); 
		        params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail));  
				params.add(new BasicNameValuePair("latitude",String.valueOf(latitude)));
				params.add(new BasicNameValuePair("longitude",String.valueOf(longitude)));

				HttpUtility httpUtility=new HttpUtility(httpUrl,params);
	
				try{
					
						System.out.println("GetNearbyTask location:"+String.valueOf(latitude)+String.valueOf(longitude));
						
						String strResult=httpUtility.doPost();
						
						Gson gson = new Gson();
						bookList = gson.fromJson(strResult, new TypeToken<ArrayList<BookBean>>(){}.getType());
						
						
				}
				catch(Exception e){
					return false;
				}
				
				
				
			}
				
			return true;
				
		}
		
		protected void onPostExecute(final Boolean success) {
			
			
		//	System.out.println(bookList.get(0).getBookname().toString());
			
		}
	}
	 
	private class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(final Location location) {
			
			
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
			 locationManager.requestLocationUpdates(provider, 10000, 10, locationListener );
			
		}
		
	}

	public  void onPause(){
		
		super.onPause();
		if(locationManager!=null)
			locationManager.removeUpdates(locationListener);
	}
	
	public void onResume(){
		
		super.onResume();
		locationManager.requestLocationUpdates(provider, 10000, 10, locationListener);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		//短时间内多次进入到该界面会导致图片缓存空间（默认16M)溢出，待解决
		//03-26 22:07:03.530: E/dalvikvm-heap(6531): Out of memory on a 16777232-byte allocation.
		//03-26 22:07:03.540: E/AndroidRuntime(6531): java.lang.OutOfMemoryError
		//03-26 22:07:03.540: E/AndroidRuntime(6531): 	at android.graphics.Bitmap.nativeCreate(Native Method)

		   
		if (keyCode== KeyEvent.KEYCODE_BACK) {
		      
			bm=null;
			System.gc();
			
			FindNearbyBookActivity.this.finish();
			
		    return true;
		}
		   
		return super.onKeyDown(keyCode, event);
	}
	
	public void onDestroy(){
		
		super.onDestroy();
		bm=null;
		System.gc();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.find_nearby_book, menu);
		return true;
	}

}
