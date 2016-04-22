package shirley.com.shudu;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import shirley.com.shudu.utils.CreateSudoku;
import shirley.com.shudu.utils.DialogUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final int COLOUMNUM = 9;
    private Context context;
    private GridView gridView;
    private GridItemAdapter gridItemAdapter;
    private Button main_bt_complete;
    private List<GridItem> gridItemsData = null;
    private CreateSudoku sudoku;
    private int[][] sudokuData;
    private int[][] sudokuResult;
    private int selection = -1;
    private int level;
    private DialogUtils dialogUtils = null;
    private Chronometer chronometer = null; //计时器
    private List<Point> conflictList = null; //冲突列表
    private Set<Integer> conflictSet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView)findViewById(R.id.main_gv_show);
        findViewById(R.id.main_include_select_1).setOnClickListener(this);
        findViewById(R.id.main_include_select_2).setOnClickListener(this);
        findViewById(R.id.main_include_select_3).setOnClickListener(this);
        findViewById(R.id.main_include_select_4).setOnClickListener(this);
        findViewById(R.id.main_include_select_5).setOnClickListener(this);
        findViewById(R.id.main_include_select_6).setOnClickListener(this);
        findViewById(R.id.main_include_select_7).setOnClickListener(this);
        findViewById(R.id.main_include_select_8).setOnClickListener(this);
        findViewById(R.id.main_include_select_9).setOnClickListener(this);
        findViewById(R.id.main_include_btn_clear).setOnClickListener(this);
        chronometer = (Chronometer)findViewById(R.id.main_ch_time);
        gridView.setOnItemClickListener(this);
        level = getIntent().getIntExtra("level",1);
        initGridView(level);
        context = this;
    }

    private void initGridView(int level){
        sudoku = new CreateSudoku(level);
        sudokuData = sudoku.getSudokuData();
        sudokuResult = sudoku.getResultData();
        if(sudokuData == null){
            return;
        }
        gridItemsData = new ArrayList<GridItem>();
        for(int i = 0;i <sudokuData.length;i++){
            for(int j =0;j < sudokuData[0].length;j++){
                GridItem item = new GridItem();
                item.num = sudokuData[i][j];
                if(item.num == 0){
                    item.isFix = false;
                }
                gridItemsData.add(item);
            }
        }
        gridItemAdapter = new GridItemAdapter(this,gridItemsData,selection);
        gridView.setAdapter(gridItemAdapter);
        chronometer.start();
        // 将计时器清零
        chronometer.setBase(SystemClock.elapsedRealtime());
        //开始计时
        chronometer.start();
    }

    /**
     * 判断数独结果是否正确
     * @return
     */
    private int isResultCorrect(){
        int wrongNum = 0;  //记录错误数
        for(int i = 0;i < sudokuResult.length;i++){
            for(int j=0;j < sudokuResult[0].length;j++){
                System.out.print(sudokuResult[i][j]);
                int position = COLOUMNUM * i + j;
                GridItem item = (GridItem)gridItemAdapter.getItem(position);
                if(item.num != sudokuResult[i][j]){
                    wrongNum++;
                }
            }
            System.out.println();
        }
        return wrongNum;
    }

    @Override
    public void onClick(View v) {
        int key = 1;
        switch (v.getId()){
            case R.id.main_include_select_1:
                key = 1;
                updateGridState(key);
                break;
            case R.id.main_include_select_2:
                key = 2;
                updateGridState(key);
                break;
            case R.id.main_include_select_3:
                key = 3;
                updateGridState(key);
                break;
            case R.id.main_include_select_4:
                key = 4;
                updateGridState(key);
                break;
            case R.id.main_include_select_5:
                key = 5;
                updateGridState(key);
                break;
            case R.id.main_include_select_6:
                key = 6;
                updateGridState(key);
                break;
            case R.id.main_include_select_7:
                key = 7;
                updateGridState(key);
                break;
            case R.id.main_include_select_8:
                key = 8;
                updateGridState(key);
                break;
            case R.id.main_include_select_9:
                key = 9;
                updateGridState(key);
                break;
            case R.id.main_include_btn_clear:
                key = 0;
                updateGridState(key);
                break;
            default:break;
        }

    }

    /**
     * 更新格子的状态
     * @param key
     */
    private void updateGridState(int key){
        if(selection == -1){
            return;
        }
        gridItemsData.get(selection).num = key;
        isItemValidate();
        if(isComplete()){
            chronometer.stop();
            dialogUtils = new DialogUtils(context, "选择", "成功", new DialogUtils.OnDialogSelectId() {
                @Override
                public void onClick(int whichButton) {
                    switch (whichButton) {
                        case 0:
                            dialogUtils.dismiss();
                            break;
                        case 1:
                            initGridView(level);
                            dialogUtils.dismiss();
                            break;
                    }
                }
            });
            dialogUtils.show();
        }
        gridItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selection = position;
        gridItemAdapter.setSelection(selection);
        if(gridItemsData.get(position).isFix){
            return;
        }
        sudoku.setHighLight(gridItemsData,selection);
        gridItemAdapter.notifyDataSetChanged();
    }

    public boolean isItemValidate(){
        boolean isUpdate = updateConflictState();
        return !isUpdate;
    }

    /**
     * 冲突列表放到set里
     */
    private void addConflictToset(){
        conflictList = sudoku.validateForList(gridItemsData);
        if(conflictList == null || conflictList.size() == 0){
            return;
        }
        if(conflictSet == null){
            conflictSet = new HashSet<Integer>();
        }
        else{
            conflictSet.clear();
        }
        for(Point pt:conflictList){
            int position = pt.x * COLOUMNUM + pt.y;
            conflictSet.add(position);
        }
    }



    /**
     * 更新冲突状态
     *
     */
    public boolean updateConflictState(){
       addConflictToset();
        if(conflictSet == null || conflictSet.size() == 0){
            return false;
        }
        int size = gridItemsData.size();
        for(int i = 0;i < size;i++){
            if(conflictSet.contains(i)){
                gridItemsData.get(i).isSame = true;
            }
            else{
                gridItemsData.get(i).isSame = false;
            }
        }
        return true;
    }

    /**
     * 判断是否完成
     * @return
     */
    public boolean isComplete(){
        for(GridItem item:gridItemsData){
            if(item.num == 0 || item.isSame == true){
                return false;
            }
        }
        return true;
    }
}
