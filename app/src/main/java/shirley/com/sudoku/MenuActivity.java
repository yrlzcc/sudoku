package shirley.com.sudoku;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import sdw.sea.erd.normal.spot.SpotManager;
import shirley.com.sudoku.uiBase.BaseActivity;
import shirley.com.sudoku.utils.AdUtils;
import shirley.com.sudoku.utils.Constans;
import shirley.com.sudoku.utils.DialogUtils;

public class MenuActivity extends BaseActivity implements View.OnClickListener {

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
        checkUpdate();
        AdUtils.openAd(this);
        updateButtonText();
    }

    /**
     * 更新当前级别
     */
    private void updateButtonText(){
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
        int level = 1;
        switch (v.getId()) {
            case R.id.menu_level1:
                level = Constans.LEVEL1;
                break;
            case R.id.menu_level2:
                level = Constans.LEVEL2;
                break;
            case R.id.menu_level3:
                level = Constans.LEVEL3;
                break;
            case R.id.menu_level4:
                level = Constans.LEVEL4;
                break;
            default:
                break;
        }
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
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
}
