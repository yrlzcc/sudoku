package shirley.com.sudoku;

import android.content.Intent;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

import java.util.logging.Handler;

import sdw.sea.erd.AdManager;
import sdw.sea.erd.normal.spot.SplashView;
import sdw.sea.erd.normal.spot.SpotDialogListener;
import sdw.sea.erd.normal.spot.SpotManager;
import shirley.com.sudoku.uiBase.BaseActivity;
import shirley.com.sudoku.utils.AdUtils;
import shirley.com.sudoku.utils.ReadSudokuUtil;

public class SplashActivity extends BaseActivity {
    private static final int READ_COMPLETE = 1;
    protected ReadSudokuUtil utils = null;
    public Handler handler = null;
    private boolean isAdComplete = false;
    private boolean isOpen = false;
//    private boolean is

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashView splashView = SpotManager.getInstance(this).getSplashView(this);
        splashView.hideCloseBtn(false);
//        splashView.setIsJumpTargetWhenFail(true);
//        Intent intent = new Intent(SplashActivity.this,MenuActivity.class);
//        splashView.setIntent(intent);
//开屏也可以作为控件加入到界面中。
        setContentView(splashView.getSplashView());

        isOpen = AdUtils.openSplashAd(this, splashView,
                new SpotDialogListener() {

                    @Override
                    public void onShowSuccess() {
                        Log.i("YoumiAdDemo", "开屏展示成功");
                        isAdComplete = false;
                    }

                    @Override
                    public void onShowFailed() {
                        Log.i("YoumiAdDemo", "开屏展示失败。");
                        isAdComplete = true;
                    }

                    @Override
                    public void onSpotClosed() {
                        Log.i("YoumiAdDemo", "开屏关闭。");
                        isAdComplete = true;
                    }

                    @Override
                    public void onSpotClick(boolean b) {

                    }
                });
//            SpotManager.getInstance(this).showSplashSpotAds(this, MenuActivity.class);

        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                utils = new ReadSudokuUtil(SplashActivity.this);
                sudokuData = utils.read();
                Message message = new Message();
                message.what = READ_COMPLETE;
                mainThreadHandler.sendMessageDelayed(message,2000);

            }
        }).start();
    }


    public MainHandler mainThreadHandler = new MainHandler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what){
                case READ_COMPLETE:
                        finish();
                        Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
                        startActivity(intent);
                    break;
            }
        }
    };
}
