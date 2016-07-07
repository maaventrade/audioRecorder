package com.alexmochalov.rec;

import android.content.*;
import android.view.*;
import android.widget.*;
import android.widget.CheckBox.*;

import java.text.*;
import java.util.*;

import com.alexmochalov.audiorecorder.R;
import com.alexmochalov.audiorecorder.R.id;
import com.alexmochalov.audiorecorder.R.layout;

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
	
	boolean checkBoxIsVisible = false;
	int checkBoxWidth = 0;

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
		text.setText(entry.audioFileName);

		text = (TextView)convertView.findViewById(R.id.textViewTextFile);
		text.setText(entry.textFileName);
		
		text = (TextView)convertView.findViewById(R.id.textViewDateTime);
		text.setText(entry.getDateTimeStr());
		
		text = (TextView)convertView.findViewById(R.id.textViewDuration);
		text.setText(entry.getDurationStr());
		
		CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.rawCheckBox1);
		if (checkBoxIsVisible){
			checkBox.setVisibility(View.VISIBLE);
			checkBox.setWidth(80); /// ????
		} else {
			if (checkBox.getWidth() > 0)
				checkBoxWidth = checkBox.getWidth();
			checkBox.setVisibility(View.INVISIBLE);
			checkBox.setWidth(0);
		}		
		
		// присваиваем чекбоксу обработчик
		checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView,
			        boolean isChecked) {
		    	values.get((Integer)buttonView.getTag()).selected = isChecked;
			    }
		});
		
		// пишем позицию
		checkBox.setTag(position);
		// заполняем данными из товаров: в корзине или нет
		//checkBox.setChecked(p.box);		

		return convertView;
	}

	
	public int getCount(){
		return values.size();
	}

	public long getItemId(int position){
		return position;
	}	

}

