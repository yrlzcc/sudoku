package shirley.com.sudoku;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import shirley.com.sudoku.model.BaseItem;
import shirley.com.sudoku.model.GradeItem;
import shirley.com.sudoku.uiBase.BaseActivity;
import shirley.com.sudoku.uiBase.BaseInputStack;
import shirley.com.sudoku.uiBase.SettingPreferences;
import shirley.com.sudoku.utils.Constans;
import shirley.com.sudoku.utils.DialogUtils;
import shirley.com.sudoku.utils.Game;
import shirley.com.sudoku.utils.ModeData;
import shirley.com.sudoku.utils.ProgressDialogUtils;
import shirley.com.sudoku.utils.UpdateAction;
import shirley.com.sudoku.model.GridItem;
import shirley.com.sudoku.utils.Utils;
import shirley.com.sudoku.view.GridItemAdapter;

public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, Observer, OnMenuItemClickListener, OnMenuItemLongClickListener {

    private final int COLOUMNUM = 9;
    private Context context;
    private GridView gridView;
    private GridItemAdapter gridItemAdapter;
    private ProgressDialogUtils progressDialog = null;
    private List<GridItem> gridItemsData = null;
    List<GradeItem> gameList = null; //当前级别对应的所有游戏
    private int selection = -1;
    private DialogUtils dialogUtils = null;
    private Chronometer chronometer = null; //计时器
    private Game game;
    private int level;
    private boolean isInitSuccess = true; //是否加载成功
    private boolean isMark = false; //是否是标记状态
    private View main_include_btn_pre; //向前按钮
    private View main_include_btn_next; //向后按钮
    private View main_include_btn_clear; //清除按钮
    private View main_include_btn_mark; //标记按钮
    private TextView tv_title_info; //显示信息的textview
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readSettingValue();
        gridView = (GridView) findViewById(R.id.main_gv_show);
        setVisualByMode();
        main_include_btn_clear = findViewById(R.id.main_include_btn_clear);
        main_include_btn_clear.setOnClickListener(this);
        main_include_btn_mark = findViewById(R.id.main_include_btn_mark);
        main_include_btn_mark.setOnClickListener(this);
        main_include_btn_pre = findViewById(R.id.main_include_btn_pre);
        main_include_btn_pre.setOnClickListener(this);
        main_include_btn_next = findViewById(R.id.main_include_btn_next);
        main_include_btn_next.setOnClickListener(this);
        findViewById(R.id.imagebutton_right).setOnClickListener(this);
        findViewById(R.id.imagebutton_left).setOnClickListener(this);
        tv_title_info = (TextView)findViewById(R.id.tv_title_info);
        chronometer = (Chronometer) findViewById(R.id.main_ch_time);
        gridView.setOnItemClickListener(this);
        context = this;
        level = this.getIntent().getIntExtra("level",0);
//        showProgressDialog("正在生成数独...");
        System.out.println("--------------------level------------------" + level);
        gameList = app.sudokuData.sudokuList.get(level).getGameList();
        if (currentGrade[level] == 0) {
            currentGrade[level]++;  //没开始是0，第一次进来就变为1
            initGame(level);
        }/* else if(currentLevel != level){
            showChoiceDialog();
        }*/else{
            resumeGame();
        }
        currentLevel = level;
        fragmentManager = getSupportFragmentManager();
        initMenuFragment();
    }


    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize(120);
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);
        close.setBgResource(R.drawable.shape_drop_item_back);

        MenuObject again = new MenuObject("重来");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_perm_group_sync_settings);
        again.setBitmap(b);
        again.setBgResource(R.drawable.shape_drop_item_back);

        MenuObject settings = new MenuObject("设置");
        settings.setResource(R.mipmap.ic_menu_manage);
        settings.setBgResource(R.drawable.shape_drop_item_back);

        MenuObject help = new MenuObject("帮助");
        help.setResource(R.mipmap.ic_menu_help);
        help.setBgResource(R.drawable.shape_drop_item_back);

        MenuObject solution = new MenuObject("解题");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_menu_view));
        solution.setDrawable(bd);
        solution.setBgResource(R.drawable.shape_drop_item_back);

        menuObjects.add(close);
        menuObjects.add(again);
        menuObjects.add(settings);
        menuObjects.add(help);
        if(isDebug) {
            menuObjects.add(solution);
        }
        return menuObjects;
    }
    /**
     * 更新关数
     */
    private void updateGrade() {

    }

    /**
     * 根据模式设置选择面板
     */
    private void setVisualByMode() {
        if (mode == Constans.MODE_NUM) {
            findViewById(R.id.ll_num_select).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_color_select).setVisibility(View.GONE);
            findViewById(R.id.main_include_select_1).setOnClickListener(this);
            findViewById(R.id.main_include_select_2).setOnClickListener(this);
            findViewById(R.id.main_include_select_3).setOnClickListener(this);
            findViewById(R.id.main_include_select_4).setOnClickListener(this);
            findViewById(R.id.main_include_select_5).setOnClickListener(this);
            findViewById(R.id.main_include_select_6).setOnClickListener(this);
            findViewById(R.id.main_include_select_7).setOnClickListener(this);
            findViewById(R.id.main_include_select_8).setOnClickListener(this);
            findViewById(R.id.main_include_select_9).setOnClickListener(this);
        } else {
            findViewById(R.id.ll_num_select).setVisibility(View.GONE);
            findViewById(R.id.ll_color_select).setVisibility(View.VISIBLE);
            findViewById(R.id.main_include_color_select_1).setOnClickListener(this);
            findViewById(R.id.main_include_color_select_2).setOnClickListener(this);
            findViewById(R.id.main_include_color_select_3).setOnClickListener(this);
            findViewById(R.id.main_include_color_select_4).setOnClickListener(this);
            findViewById(R.id.main_include_color_select_5).setOnClickListener(this);
            findViewById(R.id.main_include_color_select_6).setOnClickListener(this);
            findViewById(R.id.main_include_color_select_7).setOnClickListener(this);
            findViewById(R.id.main_include_color_select_8).setOnClickListener(this);
            findViewById(R.id.main_include_color_select_9).setOnClickListener(this);
        }
    }

    /**
     * 初始化按钮状态
     */
    private void initBtnState() {
        if (main_include_btn_clear != null) {
            main_include_btn_clear.setEnabled(false);
        }
        if (main_include_btn_pre != null) {
            main_include_btn_pre.setEnabled(false);
        }
        if (main_include_btn_next != null) {
            main_include_btn_next.setEnabled(false);
        }
    }

    /**
     * 初始化游戏
     *
     * @param level
     */
    private void initGame(final int level) {
        initData();
//        initBtnState();
        isMark = false;
        selection = -1;
        BaseInputStack.getInstance(context).reset();
        updateBtnState();
        updateClearState();
        updateMarkState();
        gridItemAdapter = new GridItemAdapter(context, gridItemsData, selection);
        gridItemAdapter.setMode(mode);
        gridView.setAdapter(gridItemAdapter);
        game = new Game(context);
        game.addObserver(MainActivity.this);
        game.setGame(gameList.get(currentGrade[level]).getGame(), gameList.get(currentGrade[level]).getSolution());
        tv_title_info.setText(String.format(getResources().getString(ModeData.levelString[level][1]),currentGrade[level]));
    }

    private void resumeGame() {
        restore();
        updateBtnState();
        updateClearState();
        updateMarkState();
        game = new Game(context);
        game.addObserver(MainActivity.this);
        game.resumeGame(gameList.get(currentGrade[currentLevel]).getSolution());
        gridToGame();
        gridItemAdapter = new GridItemAdapter(context, gridItemsData, selection);
        gridItemAdapter.setMode(mode);
        gridView.setAdapter(gridItemAdapter);
        tv_title_info.setText(String.format(getResources().getString(ModeData.levelString[level][1]), currentGrade[level]));
    }

    private void gridToGame() {
        if (gridItemsData != null) {
            for (int i = 0; i < gridItemsData.size(); i++) {
                int x = i / 9;
                int y = i % 9;
                game.setNumber(x, y, gridItemsData.get(i).num);
                game.setReference(x,y,gridItemsData.get(i).isSelected);
                game.setConflict(x,y,gridItemsData.get(i).isSame);
            }
        }
    }

    /**
     * 初始化九宫格
     */
    private void initData() {
        if (gridItemsData == null) {
            gridItemsData = new ArrayList<GridItem>();
        }
        gridItemsData.clear();
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
            case R.id.main_include_color_select_1:
                key = 1;
                onNumClick(key);
                break;
            case R.id.main_include_select_2:
            case R.id.main_include_color_select_2:
                key = 2;
                onNumClick(key);
                break;
            case R.id.main_include_select_3:
            case R.id.main_include_color_select_3:
                key = 3;
                onNumClick(key);
                break;
            case R.id.main_include_select_4:
            case R.id.main_include_color_select_4:
                key = 4;
                onNumClick(key);
                break;
            case R.id.main_include_select_5:
            case R.id.main_include_color_select_5:
                key = 5;
                onNumClick(key);
                break;
            case R.id.main_include_select_6:
            case R.id.main_include_color_select_6:
                key = 6;
                onNumClick(key);
                break;
            case R.id.main_include_select_7:
            case R.id.main_include_color_select_7:
                key = 7;
                onNumClick(key);
                break;
            case R.id.main_include_select_8:
            case R.id.main_include_color_select_8:
                key = 8;
                onNumClick(key);
                break;
            case R.id.main_include_select_9:
            case R.id.main_include_color_select_9:
                key = 9;
                onNumClick(key);
                break;
            case R.id.main_include_btn_mark:
                isMark = !isMark;
                if (isMark) {
                    main_include_btn_mark.setSelected(true);
                } else {
                    main_include_btn_mark.setSelected(false);
                }
                break;
            case R.id.main_include_btn_clear:
                key = 0;
                onNumClick(key);
                break;
            case R.id.main_include_btn_pre: {
//                AdUtils.openAd(this);
                BaseItem item = BaseInputStack.getInstance(context).getPre();
                updateBtnState();
                pre(item);
                if (isConflictHelpOpen) {
                    checkConflict();
                }
                updateClearState();
                gridItemAdapter.setSelection(selection);
                gridView.setSelection(selection);
                gridItemAdapter.notifyDataSetChanged();
                break;
            }
            case R.id.main_include_btn_next:
                BaseItem item = BaseInputStack.getInstance(context).getNext();
                updateBtnState();
                next(item);
                if (isConflictHelpOpen) {
                    checkConflict();
                }
                if (isComplete()) {
                    chronometer.stop();
                    showCompleteDialog();
                }
                updateClearState();
                gridItemAdapter.setSelection(selection);
                gridView.setSelection(selection);
                gridItemAdapter.notifyDataSetChanged();
                break;
            case R.id.imagebutton_left:
                showExitDialog();
                break;
            case R.id.imagebutton_right:
//                Intent intent = new Intent(this, SettingActivity.class);
//                startActivity(intent);
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 更新向前向后撤销按钮的状态
     */
    private void updateBtnState() {
        if (BaseInputStack.getInstance(context).isPreEnable()) {
            main_include_btn_pre.setEnabled(true);
        } else {
            main_include_btn_pre.setEnabled(false);
        }
        if (BaseInputStack.getInstance(context).isNextEnable()) {
            main_include_btn_next.setEnabled(true);
        } else {
            main_include_btn_next.setEnabled(false);
        }
    }

    /**
     * 更新清除按钮
     */
    private void updateClearState() {
        if (selection == -1 || gridItemsData == null || gridItemsData.isEmpty()) {
            main_include_btn_clear.setEnabled(false);
            return;
        }
        GridItem item = gridItemsData.get(selection);
        boolean isMarkEmpty = (item.isMark && (item.marknums == null || item.marknums.length == 0));
        boolean isNumEmpty = (!item.isMark && item.num == 0);
        if (item.isFix || isMarkEmpty || isNumEmpty) {
            main_include_btn_clear.setEnabled(false);
        } else {
            main_include_btn_clear.setEnabled(true);
        }
    }

    /**
     * 更新标记按钮
     */
    private void updateMarkState() {
        if(isMark){
            main_include_btn_mark.setSelected(true);
        }
        else{
            main_include_btn_mark.setSelected(false);
        }
    }

    /**
     * 选中数字键
     *
     * @param key
     */
    private void onNumClick(int key) {
        game.setSelectedNumber(key);
    }

    /**
     * Sets the fields corresponding to given game.
     *
     * @param game Game to be set.
     */
    public void gameToGrid(Game game) {
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
        if(true) {
            // 将计时器清零
            chronometer.setBase(SystemClock.elapsedRealtime());
            //开始计时
            chronometer.start();
        }
    }

    /**
     * 记录当前操作
     *
     * @param key
     */
    private void push(int key) {
        if (selection == -1) {
            return;
        }
        BaseItem item = new BaseItem();
        //记录输入数字
        item.num = key;
        item.position = selection;
        item.isMark = isMark;
        //记录输入前的状态
        if (gridItemsData != null && !gridItemsData.isEmpty()) {
            GridItem gridItem = gridItemsData.get(selection);
            if (!gridItem.isMark) {
                item.prenum = gridItem.num;
            } else {
                item.prenum = gridItem.currentMarkNum;
                item.ispreMark = gridItem.isMark;
                item.preMarkNum = copy(gridItem.marknums);
            }
        }
        //多次在同一格子输入相同数字，只记录一次的
        if (isInputSame(item)) {
            return;
        }
        BaseInputStack.getInstance(context).popToCurrentPosition(); //每次push之前把撤销之后的无效部分清掉
        BaseInputStack.getInstance(context).pushCurrentInput(item);
    }

    /**
     * 是否输入同一个数字
     *
     * @param item
     * @return
     */
    private boolean isInputSame(BaseItem item) {
        if (item == null) {
            return false;
        }
        if (item.isMark == item.ispreMark && item.num == item.prenum) {
            return true;
        }
        return false;
    }

    /**
     * 向前
     */
    private void pre(BaseItem baseItem) {
        if (gridItemsData == null || gridItemsData.isEmpty() || baseItem == null) {
            return;
        }
        GridItem item = gridItemsData.get(baseItem.position);
        if (baseItem.ispreMark) {
            setMarkNumber(baseItem.position, baseItem.preMarkNum);
        } else {
            setNumber(baseItem.position, baseItem.prenum);
        }
        item.isMark = baseItem.ispreMark;
        selection = baseItem.position;
    }

    /**
     * 向后
     *
     * @param baseItem
     */
    private void next(BaseItem baseItem) {
        if (gridItemsData == null || gridItemsData.isEmpty() || baseItem == null) {
            return;
        }
        GridItem item = gridItemsData.get(baseItem.position);
        if (baseItem.isMark && baseItem.num != 0) {
            addMarkNumber(baseItem.position, baseItem.num);
        } else {
            setNumber(baseItem.position, baseItem.num);
        }
        item.isMark = baseItem.isMark;
        selection = baseItem.position;
    }

    /**
     * 将选中数字加到格子里
     *
     * @param key
     */
    private void addNumToGrid(int key) {
        System.out.println("selection:" + selection);
        if (selection == -1) {
            return;
        }
        if (isMark && key != 0) {
            addMarkNumber(selection, key);
        } else {
            setNumber(selection, key);
        }
        if (isConflictHelpOpen) {
            checkConflict();
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
        gridItemsData.get(position).currentMarkNum = 0;
        gridItemsData.get(position).isMark = false;
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
        if (game != null) {
            game.setNumber(x, y, key);
        }
    }

    /**
     * @param position
     * @param marknums
     */
    private void setMarkNumber(int position, int[] marknums) {
        if (position == -1 || gridItemsData == null) {
            return;
        }
        GridItem item = gridItemsData.get(position);
        int num = item.num;
        item.marknums = marknums;
        if (num != 0) {
            gridItemsData.get(position).num = 0;
            setGameNumber(position, 0);
        }
    }

    /**
     * 输入标记，输入的同时清空输入数字
     *
     * @param position
     * @param key
     */
    private void addMarkNumber(int position, int key) {
        if (position == -1 || gridItemsData == null) {
            return;
        }
        GridItem item = gridItemsData.get(position);
        if (item.marknums == null) {
            item.marknums = new int[9];
        }
        item.isMark = isMark;
        item.currentMarkNum = key;
        gridItemsData.get(position).marknums[key - 1] = key;
        gridItemsData.get(position).currentMarkNum = key;
        int num = item.num;
        if (num != 0) {
            gridItemsData.get(position).num = 0;
            setGameNumber(position, 0);
        }
    }

    /**
     * 清空当前格子的内容
     */
    private void clearCell(int position) {
        setNumber(position, 0);
    }

    /**
     * 弹出是否开始新游戏对话框
     */
    private void showChoiceDialog() {
        dialogUtils = new DialogUtils(context, "选择", String.format(context.getResources().getString(R.string.sudoku_dialog_choic_tips)), new DialogUtils.OnDialogSelectId() {
            @Override
            public void onClick(int whichButton) {
                switch (whichButton) {
                    case 0:
                        dialogUtils.dismiss();
                        resumeGame();
                        break;
                    case 1:
                        currentGrade[currentLevel]++;
                        initGame(currentLevel);
                        dialogUtils.dismiss();
                        break;
                }
            }
        });
        dialogUtils.setConfirmText(context.getResources().getString(R.string.sudoku_again));
        dialogUtils.show();
    }

    /**
     * 弹出完成对话框
     */
    private void showCompleteDialog() {
//        AdUtils.openAd(this);
        dialogUtils = new DialogUtils(context, "选择", String.format(context.getResources().getString(R.string.sudoku_dialog_complete_tips), chronometer.getText().toString()), new DialogUtils.OnDialogSelectId() {
            @Override
            public void onClick(int whichButton) {
                switch (whichButton) {
                    case 0:
                        dialogUtils.dismiss();
                        break;
                    case 1:
                        currentGrade[currentLevel]++;
                        initGame(currentLevel);
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
                        SettingPreferences.setSettingValue(MainActivity.this, SettingPreferences.KEY_FIRST_LUNCH, Utils.getVersion(MainActivity.this));
                        save();
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
        gridView.setSelection(selection);
        if (gridItemsData.get(position).isFix) {
            return;
        }
        int x = position / COLOUMNUM;
        int y = position % COLOUMNUM;
        game.setSelectedPosition(x, y); //
    }

    /**
     * 检查冲突
     *
     * @return
     */
    public void checkConflict() {
        if (game == null) {
            return;
        }
        game.checkConflict();
        setConflictState(game.isConflict());
    }

    private void clearConflict() {
        if (game == null) {
            return;
        }
        game.clearConflict();
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
        switch ((UpdateAction) data) {
            case NEW_GAME:
                dissmissProgressDialog();
                game = (Game) observable;
                isInitSuccess = true;
                if (!gridItemsData.isEmpty()) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gameToGrid(game);
                            initTime();
                        }
                    });
                }
                break;
            case RESUME_GAME:
                initTime();
                break;
            case CHECK:
//                setGameCheck((Game)o);
                break;
            case SELECTED_NUMBER: {
                Game game = (Game) observable;
                push(game.getSelectedNumber());
                updateClearState();  //更新清除按钮
                updateBtnState();   //更新撤销按钮
                addNumToGrid(game.getSelectedNumber());
                break;
            }
            case CANDIDATES:
            case HELP:
//                setCandidates((Game)o);
                break;
            case INPUT_NUMBER:
                break;
            case SELECTED_POSITION: {
                Game game = (Game) observable;
                if (isHighlightTipsOpen) {
                    game.checkReference(selection / COLOUMNUM, selection % COLOUMNUM);
                    setHighLight(game.isReference());
                }
                if (isConflictHelpOpen) {
                    game.checkConflict();
                    setConflictState(game.isConflict());
                }
                updateClearState();
                gridItemAdapter.notifyDataSetChanged();
            }
            break;
        }
    }

    /**
     * 设置高亮提示
     */
    public void setHighLight(boolean[][] reference) {
        if (reference == null || gridItemsData.isEmpty() || selection == -1) {
            return;
        }
        int size = gridItemsData.size();
        for (int i = 0; i < size; i++) {
            gridItemsData.get(i).isSelected = reference[i / COLOUMNUM][i % COLOUMNUM];
        }
    }

    /**
     * 设置冲突状态
     *
     * @param conflict
     */
    public void setConflictState(boolean[][] conflict) {
        if (conflict == null || gridItemsData.isEmpty() || selection == -1) {
            return;
        }
        int size = gridItemsData.size();
        for (int i = 0; i < size; i++) {
            gridItemsData.get(i).isSame = conflict[i / COLOUMNUM][i % COLOUMNUM];
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

    private int[] copy(int[] item) {
        int[] copy = new int[9];

        for (int x = 0; x < 9; x++)
            for (int y = 0; y < 9; y++) {
                copy[y] = item[y];
            }
        return copy;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isInitSuccess) {
            return;
        }
        if (isHighlightTipsOpen && selection != -1) {
            game.checkReference(selection / COLOUMNUM, selection % COLOUMNUM);
        }
        if (isConflictHelpOpen) {
            game.checkConflict();
        } else {
            game.clearConflict();
        }
        setConflictState(game.isConflict());
//        if (isAutoFill) {
//            setAutoFill();
//            if (isComplete()) {
//                chronometer.stop();
//                showCompleteDialog();
//            }
//            isAutoFill = false;
//        }
        if (gridItemAdapter != null) {
            gridItemAdapter.setConflictHelpState(isConflictHelpOpen);
            gridItemAdapter.setHighLightState(isHighlightTipsOpen);
            gridItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
//                showExitDialog();
                writeSettingValue();
                SettingPreferences.setSettingValue(MainActivity.this, SettingPreferences.KEY_FIRST_LUNCH, Utils.getVersion(MainActivity.this));
                save();
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 存储当前数独数据，包括数独，操作栈，当前选中位置，标记状态,时间
     */
    private void save() {
        if (gridItemsData != null) {
            Gson gs = new Gson();
            String str = gs.toJson(gridItemsData);
            SettingPreferences.setSettingValue(MainActivity.this, SettingPreferences.KEY_CURRENT_SUDOKU_DATA + level, str);
            BaseInputStack.getInstance(context).save();
            SettingPreferences.setSettingValue(MainActivity.this, SettingPreferences.KEY_CURRENT_SUDOKU_ISMARK, isMark);
            SettingPreferences.setSettingValue(MainActivity.this, SettingPreferences.KEY_CURRENT_SUDOKU_SELECTION, selection);
            SettingPreferences.setSettingValue(MainActivity.this, SettingPreferences.KEY_CURRENT_SUDOKU_CURRENTLEVEL, currentLevel);
            SettingPreferences.setSettingValue(MainActivity.this, SettingPreferences.KEY_CURRENT_SUDOKU_TIME, chronometer.getText().toString());
            SettingPreferences.setSettingValue(MainActivity.this, SettingPreferences.KEY_CURRENT_CURRENT_GRADE, Utils.arrToString(currentGrade));
        }
    }

    /**
     * 恢复当前数独
     */
    private void restore() {
        String str = SettingPreferences.getSetStringValue(MainActivity.this, SettingPreferences.KEY_CURRENT_SUDOKU_DATA + level);
        Gson gs = new Gson();
        gridItemsData = gs.fromJson(str, new TypeToken<List<GridItem>>() {
        }.getType());
        BaseInputStack.getInstance(context).restore();
        isMark = SettingPreferences.getSettingValue(MainActivity.this, SettingPreferences.KEY_CURRENT_SUDOKU_ISMARK, false);
        selection = SettingPreferences.getValue(MainActivity.this, SettingPreferences.KEY_CURRENT_SUDOKU_SELECTION, -1);
    }


    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch(position){
            case 0:
                break;
            case 1:
                initGame(currentLevel);
                break;
            case 2:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case 3:
                break;
            case 4:
                setAutoFill();
                gridItemAdapter.notifyDataSetChanged();
                if (isComplete()) {
                    chronometer.stop();
                    showCompleteDialog();
                }
                break;
        }
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {

    }
}
