package com.alexmochalov.audiorecorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.ListView;

public class RawRecords{
	private static ArrayList<RawRecord> list = new ArrayList<RawRecord>();	
	
	static void setAdapter(Context context, ListView listView ){
		ArrayAdapterRecords adapter = new ArrayAdapterRecords(context,
				  R.layout.raw, list);
		listView.setAdapter(adapter);
		
	}

	public static void add(String string) {
		list.add(new RawRecord(string));
	}
	
	public static void loadFromDatabase(Context context){
		ContentResolver cr = context.getContentResolver();
	
		String[] result_columns = new String[] {
				RecProvider.KEY_ID,
				RecProvider.AUDIOFILENAME,
				RecProvider.TEXTFILENAME,
				RecProvider.DATE,
				RecProvider.DURATION
		};
		
		String where = null;
		String whereArgs[] = null;
		String order = null;
		
		RecProvider.currentTable = "records";
		
		Cursor cursor = cr.query(RecProvider.CONTENT_URI,
				result_columns, where, whereArgs, order);
		
		int n = 1;
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndexOrThrow
					(RecProvider.KEY_ID));
			String audioFileName = cursor.getString(cursor.getColumnIndexOrThrow
					(RecProvider.AUDIOFILENAME));
			String textFileName = cursor.getString(cursor.getColumnIndexOrThrow
					(RecProvider.TEXTFILENAME));
			long date = cursor.getLong(cursor.getColumnIndexOrThrow
					(RecProvider.DATE));
			long duration = cursor.getLong(cursor.getColumnIndexOrThrow
					(RecProvider.DURATION));
			
			// Create new Event 
			RawRecord record = new RawRecord(id, audioFileName, textFileName, date, duration);
			list.add(record);
		}
		
		cursor.close();
		
	}
	
	
}

