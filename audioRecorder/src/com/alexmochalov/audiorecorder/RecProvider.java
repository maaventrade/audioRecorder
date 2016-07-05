package com.alexmochalov.audiorecorder;

import java.io.File;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class RecProvider extends ContentProvider
{
	public static long newRowID;
	public static String currentTable;

	// Path to this data source
	public static final Uri CONTENT_URI = 
	Uri.parse("content://com.alexmochalov.recprovider/records");

	public static final String KEY_ID = "_id";
	public static final String DATE = "date";
	public static final String DURATION = "duration";
	public static final String AUDIOFILENAME = "audiofilename";
	public static final String TEXTFILENAME = "textfilename";

	public static final String TEXT = "text";

	public static final String KEY_ID_REC = "_idr";
	public static final String KEY_ID_TAG = "_idt";
	
	
	private FLDatabaseHelper dbHelper;

	private static	final	int INFO	= 1;
	private static	final	int INFO_ID	= 2;
	private static	final	UriMatcher	uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH) ;
		uriMatcher.addURI("com.alexmochalov.recprovider", "records",
						  INFO);
		uriMatcher.addURI("com.alexmochalov.recprovider", "records/#",
						  INFO_ID) ;
	}	

	private static class FLDatabaseHelper extends SQLiteOpenHelper{

		private	static final String	TAG = "RecProvider";

		private static final String DATABASE_NAME = MainActivity.DATABASE_NAME; //"records.db";

		private static final String TABLE_REC = "records";
		private static final String TABLE_TAG = "tags";
		private static final String TABLE_TAG_REC = "tr";

		private static final int DATABASE_VERSION = 4;

		private static final String DATABASE_CREATE_REC = 
		"create table "+
		TABLE_REC +" (" + KEY_ID +
		" integer primary key autoincrement, " +
		DATE + " integer, " +
		DURATION + " integer, " +
		AUDIOFILENAME + " text, "+
		TEXTFILENAME + " text "+
		");" ;

		private static final String DATABASE_CREATE_TAG = 
		"create table "+
		TABLE_TAG +" (" + KEY_ID +
		" integer primary key autoincrement, " +
		TEXT + " text "+
		");" ;

		private static final String DATABASE_CREATE_TAG_REC = 
		"create table "+
		TABLE_TAG_REC +" (" + KEY_ID +
		" integer primary key autoincrement, " +
		KEY_ID_REC + " integer,"+
		KEY_ID_TAG + " integer "+
		");" ;

		//
		//private SQLiteDatabase timelineDB;

		public FLDatabaseHelper(Context context, String name,
								CursorFactory factory, int version) {
			super(context, name, factory, version);
			Log.d(TAG, "version "+version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_REC);
			db.execSQL(DATABASE_CREATE_TAG);
			db.execSQL(DATABASE_CREATE_TAG_REC);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "Upgrading database from version " + oldVersion + " to "
				  + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_REC);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG_REC);
			Log.d("", "DROP");
			onCreate(db);
		}

	}

	
	@Override
	public boolean onCreate() {
		Context context = getContext();
		dbHelper = new FLDatabaseHelper(context,
										FLDatabaseHelper.DATABASE_NAME, null,
										FLDatabaseHelper.DATABASE_VERSION);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
						String[] selectionArgs, String sortOrder) {

		SQLiteDatabase database = dbHelper. getWritableDatabase ();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(currentTable);

		Log.d("", "uriMatcher.match(uri): "+uriMatcher.match(uri));

		//
		switch (uriMatcher.match(uri)) {
			case INFO_ID: qb.appendWhere(KEY_ID + " = " + uri.getPathSegments().get(1));
				break;
			default : break;
		}
		//
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = null;  //DATE_FROM_COLUMN;
		} else {
			orderBy = sortOrder;
		}

		//
		Cursor c = qb.query(database,
							projection,
							selection, selectionArgs,
							null, null,
							orderBy);

		//
		//
		c.setNotificationUri(getContext().getContentResolver(), uri);
		//
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri))	{
			case INFO: return "vnd.android.cursor.dir/alexmochalov.audiorecorder";
			case INFO_ID: return "vnd.android.cursor.item/alexmochalov.audiorecorder";
			default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}	
	}

	public long getNewRowID(){
		return newRowID;
	}

	@Override
	public Uri insert(Uri _uri, ContentValues values) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		newRowID = database.insert(currentTable, "records",
								   values);
		//
		if (newRowID > 0)	{
			Uri uri = ContentUris.withAppendedId(CONTENT_URI, newRowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}
		throw new SQLException("Failed to insert row into " + _uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		int count;
		switch (uriMatcher.match(uri))	{
			case INFO:
				count = database.delete(
					currentTable,
					selection, selectionArgs);
				break;
			case INFO_ID:
				String segment = uri.getPathSegments().get(1);
				count = database.delete(currentTable,
										KEY_ID + "="
										+ segment
										+ (!TextUtils.isEmpty(selection) ? " AND ("
										+ selection + ')'	:	"")	,	selectionArgs);
				break;
			default: throw new IllegalArgumentException
				("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
					  String[] selectionArgs) {
		SQLiteDatabase database = dbHelper.getWritableDatabase() ;
		int count;

		switch (uriMatcher.match(uri))	{
			case INFO:
				count = database.update
				(currentTable,
				 values, selection, selectionArgs);
				break;
			case INFO_ID:
				String segment = uri.getPathSegments().get(1);
				count = database.update
				(currentTable,
				 values, KEY_ID
				 + "=" + segment
				 + (!TextUtils.isEmpty(selection) ? " AND ("
				 + selection + ')'	:	""), selectionArgs);
				break;
			default: throw new IllegalArgumentException
				("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}


}
