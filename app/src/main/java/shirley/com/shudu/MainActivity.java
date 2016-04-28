package shirley.com.shudu;

import android.content.Context;
import android.graphics.Point;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import shirley.com.shudu.utils.DialogUtils;
import shirley.com.shudu.utils.Game;
import shirley.com.shudu.utils.UpdateAction;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener,Observer {

    private final int COLOUMNUM = 9;
    private Context context;
    private GridView gridView;
    private GridItemAdapter gridItemAdapter;
    private Button main_bt_complete;
    private List<GridItem> gridItemsData = null;
//    private CreateSudoku sudoku;
//    private int[][] sudokuData;
//    private int[][] sudokuResult;
    private int selection = -1;
    private int level;
    private DialogUtils dialogUtils = null;
    private Chronometer chronometer = null; //计时器
    private List<Point> conflictList = null; //冲突列表
    private Set<Integer> conflictSet = null;
    private Game game;

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
        initGame(level);
        context = this;
    }

    private void initGame(int level){
        game = new Game();
        game.addObserver(this);
        game.newGame(level);
    }


    @Override
    public void onClick(View v) {
        int key = 1;
        switch (v.getId()){
            case R.id.main_include_select_1:
                key = 1;
//                updateGridState(key);
                break;
            case R.id.main_include_select_2:
                key = 2;
//                updateGridState(key);
                break;
            case R.id.main_include_select_3:
                key = 3;
//                updateGridState(key);
                break;
            case R.id.main_include_select_4:
                key = 4;
//                updateGridState(key);
                break;
            case R.id.main_include_select_5:
                key = 5;
//                updateGridState(key);
                break;
            case R.id.main_include_select_6:
                key = 6;
//                updateGridState(key);
                break;
            case R.id.main_include_select_7:
                key = 7;
//                updateGridState(key);
                break;
            case R.id.main_include_select_8:
                key = 8;
//                updateGridState(key);
                break;
            case R.id.main_include_select_9:
                key = 9;
//                updateGridState(key);
                break;
            case R.id.main_include_btn_clear:
                key = 0;
//                updateGridState(key);
                break;
            default:break;
        }
        game.setSelectedNumber(key);

    }


    /**
     * Sets the fields corresponding to given game.
     *
     * @param game  Game to be set.
     */
    public void setGame(Game game) {
        gridItemsData = new ArrayList<GridItem>();
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                GridItem item = new GridItem();
                item.num = game.getNumber(x, y);
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
     * 获取冲突集合
     * @return
     */
    public Set getConflictSet(){
        Set set =new HashSet<Integer>();
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                boolean isValid = game.checkValid(x,y);
                if(!isValid){
                    set.add(9*x+y);
                }
            }
        }
        return set;
    }



    /**
     * 更新格子的状态
     * @param key
     */
    private void updateGridState(int key){
        System.out.println("selection:" +selection);
        if(selection == -1){
            return;
        }
        int x = selection/9;
        int y = selection%9;
        game.setNumber(x,y,key);
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
                            initGame(level);
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
        setHighLight(gridItemsData, selection);
        gridItemAdapter.notifyDataSetChanged();
    }

    public boolean isItemValidate(){
        boolean isUpdate = updateConflictState();
        return !isUpdate;
    }



    /**
     * 更新冲突状态
     *
     */
    public boolean updateConflictState(){
        if(conflictSet == null){
            conflictSet = new HashSet<Integer>();
        }
        conflictSet.clear();
        conflictSet.addAll(getConflictSet());
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

    /**
     * Method called when model sends update notification.
     *
     * @param observable    The model.
     * @param data   The UpdateAction.
     */
    @Override
    public void update(Observable observable, Object data) {
        System.out.println("update");
            switch ((UpdateAction)data) {
                case NEW_GAME:
                    setGame((Game)observable);
                    break;
                case CHECK:
//                setGameCheck((Game)o);
                    break;
                case SELECTED_NUMBER:
                    Game game = (Game)observable;
                    updateGridState(game.getSelectedNumber());
                    break;
                case CANDIDATES:
                case HELP:
//                setCandidates((Game)o);
                    break;
            }
        }

    /**
     * 设置高亮提示
     * @param data
     * @param position
     */
    public void setHighLight(List<GridItem> data, int position) {
        for(GridItem item:data){
            item.isSelected = false;  //初始化
        }
        int m = 0, n = 0, p = 0, q = 0; // m,n是计数器，p,q用于确定测试点的方格位置
        int x = position/9;
        int y = position%9;
        for (m = 0; m < 9; m++) {
            int tempm = m*9+y;
            data.get(tempm).isSelected = true;
        }
        for (n = 0; n < 9; n++) {
            int tempn = 9*x+n;
            data.get(tempn).isSelected = true;
        }
        for (p = x / 3 * 3, m = 0; m < 3; m++) {
            for (q = y / 3 * 3, n = 0; n < 3; n++) {
                int temppq = 9 * (p + m) + q + n;
                data.get(temppq).isSelected = true;
            }
        }
    }
}
