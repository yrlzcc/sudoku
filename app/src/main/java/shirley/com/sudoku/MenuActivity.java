package shirley.com.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import shirley.com.sudoku.uiBase.BaseActivity;
import shirley.com.sudoku.uiBase.SettingPreferences;
import shirley.com.sudoku.utils.AdUtils;
import shirley.com.sudoku.utils.Constans;
import shirley.com.sudoku.utils.DialogUtils;
import shirley.com.sudoku.utils.ShareUtils;
import shirley.com.sudoku.utils.Utils;

public class MenuActivity extends BaseActivity implements View.OnClickListener, UMShareListener {

    private DialogUtils dialogUtils = null;
    private Button level1,level2,level3,level4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UmengUpdateAgent.update(this);
//        UmengUpdateAgent.setDeltaUpdate(false);
        setContentView(R.layout.activity_menu);
        level1 = (Button)findViewById(R.id.menu_level1);
        level1.setOnClickListener(this);
        level2 = (Button)findViewById(R.id.menu_level2);
        level2.setOnClickListener(this);
        level3 = (Button)findViewById(R.id.menu_level3);
        level3.setOnClickListener(this);
        level4 = (Button)findViewById(R.id.menu_level4);
        level4.setOnClickListener(this);
        findViewById(R.id.menu_bottommenu_setting).setOnClickListener(this);
        findViewById(R.id.menu_bottommenu_share).setOnClickListener(this);
        findViewById(R.id.menu_bottommenu_help).setOnClickListener(this);
        readSettingValue();
        currentGrade = Utils.stringToArr(SettingPreferences.getSetStringValue(this, SettingPreferences.KEY_CURRENT_CURRENT_GRADE));
        if(currentGrade == null){
            currentGrade = new int[4];
        }
        currentLevel = SettingPreferences.getValue(this, SettingPreferences.KEY_CURRENT_SUDOKU_CURRENTLEVEL,0);
        checkUpdate();
//        AdUtils.openBanner(this);
        updateButtonText();
        AdUtils.openTestAd(this);
    }

    /**
     * 更新当前级别
     */
    private void updateButtonText(){
        System.out.println("---------------------grade:" + currentGrade);
        if(level1 != null)
        level1.setText(String.format(getResources().getString(R.string.level1),currentGrade[0]));
        if(level2 != null)
        level2.setText(String.format(getResources().getString(R.string.level2),currentGrade[1]));
        if(level3 != null)
        level3.setText(String.format(getResources().getString(R.string.level3),currentGrade[2]));
        if(level4 != null)
        level4.setText(String.format(getResources().getString(R.string.level4),currentGrade[3]));
    }
    @Override
    public void onClick(View v) {
        int level = 0;
        switch (v.getId()) {
            case R.id.menu_level1: {
                level = Constans.LEVEL1;
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("level", level);
                startActivity(intent);
            }
                break;
            case R.id.menu_level2:{
                level = Constans.LEVEL2;
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("level", level);
                startActivity(intent);
            }
                break;
            case R.id.menu_level3: {
                level = Constans.LEVEL3;
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("level", level);
                startActivity(intent);
            }
                break;
            case R.id.menu_level4: {
                level = Constans.LEVEL4;
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("level", level);
                startActivity(intent);
            }
                break;
            case R.id.menu_bottommenu_setting: {
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.menu_bottommenu_share:
                ShareUtils.getInstance().showShare(MenuActivity.this,MenuActivity.this.getResources().getString(R.string.sudoku_share_text).toString(),MenuActivity.this);
                break;
            case R.id.menu_bottommenu_help: {
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
            }
                break;
            default:
                break;
        }
    }

    private void checkUpdate() {
        PgyUpdateManager.register(this,
                new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {
                        System.out.println("onUpdateAvailable:" + result);
                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        dialogUtils = new DialogUtils(MenuActivity.this, "更新", appBean.getReleaseNote(), new DialogUtils.OnDialogSelectId() {
                            @Override
                            public void onClick(int whichButton) {
                                switch (whichButton) {
                                    case 0:
                                        dialogUtils.dismiss();
                                        break;
                                    case 1:
                                        dialogUtils.dismiss();
                                        startDownloadTask(
                                                MenuActivity.this,
                                                appBean.getDownloadURL());
                                        break;
                                }
                            }
                        });
                        dialogUtils.show();
                        dialogUtils.setConfirmText(MenuActivity.this.getResources().getString(R.string.sudoku_download));
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        System.out.println("onNoUpdateAvailable-------------------");
                    }
                }

        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateButtonText();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onResume(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showExitDialog();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 弹出退出对话框
     */
    private void showExitDialog() {
        dialogUtils = new DialogUtils(this, "退出", this.getResources().getString(R.string.sudoku_dialog_exit), new DialogUtils.OnDialogSelectId() {
            @Override
            public void onClick(int whichButton) {
                switch (whichButton) {
                    case 0:
                        dialogUtils.dismiss();
                        break;
                    case 1:
                        System.exit(0);
                        break;
                }
            }
        });
        dialogUtils.show();
    }

    /**
     * 读取设置参数
     */
    private void readSettingValue() {
        isHighlightTipsOpen = SettingPreferences.getSettingValue(this, SettingPreferences.KEY_SETTING_SWITCH_TIPS, true);
        isConflictHelpOpen = SettingPreferences.getSettingValue(this, SettingPreferences.KEY_SETTING_SWITCH_CONFLICT, true);
        isSoundOpen = SettingPreferences.getSettingValue(this, SettingPreferences.KEY_SETTING_SWITCH_SOUND, true);
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        Toast.makeText(this, " 分享成功啦", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        Toast.makeText(this," 分享失败啦", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        Toast.makeText(this," 分享取消啦", Toast.LENGTH_SHORT).show();
    }
}
