package com.shining.ibookclub.support;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.shining.ibookclub.R;
import com.shining.ibookclub.bean.Bean;
import com.shining.ibookclub.bean.BookBean;
import com.shining.ibookclub.bean.TimelineBean;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<Bean> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, ArrayList d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }
    
    public void addData(Activity a,ArrayList d){
    	
    	 activity = a;
    	 
    	 data.addAll(d);
    	 
    	 System.out.println(data.toString());
    
    	Collections.sort(data,new SortByTimeStamp());
    
    	System.out.println("LazyAdapter AddData:"+data.toString());
    	
    	 inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         imageLoader=new ImageLoader(activity.getApplicationContext());
    }
    
    

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.item, null);

        TextView text=(TextView)vi.findViewById(R.id.text);
        ImageView image=(ImageView)vi.findViewById(R.id.image);
        text.setText(data.get(position).getMessage());
        imageLoader.DisplayImage(data.get(position).getAvatar(), image);
        return vi;
    }
    
    class SortByTimeStamp implements Comparator{
    	
    	public int compare(Object o1,Object o2){
    		
    		
    		Bean b1=(Bean)o1;
    		Bean b2=(Bean)o2;
    		
    		
    		return Timestamp.valueOf(b2.getTimeStamp()).compareTo(Timestamp.valueOf(b1.getTimeStamp()));

    	}
    }
}