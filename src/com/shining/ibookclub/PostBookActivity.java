package com.shining.ibookclub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.MapController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shining.ibookclub.bean.BookBean;
import com.shining.ibookclub.support.HttpUtility;
import com.shining.ibookclub.support.LoginSingleton;


import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;

import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class PostBookActivity extends Activity {
	
	private BookBean bookBean;

	private GoogleMap gmap;
	
	private LocationManager locationManager;
	
	private LocationListener locationListener;
	
	private ImageButton button_getLocation;
	
	private ImageButton button_postBook;
	
	private double latitude;
	
	private double longitude;
	
	private String provider;
	
	private ListView listView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_book);
		
		bookBean=(BookBean)getIntent().getSerializableExtra("bean");
		
		 gmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragment_map)).getMap(); 
	
	   
	     locationListener=new MyLocationListener();
	     
	     listView=(ListView)findViewById(R.id.listview_book);
	     
	     listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,getData(bookBean)));
	     
	 	
		button_getLocation=(ImageButton)findViewById(R.id.button_getLocation);
			
		button_getLocation.setOnClickListener(new OnClickListener(){

		
			public void onClick(View arg0) {
				
					
			         Geocoder gc=new Geocoder(getBaseContext(),Locale.getDefault());  
			         locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
			          
			         if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			        	 provider=LocationManager.GPS_PROVIDER;
			         else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			        	 provider=LocationManager.NETWORK_PROVIDER;
			         else
			        	 Toast.makeText(getBaseContext(), "没有可用的位置数据，请打开GPS或者连接网络！", Toast.LENGTH_SHORT).show();
			      
			         Location location=locationManager.getLastKnownLocation(provider); 
			   
			         if(location == null){ 
			             locationManager.requestLocationUpdates(provider, 0, 0, locationListener );
			             location =locationManager.getLastKnownLocation(provider);
			         } 
			         
			         latitude=location.getLatitude();//纬度
			         longitude=location.getLongitude();//经度
			      
			         gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 15));  
					 gmap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Marker")); 
			}
			
		
		});
		
		
		button_postBook=(ImageButton)findViewById(R.id.button_postbook);
		button_postBook.setOnClickListener(new OnClickListener(){

		
			public void onClick(View arg0) {
			
				PostBookTask postBookTask=new PostBookTask();
				postBookTask.execute((Void)null);
			}
		});
	}
	

	private List<String> getData(BookBean bean){
	         
		  List<String> data = new ArrayList<String>();
		   
		  data.add("书名："+bean.getBookname());
		  
		  data.add("ISBN："+bean.getIsbn());
		        
		  data.add("作者："+bean.getAuthor());
		  
		  data.add("出版社："+bean.getPublisher());
		  
		  data.add("简介："+bean.getSummary());
		       
		       
		  return data;
	 }
	
	public class PostBookTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
		
			Boolean result=false;
			String httpUrl=LoginSingleton.SERVER_URL+"addBook.action";
			
			if(LoginSingleton.isLoginSuccess()){
				
				
				List <NameValuePair> params = new ArrayList <NameValuePair>(); 
		        params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail));  
		        Gson gsonBookInfo=new Gson();
				params.add(new BasicNameValuePair("bookbean_gson",gsonBookInfo.toJson(bookBean)));
				System.out.println("PostBookTask gson:"+gsonBookInfo.toJson(bookBean));
				params.add(new BasicNameValuePair("latitude",String.valueOf(latitude)));
				params.add(new BasicNameValuePair("longitude",String.valueOf(longitude)));
			
		
				
				HttpUtility httpUtility=new HttpUtility(httpUrl,params);
				
		      
		  
				try{
					
						String strResult=httpUtility.doPost();
					//	Gson gson = new Gson();
					//	bookList = gson.fromJson(strResult, new TypeToken<ArrayList<BookBean>>(){}.getType());
						
						System.out.println("PostBookActivity:"+strResult);
						
						JSONObject jsonObj=new JSONObject(strResult);
						result=jsonObj.getBoolean("ActionResult");
				}
				catch(Exception e){
					return false;
				}
				
				
				return result;
			}
			
			return null;
		}
		
		protected void onPostExecute(final Boolean success) {
			
			
			Toast.makeText(getApplicationContext(), "发布成功！", Toast.LENGTH_SHORT).show();
			
			PostBookActivity.this.finish();
		}
		
		
		
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
