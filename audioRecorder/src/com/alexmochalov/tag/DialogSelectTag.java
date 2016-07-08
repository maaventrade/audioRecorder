package com.alexmochalov.tag;
import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

import java.util.*;

import com.alexmochalov.audiorecorder.R;
import com.alexmochalov.audiorecorder.RecProvider;
import com.alexmochalov.audiorecorder.R.id;
import com.alexmochalov.audiorecorder.R.layout;

public class DialogSelectTag extends Dialog
{
	Dialog dialog;
	Context mContext;
	
	public MyCallback callback = null;
	public interface MyCallback {
		void onItemClick(Tag tag); 
	} 
	
	ArrayList<Tag> list = new ArrayList<Tag>();

	public DialogSelectTag(Context context) {
		super(context);
		mContext = context;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dialog = this;
		dialog.setTitle("tags");
	 	dialog.setContentView(R.layout.dialog_select_tag);

		ListView listView = (ListView)findViewById(R.id.tagsList);

		loadFromDatabase(mContext);
		
		ArrayAdapterTags adapter = new ArrayAdapterTags(mContext,
												R.layout.raw_tag
												, list);
		adapter.setCheckBoxIsVisible(false);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					if (callback != null)
						callback.onItemClick(list.get(position));
					dismiss();
				}});
		
	}
	
	public void loadFromDatabase(Context context){
		
		ContentResolver cr = context.getContentResolver();
	
		String[] result_columns = new String[] {
				RecProvider.KEY_ID,
				RecProvider.TEXT
		};
		
		String where = null;
		String whereArgs[] = null;
		String order = null;
		
		RecProvider.currentTable = "tags";
		
		Cursor cursor = cr.query(RecProvider.CONTENT_URI,
				result_columns, where, whereArgs, order);
		
		list.clear();
		
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndexOrThrow
					(RecProvider.KEY_ID));
			String text = cursor.getString(cursor.getColumnIndexOrThrow
					(RecProvider.TEXT));
			
			// Create new Event 
			Tag tag = new Tag(id, text);
			list.add(tag);
		}
		
		cursor.close();
		
	}
	
}
