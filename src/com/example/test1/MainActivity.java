package com.example.test1;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
		private static final String PREFS_NAME = "MyInfo";//���������û���Ϣ
	 	public static EditText Num,Pwd;
	 	private static Button Login,Post,pwdback;
	 	public static CheckBox RememberPwd,AutoLogin;
		private DBHelper dbHelper;
		public static SQLiteDatabase sdb;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        AppConnect.getInstance(this);
//		LinearLayout container =(LinearLayout)findViewById(R.id.AdLinearLayout);   
//		new AdView(this,container).DisplayAd();
      //���ʵ������
        Num=(EditText) findViewById(R.id.et_Num);
        Pwd=(EditText) findViewById(R.id.et_Pwd);
        Login=(Button) findViewById(R.id.btn_Login);
        Post=(Button) findViewById(R.id.Post);
        pwdback = (Button) findViewById(R.id.pwdback);
        RememberPwd=(CheckBox)findViewById(R.id.checkBox1);
        Post.setOnClickListener(this);
        Login.setOnClickListener(this);
        pwdback.setOnClickListener(this);
        dbHelper=new DBHelper(this,"Info.db");
        LoadUserDate();
   
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
    	case R.id.btn_Login:
    		if(Num.getText().toString().equals("")){
				Toast.makeText(MainActivity.this,"�˺Ų���Ϊ�� ", Toast.LENGTH_SHORT).show();
				return;
			}
			if(Pwd.getText().toString().equals("")){
				Toast.makeText(MainActivity.this,"���벻��Ϊ�� ", Toast.LENGTH_SHORT).show();
				return;
			}
			SaveUserDate();
			boolean bool=login(Num.getText().toString(),Pwd.getText().toString());
			if(bool){	
				Toast.makeText(MainActivity.this, "��¼�ɹ���", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.putExtra("name",Num.getText().toString());
				intent.setClass(MainActivity.this, BluetoothActivity.class);
  				MainActivity.this.startActivity(intent);
		
			}else{
				Toast.makeText(MainActivity.this, "�û�������������", Toast.LENGTH_LONG).show();
				return;
			}
    		break;
				
    	case R.id.Post:
    		Intent intent=new Intent(MainActivity.this,Register.class);
    		startActivity(intent);

			break;
    	case R.id.pwdback:
    		//System.out.println("����Ӧ");
    		Intent intent1 = new Intent();
			intent1.putExtra("name",Num.getText().toString());
			intent1.setClass(MainActivity.this, pwdback.class);
				MainActivity.this.startActivity(intent1);
				break;
		}
	}

	 //��¼
  	public boolean login(String username,String password){
  		String name = new Security().Encryption(username);
  		String pwd = new Security().Encryption(password);
  		/*SQLiteDatabase*/ 
  		sdb=dbHelper.getReadableDatabase();
  		String sql="select * from people where name=? and pwd=?";
  		Cursor cursor=sdb.rawQuery(sql, new String[]{name,pwd});		
  		if(cursor.moveToFirst()==true){
  			cursor.close();
  			return true;
  		}
  		return false;
  	}
  	//�˳�
  	long flag= -1;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
            if (flag == -1||System.currentTimeMillis() - flag >2000) {
                Toast.makeText(this, "�ٵ��һ���˳�", Toast.LENGTH_SHORT).show();
                flag = System.currentTimeMillis();
 
            } else if (System.currentTimeMillis() - flag < 2000) {
                Intent exit = new Intent(Intent.ACTION_MAIN);
                exit.addCategory(Intent.CATEGORY_HOME);
                exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(exit);
                System.exit(0);
            }
        }
        return true;
    }
  	/* public boolean onKeyDown(int keyCode, KeyEvent event)  {  
         if (keyCode == KeyEvent.KEYCODE_BACK )   {  
        	 AlertDialog.Builder builder = new Builder(MainActivity.this); 
       		 builder.setIcon(android.R.drawable.ic_dialog_info);
       	        builder.setMessage("ȷ��Ҫ�˳�?"); 
       	        builder.setTitle("��ʾ"); 
       	        builder.setPositiveButton("ȷ��", 
       	                new android.content.DialogInterface.OnClickListener() { 
       	                    public void onClick(DialogInterface dialog, int which) { 
       	                        dialog.dismiss(); 
       	                     MainActivity.this.finish(); 
       	                    } 
       	                }); 
       	        builder.setNegativeButton("ȡ��", 
       	                new android.content.DialogInterface.OnClickListener() { 
       	                    public void onClick(DialogInterface dialog, int which) { 
       	                        dialog.dismiss(); 
       	                    } 
       	                }); 
       	        		builder.create().show();  
         }    
         return false;       
     }*/
  	 

  	
  	/**
	 * �����û���Ϣ
	 */
	private void SaveUserDate() {
		// ���������ļ�
		SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);
		// д�������ļ�
		Editor spEd = sp.edit();
		if (RememberPwd.isChecked()) {
			spEd.putBoolean("isSave", true);
			spEd.putString("name", Num.getText().toString());
			spEd.putString("password", Pwd.getText().toString());
		} else {
			spEd.putBoolean("isSave", false);
			spEd.putString("name", "");
			spEd.putString("password", "");
		}
		spEd.commit();
	}

	/**
	 * �����Ѽ�ס���û���Ϣ
	 */
	private void LoadUserDate() {
		SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);

		if (sp.getBoolean("isSave", false)) {
			String username = sp.getString("name", "");
			String userpassword = sp.getString("password", "");
			if (!("".equals(username) && "".equals(userpassword))) {
				Num.setText(username);
				Pwd.setText(userpassword);
				RememberPwd.setChecked(true);
			}
		}
	}
  	    
	 
}
