package com.example.fm13dt160demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.string;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

@SuppressLint("DefaultLocale") 
public class MainActivity extends BaseAct {
	private NfcV nfcv;
	private NfcA nfca;
	ArrayList<String> list = new ArrayList<String>();
	private Button btnReadMemory,btnWriteMemory,btnGetRandom,btnAuth,btnGetTemperature,btnStart_StopRTC,
	btnDeepSleep,btnWakeup,btnWriteReg,btnReadReg,btnLedCtrl,btnOpModeChk,btnFieldStrengthChk,btnReadSingleBlock,btnWriteSingleBlock,btnGetTemperature2,btnExcute,
	btnSetTemperature,btnGetTemperature3;
	private EditText etUid,etFirstAddr,etNumByte,etDataNum,etData,etCmd_Cfg,etPW,etBlockAddr,etStopRtcPW,etRegAddr,etAnalogPara
    ,etResult;
	private Spinner spinnerInstructs;
	private List<String> data_list;
	private ArrayAdapter<String> arr_adapter;
	private int selectNo=0;
    public static  String mUid="uid";
    private static final int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A
            | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK 
            | NfcAdapter.FLAG_READER_NFC_V;
            //NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS;

	protected static final String TAG = "tag";
    private String uid="";
//    private NfcAdapter mNfcAdapter;
    private NfcAdapter.ReaderCallback mReaderCallback = new NfcAdapter.ReaderCallback() {
        @Override
        public void onTagDiscovered(Tag tag) {
            Log.d(TAG, "onTagDiscovered: " + Arrays.toString(tag.getTechList()));
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] duration = {15, 300, 60, 90};
            vibrator.vibrate(duration, -1);
            /*
    		 * UID from a byte array from tag.getId(). Corrects the byte order of
    		 * the UID to MSB first. Nicklaus Ng
    		 */
    		String uidString = new String();
    		byte[] uid = tag.getId();
    	    for (int index = uid.length - 1; index >= 0; --index) {
    			uidString += String.format("%02x", uid[index]);
    		}
    	    uidString=uidString.toUpperCase();
//    	    
//    	    etUid.setText(uidString);
    	 
    	    String uidStringRev = new String();
    		for (int index = 0; index < uid.length; index++) {
    			uidStringRev += String.format("%02x", uid[index]);
    		}
    		
    		String[] techList = tag.getTechList();

//			ArrayList<String> list = new ArrayList<String>();
    		list.clear();
			for (String string : techList) {
				list.add(string);
				System.out.println("tech=" + string);
			}
			if (list.contains("android.nfc.tech.NfcA")) {
			   final String uidfinalString=uidStringRev;
	    	    new Thread(new Runnable() {
	                @Override
	                public void run() {
	                	Message msg = new Message();
	                	msg.what=100;
	                	msg.obj=uidfinalString;
//		                    mHandler.sendEmptyMessage(100);
	                	mHandler.sendMessage(msg);
	                }
	            }).start();

				nfca = android.nfc.tech.NfcA.get(tag);	

	    		if (nfca == null) {
	    			return;
	    		}

	    		try {
	    			nfca.connect();			
	    			nfca.close();
	    		} catch (IOException e) {
	    			System.out.println(e.getMessage().toString());
	    		}
			}else if (list.contains("android.nfc.tech.NfcV")) {
			   final String uidfinalString=uidString;
	    	    new Thread(new Runnable() {
	                @Override
	                public void run() {
	                	Message msg = new Message();
	                	msg.what=100;
	                	msg.obj=uidfinalString;
//		                    mHandler.sendEmptyMessage(100);
	                	mHandler.sendMessage(msg);
	                }
	            }).start();

				nfcv = android.nfc.tech.NfcV.get(tag);	

	    		if (nfcv == null) {
	    			return;
	    		}

	    		try {
	    			nfcv.connect();			
	    			nfcv.close();
	    		} catch (IOException e) {
	    			System.out.println(e.getMessage().toString());
	    		}
			}
    			
            
            
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        init(false, true);
        initEvents();
        
    }
    @SuppressLint("HandlerLeak") 
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
            	etUid.setText((String)msg.obj);
            	uid=etUid.getText().toString();
            }
        }
    };

    @Override
	protected void initViews() {
		// TODO Auto-generated method stub
    	
    	btnReadMemory=(Button) findViewById(R.id.btnReadMemory);
    	btnReadSingleBlock=(Button) findViewById(R.id.btnReadSingleBlock);
    	btnWriteMemory=(Button) findViewById(R.id.btnWriteMemory);
    	btnWriteSingleBlock=(Button) findViewById(R.id.btnWriteSingleBlock);
    	btnGetRandom=(Button) findViewById(R.id.btnGetRandom);
    	btnAuth=(Button) findViewById(R.id.btnAuth);
    	btnGetTemperature=(Button) findViewById(R.id.btnGetTemperature);
    	btnGetTemperature2=(Button) findViewById(R.id.btnGetTemperature2);
    	btnStart_StopRTC=(Button) findViewById(R.id.btnStart_StopRTC);    	
    	btnDeepSleep=(Button) findViewById(R.id.btnDeepSleep);
    	btnWakeup=(Button) findViewById(R.id.btnWakeup);
    	btnWriteReg=(Button) findViewById(R.id.btnWriteReg);
    	btnReadReg=(Button) findViewById(R.id.btnReadReg);
    	btnLedCtrl=(Button) findViewById(R.id.btnLedCtrl);
    	btnOpModeChk=(Button) findViewById(R.id.btnOpModeChk);
    	btnFieldStrengthChk=(Button) findViewById(R.id.btnFieldStrengthChk);
    	btnExcute=(Button) findViewById(R.id.btnExcute);
    	btnGetTemperature3=(Button) findViewById(R.id.btnGetTemperature3);
    	btnSetTemperature=(Button) findViewById(R.id.btnSetTemperature);
    	
    	
    	etUid=(EditText) findViewById(R.id.etUid);
		etFirstAddr=(EditText) findViewById(R.id.etFirstAddr);
		etNumByte=(EditText) findViewById(R.id.etNumByte);
		etDataNum=(EditText) findViewById(R.id.etDataNum);
		etData=(EditText) findViewById(R.id.etData);
		etCmd_Cfg=(EditText) findViewById(R.id.etCmd_Cfg);
		etPW=(EditText) findViewById(R.id.etPW);
		etBlockAddr=(EditText) findViewById(R.id.etBlockAddr);
		etStopRtcPW=(EditText) findViewById(R.id.etStopRtcPW);
		etRegAddr=(EditText) findViewById(R.id.etRegAddr);
		etAnalogPara=(EditText) findViewById(R.id.etAnalogPara);
		etResult=(EditText) findViewById(R.id.etResult);
		
		spinnerInstructs =(Spinner) findViewById(R.id.spinnerInstructs);
		
		//数据
        data_list = new ArrayList<String>();
        data_list.add("唤醒");
        data_list.add("查看唤醒");
        data_list.add("设置延时");
        data_list.add("查看延时");
        data_list.add("设置测温间隔");
        data_list.add("查看测温间隔");
        data_list.add("开始rtc测温");
        data_list.add("Flow_Flag_Reset");
        data_list.add("查看电池,测温等状态");
        data_list.add("读取温度数据");
        data_list.add("开始实时测温");
        data_list.add("获取实时测温结果");
        data_list.add("设置测温次数");
        data_list.add("查看测温次数");
        data_list.add("设置最小温度值");
        data_list.add("设置最大温度值");
        data_list.add("获取最大最小温度值");
//        data_list.add("写测温间隔");
//        data_list.add("读取测温间隔");
        data_list.add("设置启动时间");
        data_list.add("读取启动时间");
        data_list.add("休眠");
        data_list.add("配置iOS系统参数");
        data_list.add("获取电池电压");
        
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinnerInstructs.setAdapter(arr_adapter);
    	
	}
    @Override
    protected void initEvents() {
    	super.initEvents();
    	btnReadMemory.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etFirstAddr.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "First Addr 不能为空", Toast.LENGTH_SHORT).show();	
						return;
					}
					if (etNumByte.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "Num of byte 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);
						int firstAddress=Integer.parseInt(etFirstAddr.getText().toString(),16);
						int numOfByte=Integer.parseInt(etNumByte.getText().toString());
						byte[] result=fm13dt160.readMemory(firstAddress, numOfByte);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etFirstAddr.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "First Addr 不能为空", Toast.LENGTH_SHORT).show();	
						return;
					}
					if (etNumByte.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "Num of byte 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);
						int firstAddress=Integer.parseInt(etFirstAddr.getText().toString(),16);
						int numOfByte=Integer.parseInt(etNumByte.getText().toString());
						byte[] result=fm13dt160.readMemory(firstAddress, numOfByte);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}
				}
								
			}

			
		});
    	btnReadSingleBlock.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if (nfcv == null ) {
					Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (etFirstAddr.getText().toString().equals("")) {
					Toast.makeText(MainActivity.this, "First Addr 不能为空", Toast.LENGTH_SHORT).show();	
					return;
				}
				
				try {
					nfcv.connect();	
					FM13DT160 fm13dt160=new FM13DT160(nfcv);
					int firstAddress=Integer.parseInt(etFirstAddr.getText().toString(),16);
					
					byte[] result=fm13dt160.readSingleBlock(firstAddress);
					etResult.setText(Utility.Bytes2HexString2(result));
					nfcv.close();
				} catch (IOException e) {
//					System.out.println(e.getMessage().toString());
					Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
				}				
			}

			
		});
    	btnWriteMemory.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etFirstAddr.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "First Addr 不能为空", Toast.LENGTH_SHORT).show();	
						return;
					}
					if (etDataNum.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "Data Number 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etData.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "Data 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);
						int firstAddress=Integer.parseInt(etFirstAddr.getText().toString(),16);
						int dataNum=Integer.parseInt(etDataNum.getText().toString());
						byte[] data=Utility.HexString2Bytes(etData.getText().toString());
						byte[] result=fm13dt160.writeMemory(firstAddress, dataNum, data);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etFirstAddr.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "First Addr 不能为空", Toast.LENGTH_SHORT).show();	
						return;
					}
					if (etDataNum.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "Data Number 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etData.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "Data 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);
						int firstAddress=Integer.parseInt(etFirstAddr.getText().toString(),16);
						int dataNum=Integer.parseInt(etDataNum.getText().toString());
						byte[] data=Utility.HexString2Bytes(etData.getText().toString());
						byte[] result=fm13dt160.writeMemory(firstAddress, dataNum, data);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}
				}
									
			}

			
		});
    	btnWriteSingleBlock.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if (nfcv == null ) {
					Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (etFirstAddr.getText().toString().equals("")) {
					Toast.makeText(MainActivity.this, "First Addr 不能为空", Toast.LENGTH_SHORT).show();	
					return;
				}
				
				if (etData.getText().toString().equals("")) {
					Toast.makeText(MainActivity.this, "Data 不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				try {
					nfcv.connect();	
					FM13DT160 fm13dt160=new FM13DT160(nfcv);
					int blockNumber=Integer.parseInt(etFirstAddr.getText().toString(),16);
					
					byte[] blockData=Utility.HexString2Bytes(etData.getText().toString());
					byte[] result=fm13dt160.writeSingleBlock(blockNumber, blockData);
					etResult.setText(Utility.Bytes2HexString2(result));
					nfcv.close();
				} catch (IOException e) {
//					System.out.println(e.getMessage().toString());
					Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
				}					
			}

			
		});
    	btnGetRandom.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);
						
						byte[] result=fm13dt160.getRandom();
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);
						
						byte[] result=fm13dt160.getRandom();
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}
				
							
			}

			
		});
    	
    	btnAuth.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etPW.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "PW Data 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);
						
						byte[] result1=fm13dt160.getRandom();
	//					String str=Utility.Bytes2HexString(result1);
						
						int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);
						byte[] pwData=Utility.HexString2Bytes(etPW.getText().toString());
						for (int i = 0; i < pwData.length; i++) {
							pwData[i]=(byte) (pwData[i]^result1[i]);
						}
						
						byte[] result=fm13dt160.auth(cmd_cfg, pwData);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
	//					System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etPW.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "PW Data 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);
						
						byte[] result1=fm13dt160.getRandom();
	//					String str=Utility.Bytes2HexString(result1);
						
						int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);
						byte[] pwData=Utility.HexString2Bytes(etPW.getText().toString());
						for (int i = 0; i < pwData.length; i++) {
							pwData[i]=(byte) (pwData[i]^result1[i]);
						}
						
						byte[] result=fm13dt160.auth(cmd_cfg, pwData);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
	//					System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}
											
			}

			
		});
    	
    	btnGetTemperature.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
