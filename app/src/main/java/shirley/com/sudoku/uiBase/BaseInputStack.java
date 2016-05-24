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
    private static int cursor = -1;
    private boolean isPreEnable = false;  //是否可向前
    private boolean isNextEnable = false; //是否可向后

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
    public  void pushCurrentInput(BaseItem item){
        if(inputlist == null){
            return;
        }
        cursor++;
        inputlist.add(item);
        updatePreState();
        updateNextState();
    }

    /**
     * 出栈到指定位置，出要是撤销完成之后的操作
     * @return
     */
    public  BaseItem popToCurrentPosition() {
        if (isEmpty()) {
            return null;
        }
        int size = inputlist.size();
        for (int i = size - 1; i >= 0; i--) {
            if (i == cursor) {
                return inputlist.get(i);
            }
            inputlist.remove(i);
        }
        return null;
    }

    /**
     * 往前撤销
     * @return
     */
    public  BaseItem getPre(){
        if (isEmpty()) {
            return null;
        }
        if(cursor < 0){
            return null;
        }
        BaseItem item = inputlist.get(cursor);
        cursor --;
        updatePreState();
        updateNextState();
        return item;
    }

    /**
     * 更新向前的状态
     */
    private void updatePreState(){
        if(cursor < 0){
            isPreEnable = false;
        }
        else{
            isPreEnable = true;
        }
    }

    /**
     * 往后撤销
     * @return
     */
    public  BaseItem getNext(){
        if (isEmpty()) {
            return null;
        }
        int size = inputlist.size();
        if((cursor+1) >= size){
            return null;
        }
        cursor++;
        BaseItem item = inputlist.get(cursor);
        updatePreState();
        updateNextState();
        return item;
    }

    /**
     * 更新向后的状态
     */
    private void updateNextState(){
        if(isEmpty()){
            isPreEnable = false;
            return;
        }
        int size = inputlist.size();
        if((cursor+1) >= size){
            isNextEnable = false;
        }
        else{
            isNextEnable = true;
        }
    }


    /**
     * 判空
     * @return
     */
    public  boolean isEmpty(){
        if (inputlist == null || inputlist.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 向前是否可用
     * @return
     */
    public boolean isPreEnable() {
        return isPreEnable;
    }

    /**
     * 向后是否可用
     * @return
     */
    public boolean isNextEnable() {
        return isNextEnable;
    }

    /**
     * 清空栈的数据
     * @return
     */
    public void reset(){
        if(inputlist != null){
            inputlist.clear();
        }
        cursor = -1;
        isPreEnable = false;
        isNextEnable = false;
    }
}
