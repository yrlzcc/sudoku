package shirley.com.sudoku.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/14.
 */
public class GridItem {
    public int  num;
    public boolean isFix = true;  //是否是原始固定的
    public boolean isSelected = false;
    public boolean isSame = false; //是否有重复
    public int[] marknums;  //标记数字
    public boolean isMark = false; //是否是标记
    public int currentMarkNum = 0; //当前标记数字
}
