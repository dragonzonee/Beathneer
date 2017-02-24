package com.example.test1;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BluetoothActivity extends Activity {
	static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	Button btnSearch, btnExit;
	EditText fileName;
	private String personname;
	private TextView personInfo;
	ToggleButton tbtnSwitch;
	ListView lvBTDevices;
	ArrayAdapter<String> adtDevices;
	List<String> lstDevices = new ArrayList<String>();
	BluetoothAdapter btAdapt;
	public static BluetoothSocket btSocket;
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		
		personInfo = (TextView)findViewById(R.id.text11);
		Intent intent2 = getIntent();
		personname = intent2.getStringExtra("name");
		personInfo.setText("��ǰ�˻���"+personname);
		
		
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		// Button ����
		fileName = (EditText) this.findViewById(R.id.fileName);
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
		fileName.setText(dateFormat.format(date));
		btnSearch = (Button) this.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new ClickEvent());
		//btnExit = (Button) this.findViewById(R.id.btnExit);
		//btnExit.setOnClickListener(new ClickEvent());
		// ToogleButton����
		tbtnSwitch = (ToggleButton) this.findViewById(R.id.tbtnSwitch);
		tbtnSwitch.setOnClickListener(new ClickEvent());
		 
		// ListView��������Դ ������
		lvBTDevices = (ListView) this.findViewById(R.id.lvDevices);
		adtDevices = new ArrayAdapter<String>(BluetoothActivity.this,
				android.R.layout.simple_list_item_1, lstDevices);
		lvBTDevices.setAdapter(adtDevices);
		lvBTDevices.setOnItemClickListener(new ItemClickEvent());
		
		btAdapt = BluetoothAdapter.getDefaultAdapter();// ��ʼ��������������

		if (btAdapt.getState() == BluetoothAdapter.STATE_OFF)// ��ȡ����״̬����ʾ
			tbtnSwitch.setChecked(false);
		else if (btAdapt.getState() == BluetoothAdapter.STATE_ON)
			tbtnSwitch.setChecked(true);
		
		// ע��Receiver����ȡ�����豸��صĽ��
		IntentFilter intent = new IntentFilter();
		intent.addAction(BluetoothDevice.ACTION_FOUND);// ��BroadcastReceiver��ȡ���������
		intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(searchDevices, intent);
	}
 
	private BroadcastReceiver searchDevices = new BroadcastReceiver() {
		
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Bundle b = intent.getExtras();
			Object[] lstName = b.keySet().toArray();

			// ��ʾ�����յ�����Ϣ����ϸ��
			for (int i = 0; i < lstName.length; i++) {
				String keyName = lstName[i].toString();
				Log.i(keyName, String.valueOf(b.get(keyName)));
			}
			//�����豸ʱ��ȡ���豸��MAC��ַ
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				String str= device.getName() + "|" + device.getAddress();
				if (lstDevices.indexOf(str) == -1)// ��ֹ�ظ����
					lstDevices.add(str); // ��ȡ�豸���ƺ�mac��ַ
				adtDevices.notifyDataSetChanged();
			}
		}
	};

	@Override
	protected void onDestroy() {
	    this.unregisterReceiver(searchDevices);
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	class ItemClickEvent implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if(!fileName.getText().toString().equals("")){
				String filename = fileName.getText().toString();
				btAdapt.cancelDiscovery();
				String str = lstDevices.get(arg2);
				String[] values = str.split("\\|");
				String address=values[1];
				Log.i("address",values[1]);
				UUID uuid = UUID.fromString(SPP_UUID);
				BluetoothDevice btDev = btAdapt.getRemoteDevice(address);
				try {
					btSocket = btDev
							.createRfcommSocketToServiceRecord(uuid);
					btSocket.connect();
					//�򿪲���ͼʵ��
					Intent intent = new Intent();
					intent.putExtra("fileName", filename);
					Intent intent2 = new Intent();
					intent.putExtra("name",personname);
					intent.setClass(BluetoothActivity.this, ZhongjianActivity.class);
					startActivity(intent);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				new AlertDialog.Builder(arg1.getContext()).setTitle("Android��ʾ").setMessage("���������ݱ�����ļ���").setPositiveButton("ȷ��" ,  null ).show();
			}
		}
	}
	
	class ClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == btnSearch)// ���������豸����BroadcastReceiver��ʾ���
			{
				if (btAdapt.getState() == BluetoothAdapter.STATE_OFF) {// ���������û����
					Toast.makeText(BluetoothActivity.this, "���ȴ�����", 1000).show();
					return;
				}
				setTitle("����������ַ��" + btAdapt.getAddress());
				lstDevices.clear();
				btAdapt.startDiscovery();
			}else if (v == tbtnSwitch) {// ������������/�ر�
				if (tbtnSwitch.isChecked() == false)
					btAdapt.enable();
				else if (tbtnSwitch.isChecked() == true)
					btAdapt.disable();
			} else if (v == btnExit) {
				try {
					if (btSocket != null)
						btSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				BluetoothActivity.this.finish();
			}
		}

	}
}