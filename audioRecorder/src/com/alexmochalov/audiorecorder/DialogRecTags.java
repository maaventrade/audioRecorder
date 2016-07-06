package com.alexmochalov.audiorecorder;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

import java.util.*;

public class DialogRecTags extends Dialog
{
	
	Dialog dialog;
	Context mContext;
	String mRecName;
	//RawRecord mRawRecord;
	
	ArrayList<Tag> list = new ArrayList<Tag>();

	public DialogRecTags(Context context, String recName) {
		super(context);
		mContext = context;
		mRecName = recName;
		//mRawRecord = rawRecord;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dialog = this;
		dialog.setTitle(mContext.getResources().getString(R.string.dialog_rec_tags)+" <"+mRecName+">");
	 	dialog.setContentView(R.layout.dialog_rec_tags);

		ListView listView = (ListView)findViewById(R.id.dialogrectagList);
		
		list.add(new Tag("11212"));
		list.add(new Tag("asdadasd"));
		
		ArrayAdapterTags adapter = new ArrayAdapterTags(mContext,
												R.layout.raw_tag
												, list);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					//DialogSaveRec dialog = new DialogSaveRec(mContext, list.get(position));
					//dialog.show();
				}});
		
		/*

		Button b = (Button)dialog.findViewById(R.id.dialogmodegroupprefButtonOk);
		b.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) 
				{
					int i;
					EditText e = (EditText)dialog.
						findViewById(R.id.editTextCount);

					int max_count = toInt(e.getText().toString(), 3);
					Editor editor = prefs.edit();
					editor.putInt("count", Math.max(max_count, 2));

					e = (EditText)dialog.
						findViewById(R.id.editTextTimeAnswer);
					int time_answer = toInt(e.getText().toString(), 4);
					editor.putInt("time_answer", Math.max(time_answer, 1));

					e = (EditText)dialog.
						findViewById(R.id.editTextTimeBetweenGroups);
					int time_between_groups = toInt(e.getText().toString(), 2);
					editor.putInt("time_between_groups", Math.max(time_between_groups, 1));
					editor.apply();

					Params.timeWaiting = time_answer;
					Params.timeBetween = time_between_groups;
					mSurface.setMaxCount(max_count);

					dialog.dismiss();
				}

			});

		b = (Button)dialog.findViewById(R.id.dialogmodegroupprefButtonCancel);
		b.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View d)
				{
					dialog.dismiss();
				}
			});
			
			*/
	}

	protected int toInt(String string, int defaultValue) {
		try{
			return Integer.parseInt(string); 
		} catch(Exception e){
			return defaultValue;
		}
	}
}
