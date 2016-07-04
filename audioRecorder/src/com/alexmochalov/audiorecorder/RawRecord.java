package com.alexmochalov.audiorecorder;

import android.content.*;
import java.text.*;

public class RawRecord {
	int id;
	String audioFileName;
	String textFileName;
	long dateTime;
	long duration;
	
	public RawRecord(String pAudioFileName) {
		audioFileName = pAudioFileName;
	}
	
	public RawRecord(int id, String audioFileName, String textFileName, long dateTime, long duration) {
		this.id = id;
		this.audioFileName = audioFileName;
		this.textFileName = textFileName;
		this.dateTime = dateTime;
		this.duration = duration;
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
	
}
