package shirley.com.sudoku.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;

import com.tencent.connect.common.UIListenerManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import shirley.com.sudoku.R;

/**
 * Created by Administrator on 2016/6/17.
 */
public class ShareUtils {
    private static ShareUtils instance = null;

    final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };

    public static ShareUtils getInstance(){
        if(instance == null){
            instance = new ShareUtils();
        }
        return instance;
    }


    /**
     * 游戏下拉菜单中的分享
     *
     */
    public void showShare(Activity activity,String strText,UMShareListener listener) {
        Constans.SHAREURL= MobclickAgent.getConfigParams(activity, "shareUrl");
        System.out.println("shareurl:" + Constans.SHAREURL);
        UMImage image = new UMImage(activity,
                BitmapFactory.decodeResource(activity.getResources(), R.mipmap.icon));
        ShareContent content = new ShareContent();
        content.mTitle = "高智商游戏";
        content.mText = strText;
        new ShareAction(activity).setDisplayList(displaylist)
                .withText(strText)
                .withTitle("高智商游戏")
                .setShareContent(content)
                .withMedia(image)
                .withTargetUrl(Constans.SHAREURL)
                .setCallback(listener)
                .open();
    }

}
