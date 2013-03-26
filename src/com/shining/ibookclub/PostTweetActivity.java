package com.shining.ibookclub;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PostTweetActivity extends Activity {
	
	private Button button_post_tweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_tweet);
		
		button_post_tweet=(Button)findViewById(R.id.button_post_tweet);
		
		button_post_tweet.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
			
				Toast.makeText(getBaseContext(), "发布成功！", Toast.LENGTH_SHORT).show();
				PostTweetActivity.this.finish();
			}	
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_tweet, menu);
		return true;
	}

}
