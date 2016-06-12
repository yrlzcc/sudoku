package shirley.com.sudoku.uiBase;



import android.content.Context;
import android.content.SharedPreferences;

import shirley.com.sudoku.utils.Utils;

/**
 * Created by Administrator on 2016/5/9.
 */
public class SettingPreferences {
  private static final String SETTING_PRE_NAME = "setting_pre_config";

    public static final String KEY_SETTING_SWITCH_CONFLICT = "key_setting_switch_conflict";
    public static final String KEY_SETTING_SWITCH_TIPS = "key_setting_switch_tips";
    public static final String KEY_SETTING_SWITCH_SOUND = "key_setting_switch_sound";
    public static final String  KEY_CURRENT_SUDOKU_DATA = "key_current_sudoku_data";
    public static final String  KEY_CURRENT_SUDOKU_INPUTLIST = "key_current_sudoku_inputlist";
    public static final String  KEY_CURRENT_SUDOKU_INPUTLIST_CURSOR = "key_current_sudoku_inputlist_cursor";
    public static final String  KEY_CURRENT_SUDOKU_SELECTION = "key_current_sudoku_selection";
    public static final String  KEY_CURRENT_SUDOKU_ISMARK = "key_current_sudoku_ismark";
    public static final String  KEY_CURRENT_SUDOKU_CURRENTLEVEL = "key_current_sudoku_level";
    public static final String  KEY_CURRENT_SUDOKU_TIME = "key_current_sudoku_time";
    public static final String  KEY_CURRENT_CURRENT_GRADE = "key_current_current_grade";
    /** 是否是首次运行 */
    public static final String KEY_FIRST_LUNCH = "first_lunch";

    /**
     * @param context
     *            上下文对象
     * @param key
     *            sharedPreferences 存储的key
     * @param value
     *            Boolean类型的值
     */
    public static void setSettingValue(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * @param context
     *            上下文对象
     * @param key
     *            sharedPreferences 存储的key
     * @param value
     *            String类型的值
     */
    public static void setSettingValue(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * @param context
     *            上下文对象
     * @param key
     *            sharedPreferences 存储的key
     * @param value
     *            Int类型的值
     */
    public static void setSettingValue(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    /**
     * @param context
     *            上下文对象
     * @param key
     *            sharedPreferences 存储的key
     * @param value
     *            Long类型的值
     */
    public static void setSettingValue(Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    /**
     * @param context
     *            上下文对象
     * @param key
     *            sharedPreferences 存储的key
     * @param defaultValue
     *            默认的值
     * @return
     */
    public static boolean getSettingValue(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, defaultValue);
        return value;
    }

    public static String getSetStringValue(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        String value = sp.getString(key, "");
        return value;
    }

    public static int getValue(Context context, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        int value = sp.getInt(key, defaultValue);
        return value;
    }

    public static long getValue(Context context, String key, long defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        long value = sp.getLong(key, defaultValue);
        return value;
    }

    public static boolean isFristLunch(Context context) {
        String version = getSetStringValue(context, KEY_FIRST_LUNCH);
        if (version.equals(Utils.getVersion(context))) {
            return false;
        }
        return true;

    }
}

