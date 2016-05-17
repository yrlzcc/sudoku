package shirley.com.sudoku.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class BaseItem {
    public int num;  //数字
    public int position;  //位置
    public boolean isMark = false; //是否是标记

    public int prenum; //当前格前一个数字
    public boolean ispreMark = false; //当前格前一个是否是标记
    public int[] preMarkNum;

}
