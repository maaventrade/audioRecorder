package com.alexmochalov.rec;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;

import com.alexmochalov.audiorecorder.MainActivity;
import com.alexmochalov.audiorecorder.R;
import com.alexmochalov.audiorecorder.RecProvider;
import com.alexmochalov.audiorecorder.R.layout;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Records {
	private static ArrayList<Rec> list = new ArrayList<Rec>();

	private static ArrayAdapterRecords adapter;

	public static void setEdit(ListView listView,boolean p)
	{
		adapter = (ArrayAdapterRecords)listView.getAdapter();
		adapter.checkBoxIsVisible = p;
		adapter.notifyDataSetChanged();
	}

	public static void add(Rec rawRecord)
	{
	}	
	
	public static void setAdapter(final Context context, ListView listView ){
		ArrayAdapterRecords adapter = new ArrayAdapterRecords(context,
				  R.layout.raw, list);
		listView.setAdapter(adapter);		
	}

	//public static void add(String string) {
	//	list.add(new Rec(string));
	//}
	
	public static void notify(ListView listView){
		ArrayAdapterRecords adapter = (ArrayAdapterRecords)listView.getAdapter();
		adapter.notifyDataSetChanged();
	}
	
	public static void loadFromDatabase(Context context){

		ContentResolver cr = context.getContentResolver();
	
		String where = null;
		String whereArgs[] = null;
		String order = "REC._id DESC";
		
		RecProvider.currentTable = "records as REC left join tr as TR on REC._id = TR._idr left join tags as TG on TG._id = TR._idt ";
	    String result_columns[] = { "REC.audiofilename",
				 "REC.textfilename",
				 "REC.duration",
				 "REC.date",
				 "REC._id as rec_id",
				 "TG._id as tag_id",
				 "TG.text as tag_text",
				 "TR._id as tr_id" };
		
		Cursor cursor = cr.query(RecProvider.CONTENT_URI,
				result_columns, where, whereArgs, order);
		
		list.clear();
		int id0 = -1;
		while (cursor.moveToNext()) {
			
			int id = cursor.getInt(cursor.getColumnIndexOrThrow
					("rec_id"));
			int tag_id = cursor.getInt(cursor.getColumnIndexOrThrow
					("tag_id"));
			int tr_id = cursor.getInt(cursor.getColumnIndexOrThrow
					("tr_id"));
			String tag_text = cursor.getString(cursor.getColumnIndexOrThrow
					("tag_text"));
			
			String audioFileName = cursor.getString(cursor.getColumnIndexOrThrow
					("audiofilename"));
			String textFileName = cursor.getString(cursor.getColumnIndexOrThrow
					("textfilename"));
			long date = cursor.getLong(cursor.getColumnIndexOrThrow
					("date"));
			long duration = cursor.getLong(cursor.getColumnIndexOrThrow
					("duration"));
			
			// Create new Event
			if (id0 != id){
				Rec record = new Rec(id, audioFileName, textFileName, date, duration,  tag_id, tag_text, tr_id);
				list.add(record);
				id0 = id;
			} else {
				Rec record =  list.get(list.size()-1);
				record.addTag(tag_id, tag_text, tr_id);
			}
			
		}
		
		cursor.close();
		
	}

	public static void delete(Context context) {
		RecProvider.currentTable = "records";
		for (Rec r : list){
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

	public static Rec get(int position) {
		return list.get(position);
	}

	
}

