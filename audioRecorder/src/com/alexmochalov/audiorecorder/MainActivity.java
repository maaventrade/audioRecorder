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
import android.os.*;

public class MainActivity extends Activity {

	// States of the application. They are displayed in toolbar menu
	public enum State {
			list, read, record, play, edit
			}

	public State mState = State.read;
	
	public static final String PROGRAMM_FOLDER = "xolosoft";
	public static final String APP_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+PROGRAMM_FOLDER+"/audiorecorder";

	Menu mMenu;

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
		setMenu(State.list);
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
			setMenu(State.read);
			
			return true;
		}
		case R.id.action_settings: {
			return true;
		}
			case R.id.action_rec:
				// Start voice recording
				Media.startRecording();
				setMenu(State.record);
				return true;
			case R.id.action_stop:
				// Stop recording or playing
				if (mState == State.record)
					Media.stopRecording();
				else if (mState == State.play)
					Media.stopPlaying();

				
				setMenu(State.read);
				return true;
		
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mState == State.read) {
				setContentView(R.layout.activity_main);
				setMenu(State.list);
				RawRecords.setAdapter(this, (ListView)findViewById(R.id.listViewRecords));
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void setMenu(State state) {
		mState = state;
		if (mState == State.list){
			mMenu.setGroupVisible(R.id.group_list, true);
			mMenu.setGroupVisible(R.id.group_record, false);
			
		}
		else {
			mMenu.setGroupVisible(R.id.group_list, false);
			mMenu.setGroupVisible(R.id.group_record, true);
		}
	}

}
