package com.example.test1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class ZhongjianActivity extends Activity {
	private Button bt01;
	private String personname;
	private TextView personInfo;
	private String uploadtime;
	private TextView uploadview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhongjian);
		personInfo = (TextView)findViewById(R.id.textView11);
		Intent intent = getIntent();
		personname = intent.getStringExtra("name");
		personInfo.setText("»¶Ó­µÇÂ½ "+personname);
		uploadview = (TextView)findViewById(R.id.textView12);
		Intent intent2 =getIntent();
		uploadtime = intent.getStringExtra("fileName");
		uploadview.setText("µÇÂ¼Ê±¼ä:"+uploadtime);
		
		bt01 = (Button)findViewById(R.id.button1);
		bt01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ZhongjianActivity.this, JieguoActivity.class);
				startActivity(intent);
			}
		});
	}
	
	
}
