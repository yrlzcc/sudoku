package shirley.com.sudoku;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Message;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import shirley.com.sudoku.uiBase.BaseActivity;
import shirley.com.sudoku.uiBase.SettingPreferences;
import shirley.com.sudoku.utils.DialogUtils;
import shirley.com.sudoku.utils.Game;
import shirley.com.sudoku.utils.ProgressDialogUtils;
import shirley.com.sudoku.utils.UpdateAction;
import shirley.com.sudoku.model.GridItem;
import shirley.com.sudoku.view.GridItemAdapter;

public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, Observer {

    private final int COLOUMNUM = 9;
    private Context context;
    private GridView gridView;
    private GridItemAdapter gridItemAdapter;
    private ProgressDialogUtils progressDialog = null;
    private List<GridItem> gridItemsData = null;
    private int selection = -1;
    private int level;
    private DialogUtils dialogUtils = null;
    private Chronometer chronometer = null; //计时器
    private Game game;
    private boolean isInitSuccess = false; //是否加载成功
    private boolean isMark = false; //是否是标记状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readSettingValue();
        gridView = (GridView) findViewById(R.id.main_gv_show);
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
        findViewById(R.id.main_include_btn_mark).setOnClickListener(this);
        findViewById(R.id.imagebutton_right).setOnClickListener(this);
        findViewById(R.id.imagebutton_left).setOnClickListener(this);
        chronometer = (Chronometer) findViewById(R.id.main_ch_time);
        gridView.setOnItemClickListener(this);
        level = getIntent().getIntExtra("level", 1);
        context = this;
        showProgressDialog("正在生成数独...");
        initGame(level);
    }

    /**
     * 初始化游戏
     *
     * @param level
     */
    private void initGame(final int level) {
        initData();
        gridItemAdapter = new GridItemAdapter(context, gridItemsData, selection);
        gridView.setAdapter(gridItemAdapter);
        new Thread() {
            public void run() {
                game = new Game(context);
                game.addObserver(MainActivity.this);
                game.newGame(level);
            }
        }.start();
    }

    /**
     * 初始化九宫格
     */
    private void initData() {
        gridItemsData = new ArrayList<GridItem>();
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                GridItem item = new GridItem();
                item.num = 0;
                item.isSelected = false;
                item.isSame = false;
                item.isFix = false;
                gridItemsData.add(item);
            }
        }
    }

    /**
     * 显示进度条
     *
     * @param content
     */
    private void showProgressDialog(String content) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialogUtils(this, content, null);
        } else {
            progressDialog.setContent(content);
        }
        progressDialog.show();
    }

    /**
     * 隐藏进度条
     */
    private void dissmissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        int key = 1;
        switch (v.getId()) {
            case R.id.main_include_select_1:
                key = 1;
                onItemClick(key);
                break;
            case R.id.main_include_select_2:
                key = 2;
                onItemClick(key);
                break;
            case R.id.main_include_select_3:
                key = 3;
                onItemClick(key);
                break;
            case R.id.main_include_select_4:
                key = 4;
                onItemClick(key);
                break;
            case R.id.main_include_select_5:
                key = 5;
                onItemClick(key);
                break;
            case R.id.main_include_select_6:
                key = 6;
                onItemClick(key);
                break;
            case R.id.main_include_select_7:
                key = 7;
                onItemClick(key);
                break;
            case R.id.main_include_select_8:
                key = 8;
                onItemClick(key);
                break;
            case R.id.main_include_select_9:
                key = 9;
                onItemClick(key);
                break;
            case R.id.main_include_btn_mark:
                isMark = !isMark;
                break;
            case R.id.main_include_btn_clear:
                key = 0;
                onItemClick(key);
                break;
            case R.id.imagebutton_left:
                showExitDialog();
                break;
            case R.id.imagebutton_right:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 选中数字键
     *
     * @param key
     */
    private void onItemClick(int key) {
        game.setSelectedNumber(key);
    }

    /**
     * Sets the fields corresponding to given game.
     *
     * @param game Game to be set.
     */
    public void setGame(Game game) {
        if (gridItemsData == null) {
            gridItemsData = new ArrayList<GridItem>();
        }
        gridItemsData.clear();
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                GridItem item = new GridItem();
                item.num = game.getNumber(x, y);
                if (item.num == 0) {
                    item.isFix = false;
                }
                gridItemsData.add(item);
            }
        }
        gridItemAdapter.notifyDataSetChanged();
    }

    /**
     * 启动计时器
     */
    private void initTime() {
        chronometer.start();
        // 将计时器清零
        chronometer.setBase(SystemClock.elapsedRealtime());
        //开始计时
        chronometer.start();
    }


    /**
     * 更新格子的状态
     *
     * @param key
     */
    private void updateGridState(int key) {
        System.out.println("selection:" + selection);
        if (selection == -1) {
            return;
        }
        if (isMark && key != 0) {
            setMarkNumber(selection, key);
        } else {
            setNumber(selection, key);
        }
        if (isConflictHelpOpen) {
            isItemValidate();
        }
        if (isComplete()) {
            chronometer.stop();
            showCompleteDialog();
        }
        gridItemAdapter.notifyDataSetChanged();


    }

    /**
     * 设置输入数字,输入的同时标志清空
     *
     * @param position
     * @param key
     */
    private void setNumber(int position, int key) {
        if (position == -1) {
            return;
        }
        gridItemsData.get(position).num = key;
        gridItemsData.get(position).marknums = null;
        gridItemsData.get(position).markcount = 0;
        setGameNumber(position, key);
    }

    /**
     * 给游戏设置当前位置的数字
     *
     * @param position
     * @param key
     */
    private void setGameNumber(int position, int key) {
        int x = position / 9;
        int y = position % 9;
        game.setNumber(x, y, key);
    }

    /**
     * 输入标记，输入的同时将已输入的数字也转化成标记数字
     *
     * @param position
     * @param key
     */
    private void setMarkNumber(int position, int key) {
        if (position == -1) {
            return;
        }
        if (gridItemsData.get(position).markcount == 0) {
            gridItemsData.get(position).marknums = new int[9];
        }
        gridItemsData.get(position).marknums[key - 1] = key;
        int num = gridItemsData.get(position).num;
        //将已输入的数字转化成标记数字
        if (num != 0) {
            gridItemsData.get(position).marknums[num - 1] = num;
            gridItemsData.get(position).num = 0;
            setGameNumber(position, 0);
        }
        gridItemsData.get(position).markcount = gridItemsData.get(position).marknums.length;
    }

    /**
     * 清空当前格子的内容
     */
    private void clearCell(int position) {
        setNumber(position, 0);
    }

    /**
     * 弹出完成对话框
     */
    private void showCompleteDialog() {
        dialogUtils = new DialogUtils(context, "选择", context.getResources().getString(R.string.sudoku_dialog_complete_tips), new DialogUtils.OnDialogSelectId() {
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
        dialogUtils.setConfirmText(context.getResources().getString(R.string.sudoku_again));
        dialogUtils.show();
    }

    /**
     * 弹出退出对话框
     */
    private void showExitDialog() {
        dialogUtils = new DialogUtils(context, "退出", context.getResources().getString(R.string.sudoku_dialog_exit), new DialogUtils.OnDialogSelectId() {
            @Override
            public void onClick(int whichButton) {
                switch (whichButton) {
                    case 0:
                        dialogUtils.dismiss();
                        break;
                    case 1:
                        dialogUtils.dismiss();
                        writeSettingValue();
                        finish();
                        break;
                }
            }
        });
        dialogUtils.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selection = position;
        gridItemAdapter.setSelection(selection);
        if (gridItemsData.get(position).isFix) {
            return;
        }
        if (isHighlightTipsOpen) {
            game.checkReference(position / COLOUMNUM, position % COLOUMNUM);
        }
        gridItemAdapter.notifyDataSetChanged();
    }

    /**
     * 检查冲突
     *
     * @return
     */
    public boolean isItemValidate() {
        boolean isUpdate = updateConflictState();
        return !isUpdate;
    }


    /**
     * 更新冲突状态
     */
    public boolean updateConflictState() {
        if (gridItemsData == null) {
            return false;
        }
        int size = gridItemsData.size();
        for (int i = 0; i < size; i++) {
            GridItem item = gridItemsData.get(i);
            int x = i / COLOUMNUM;
            int y = i % COLOUMNUM;
            if (item.num != 0 && !game.checkValid(x, y)) {
                gridItemsData.get(i).isSame = true;
            } else {
                gridItemsData.get(i).isSame = false;
            }
        }
        return true;
    }

    /**
     * 判断是否完成
     *
     * @return
     */
    public boolean isComplete() {
        for (GridItem item : gridItemsData) {
            if (item.num == 0 || item.isSame == true) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method called when model sends update notification.
     *
     * @param observable The model.
     * @param data       The UpdateAction.
     */
    @Override
    public void update(Observable observable, Object data) {
        System.out.println("update");
        switch ((UpdateAction) data) {
            case NEW_GAME:
                dissmissProgressDialog();
                game = (Game) observable;
                isInitSuccess = true;
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setGame(game);
                        initTime();
                    }
                });
                break;
            case CHECK:
//                setGameCheck((Game)o);
                break;
            case SELECTED_NUMBER: {
                Game game = (Game) observable;
                updateGridState(game.getSelectedNumber());
                break;
            }
            case CANDIDATES:
            case HELP:
//                setCandidates((Game)o);
                break;
            case INPUT_NUMBER:
                break;
            case CELL_CLICK: {
                Game game = (Game) observable;
                setHighLight(game.isReference());
            }
                break;
        }
    }

    /**
     * 设置高亮提示
     */
    public void setHighLight(boolean[][] reference) {
        if (reference == null || gridItemsData.isEmpty()) {
            return;
        }
        int size = gridItemsData.size();
        for(int i = 0;i < size;i++){
            gridItemsData.get(i).isSelected = reference[i/COLOUMNUM][i%COLOUMNUM];
        }
    }

    /**
     * 保存设置参数
     */
    private void writeSettingValue() {
        SettingPreferences.setSettingValue(MainActivity.this, SettingPreferences.KEY_SETTING_SWITCH_TIPS, isHighlightTipsOpen);
        SettingPreferences.setSettingValue(MainActivity.this, SettingPreferences.KEY_SETTING_SWITCH_CONFLICT, isConflictHelpOpen);
        SettingPreferences.setSettingValue(MainActivity.this, SettingPreferences.KEY_SETTING_SWITCH_SOUND, isSoundOpen);
    }

    /**
     * 读取设置参数
     */
    private void readSettingValue() {
        isHighlightTipsOpen = SettingPreferences.getSettingValue(MainActivity.this, SettingPreferences.KEY_SETTING_SWITCH_TIPS, true);
        isConflictHelpOpen = SettingPreferences.getSettingValue(MainActivity.this, SettingPreferences.KEY_SETTING_SWITCH_CONFLICT, true);
        isSoundOpen = SettingPreferences.getSettingValue(MainActivity.this, SettingPreferences.KEY_SETTING_SWITCH_SOUND, true);
    }


    /**
     * 自动填充所有数据
     */
    private void setAutoFill() {
        if (gridItemsData == null || gridItemsData.isEmpty() || game == null) {
            return;
        }
        gridItemsData.clear();
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                GridItem item = new GridItem();
                item.num = game.getSolutionNumber(x, y);
                gridItemsData.add(item);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isInitSuccess) {
            return;
        }
        if (isHighlightTipsOpen) {
            game.checkReference(selection / COLOUMNUM, selection % COLOUMNUM);
        }
        if (isConflictHelpOpen) {
            isItemValidate();
        }
        if (isAutoFill) {
            setAutoFill();
            if (isComplete()) {
                chronometer.stop();
                showCompleteDialog();
            }
        }
        if (gridItemAdapter != null) {
            gridItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showExitDialog();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
