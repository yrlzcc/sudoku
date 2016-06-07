package shirley.com.sudoku.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/4/18.
 */
public class Utils {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取程序的版本
     *
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
        return packageInfo.versionName;
    }

    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
        return packageInfo.versionCode;
    }

    public static String getImei(Context context) {
        String Imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        return Imei;
    }

    public static String getImsi(Context context) {
        String Imsi = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        return Imsi;
    }

    /**
     * 获得型号
     */
    public static String getMODEL() {
        return android.os.Build.MODEL;
    }

    /**
     * 获得品牌
     */
    public static String getBRAND() {
        return android.os.Build.BRAND;
    }

    /**
     * 获得渠道号
     *
     * @param context
     * @return
     */
    public static String getChanel(Context context) {
        String CHANNELID = "update";
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = ai.metaData.get("UMENG_CHANNEL");
            if (value != null) {
                CHANNELID = value.toString();
            }
        } catch (Exception e) {
            //
        }

        return CHANNELID;
    }

    public static int string2Int(String value) {
        int result = -1;
        try {
            result = Integer.parseInt(value);
        } catch (Exception e) {
        }
        return result;
    }

    public static boolean string2Boolean(String value) {
        boolean result = false;
        try {
            result = Boolean.parseBoolean(value);
        } catch (Exception e) {
        }
        return result;
    }

    public static short string2Short(String value) {
        short result = -1;
        try {
            result = Short.parseShort(value);
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 判断是否为手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }
        String phone = "";
        if (mobiles.startsWith("+86")) {
            phone = mobiles.substring(3);
        } else {
            phone = mobiles;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[^4,//D]))\\d{8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 判断密码是否合法 密码可使用英文、数字或下划线，长度为6~16个字
     *
     * @param passwd
     * @return
     */
    public static boolean isPasswd(String passwd) {
        String str = "^[a-zA-Z0-9_-]{6,16}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(passwd);
        return m.matches();
    }

    /**
     * 获取当前时间前一个月的时间戳 ，精确到秒
     *
     * @return long 类型的时间戳 精确到秒
     */
    public static long getLastMonthTime() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis());
        ;
        calendar.add(GregorianCalendar.MONTH, -1);
        long time = calendar.getTimeInMillis() / 1000;
        return time;
    }

    /**
     * @param file
     *            deleted file
     * @param isDeleteRootFile
     *            是否删除文件根目录下面的文件 true——根目录下的文件和文件夹都删除 ； FALSE——只删除根目录下的目录
     */
    /**
     * @param filePath           要删除的文件或目录的路径
     * @param isDeleteCurrentDir 如果要删除的路径是目录的话 是否删除当前目录 true:删除当前目录 false:保留当前目录
     * @param isDeleteSubFile    如果要删除的路径是目录的话 是否只删除当前目录下的目录 不删除文件 true:删除当前目录下的所有文件
     *                           false:保留当前目录下的文件,只删除目录
     */
    public static void deleteFile(String filePath, boolean isDeleteCurrentDir, boolean isDeleteSubFile) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File subFile : files) {
                        if (subFile.isDirectory()) {
                            deleteFile(subFile.getAbsolutePath(), true, true);
                        } else {
                            if (isDeleteSubFile)
                                subFile.delete();
                        }
                    }
                }
                if (isDeleteCurrentDir)
                    file.delete();
            } else {
                file.delete();
            }
        }
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    private static Hashtable<Integer, Bitmap> ht_bitmap = new Hashtable<Integer, Bitmap>();

    public static Bitmap getMergeImageMap(Context context, int number, int sourceImgId, float gravity, int color, float textSize) {

        if (ht_bitmap.containsKey(number)) {
            return ht_bitmap.get(number);
        } else {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getResources().getDrawable(sourceImgId);
            Bitmap sourceBitmap = bitmapDrawable.getBitmap();
            Bitmap newBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(sourceBitmap, new Matrix(), null);

            Paint paint = new Paint();
            paint.setColor(context.getResources().getColor(color));
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            float textsize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, metrics);
            paint.setTextSize(textsize);

            String text = number + "";
            Rect rect = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect);
            paint.setAntiAlias(true);

            canvas.drawText(number + "", (sourceBitmap.getWidth() - rect.width()) / 2, (sourceBitmap.getHeight() + rect.height()) / gravity, paint);
            ht_bitmap.put(number, newBitmap);
            return newBitmap;

        }
    }

    /**
     * 数组转字符串
     * @param arr
     * @return
     */
    public static String arrToString(int[] arr){
        String str = "";
        if(arr != null && arr.length >= 0){
            for(int a:arr){
                str += a + ",";
            }
            str.substring(0,str.length()-1);
        }
        return str;
    }

    public static int[] stringToArr(String str){
        String[] arr = null;
        if(str != null && !str.equals("") ){
            arr = str.split(",");
        }
        return stringToInt(arr);
    }

    public static int[] stringToInt(String[] arr){
        int[] intArr = null;
        if(arr != null && arr.length > 0){
            intArr = new int[arr.length];
            for(int i = 0;i < arr.length;i++){
                intArr[i] =Integer.parseInt(arr[i]);
            }
        }
        return intArr;
    }

}

