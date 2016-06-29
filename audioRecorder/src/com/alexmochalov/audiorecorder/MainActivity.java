package com.alexmochalov.audiorecorder;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends Activity {
	Menu mMenu;
	int mState = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowTitleEnabled(false);
		
		RawRecords.add("Test 1");
		RawRecords.add("Test 2");
		RawRecords.setAdapter(this, (ListView)findViewById(R.id.listViewRecords));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mMenu = menu;
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		setMenu(0);
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_add: {
			setContentView(R.layout.text);
			setMenu(1);
			return true;
		}
		case R.id.action_settings: {
			return true;
		}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mState == 1) {
				setContentView(R.layout.activity_main);
				setMenu(0);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void setMenu(int state) {
		mState = state;
		if (mState == 0){
			mMenu.setGroupVisible(R.id.group_list, true);
			mMenu.setGroupVisible(R.id.group_record, false);
			
		}
		else {
			mMenu.setGroupVisible(R.id.group_list, false);
			mMenu.setGroupVisible(R.id.group_record, true);
		}
	}

}
