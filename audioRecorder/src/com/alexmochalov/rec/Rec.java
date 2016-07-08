package com.alexmochalov.rec;

import android.app.*;
import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.alexmochalov.audiorecorder.*;
import com.alexmochalov.audiorecorder.R.id;
import com.alexmochalov.audiorecorder.R.layout;
import com.alexmochalov.tag.ArrayAdapterTags;
import com.alexmochalov.tag.DialogSelectTag;
import com.alexmochalov.tag.DialogSelectTag.MyCallback;
import com.alexmochalov.tag.Tag;

import java.io.File;
import java.text.*;
import java.util.*;

public class Rec {
	int id;
	String audioFileName;
	String textFileName;
	long dateTime;
	long duration;
	ArrayList<Tag> tags = new ArrayList<Tag>();
	ArrayList<Integer> ids = new ArrayList<Integer>();
	
	boolean selected = false;	
	
	public Rec(String pAudioFileName) {
		audioFileName = pAudioFileName;
	}
	
	public Rec(int id, String audioFileName, String textFileName, long dateTime, long duration) {
		this.id = id;
		this.audioFileName = audioFileName;
		this.textFileName = textFileName;
		this.dateTime = dateTime;
		this.duration = duration;
		
	}
	
	/**
	 * Create and fill a new RawRecord and create first Tag
	 *  
	 * @param id - identifier of this record in the database
	 * @param audioFileName - name of the audio file in the /rec
	 * @param textFileName - neme of the text file
	 * @param dateTime - date of the recording
	 * @param duration - duration of the record
	 * @param tag_id - identifier of the first tag
	 * @param tag_text - text of the first tag
	 */
	public Rec(int id, String audioFileName, String textFileName, long dateTime, long duration, int tag_id, String tag_text, int tr_id) {
		this.id = id;
		this.audioFileName = audioFileName;
		this.textFileName = textFileName;
		this.dateTime = dateTime;
		this.duration = duration;
		
		if (tag_id > 0){
			tags.add(new Tag(tag_id, tag_text));
			ids.add(tr_id);
		}	
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

		final ArrayAdapterTags adapter = new ArrayAdapterTags(context,
														R.layout.raw_tag
														, tags);
		listView.setAdapter(adapter);

		// Create a new Tag and add to Rec 
		Button btnCreate = (Button) mainView.findViewById(R.id.dialogrecCreate);
		btnCreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Title");

				final EditText input = new EditText(context);
				builder.setView(input);

				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String name = input.getText().toString();
								Tag newTag = new Tag(context, name);
								newTag.addLinkToDatabase(context, id);
								tags.add(newTag);
								adapter.notifyDataSetChanged();
							}
						});
				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});

				builder.show();
			}
		});

		// Select Tag from list
		Button btnSelect = (Button) mainView.findViewById(R.id.dialogrecSelect);
		btnSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogSelectTag dialogSelectTag = new DialogSelectTag(context);
				dialogSelectTag.callback = new MyCallback(){
					@Override
					public void onItemClick(Tag tag) {
						tag.addLinkToDatabase(context, id);
						tags.add(tag);
						adapter.notifyDataSetChanged();
					}};
						
				dialogSelectTag.show();
			}

		});

		// Delete Tag from list
		Button btnDelete = (Button) mainView.findViewById(R.id.dialogrecDelete);
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RecProvider.currentTable = "tr";
				for (int i = tags.size()-1; i >= 0; i--){
					if (tags.get(i).isSelected()){
						ContentResolver cr = context.getContentResolver();
						String w = RecProvider.KEY_ID + " = " + ids.get(i);
						cr.delete(RecProvider.CONTENT_URI, w, null);
						tags.remove(tags.get(i));
					}
				}
				adapter.notifyDataSetChanged();
			}

		});

	}

	public CharSequence getDateTimeStr() {
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
/*
	public void saveToDatabese(Context context) {
		ContentResolver cr = context.getContentResolver();

		String w = null;
		w = RecProvider.KEY_ID + " = " + id;

		ContentValues values = new ContentValues();
		values.put (RecProvider.AUDIOFILENAME, name);
		if (theme != null)
			values.put (TimeLineProvider.GROUP_ID, theme.getId());
		else
			values.put (TimeLineProvider.GROUP_ID, -1);
		values.put (TimeLineProvider.DATE_FROM_COLUMN, dateFrom.getTime().getTime());
		values.put (TimeLineProvider.DATE_TO_COLUMN, dateTo.getTimeInMillis());
		values.put (TimeLineProvider.TYPE_COLUMN, type);
		values.put (TimeLineProvider.DATE_TYPE_COLUMN, dateType);
		values.put (TimeLineProvider.TOP_COLUMN, rect.top);

		TimeLineProvider.currentTable = "info";
		cr.update(TimeLineProvider.CONTENT_URI, values, w, null);
	}	
*/

	public void addTag(int tag_id, String tag_text, int tr_id) {
		if (tag_id > 0){
			tags.add(new Tag(tag_id, tag_text));
			ids.add(tr_id);
		}	
	}	
}
