package com.alexmochalov.tag;

import android.content.*;
import android.database.Cursor;

import java.text.*;

import com.alexmochalov.audiorecorder.RecProvider;

public class Tag {
	long mId;
	String mText;
	boolean mSelected = false;	
	
	public Tag(String text) {
		mText = text;
	}
	
	public Tag(int id, String text) {
		mId = id;
		mText = text;
	}

	public Tag(Context context, String text) {
		mText = text;
		addToDatabase(context);
	}
	
	public boolean isSelected()
	{
		return mSelected;
	}
	
	public String getText()
	{
		return mText;
	}
		
	public void addToDatabase(Context context) {
		ContentResolver cr = context.getContentResolver();

		//String w = null;
		//w = TimeLineProvider.NAME_COLUMN + " = \"" + name + "\" and "+
		//	TimeLineProvider.GROUP_ID + " = "+themeId;
		//
		RecProvider.currentTable = "tags";
		//Cursor query = cr.query(TimeLineProvider.CONTENT_URI, null, w,
		//		null, null);
		
		//if (query.getCount()==0) {
			
			ContentValues values = new ContentValues();
			values.put (RecProvider.TEXT, mText);
			
			cr.insert(RecProvider.CONTENT_URI, values);
			mId = RecProvider.newRowID;
		//}	
		//query.close();
	}

	public void saveToDatabase(Context context) {
	}

	/**
	 * Adds link between the Tag and the Record to the database
	 * 
	 * @param context
	 * @param id - database identifier of the Record 
	 */
	public void addLinkToDatabase(Context context, int id) {
		ContentResolver cr = context.getContentResolver();

		RecProvider.currentTable = "tr";
		
		ContentValues values = new ContentValues();
		
		String w = RecProvider.KEY_ID_REC + " = \"" + id + "\" and "+
				RecProvider.KEY_ID_TAG + " = "+mId;
		
		Cursor query = cr.query(RecProvider.CONTENT_URI, null, w,
				null, null);
		
		if (query.getCount()==0) { // Check the uniqueness of the link 
			values.put (RecProvider.KEY_ID_REC, id);
			values.put (RecProvider.KEY_ID_TAG, mId);
			cr.insert(RecProvider.CONTENT_URI, values);
		}	
	}

	public long getId() {
		return mId;
	}

	public String getAudioFileName() {
		return null;
	}	
}
