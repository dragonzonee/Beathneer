package com.example.test1;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class pwdback extends Activity implements OnClickListener{

	private EditText nameback,mailback;
	private Button back;
	private Spinner Qspinner1;
	private int flag;
	//private static String nameback1,mailback1;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.pwdback);
	       
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
