package com.example.test1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper extends SQLiteOpenHelper{
//	private static final String TAG = "DBHelper";
	
	private final static int DB_VERSION=1;
	private final static String DB_NAME="Info.db";
	private final static String TABLE_NAME="people";
	private final static String USER_NAME= "name";
	private final static String USER_PSW="pwd";
	private final static String USER_SEX="sex";
	private final static String USER_RESOURCE="resource";
	private final static String USER_POSITION="position";
	private final static String USER_QUESTION="question";
	private final static String USER_MAIL="mail";
	private final static String OS_GRADE="os";
	private final static String NETWORK_GRADE="NetWork";
	private final static String DBASE_GRADE="DBase";
	//private SQLiteDatabase SQLdb = null;
	//private  final static String ID="_id";
	  
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context,DB_NAME , null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}
	public DBHelper(Context context,String name){
		this(context,name,DB_VERSION);
	}
	public DBHelper(Context context, String name, int version) {
		this(context,name,null,version);
	}


	 
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL ("create table "+TABLE_NAME+"(id integer primary key autoincrement,"+USER_NAME+" text, "+USER_PSW+" text,"+USER_SEX+" text," +
				""+USER_QUESTION+" text,"+OS_GRADE+" text,"+NETWORK_GRADE+" text, "+DBASE_GRADE+" text, "+USER_POSITION+" text default '学生' ,"+USER_RESOURCE+" text  default null,"+USER_MAIL+" text)");
       db.execSQL("insert into "+TABLE_NAME+"(name,pwd,sex,position,question,resource,Mail) values(?,?,?,?,?,?,?)", new String[]{"benjo","benjo","男","班主任","你的出生地在哪?","ABCDE","河南"});


		
	}


	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}
	
	public void insert(String name,String pwd){
		//System.out.println("被调用1");
		SQLiteDatabase db=this.getWritableDatabase();
		//System.out.println("被调用1。1");
		db.execSQL("insert into "+TABLE_NAME+"(name,pwd) values(?,?)", new String[]{name,pwd});
//	System.out.println("被调用2");
	}
}

		

	
	

