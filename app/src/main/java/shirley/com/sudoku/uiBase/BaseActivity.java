package shirley.com.sudoku.uiBase;
import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;

import shirley.com.sudoku.BaseApplication;
import shirley.com.sudoku.model.SudokuData;
import shirley.com.sudoku.utils.AdUtils;
import shirley.com.sudoku.utils.ReadSudokuUtil;

public class BaseActivity extends FragmentActivity {
	public static final boolean isDebug = false; // app是否是测试包
	protected static final int SETTING = 10;
	public boolean isDestory;
	public boolean isResume;
	private boolean exit;
	private ApplicationManager applicationManager;
	protected BaseApplication app = BaseApplication.getInstance();
	protected static boolean isSoundOpen = true;  //声音开关
	protected static boolean isHighlightTipsOpen = true;  //高亮提示开关
	protected static boolean isConflictHelpOpen = true; 	//帮助开关
//	protected static boolean isAutoFill = false; //自动填充所有开关
	protected static int mode = 1; //模式
	protected static int currentLevel = 0;
	protected static int[] currentGrade ;
	protected static SudokuData sudokuData = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setExit(false);
		if (applicationManager == null) {
			applicationManager = ApplicationManager.getScreenManager();
		}
		applicationManager.pushActivity(this);
	}

	@Override
	protected void onResume() {
		isDestory = false;
		isResume = true;
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		// super.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();
		isResume = false;
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		checkAppBackGround();
	}

	@Override
	protected void onDestroy() {
		isDestory = true;
		super.onDestroy();
	}

	/**
	 * 检测App是否在后台运行
	 * 
	 * @return
	 */
	private boolean checkAppBackGround() {
		return false;
	}

	public static boolean isRunningBackGround = false;

	/****************************** 保存读取当前用户信息 结束 **********************************/

	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

	public void popAllActivity() {
		applicationManager.popAllActivity();
	}

	@Override
	public void finish() {
		super.finish();
		applicationManager.removeActivity(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		applicationManager.popActivity(this);
	}

	public void rebootApplication(Class<? extends Activity> launchClz) {
		applicationManager.popAllActivity();
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(this, launchClz);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 500, pendingIntent);
		System.exit(0);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	//可以将一下代码加到你的MainActivity中，或者在任意一个需要调用分享功能的activity当中
//	String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS};
//	ActivityCompat.requestPermissions(this,mPermissionList, 100);

	public class MainHandler extends Handler {

		public void handleMessage(Message msg) {
			switch (msg.what) {

			}
		}
	}

}