package com.alexmochalov.audiorecorder;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import java.io.*;

import com.alexmochalov.rec.DialogEditRec;
import com.alexmochalov.rec.Rec;
import com.alexmochalov.rec.Records;

public class MainActivity extends Activity {
	// States of the application.
	public enum State {
		list, list_edit, rec_edit, read, record, play, edit
	}

	public State mState = State.read;

	public static final String PROGRAMM_FOLDER = "xolosoft";
	public static final String APP_FOLDER = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/"
			+ PROGRAMM_FOLDER + "/audiorecorder";
	public static final String REC_FOLDER = APP_FOLDER + "/rec";
	public static final String REC_TEMP = APP_FOLDER + "/temp.3gp";

	public static final String DATABASE_NAME = APP_FOLDER + "/records.db";
	
	Menu mMenu;
	ActionBar actionBar;

	String textFileName;
	
	Rec currentRecord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		Records.loadFromDatabase(this);
		
		checkDirectory(PROGRAMM_FOLDER);
		checkDirectory(APP_FOLDER);
		checkDirectory(REC_FOLDER);

		textFileName = getResources().getString(R.string.text_not_loaded);
	} 

	private void setOnItemCLickListener(ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//setMenu(State.rec_edit);
				currentRecord = Records.get(position);
				DialogEditRec dialogEditRec = new DialogEditRec(MainActivity.this, currentRecord);
				dialogEditRec.show();
				
				//currentRecord.edit(MainActivity.this,
			   	//	findViewById(android.R.id.content));
				

			}
		});
	}

	private void checkDirectory(String dir) {
		File file = new File(dir);
		if (!file.exists())
			file.mkdir();
		else if (!file.isDirectory()) {
			file.delete();
			file.mkdir();
		}
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
		case android.R.id.home: {
			if (mState == State.list_edit) {
				setMenu(State.list);
				Records.setEdit(
						(ListView) findViewById(R.id.listViewRecords), false);
			} else
			if (mState == State.rec_edit) {
				setMenu(State.list);
				//currentRecord.saveToDatabese(MainActivity.this);
				Records.setEdit(
						(ListView) findViewById(R.id.listViewRecords), false);
			} else 
				if (mState == State.rec_edit) {
				setMenu(State.list);
				// currentRecord.saveToDatabese(MainActivity.this);
				Records.setEdit(
						(ListView) findViewById(R.id.listViewRecords), false);
			}
			return true;
		}
		case R.id.action_add: {
			setMenu(State.read); 
			return true;
		}
		case R.id.action_edit: {
			setMenu(State.list_edit);
			Records.setEdit((ListView) findViewById(R.id.listViewRecords),
					true);
			return true;
		}
		case R.id.action_settings: {
			return true;
		}
		case R.id.action_delete:{
			Records.delete(this);
			return true;
		}
		case R.id.action_rec:
			// Start voice recording
			if (Media.startRecording()) {
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
	 * private void setMediaTimer() { if (mState == State.read) { } else if
	 * (mState == State.record) { Media.dropTimer();
	 * //seekBar.setVisibility(View.INVISIBLE); Media.startShowTime(); } else if
	 * (mState == State.play) { Media.startShowTime(); }
	 * 
	 * }
	 */

	//
	void dialogSaveRecord() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

		builder.setTitle(getResources().getString(R.string.dialog_save_audio));

		builder.setMessage(getResources().getString(
				R.string.dialog_save_audio_name));

		final EditText input = new EditText(this);
		input.setText(newRecordName());
		builder.setView(input);

		// Yes Button
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					copy(new File(REC_TEMP), new File(REC_FOLDER + "/"
							+ input.getText().toString() + ".3gp"));
				} catch (IOException e) {
					Toast.makeText(MainActivity.this, e.toString(),
							Toast.LENGTH_LONG).show();
					return;
				}

				Rec rawRecord = new Rec(0, input.getText()
						.toString(), textFileName, Media.getTimeStarting()
						+ Media.getDuration(), Media.getDuration());
				
				Records.add(rawRecord);
				rawRecord.addToDatabase(MainActivity.this);
				setContentViewList();
				// RawRecords.notify((ListView)findViewById(R.id.listViewRecords));
				Records.loadFromDatabase(MainActivity.this);
				dialog.dismiss();
			}
		});

		// No Button
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Toast.makeText(getApplicationContext(),"No button Clicked",Toast.LENGTH_LONG).show();
				// Log.i("Code2care ","No button Clicked!");
				setContentViewList();
				dialog.dismiss();
			}
		});

		// Cancel Button
		builder.setNeutralButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Toast.makeText(getApplicationContext(),"Cancel button Clicked",Toast.LENGTH_LONG).show();
						// Log.i("Code2care ","Cancel button Clicked!");
						dialog.dismiss();
					}
				});

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	private CharSequence newRecordName() {
		for (int i = 0; i < 999999; i++) {
			File file = new File(REC_FOLDER + "/"
					+ getResources().getString(R.string.new_record) + i
					+ ".3gp");
			if (!file.exists())
				return getResources().getString(R.string.new_record) + i;
		}

		return "";
	}

	void setContentViewList() {
		setMenu(State.list); 
		ListView listView  = (ListView) findViewById(R.id.listViewRecords); 
		Records.setAdapter(this,
				listView);
		setOnItemCLickListener(listView);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mState == State.read) {
				if (Media.getDuration() > 0){
					dialogSaveRecord();
				} else
					setContentViewList();
				return true;
				
			} else if (mState == State.list_edit) {
				setMenu(State.list);
				Records.setEdit(
						(ListView) findViewById(R.id.listViewRecords), false);
				return true;
			} else if (mState == State.rec_edit) {
				setContentViewList();
				return true;
			}
		}	
		return super.onKeyDown(keyCode, event);
	}

	private void setMenu(State state) {
		mState = state;
		if (mState == State.list) {
			setContentView(R.layout.activity_main);
			ListView listView  = (ListView) findViewById(R.id.listViewRecords); 
			Records.setAdapter(this,
					listView);			
			setOnItemCLickListener(listView);
			
			mState = State.list;
			
			mMenu.setGroupVisible(R.id.group_list, true);
			mMenu.setGroupVisible(R.id.group_record, false);
			mMenu.setGroupVisible(R.id.group_list_edit, false);
			
			actionBar.setIcon(R.drawable.mic);

		} else if (mState == State.list_edit) {
			mMenu.setGroupVisible(R.id.group_list, false);
			mMenu.setGroupVisible(R.id.group_record, false);
			mMenu.setGroupVisible(R.id.group_list_edit, true);

			actionBar.setIcon(R.drawable.ok1);
			
		} else if (mState == State.rec_edit) {
			setContentView(R.layout.rec);
			
			mMenu.setGroupVisible(R.id.group_list, false);
			mMenu.setGroupVisible(R.id.group_record, false);
			mMenu.setGroupVisible(R.id.group_list_edit, true);

			actionBar.setIcon(R.drawable.ok1);
			
		} else {
			mMenu.setGroupVisible(R.id.group_list, false);
			mMenu.setGroupVisible(R.id.group_list_edit, false);
			// mMenu.setGroupVisible(R.id.group_record, true);
			if (state == State.record) {
				// mMenu.setGroupVisible(R.id.group_edit, false);
				mMenu.setGroupVisible(R.id.group_record, true);

				mMenu.findItem(R.id.action_rec).setIcon(R.drawable.recordu);
				mMenu.findItem(R.id.action_rec).setEnabled(false);

				mMenu.findItem(R.id.action_play).setIcon(R.drawable.playu);
				mMenu.findItem(R.id.action_play).setEnabled(false);

				mMenu.findItem(R.id.action_stop).setIcon(R.drawable.stopc);
				mMenu.findItem(R.id.action_stop).setEnabled(true);

				actionBar.setIcon(R.drawable.ok1);
			} else if (state == State.play) {
				// mMenu.setGroupVisible(R.id.group_edit, false);
				mMenu.setGroupVisible(R.id.group_record, true);

				mMenu.findItem(R.id.action_rec).setIcon(R.drawable.recordu);
				mMenu.findItem(R.id.action_rec).setEnabled(false);

				mMenu.findItem(R.id.action_play).setIcon(R.drawable.playu);
				mMenu.findItem(R.id.action_play).setEnabled(false);

				mMenu.findItem(R.id.action_stop).setIcon(R.drawable.stopc);
				mMenu.findItem(R.id.action_stop).setEnabled(true);

				mMenu.findItem(R.id.action_pause).setIcon(R.drawable.pausec);
				mMenu.findItem(R.id.action_pause).setEnabled(true);

				actionBar.setIcon(R.drawable.ok1);
			} else if (state == State.read) {
				setContentView(R.layout.text);
				// mMenu.setGroupVisible(R.id.group_edit, false);
				mMenu.setGroupVisible(R.id.group_record, true);

				mMenu.findItem(R.id.action_rec).setIcon(R.drawable.recordc);
				mMenu.findItem(R.id.action_rec).setEnabled(true);

				mMenu.findItem(R.id.action_play).setIcon(R.drawable.playc);
				mMenu.findItem(R.id.action_play).setEnabled(true);

				mMenu.findItem(R.id.action_stop).setIcon(R.drawable.stopu);
				mMenu.findItem(R.id.action_stop).setEnabled(false);

				mMenu.findItem(R.id.action_pause).setIcon(R.drawable.pauseu);
				mMenu.findItem(R.id.action_pause).setEnabled(false);

				actionBar.setIcon(R.drawable.ok1);
			} else {
				// mMenu.setGroupVisible(R.id.group_edit, true);
				// mMenu.setGroupVisible(R.id.group_record, false);
				// actionBar.setIcon(R.drawable.mic);
			}

		}
	}

	public void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst); // Transfer bytes from in
														// to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}
}