//				if (list.contains("android.nfc.tech.NfcA")) {
//				
//			}else if (list.contains("android.nfc.tech.NfcV")) {
//				
//			}
				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etBlockAddr.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "user area block addr 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);					
						int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);					
						int blockAddr=Integer.parseInt(etBlockAddr.getText().toString(),16);
						byte[] result=fm13dt160.getTemperature(cmd_cfg, blockAddr);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etBlockAddr.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "user area block addr 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);					
						int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);					
						int blockAddr=Integer.parseInt(etBlockAddr.getText().toString(),16);
						byte[] result=fm13dt160.getTemperature(cmd_cfg, blockAddr);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}
				}
									
			}

			
		});
    	//两次测温
    	btnGetTemperature2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etBlockAddr.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "user area block addr 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);					
						int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);					
						int blockAddr=Integer.parseInt(etBlockAddr.getText().toString(),16);
						byte[] result=fm13dt160.getTemperature(cmd_cfg, blockAddr);
						etResult.setText(Utility.Bytes2HexString2(result));
						
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cmd_cfg=cmd_cfg|0x84;
						result=fm13dt160.getTemperature(cmd_cfg, blockAddr);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etBlockAddr.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "user area block addr 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);					
						int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);					
						int blockAddr=Integer.parseInt(etBlockAddr.getText().toString(),16);
						byte[] result=fm13dt160.getTemperature(cmd_cfg, blockAddr);
						etResult.setText(Utility.Bytes2HexString2(result));
						
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cmd_cfg=cmd_cfg|0x84;
						result=fm13dt160.getTemperature(cmd_cfg, blockAddr);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}
				}
									
			}

			
		});
    	
    	btnStart_StopRTC.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);
					
					if ((cmd_cfg & 0x80) == 0x80 && etStopRtcPW.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "stop rtc password 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);
						if (cmd_cfg==0) {
							etStopRtcPW.setText("00000000");
						}
						byte[] psw=Utility.HexString2Bytes(etStopRtcPW.getText().toString());
						byte[] rand = new byte[5];
						if ((cmd_cfg & 0x80)==0x80 ) {
							rand=fm13dt160.getRandom();
						}
						byte[] userRev = new byte[4];
						for (int i = 0; i < userRev.length; i++) {
							userRev[i]=(byte) (psw[i]^rand[i+1]);
						}
						byte[] result=fm13dt160.startOrFlowFlagReset(cmd_cfg, userRev);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);
					
					if ((cmd_cfg & 0x80) == 0x80 && etStopRtcPW.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "stop rtc password 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);
						if (cmd_cfg==0) {
							etStopRtcPW.setText("00000000");
						}
						byte[] psw=Utility.HexString2Bytes(etStopRtcPW.getText().toString());
						byte[] rand;
						if ((cmd_cfg & 0x80)==0x80 ) {
							rand=fm13dt160.getRandom();
						}
						byte[] result=fm13dt160.start_stopRTC(cmd_cfg, psw);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}
				}
							
			}

			
		});
    	
    	btnDeepSleep.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);
					
					
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);
						
						byte[] result=fm13dt160.deepSleep(cmd_cfg);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);
					
					
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);
						
						byte[] result=fm13dt160.deepSleep(cmd_cfg);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}
									
			}

			
		});
    	
    	btnWakeup.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);
					
					
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);
						
						byte[] result=fm13dt160.wakeup(cmd_cfg);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);
					
					
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);
						
						byte[] result=fm13dt160.wakeup(cmd_cfg);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}
						
			}

			
		});
    	
    	btnWriteReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etRegAddr.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "Reg Addr 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}	
					if (etAnalogPara.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "Analog Para 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);
						
						int regAddr=Integer.parseInt(etRegAddr.getText().toString(),16);
						int analogPara=Integer.parseInt(etAnalogPara.getText().toString(),16);
						byte[] result=fm13dt160.writeReg(regAddr, analogPara);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}		
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etRegAddr.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "Reg Addr 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}	
					if (etAnalogPara.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "Analog Para 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);
						
						int regAddr=Integer.parseInt(etRegAddr.getText().toString(),16);
						int analogPara=Integer.parseInt(etAnalogPara.getText().toString(),16);
						byte[] result=fm13dt160.writeReg(regAddr, analogPara);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}		
				}
							
			}

			
		});
    	
    	btnReadReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etRegAddr.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "Reg Addr 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}	
					
					
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);
						
						int regAddr=Integer.parseInt(etRegAddr.getText().toString(),16);
						
						byte[] result=fm13dt160.readReg(regAddr);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etRegAddr.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "Reg Addr 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}	
					
					
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);
						
						int regAddr=Integer.parseInt(etRegAddr.getText().toString(),16);
						
						byte[] result=fm13dt160.readReg(regAddr);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}
				}
										
			}

			
		});
    	
    	btnLedCtrl.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}	
					
					
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);
						
						int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);
						
						byte[] result=fm13dt160.ledCtrl(cmd_cfg);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}	
					
					
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);
						
						int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);
						
						byte[] result=fm13dt160.ledCtrl(cmd_cfg);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}
							
			}

			
		});
    	
    	btnOpModeChk.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}	
					
					
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);
						
						byte[] cmd_cfg=Utility.HexString2Bytes(etCmd_Cfg.getText().toString());
						if(cmd_cfg.length!=3){
							Toast.makeText(MainActivity.this, "cmd cfg 为3字节", Toast.LENGTH_SHORT).show();
							return;	
						}
						byte[] result=fm13dt160.op_Mode_Chk(cmd_cfg);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}	
					
					
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);
						
						byte[] cmd_cfg=Utility.HexString2Bytes(etCmd_Cfg.getText().toString());
						if(cmd_cfg.length!=3){
							Toast.makeText(MainActivity.this, "cmd cfg 为3字节", Toast.LENGTH_SHORT).show();
							return;	
						}
						byte[] result=fm13dt160.op_Mode_Chk(cmd_cfg);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}
								
			}

			
		});
    	
    	btnFieldStrengthChk.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}	
					
					
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);
						
						int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);
						
						byte[] result=fm13dt160.field_strength_chk(cmd_cfg);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (etCmd_Cfg.getText().toString().equals("")) {
						Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
						return;
					}	
					
					
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);
						
						int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);
						
						byte[] result=fm13dt160.field_strength_chk(cmd_cfg);
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
//						System.out.println(e.getMessage().toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}
							
			}

			
		});
    	
    	spinnerInstructs.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			
			@Override
			public void onItemSelected(AdapterView<?>parent,View arg1,
					int pos,long id){				
				selectNo=(int) parent.getItemIdAtPosition(pos);
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0){
				
			}
		});
    	
    	btnExcute.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				if (list.contains("android.nfc.tech.NfcA")) {
					if (nfca == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}				
					
					try {
						nfca.connect();	
						FM13DT160_NfcA fm13dt160=new FM13DT160_NfcA(nfca);
						byte[] result=null;
						switch (selectNo) {
						case 0:
							result=fm13dt160.setWakeup();
							break;
						case 1:
							result=fm13dt160.getWakeup();
							break;
							
						case 2:
							result=fm13dt160.setDelay((byte)0x05);
							break;
						case 3:
							result=fm13dt160.getDelay();
							break;	
						case 4:
							result=fm13dt160.setInterval((byte)0x05);
							break;	
						case 5:
							result=fm13dt160.getInterval();
							break;	
						case 6:
							result=fm13dt160.startRTC();
							break;	
						case 7:
							result=fm13dt160.Flow_Flag_Reset();
							break;	
						case 8:
							result=fm13dt160.getState();
							break;	
						case 9:
							result=fm13dt160.readTemperatures();
							break;	
						case 10:
							result=fm13dt160.startTemperature();
							break;	
						case 11:
							result=fm13dt160.getTemperature();
							break;	
						case 12:
							result=fm13dt160.setMeasuredCount(new byte[]{0x00,0x10});
							break;	
						case 13:
							result=fm13dt160.getMeasuredCount();
							break;	
						case 14:
							result=fm13dt160.setMinTemperature(new byte[]{0x00,0x02});
							break;	
						case 15:
							result=fm13dt160.setMaxTemperature(new byte[]{0x00,0x08});
							break;	
						case 16:
							result=fm13dt160.getMaxMinTemperature();
							break;
					    case 17:
					    	byte[] startTime=new byte[]{(byte) getTimeHex()[0],(byte) getTimeHex()[1],(byte) getTimeHex()[2],(byte) getTimeHex()[3]};//new byte[]{0x11,0x22,0x33,0x44};
							result=fm13dt160.setStartTime(startTime);
					    	break;
					    case 18:
					    	result=fm13dt160.getStartTime();
					    	break;
					    case 19:
					    	result=fm13dt160.sleep();
					    	break;
					    case 20:
					    	result=writeParametersForiOS(fm13dt160);
					    	break;
					    case 21:
					    	result=fm13dt160.getBatteryLevel();
						default:
							break;
						}
						
						etResult.setText(Utility.Bytes2HexString2(result));
						nfca.close();
					} catch (IOException e) {
//						System.out.println(e.toString());
						e.printStackTrace();
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}else if (list.contains("android.nfc.tech.NfcV")) {
					if (nfcv == null ) {
						Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
						return;
					}				
					
					try {
						nfcv.connect();	
						FM13DT160 fm13dt160=new FM13DT160(nfcv);
						byte[] result=null;
						switch (selectNo) {
						case 0:
							result=fm13dt160.setWakeup();
							break;
						case 1:
							result=fm13dt160.getWakeup();
							break;
							
						case 2:
							result=fm13dt160.setDelay((byte)0x05);
							break;
						case 3:
							result=fm13dt160.getDelay();
							break;	
						case 4:
							result=fm13dt160.setInterval((byte)0x05);
							break;	
						case 5:
							result=fm13dt160.getInterval();
							break;	
						case 6:
							result=fm13dt160.startRTC();
							break;	
						case 7:
							result=fm13dt160.stopRTC();
							break;	
						case 8:
							result=fm13dt160.getState();
							break;	
						case 9:
							result=fm13dt160.readTemperatures();
							break;	
						case 10:
							result=fm13dt160.startTemperature();
							break;	
						case 11:
							result=fm13dt160.getTemperature();
							break;	
						case 12:
							result=fm13dt160.setMeasuredCount(new byte[]{0x00,0x10});
							break;	
						case 13:
							result=fm13dt160.getMeasuredCount();
							break;	
						case 14:
							result=fm13dt160.setMinTemperature(new byte[]{0x00,0x02});
							break;	
						case 15:
							result=fm13dt160.setMaxTemperature(new byte[]{0x00,0x08});
							break;	
						case 16:
							result=fm13dt160.getMaxMinTemperature();
							break;
					    case 17:
					    	byte[] startTime=new byte[]{(byte) getTimeHex()[0],(byte) getTimeHex()[1],(byte) getTimeHex()[2],(byte) getTimeHex()[3]};//new byte[]{0x11,0x22,0x33,0x44};
							result=fm13dt160.setStartTime(startTime);
					    	break;
					    case 18:
					    	result=fm13dt160.getStartTime();
					    	break;
					    case 19:
					    	result=fm13dt160.sleep();
					    	break;
					    case 20:
					    	result=writeParametersForiOS(fm13dt160);
					    	break;
					    case 21:
					    	result=fm13dt160.getBatteryLevel();
						default:
							break;
						}
						
						etResult.setText(Utility.Bytes2HexString2(result));
						nfcv.close();
					} catch (IOException e) {
						System.out.println(e.toString());
						Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
					}	
				}
							
			}

			private byte[] writeParametersForiOS(FM13DT160 fm13dt160) {
				// TODO Auto-generated method stub
				byte[] result=null;
				String string="e1407e0103ff0248920f10742f64656d6f2e6e73732e666d73685acee62b"
						+ "000000000000000000000000120307742f70"
//						+ "1d112233445566"
						+ uid.substring(2)
						+ "420300000210632f6d";
				int firstAddress=0;
				int dataNum=3;
				byte[] data=new byte[]{0,0,0,0};
				for (int i = 0; i < 16; i++) {
					String subString=string.substring(i*8, i*8+8);
					byte[] byts=Utility.HexString2Bytes(subString);
					firstAddress=i*4;
					data=byts;
					try {
						result=fm13dt160.writeMemory(firstAddress, dataNum, data);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return result;
			}
			
			private byte[] writeParametersForiOS(FM13DT160_NfcA fm13dt160) {
				// TODO Auto-generated method stub
				byte[] result=null;
				String string="03FF023C"
						+"92090A64"
						+"656D6F2E"
						+"666D7368"
						+"5C9AE612"
						+"00000028"
						+"00011203"
						+"07742F70"
//						+ "1d112233445566"
						+ uid
						+ "42"
						+ "03000002"
						+ "10632f6d"
						+"10632F6D"
						+"4C000000"
						+"E7030000"
						+"E7030011"
						+"00020000";
				int firstAddress=0;
				int dataNum=3;
				byte[] data=new byte[]{0,0,0,0};
				for (int i = 0; i < 16; i++) {
					String subString=string.substring(i*8, i*8+8);
					byte[] byts=Utility.HexString2Bytes(subString);
					firstAddress=i*4+16;
					data=byts;
					try {
						result=fm13dt160.writeMemory(firstAddress, dataNum, data);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return result;
			}

			
		});
    	
    	btnSetTemperature.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if (nfcv == null ) {
					Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
					return;
				}
//				if (etCmd_Cfg.getText().toString().equals("")) {
//					Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				if (etBlockAddr.getText().toString().equals("")) {
//					Toast.makeText(MainActivity.this, "user area block addr 不能为空", Toast.LENGTH_SHORT).show();
//					return;
//				}
				try {
					nfcv.connect();	
					FM13DT160 fm13dt160=new FM13DT160(nfcv);					
//					int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);					
//					int blockAddr=Integer.parseInt(etBlockAddr.getText().toString(),16);
//					byte[] result=fm13dt160.getTemperature(cmd_cfg, blockAddr);
					byte[] result=fm13dt160.startTemperature();
					etResult.setText(Utility.Bytes2HexString2(result));
					nfcv.close();
				} catch (IOException e) {
//					System.out.println(e.getMessage().toString());
					Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
				}					
			}

			
		});
    	btnGetTemperature3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if (nfcv == null ) {
					Toast.makeText(MainActivity.this, "请先触碰标签！", Toast.LENGTH_SHORT).show();
					return;
				}
