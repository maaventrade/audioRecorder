package com.alexmochalov.audiorecorder;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
	// States of the application.
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
				if (Media.startRecording()){
					setMenu(State.record);
				}
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

	/*
	private void setMediaTimer() {
		if (mState == State.read) {
		} else if (mState == State.record) {
			Media.dropTimer();
			//seekBar.setVisibility(View.INVISIBLE);
			Media.startShowTime();
		} else if (mState == State.play) {
			Media.startShowTime();
		}

	}
	*/
	
	//
	void dialogSaveRecord(){
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

		builder.setTitle(getResources().getString(R.string.dialog_save_audio));

        builder.setMessage(getResources().getString(R.string.dialog_save_audio_name));
		
		final TextView tvDuration = new TextView(this);
		tvDuration.setText("Dfddhgd");
        builder.setView(tvDuration);
		
        final EditText input = new EditText(this);
        builder.setView(input);
        
        //Yes Button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(),"Yes button Clicked",Toast.LENGTH_LONG).show();
                //Log.i("Code2care ", "Yes button Clicked!");
            	setContentViewList();
                dialog.dismiss();
            }
        });

        //No Button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(),"No button Clicked",Toast.LENGTH_LONG).show();
                //Log.i("Code2care ","No button Clicked!");
            	setContentViewList();
                dialog.dismiss();
            }
        });

        //Cancel Button
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(),"Cancel button Clicked",Toast.LENGTH_LONG).show();
                //Log.i("Code2care ","Cancel button Clicked!");
                dialog.dismiss();
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();	
      }
	
	void setContentViewList(){
		setContentView(R.layout.activity_main);
		setMenu(State.list);
		RawRecords.setAdapter(this, (ListView)findViewById(R.id.listViewRecords));
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mState == State.read) {
				//if (Media.getDuration() > 0){
					dialogSaveRecord();
				//}
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
			//mMenu.setGroupVisible(R.id.group_record, true);
			if (state == State.record) {
				//mMenu.setGroupVisible(R.id.group_edit, false);
				mMenu.setGroupVisible(R.id.group_record, true);

				mMenu.findItem(R.id.action_rec).setIcon(R.drawable.recordu);
				mMenu.findItem(R.id.action_rec).setEnabled(false);
				
				mMenu.findItem(R.id.action_play).setIcon(R.drawable.playu);
				mMenu.findItem(R.id.action_play).setEnabled(false);

				mMenu.findItem(R.id.action_stop).setIcon(R.drawable.stopc);
				mMenu.findItem(R.id.action_stop).setEnabled(true);
				
				//actionBar.setIcon(R.drawable.pencil);
				//actionBar.setTitle("");
			} else if (state == State.play) {
				//mMenu.setGroupVisible(R.id.group_edit, false);
				mMenu.setGroupVisible(R.id.group_record, true);

				mMenu.findItem(R.id.action_rec).setIcon(R.drawable.recordu);
				mMenu.findItem(R.id.action_rec).setEnabled(false);
				
				mMenu.findItem(R.id.action_play).setIcon(R.drawable.playu);
				mMenu.findItem(R.id.action_play).setEnabled(false);

				mMenu.findItem(R.id.action_stop).setIcon(R.drawable.stopc);
				mMenu.findItem(R.id.action_stop).setEnabled(true);

				mMenu.findItem(R.id.action_pause).setIcon(R.drawable.pausec);
				mMenu.findItem(R.id.action_pause).setEnabled(true);
				
				//actionBar.setIcon(R.drawable.pencil);
			    //actionBar.setTitle("");
			} else if (state == State.read) { 
				//mMenu.setGroupVisible(R.id.group_edit, false);
				mMenu.setGroupVisible(R.id.group_record, true);

				mMenu.findItem(R.id.action_rec).setIcon(R.drawable.recordc);
				mMenu.findItem(R.id.action_rec).setEnabled(true);

				mMenu.findItem(R.id.action_play).setIcon(R.drawable.playc);
				mMenu.findItem(R.id.action_play).setEnabled(true);

				mMenu.findItem(R.id.action_stop).setIcon(R.drawable.stopu);
				mMenu.findItem(R.id.action_stop).setEnabled(false);

				mMenu.findItem(R.id.action_pause).setIcon(R.drawable.pauseu);
				mMenu.findItem(R.id.action_pause).setEnabled(false);
				
				//actionBar.setIcon(R.drawable.pencil);
				
			    //actionBar.setTitle("");
			    //actionBar.setSubtitle("");
			} else {
				//mMenu.setGroupVisible(R.id.group_edit, true);
				//mMenu.setGroupVisible(R.id.group_record, false);
		        //actionBar.setIcon(R.drawable.mic);
			}
			
		}
	}

}
