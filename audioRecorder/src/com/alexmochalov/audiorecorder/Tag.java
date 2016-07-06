package com.alexmochalov.audiorecorder;

import android.content.*;

import java.text.*;

public class Tag {
	int mId;
	String mText;
	
	boolean selected = false;	
	
	public Tag(String text) {
		mText = text;
	}
	
	public Tag(int id, String text) {
		mId = id;
		mText = text;
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
		//}	
		//query.close();
	}

}
