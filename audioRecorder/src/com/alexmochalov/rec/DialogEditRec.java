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

	public DialogEditRec(Context context, Rec record) {
		super(context);
		mContext = context;
		mRecord = record;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
Log.d("","create");
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

		// Create a new Tag and add to Rec 
		Button btnCreate = (Button) findViewById(R.id.dialogrecCreate);
		btnCreate.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Title");

				final EditText input = new EditText(mContext);
				builder.setView(input);

				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String name = input.getText().toString();
								Tag newTag = new Tag(mContext, name);
								newTag.addLinkToDatabase(mContext, mRecord.id);
								mRecord.tags.add(newTag);
								adapter.notifyDataSetChanged();
							}
						});
				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});

				builder.show();
			}
		});

		// Select Tag from list
		Button btnSelect = (Button) findViewById(R.id.dialogrecSelect);
		btnSelect.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogSelectTag dialogSelectTag = new DialogSelectTag(mContext);
				dialogSelectTag.callback = new MyCallback(){
					@Override
					public void onItemClick(Tag tag) {
						tag.addLinkToDatabase(mContext, mRecord.id);
						mRecord.tags.add(tag);
						adapter.notifyDataSetChanged();
					}};
						
				dialogSelectTag.show();
			}

		});

		// Delete Tag from list
		Button btnDelete = (Button) findViewById(R.id.dialogrecDelete);
		btnDelete.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				RecProvider.currentTable = "tr";
				for (int i = mRecord.tags.size()-1; i >= 0; i--){
					if (mRecord.tags.get(i).isSelected()){
						ContentResolver cr = mContext.getContentResolver();
						String w = RecProvider.KEY_ID + " = " + mRecord.ids.get(i);
						cr.delete(RecProvider.CONTENT_URI, w, null);
						mRecord.tags.remove(mRecord.tags.get(i));
					}
				}
				adapter.notifyDataSetChanged();
			}

		});
		
	}

}
