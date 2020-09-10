package com.example.fm13dt160demo;

import java.lang.reflect.Method;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

/**
 * @author Sai 类说明：基本Act�?
 */
public abstract class BaseAct extends FragmentActivity {

	private NfcAdapter nfcAdapter;
	private PendingIntent nfcPendingIntent;
	private IntentFilter[] tagFilters;
	private String[][] techList;
	private Bundle bundle;

	private boolean hasBackOnActionBar;
	private boolean hasNfc;

	/** 初始化视�? **/
	protected abstract void initViews();

	/** 初始化事�? **/
	protected void initEvents() {

		if (hasBackOnActionBar) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		if (hasNfc) {
			nfcAdapter = NfcAdapter.getDefaultAdapter(this);
			nfcPendingIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, getClass())
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			techList = new String[][] { { NfcV.class.getName() } };
			IntentFilter ndefDetected = new IntentFilter(
					NfcAdapter.ACTION_NDEF_DISCOVERED);
			tagFilters = new IntentFilter[] { ndefDetected };
		}
	}

	
	
	/** 初始�? **/
	protected void init(boolean hasBackOnActionBar, boolean hasNfc) {
		setHasBackOnActionBar(hasBackOnActionBar);
		setHasNfc(hasNfc);
	}

	/** 短暂显示Toast提示(来自res) **/
	protected void showShortToast(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
	}

	/** 短暂显示Toast提示(来自String) **/
	protected void showShortToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	/** 长时间显示Toast提示(来自res) **/
	protected void showLongToast(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
	}

	/** 长时间显示Toast提示(来自String) **/
	protected void showLongToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (hasNfc) {
			nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent,
					tagFilters, techList);
//			enableReaderMode();
		}
	}
	
//	@TargetApi(19)
//    public void enableReaderMode() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){ //19 4.4
//            int READER_FLAGS = -1;
//            Bundle option = new Bundle();
//            option.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 1000);// 延迟对卡片的检测
//            if (nfcAdapter != null) {
//            	nfcAdapter.enableReaderMode(this,new MyReaderCallback(),READER_FLAGS,option);
//              //-1代表所有类别的芯片都可以识别,如果只是单独的识别一种就填写对应的数值即可
//            }
//
//        }
//    }
	
//	private class  MyReaderCallback implements NfcAdapter.ReaderCallback {
//        @Override
//        public void onTagDiscovered(Tag tag) {
//            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            long[] duration = {15, 30, 60, 90};
//            vibrator.vibrate(duration, -1);
//            String[] techList = tag.getTechList();
//            MainActivity.this.mTag = tag;
//            Intent record = new Intent("record");
//            sendBroadcast(record);
//
//        }
//    }

	@Override
	protected void onPause() {
		super.onPause();
		if (hasNfc) {
			nfcAdapter.disableForegroundDispatch(this);
//			nfcAdapter.disableReaderMode(this);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (hasNfc) {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			bundle = new Bundle();
			bundle.putParcelable("newtag", tag);
			setBundle(bundle);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (hasBackOnActionBar) {
			switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		setOverflowIconVisible(featureId, menu);
		return super.onMenuOpened(featureId, menu);
	}
	
	/**
	* 利用反射让隐藏在Overflow中的MenuItem显示Icon图标
	* @param featureId
	* @param menu
	* onMenuOpened方法中调�?
	*/
	public static void setOverflowIconVisible(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
	}

	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	public boolean isHasBackOnActionBar() {
		return hasBackOnActionBar;
	}

	public void setHasBackOnActionBar(boolean hasBackOnActionBar) {
		this.hasBackOnActionBar = hasBackOnActionBar;
	}

	public boolean isHasNfc() {
		return hasNfc;
	}

	public void setHasNfc(boolean hasNfc) {
		this.hasNfc = hasNfc;
	}
	
	
}
