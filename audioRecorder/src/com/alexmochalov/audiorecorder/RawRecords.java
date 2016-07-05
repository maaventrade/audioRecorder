package com.alexmochalov.audiorecorder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RawRecords {
	private static ArrayList<RawRecord> list = new ArrayList<RawRecord>();

	private static ArrayAdapterRecords adapter;

	public static void setEdit(ListView listView,boolean p)
	{
		adapter = (ArrayAdapterRecords)listView.getAdapter();
		adapter.checkBoxIsVisible = p;
		adapter.notifyDataSetChanged();
	}

	public static void add(RawRecord rawRecord)
	{
	}	
	
	static void setAdapter(final Context context, ListView listView ){
		ArrayAdapterRecords adapter = new ArrayAdapterRecords(context,
				  R.layout.raw, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DialogSaveRec dialog = new DialogSaveRec(context, list.get(position));
				dialog.show();
			}});
		
	}

	public static void add(String string) {
		list.add(new RawRecord(string));
	}
	
	public static void notify(ListView listView){
		ArrayAdapterRecords adapter = (ArrayAdapterRecords)listView.getAdapter();
		adapter.notifyDataSetChanged();
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
		
		list.clear();
		
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

	public static void delete(Context context) {
		for (RawRecord r : list){
			if (r.selected){
				ContentResolver cr = context.getContentResolver();
				String w = RecProvider.KEY_ID + " = " + r.id;
				cr.delete(RecProvider.CONTENT_URI, w, null);
				
				File file = new File(MainActivity.REC_FOLDER+"/"+r.audioFileName+".3gp");
				file.delete();
				
			}
		}
		loadFromDatabase(context);
		adapter.notifyDataSetChanged();
	}

	
}

