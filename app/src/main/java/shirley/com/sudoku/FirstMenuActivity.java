package shirley.com.sudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import shirley.com.sudoku.uiBase.BaseActivity;
import shirley.com.sudoku.uiBase.SettingPreferences;
import shirley.com.sudoku.utils.Constans;
import shirley.com.sudoku.utils.ProgressDialogUtils;
import shirley.com.sudoku.utils.ReadSudokuUtil;
import shirley.com.sudoku.utils.Utils;

public class FirstMenuActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_menu);
        findViewById(R.id.mode_num).setOnClickListener(this);
        findViewById(R.id.mode_color).setOnClickListener(this);
        currentGrade = Utils.stringToArr(SettingPreferences.getSetStringValue(this, SettingPreferences.KEY_CURRENT_CURRENT_GRADE));
        if(currentGrade == null){
            currentGrade = new int[4];
        }
        currentLevel = SettingPreferences.getValue(this, SettingPreferences.KEY_CURRENT_SUDOKU_CURRENTLEVEL,0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mode_num:
                mode = Constans.MODE_NUM;
                break;
            case R.id.mode_color:
                mode = Constans.MODE_COLOR;
                break;

        }
        Intent intent = new Intent(FirstMenuActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                System.exit(0);
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
