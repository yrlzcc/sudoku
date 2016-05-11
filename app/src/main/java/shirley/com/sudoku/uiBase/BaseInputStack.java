package shirley.com.sudoku.uiBase;

import java.util.ArrayList;
import java.util.List;

import shirley.com.sudoku.model.BaseItem;
import shirley.com.sudoku.model.GridItem;

/**
 * Created by Administrator on 2016/5/10.
 */
public class BaseInputStack {
    private static List<BaseItem> inputlist;
    private static BaseInputStack baseInputStack = null;

    public static BaseInputStack getInstance(){
        if(inputlist == null){
            inputlist = new ArrayList<BaseItem>();
            baseInputStack = new BaseInputStack();
        }
        return baseInputStack;
    }

    /**
     * 增加一个当前输入的记录
     * @param item
     */
    public static void pushCurrentInput(BaseItem item){
        if(item == null || inputlist == null){
            return;
        }
        inputlist.add(item);
    }

}