//				if (etCmd_Cfg.getText().toString().equals("")) {
//					Toast.makeText(MainActivity.this, "cmd cfg 不能为空", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				if (etBlockAddr.getText().toString().equals("")) {
//					Toast.makeText(MainActivity.this, "user area block addr 不能为空", Toast.LENGTH_SHORT).show();
//					return;
//				}
				try {
					nfcv.connect();	
					FM13DT160 fm13dt160=new FM13DT160(nfcv);					
//					int cmd_cfg=Integer.parseInt(etCmd_Cfg.getText().toString(),16);					
//					int blockAddr=Integer.parseInt(etBlockAddr.getText().toString(),16);
//					byte[] result=fm13dt160.getTemperature(cmd_cfg, blockAddr);
					byte[] result=fm13dt160.getTemperature();
					etResult.setText(Utility.Bytes2HexString2(result));
					nfcv.close();
				} catch (IOException e) {
//					System.out.println(e.getMessage().toString());
					Toast.makeText(MainActivity.this,"error", Toast.LENGTH_SHORT).show();
				}					
			}

			
		});
	}
    
    /** 初始化 **/
	@Override
	protected void init(boolean hasBackOnActionBar, boolean hasNfc) {
		super.init(hasBackOnActionBar, hasNfc);
	}
	
	@Override
	protected void onResume() {
		super.onResume();	
//		etUid.setText(mUid);
		enableReaderMode();

	}

	@Override
	protected void onPause() {
		super.onPause();
		disableReaderMode();

	}
	private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
