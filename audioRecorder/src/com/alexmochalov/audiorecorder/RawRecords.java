package com.alexmochalov.audiorecorder;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.widget.ListView;

public class RawRecords{
	private static ArrayList<RawRecord> list = new ArrayList<RawRecord>();	
	
	static void setAdapter(Context context, ListView listView ){
		ArrayAdapterRecords adapter = new ArrayAdapterRecords(context,
				  R.layout.raw, list);
		listView.setAdapter(adapter);
		
	}

	public static void add(String string) {
		list.add(new RawRecord(string));
		
	}
	
}

