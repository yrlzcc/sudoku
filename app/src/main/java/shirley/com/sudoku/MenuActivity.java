package shirley.com.sudoku;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import shirley.com.sudoku.uiBase.BaseActivity;
import shirley.com.sudoku.utils.Constans;

public class MenuActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UmengUpdateAgent.update(this);
//        UmengUpdateAgent.setDeltaUpdate(false);
        checkUpdate();
        setContentView(R.layout.activity_menu);
        findViewById(R.id.menu_level1).setOnClickListener(this);
        findViewById(R.id.menu_level2).setOnClickListener(this);
        findViewById(R.id.menu_level3).setOnClickListener(this);
        findViewById(R.id.menu_level4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int level = 1;
        switch (v.getId()){
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
            default:break;
        }
        Intent intent = new Intent(MenuActivity.this,MainActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }

    private void checkUpdate(){
        PgyUpdateManager.register(this,
                new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {
                        System.out.println("onUpdateAvailable:" + result);
                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        new AlertDialog.Builder(MenuActivity.this)
                                .setTitle("更新")
                                .setMessage("")
                                .setNegativeButton(
                                        "确定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                startDownloadTask(
                                                        MenuActivity.this,
                                                        appBean.getDownloadURL());
                                            }
                                        }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        System.out.println("onNoUpdateAvailable-------------------");
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                System.exit(0);
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
