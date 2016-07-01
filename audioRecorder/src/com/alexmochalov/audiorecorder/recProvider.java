package com.alexmochalov.audiorecorder;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class recProvider extends ContentProvider
{
	public static long newRowID;

	// Path to this data source
	public static final Uri CONTENT_URI = 
	Uri.parse("content://com.xolo.recprovider/records");

	public static final String KEY_ID = "_id";
	public static final String DATE = "date";
	public static final String TIME = "time";
	public static final String FILENAME = "filename";
	public static final String TEXTFILENAME = "textfilename";

	private FLDatabaseHelper dbHelper;

	private static	final	int INFO	= 1;
	private static	final	int INFO_ID	= 2;
	private static	final	UriMatcher	uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH) ;
		uriMatcher.addURI("com.xolo.recprovider", "records",
						  INFO);
		uriMatcher.addURI("com.xolo.recprovider", "records/#",
						  INFO_ID) ;
	}	

	private static class FLDatabaseHelper extends SQLiteOpenHelper{

		private	static final String	TAG = "FLProvider";

		private static final String DATABASE_NAME = "results.db";

		private static final String TABLE_RES = "results";

		private static final int DATABASE_VERSION = 1;

		private static final String DATABASE_CREATE = 
		"create table "+
		TABLE_RES +" (" + KEY_ID +
		" integer primary key autoincrement, " +
		DATE + " integer, " +
		COUNT + " integer, " +
		TIME + " integer, " +
		MIDDLE + " float, " +
		BEST + " float, "+
		DOWN + " boolean, "+
		MOVE+ " boolean, "+
		COMMENT + " text "+
		");" ;

		//
		private SQLiteDatabase timelineDB;

		public FLDatabaseHelper(Context context, String name,
								CursorFactory factory, int version) {
			super(context, name, factory, version);
			Log.d(TAG, "version "+version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d("","CREATE");
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "Upgrading database from version " + oldVersion + " to "
				  + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_RES);
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

		qb.setTables(FLDatabaseHelper.TABLE_RES);

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
			case INFO: return "vnd.android.cursor.dir/xolo.timeline";
			case INFO_ID: return "vnd.android.cursor.item/xolo.timeline";
			default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}	
	}

	public long getNewRowID(){
		return newRowID;
	}

	@Override
	public Uri insert(Uri _uri, ContentValues values) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		newRowID = database.insert(FLDatabaseHelper.TABLE_RES, "results",
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
					FLDatabaseHelper.TABLE_RES,
					selection, selectionArgs);
				break;
			case INFO_ID:
				String segment = uri.getPathSegments().get(1);
				count = database.delete(FLDatabaseHelper.TABLE_RES,
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
				(FLDatabaseHelper.TABLE_RES,
				 values, selection, selectionArgs);
				break;
			case INFO_ID:
				String segment = uri.getPathSegments().get(1);
				count = database.update
				(FLDatabaseHelper.TABLE_RES,
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
