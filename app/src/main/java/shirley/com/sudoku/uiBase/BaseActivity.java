package shirley.com.sudoku.uiBase;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends FragmentActivity {
	public static final boolean isDebug = true; // app是否是测试包
	public boolean isDestory;
	public boolean isResume;
	private boolean exit;
	private ApplicationManager applicationManager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		MobclickAgent.setDebugMode(true);
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
//		MobclickAgent.onResume(this);
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
//		MobclickAgent.onPause(this);
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

}