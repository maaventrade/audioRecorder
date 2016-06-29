package com.alexmochalov.audiorecorder;

import java.util.ArrayList;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Typeface;

/**
 * 
 * @author Alexey Mochalov
 * This Adapter shows the list of the audio files
 *
 */
public class ArrayAdapterRecords extends ArrayAdapter<RawRecord>{
	private ArrayList<RawRecord> values;
	Context context;
	int resource;

	public ArrayAdapterRecords(Context context, int res, ArrayList<RawRecord> values){
		super(context, res, values);
		this.values = values;
		this.resource = res;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) { 
			LayoutInflater inflater = (LayoutInflater) context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			convertView = inflater.inflate(R.layout.raw, null);
		}

		RawRecord entry = values.get(position);

		TextView text = (TextView)convertView.findViewById(R.id.textViewAudioFile);
		text.setText(entry.mAudioFileName);

		return convertView;
	}

	public int getCount(){
		return values.size();
	}

	public long getItemId(int position){
		return position;
	}	

}

