package com.example.test1;
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

import com.example.test1.*;
import com.example.test1.R.color;

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

public class JieguoActivity extends Activity {
	private InputStream btInput;
	private DrawThread dt;
	private SurfaceView sfv;
	private SurfaceHolder sh;
	private Canvas canvas;
	private Paint paint;
	private Paint paint1;
	private Paint paint2;
	private String fileName;
	private float oldY = 0;
	private int height;
	private int width;
	private boolean hasClosed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jieguo);
		Intent intent = getIntent();
		fileName = intent.getStringExtra("fileName");
		try {
			btInput = BluetoothActivity.btSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		paint = new Paint();//
		paint.setColor(color.antiquewhite);//
		paint.setStrokeWidth(20);//
		paint.setTextSize(100);
		paint2 = new Paint();//
		paint2.setColor(color.antiquewhite);//
		paint2.setStrokeWidth(5);//
		paint2.setTextSize(60);
		paint1 = new Paint();//
		paint1.setColor(color.bisque);//
		paint1.setStrokeWidth(40);//
		sfv = (SurfaceView) this.findViewById(R.id.sfvWave);
		sh = sfv.getHolder();
		sh.addCallback(new Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				height = sfv.getHeight();
				width = sfv.getWidth();
				dt = new DrawThread(holder, fileName);
				dt.start();
			}
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if(!hasClosed){
					closeAll();
					hasClosed = true;
				}
			}
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}
		});
	}
	
	public class Draw extends TimerTask{
		private SurfaceHolder holder;
		private byte[] temp;
		private String filePath;
		private File destDir;
		private File fileTarget_all;
		private RandomAccessFile raf_all;
		private File fileTarget_WAVE1;
		private RandomAccessFile raf_WAVE1;
		
		
		public Draw(SurfaceHolder holder, String fileName){
			this.holder = holder;
			temp = new byte[1024];
			if(sdCardIsExist()){
				filePath = Environment.getExternalStorageDirectory().getPath() 
						+ "/BloodPressureApp/RceievedData/" + fileName + "/";
				destDir = new File(filePath);
				if (!destDir.exists()) {
					destDir.mkdirs();
				}
				fileTarget_all = new File(filePath + fileName + "_all" + ".txt");
				fileTarget_WAVE1 = new File(filePath + fileName + "_pm25" + ".txt");
				try {
					raf_all = new RandomAccessFile(fileTarget_all,"rw");
					raf_WAVE1 = new RandomAccessFile(fileTarget_WAVE1,"rw");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}
		@Override
		public void run() {
			try {
				int len = btInput.read(temp);
				if (-1 != len) {
					byte[] buffer = new byte[len];
					System.arraycopy(temp, 0, buffer, 0, len);
					Map<String, List<Float>> pinInput = getZBuf(buffer, 1);
					if(pinInput.get("all").size() > 0) {
						List<Float> pinInput_all = pinInput.get("all");
						List<Float> pinInput_WAVE1 = pinInput.get("WAVE1");
						
						simpleDraw1(pinInput_WAVE1, holder);
						
						String sb_all = getString_2(pinInput_all);
						String sb_WAVE1 = getString_1(pinInput_WAVE1);
						try {
							raf_all.seek(fileTarget_all.length());
							raf_all.write(sb_all.getBytes());
							raf_WAVE1.seek(fileTarget_WAVE1.length());
							raf_WAVE1.write(sb_WAVE1.getBytes());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public Map<String, List<Float>> getZBuf(byte[] inputBuf, int road_WAVE1){
			Map<String, List<Float>> result = new HashMap<String, List<Float>>();
			List<Float> inputZBuf = new ArrayList<Float>();
			List<Float> inputBuf_WAVE1 = new ArrayList<Float>();
			int length = inputBuf.length;
			if(inputBuf.length >= 14) {
				for(int i=0; i<14; i++){
					if(inputBuf[i] == -86 && inputBuf[i+6] == -1){
						//得到7整数倍的数据个数，一共(length-i)/7组
						for(int j = 0; j < (length-i)/7; j++) {
							//每组的每个元素都要转化成正值，然后每组求出各路的值
							for(int k=0; k < 7; k++) {
								inputZBuf.add((float) (inputBuf[i+j*7+k] & 0xff));
							}
							float totalPrice = (inputZBuf.get(j*7+road_WAVE1*2-1)*256 + inputZBuf.get(j*7+road_WAVE1*2)) / 1024 *5 *550;
							inputBuf_WAVE1.add((float)Math.round(totalPrice*100)/100);
						}
						break;
					}
				}
			}
			result.put("all", inputZBuf);
			result.put("WAVE1", inputBuf_WAVE1);
			return result;
		}
		
		public boolean sdCardIsExist(){
			return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		}
		public String getString_1(List<Float> list){
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				Float p = list.remove(0);
				sb.append(p+",");
			}
			
			return sb.toString();
		}
		public String getString_2(List<Float> list){
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				float p = list.remove(0);
				sb.append((int)p+",");
			}
			
			return sb.toString();
		}
	}
	
	public class DrawThread {
		private Timer timer;
		private Draw dataRecieveTask;
		
		public DrawThread(SurfaceHolder holder, String fileName) {
			this.timer = new Timer();
			this.dataRecieveTask = new Draw(holder, fileName);
		}
		
		public void start(){
			timer.schedule(dataRecieveTask, 0, 2000);
		}
		public void stop(){
			timer.cancel();
		}
	}
	
	public void closeAll(){
		if(null != dt){
			dt.stop();
		}
		try {
			if(null != btInput){
				btInput.close();
			}
			if (BluetoothActivity.btSocket != null) {
				BluetoothActivity.btSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			closeAll();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void simpleDraw1(List<Float> inputBuf, SurfaceHolder holder) {
		float max = getMax1(inputBuf);
		float min = getMin1(inputBuf);
		float diff = getDiff1(inputBuf, max, min);
		float avg = getAverage1(inputBuf);
		try {
			if(holder != null){
				canvas = holder.lockCanvas(
					new Rect(0, 0, width, height));// 关键:获取画布
				canvas.drawColor(Color.WHITE);
				
				canvas.drawLine(29, 30, width-30, 30, paint);
				canvas.drawLine(30, 29, 30, height-30, paint);
				canvas.drawLine(width-50, 10, width-30, 31, paint);
				canvas.drawLine(width-50, 50, width-30, 29, paint);
				canvas.drawLine(10, height-50, 31, height-30, paint);
				canvas.drawLine(50, height-50, 29, height-30, paint);
				drawText(canvas, (float)Math.round(avg*100)/100+"", width-300, height-550, paint, 90);
				
				oldY = (inputBuf.get(0)-avg)/diff*width/3 + width/3 + width/15;
				//遍历inputBuf画图
				Iterator<Float> itera = inputBuf.iterator();
				int x = 100;
				while (itera.hasNext()) {
					float y0 = itera.next();
					float y =  (y0-avg)/diff*width/3 + width/3 + width/15;
					if(y0 == max){
						canvas.drawLine(y, 30, y, 60, paint);
						drawText(canvas, y0+"", y+50, 40, paint2, 90);
					}else if(y0 == min){
						canvas.drawLine(y, 30, y, 60, paint);
						drawText(canvas, y0+"", y+50, 40, paint2, 90);
					}
					canvas.drawLine(oldY, x, y, x+200, paint);
					canvas.drawPoint(oldY, x, paint1);
					oldY = y;
					x+=200;
				}
				canvas.drawPoint(oldY, x, paint1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(canvas != null){
				holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
			}
		}
	}
	
	void drawText(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle){  
        if(angle != 0){  
            canvas.rotate(angle, x, y);   
        }  
        canvas.drawText(text, x, y, paint);  
        if(angle != 0){  
            canvas.rotate(-angle, x, y);   
        }  
    }  
	
	public List<Integer> getDrawBuf1(List<Integer> drawingData, int size){
		List<Integer> drawedData_G = new LinkedList<Integer>();
		for (int i = 0; i < size; i++) {
			int r = drawingData.remove(0);
			drawedData_G.add(r);
		}
		return drawedData_G;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "退出");
        return super.onCreateOptionsMenu(menu);
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 1){
        	new Thread(new Back()).start();
        }
        return true;
    }
	
	public class Back implements Runnable{
		@Override
		public void run() {
			closeAll();
			Intent intent = new Intent(JieguoActivity.this, BluetoothActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
	
	public float getDiff1(List<Float> list, float max, float min){
		float diff = max - min;
		return diff==0 ? max:diff;
	}

	public Float getMax1(List<Float> list){
		Float max = list.get(0);
		for (int i = 0; i < list.size(); i++) {
			if(max < list.get(i)){
				max = list.get(i);
			}
		}
		return max;
	}

	public float getMin1(List<Float> list){
		float min = list.get(0);
		for (int i = 0; i < list.size(); i++) {
			if(min > list.get(i)){
				min = list.get(i);
			}
		}
		return min;
	}

	public float getAverage1(List<Float> inputBuf_ECG){
		float sum = 0;
		for (int i = 0; i < inputBuf_ECG.size(); i++) {
			sum = sum + inputBuf_ECG.get(i);
		}
		float avg = sum / inputBuf_ECG.size();
		return avg;
	}
	
	public int min(int[] size){
		int min = size[0];
		for (int i = 0; i < size.length; i++) {
			if(min > size[i]){
				min = size[i];
			}
		}
		return min;
	}
}
