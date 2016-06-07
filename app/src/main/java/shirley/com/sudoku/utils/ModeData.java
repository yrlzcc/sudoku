package shirley.com.sudoku.utils;

import android.content.Context;

import shirley.com.sudoku.R;

/**
 * Created by Administrator on 2016/5/25.
 */
public class ModeData {

    public ModeData(Context context){

    }

    public static int[][] modeData = {
            {1, R.color.mode_color_1},
            {2, R.color.mode_color_2},
            {3, R.color.mode_color_3},
            {4, R.color.mode_color_4},
            {5, R.color.mode_color_5},
            {6, R.color.mode_color_6},
            {7, R.color.mode_color_7},
            {8, R.color.mode_color_8},
            {9, R.color.mode_color_9}
    };

    public static int[][] levelString = {
            {Constans.LEVEL1, R.string.level1},
            {Constans.LEVEL2, R.string.level2},
            {Constans.LEVEL3, R.string.level3},
            {Constans.LEVEL4, R.string.level4}
    };
}
