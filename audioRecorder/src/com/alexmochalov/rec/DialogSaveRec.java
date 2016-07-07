package com.alexmochalov.rec;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

import java.util.*;

import com.alexmochalov.audiorecorder.R;
import com.alexmochalov.audiorecorder.R.id;
import com.alexmochalov.audiorecorder.R.layout;
import com.alexmochalov.audiorecorder.R.string;
import com.alexmochalov.tag.ArrayAdapterTags;
import com.alexmochalov.tag.Tag;

public class DialogSaveRec extends Dialog
{
	
	Dialog dialog;
	Context mContext;
	RawRecord mRawRecord;
	
	ArrayList<Tag> list = new ArrayList<Tag>();

	public DialogSaveRec(Context context, RawRecord rawRecord) {
		super(context);
		mContext = context;
		mRawRecord = rawRecord;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dialog = this;
		dialog.setTitle(mContext.getResources().getString(R.string.dialog_save_audio));
	 	dialog.setContentView(R.layout.dialog_save_rec);

		final EditText recName = (EditText)dialog.
			findViewById(R.id.dialogsaverecAudioFileName);
		recName.setText(mRawRecord.getAudioFileName());

		TextView t = (TextView)dialog.
				findViewById(R.id.dialogsaverecTextFileName);
		t.setText(mRawRecord.getTextFileName());
		
		t = (TextView)dialog.
				findViewById(R.id.dialogsaverecDate);
		t.setText(mRawRecord.getDateTimeStr());

		t = (TextView)dialog.
				findViewById(R.id.dialogsaverecDuration);
		t.setText(mRawRecord.getDurationStr());
		
		ListView listView = (ListView)findViewById(R.id.dialogsaverecListViewTags);
		
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
					//DialogRecTags dialog = new DialogRecTags(mContext, recName.getText().toString());
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
