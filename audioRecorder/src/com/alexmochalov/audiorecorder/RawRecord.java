package com.alexmochalov.audiorecorder;

import android.app.*;
import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.alexmochalov.audiorecorder.*;
import java.text.*;
import java.util.*;

public class RawRecord {
	int id;
	String audioFileName;
	String textFileName;
	long dateTime;
	long duration;
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	
	boolean selected = false;	
	
	public RawRecord(String pAudioFileName) {
		audioFileName = pAudioFileName;
	}
	
	public RawRecord(int id, String audioFileName, String textFileName, long dateTime, long duration) {
		this.id = id;
		this.audioFileName = audioFileName;
		this.textFileName = textFileName;
		this.dateTime = dateTime;
		this.duration = duration;
		tags.add(new Tag("1111"));
	}

	public void edit(final Context context, View mainView)
	{
		EditText recName = (EditText)mainView.findViewById(R.id.dialogsaverecAudioFileName);
		recName.setText(audioFileName);

		TextView t = (TextView)mainView.findViewById(R.id.dialogsaverecTextFileName);
		t.setText(textFileName);

		t = (TextView)mainView.findViewById(R.id.dialogsaverecDate);
		t.setText(getDateTimeStr());

		t = (TextView)mainView.findViewById(R.id.dialogsaverecDuration);
		t.setText(getDurationStr());

		ListView listView = (ListView)mainView.findViewById(R.id.dialogrecListViewTags);

		ArrayAdapterTags adapter = new ArrayAdapterTags(context,
														R.layout.raw_tag
														, tags);
		listView.setAdapter(adapter);


		Button btnCreate = (Button)mainView.findViewById(R.id.dialogrecCreate);
		btnCreate.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Title");

					final EditText input = new EditText(context);
					builder.setView(input);

					// Set up the buttons
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
						    @Override
						    public void onClick(DialogInterface dialog, int which) {
						     	String name = input.getText().toString();
								Tag newTag = new Tag(context, name);
						    }
						});
					builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						    @Override
						    public void onClick(DialogInterface dialog, int which) {
						        dialog.cancel();
						    }
						});

					builder.show();					}});
		
	}

	public CharSequence getDateTimeStr()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		return sdf.format(dateTime);
	}
	

	public CharSequence getDurationStr()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(duration);
	}
	
	public String getAudioFileName(){
		return audioFileName;
	}
	
	public void addToDatabase(Context context) {
		ContentResolver cr = context.getContentResolver();

		//String w = null;
		//w = TimeLineProvider.NAME_COLUMN + " = \"" + name + "\" and "+
		//	TimeLineProvider.GROUP_ID + " = "+themeId;
		//
		RecProvider.currentTable = "records";
		//Cursor query = cr.query(TimeLineProvider.CONTENT_URI, null, w,
		//		null, null);
		
		//if (query.getCount()==0) {
			
			ContentValues values = new ContentValues();
			values.put (RecProvider.DATE, dateTime);
			values.put (RecProvider.DURATION, duration);
			values.put (RecProvider.AUDIOFILENAME, audioFileName);
			values.put (RecProvider.TEXTFILENAME, textFileName);
			
			cr.insert(RecProvider.CONTENT_URI, values);
		//}	
		//query.close();
	}

	public CharSequence getTextFileName() {
		return textFileName;
	}	
	
}