//            int READER_FLAGS = -1;
            Bundle option = new Bundle();
            option.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 3000);// 延迟对卡片的检测
            if (nfc != null) {
                nfc.enableReaderMode(this, mReaderCallback, READER_FLAGS, option);
            }
        }

    }

    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
            if (nfc != null) {
                nfc.disableReaderMode(this);
            }
        }
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
//		handleIntent(intent);
	}
	/**
	 * 
	 * @param intent
	 */
	@SuppressLint("DefaultLocale") 
	protected void handleIntent(Intent intent) {
//		System.out.println("onNewIntent");
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (tag == null) {
			return;
		}
		/*
		 * UID from a byte array from tag.getId(). Corrects the byte order of
		 * the UID to MSB first. Nicklaus Ng
		 */
		String uidString = new String();
		byte[] uid = tag.getId();
	    for (int index = uid.length - 1; index >= 0; --index) {
			uidString += String.format("%02x", uid[index]);
		}
	    uidString=uidString.toUpperCase();
//	    
	    etUid.setText(uidString);
	    String uidStringRev = new String();
		for (int index = 0; index < uid.length; index++) {
			uidStringRev += String.format("%02x", uid[index]);
		}
				
		nfcv = android.nfc.tech.NfcV.get(tag);	

		if (nfcv == null) {
			return;
		}

		try {
			nfcv.connect();			
			Toast.makeText(MainActivity.this, "NFC已连接！", Toast.LENGTH_SHORT).show();
			
//				FM13DT160 fm13dt160=new FM13DT160(nfcv);
//				FM13DT 160.SystemInformation systemInformation=new FM13DT160.SystemInformation();
//				systemInformation=fm13dt160.getSystemInformation();
//				etResult.setText("uid:"+Utility.Bytes2HexString(systemInformation.uid)+"\nDSFID:"+systemInformation.dsfid+"\nAFI:"+systemInformation.afi+"\nNumber of Blocks:"+systemInformation.numBlocks+"\nBlock Size:"+systemInformation.blockSize+"\nIC reference:"+systemInformation.icReference);
//				byte[] rd=fm13dt160.readSingleBlock(0);
//				byte[] wr=fm13dt160.writeSingleBlock(0, new byte[]{11,22,33,44});
//				rd= fm13dt160.readSingleBlock(0);
//				byte[] rm=fm13dt160.readMemory(0x1000, 4);
//				byte[] wm=fm13dt160.writeMemory(0x1000, 3, new byte[]{11,22,33,44});
//				rm=fm13dt160.readMemory(0x1000, 4);
//				byte[] random=fm13dt160.getRandom();
//				byte[] auth=fm13dt160.auth(0, new byte[]{11,22,33,44});
//				byte[] temperature=fm13dt160.getTemperature(0x07, 0x1000);
//				int temp1=((temperature[0] & 0xFF <<8)|temperature[1] & 0xFF)&0x1FFF ;
//				try {
//					Thread.sleep(400);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				temperature=fm13dt160.getTemperature(0x07, 0x1000);
//				int temp2=((temperature[0] & 0xFF <<8)|temperature[1] & 0xFF)&0x03FF ;
//				if (temp1>temp2) {
//					return;
//				}
//				byte[] rtc=fm13dt160.startRTC();
//				rtc=fm13dt160.stopRTC();
//				byte[] res=fm13dt160.standby();
//				res=fm13dt160.PD();
//				byte[] res=fm13dt160.wakeup(0x00);
//				res=fm13dt160.wakeup(0x80);
//				byte[] res=fm13dt160.readReg(0xc000);
//				res=fm13dt160.writeReg(0xc000, 45);
//				res=fm13dt160.readReg(0xc000);
//				byte[] res=fm13dt160.ledCtrl(0x02);
//				res=fm13dt160.ledCtrl(0x00);
//				byte[] res=fm13dt160.op_Mode_Chk(new byte[]{0,0,0});
//				res=fm13dt160.field_strength_chk(0);
					
			nfcv.close();
		} catch (IOException e) {
			System.out.println(e.getMessage().toString());
		}	
		
		
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

   
    private static int[] getTimeHex() {
        int[] ints = new int[4];
        long aLong = System.currentTimeMillis() / 1000;
        String string = Long.toHexString(aLong);
        int a = Integer.parseInt(string.substring(0, 2), 16),
                b = Integer.parseInt(string.substring(2, 4), 16),
                c = Integer.parseInt(string.substring(4, 6), 16),
                d = Integer.parseInt(string.substring(6, 8), 16);
//        LogUtil.d(string);
        ints[0] = a;
        ints[1] = b;
        ints[2] = c;
        ints[3] = d;
        return ints;
    }

}
