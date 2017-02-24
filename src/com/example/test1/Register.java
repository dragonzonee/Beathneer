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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Register extends Activity{
		private EditText mynum,mypwd,pwdrepeat;
	   private Button btnAdd;
	   private DBHelper dbHelper;
	   private SQLiteDatabase sqlitedatabases;
	   private static int flag;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		mynum=(EditText) findViewById(R.id.regist_Num);
		mypwd=(EditText) findViewById(R.id.regist_Pwd);
		pwdrepeat = (EditText)findViewById(R.id.pwdrepeat);
		btnAdd=(Button) findViewById(R.id.btn_regist);
		dbHelper=new DBHelper(this,"Info.db");
		btnAdd.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(mynum.getText().toString().equals("")){
					Toast.makeText(Register.this,"�˺Ų���Ϊ�� ", Toast.LENGTH_SHORT).show();
					return;
				}
				if(mypwd.getText().toString().equals("")){
					Toast.makeText(Register.this,"���벻��Ϊ�� ", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String name = mynum.getText().toString();
				String pwd = mypwd.getText().toString();
				String name1 = new Security().Encryption(name);
				String pwd1 = new Security().Encryption(pwd);
				SQLiteDatabase db = new DBHelper(Register.this, "Info.db").getReadableDatabase();
				Cursor cursor = db.query("people",new String[] {"name"},null,null, null, null, null);
				while (cursor.moveToNext()) { 
					String name2 = cursor.getString(cursor.getColumnIndex("name"));
					
					if(name1.endsWith(name2)){
						flag=1;
						Toast.makeText(Register.this, "�˺����Ѿ��� ����������", Toast.LENGTH_LONG).show();
						mynum.setText("");
					}
				}
				System.out.println(flag);
				
				if(flag == 0){if(mypwd.getText().toString().equals(pwdrepeat.getText().toString())){
					dbHelper.insert(name1,pwd1);
					//SqlLiteLoginInfoActivity.cursor=dbHelper.select(); 
					Toast.makeText(Register.this, "ע��ɹ���", Toast.LENGTH_LONG).show();
					Intent intent=new Intent();
					intent.putExtra("name",name);
					intent.setClass(Register.this, BluetoothActivity.class);
					startActivity(intent);
				}
				else{
					Toast.makeText(Register.this, "���벻һ�� ����������",Toast.LENGTH_SHORT).show();
					mypwd.setText("");
					pwdrepeat.setText("");
					}
			}
				
			}
		});
		}
	}

