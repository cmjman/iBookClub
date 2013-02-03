package com.shining.ibookclub.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GridAdapter extends SimpleCursorAdapter{

	 	private ArrayList<Integer> selection = new ArrayList<Integer>();   
	  
	    private int mImageViewId=0;
	    private String mIdColumn;  
	
	    private ImageView imageview=null;
	    private Uri uri=null;
	   
	      
	    public GridAdapter(Context context, int layout, Cursor c,  
	            String[] from, int[] to, String idColumn,int imageViewId) {  
	        super(context, layout, c, from, to);  
	    
	        mIdColumn = idColumn;  
	        mImageViewId=imageViewId;
	    }  
	 
	    @Override 
	    public int getCount() {  
	        return super.getCount();  
	    }  
	 
	    @Override 
	    public Object getItem(int position) {  
	        return super.getItem(position);  
	    }  
	 
	  
	    public long getItemId(int position) {  
	        return super.getItemId(position);  
	    }  
	 
	  
	    public View getView(final int position, View convertView, ViewGroup parent) {  
	        View view = super.getView(position, convertView, parent);  
	   

	        Cursor cursor = getCursor();  
	        cursor.moveToPosition(position);  
	        int rowId = cursor.getInt(cursor.getColumnIndexOrThrow(mIdColumn));  
	   
	        
	        imageview=(ImageView)view.findViewById(mImageViewId);
	       
	        int  id=cursor.getInt(cursor.getColumnIndexOrThrow(mIdColumn));
	        //    uri=Uri.parse("file:///data/data/com.rss_reader/files/"+id+".png");
	
	       imageview.setImageURI(uri);
	       return view;  
	    }  
	      
	    public ArrayList<Integer> getSelectedItems(){  
	        return selection;  
	    }  
	    
}
