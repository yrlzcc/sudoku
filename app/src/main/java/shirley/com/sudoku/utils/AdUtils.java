package shirley.com.sudoku.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import sdw.sea.erd.AdManager;
import sdw.sea.erd.normal.banner.BannerManager;
import sdw.sea.erd.normal.spot.SpotDialogListener;
import sdw.sea.erd.normal.spot.SpotManager;
import sdw.sea.erd.onlineconfig.OnlineConfigCallBack;

//import net.youmi.android.AdManager;
//import net.youmi.android.banner.AdSize;
//import net.youmi.android.banner.AdView;
//import net.youmi.android.onlineconfig.OnlineConfigCallBack;

/**
 * Created by lichuang on 2016/5/18.
 */
public class AdUtils {
    private static boolean isTestOpen = false;  //自己测试时用的开关
    public static boolean isOpen = false;
    public static void setAD(Activity context){
        // 实例化 LayoutParams（重要）
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        // 设置广告条的悬浮位置
        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角

        // 实例化广告条
        View adView = BannerManager.getInstance(context).getBanner(context);
        // 调用 Activity 的 addContentView 函数
        context.addContentView(adView, layoutParams);
    }

    public static void setAdInViewGroup(Activity context,LinearLayout adLayout){

        View adView = BannerManager.getInstance(context).getBanner(context);
        adLayout.addView(adView);
    }

    public static void getOnlineVar( Context context){
        // 方法二： 异步调用（可在任意线程中调用）
        SharedPreferences sharedPreferences = context.getSharedPreferences("onlinevar", Context.MODE_PRIVATE);
        final SharedPreferences.Editor  editor = sharedPreferences.edit();
        AdManager.getInstance(context).asyncGetOnlineConfig("openAd", new OnlineConfigCallBack() {
            @Override
            public void onGetOnlineConfigSuccessful(String key, String value) {
                // TODO Auto-generated method stub
                // 获取在线参数成功
                editor.putInt(key, Integer.parseInt(value));
                editor.commit();
                int v = Integer.parseInt(value);
                System.out.println("onlinevar:" + isOpen);
                isOpen = (v == 1 ? true : false);

            }

            @Override
            public void onGetOnlineConfigFailed(String key) {
                // TODO Auto-generated method stub
                System.out.println("get online var failed!");
            }
        });
    }

    public static boolean openAd(Context context){
        if(isOpen){
            SpotManager.getInstance(context).showSpotAds(context);
        }
        if(isTestOpen){
            SpotManager.getInstance(context).showSpotAds(context);
        }
        return isOpen;
    }

    public static void openTestAd(Context context){
        if(isTestOpen){
            SpotManager.getInstance(context).showSpotAds(context);
        }
    }

    public static boolean openAd(Context context,SpotDialogListener listener){
        if(isOpen){
            SpotManager.getInstance(context).showSpotAds(context,listener);
        }
        return isOpen;
    }

    public static void openBanner(Activity context){
        if(isTestOpen){
            setAD(context);
        }
    }
}
