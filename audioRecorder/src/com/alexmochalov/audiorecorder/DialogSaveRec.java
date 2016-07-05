package com.alexmochalov.audiorecorder;
import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;

public class DialogSaveRec extends Dialog
{
	
	Dialog dialog;
	Context mContext;

	public DialogSaveRec(Context context) {
		super(context);
		mContext = context;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dialog = this;
		dialog.setTitle(mContext.getResources().getString(R.string.dialog_save_audio));
	 	dialog.setContentView(R.layout.dialog_save_rec);

		EditText e = (EditText)dialog.
			findViewById(R.id.dialogsaverecEditText1);
		//e.setText(""+i);

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

	
	
{
}
