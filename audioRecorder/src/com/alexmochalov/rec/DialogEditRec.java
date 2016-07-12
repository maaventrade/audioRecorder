package com.alexmochalov.rec;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.alexmochalov.audiorecorder.*;
import com.alexmochalov.tag.*;
import com.alexmochalov.tag.DialogSelectTag.*;

import java.util.*;

public class DialogEditRec extends Dialog
{
	Context mContext;
	Rec mRecord;
	
	ArrayList<Tag> list = new ArrayList<Tag>();

	public MyCallback callback = null;
	public interface MyCallback {
		void onButtonPressed(int par); 
	} 
	
	public DialogEditRec(Context context, Rec record) {
		super(context);
		mContext = context;
		mRecord = record;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setTitle(mContext.getResources().getString(R.string.dialog_save_audio));
	 	setContentView(R.layout.rec);
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

		EditText recName = (EditText)findViewById(R.id.dialogsaverecAudioFileName);
		recName.setText(mRecord.audioFileName);

		TextView t = (TextView)findViewById(R.id.dialogsaverecTextFileName);
		t.setText(mRecord.textFileName);
     
		t = (TextView)findViewById(R.id.dialogsaverecDate);
		t.setText(mRecord.getDateTimeStr());

		t = (TextView)findViewById(R.id.dialogsaverecDuration);
		t.setText(mRecord.getDurationStr());

		ListView listView = (ListView)findViewById(R.id.dialogrecListViewTags);

		final ArrayAdapterTags adapter = new ArrayAdapterTags(mContext,
														R.layout.raw_tag
														, mRecord.tags);
		adapter.setCheckBoxIsVisible(true);
		listView.setAdapter(adapter);

		Button btn = (Button) findViewById(R.id.recButtonNo);
		btn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (callback != null)
					callback.onButtonPressed(0);
				dismiss();
			}
		});

	}

}
