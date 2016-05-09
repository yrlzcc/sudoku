package shirley.com.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import shirley.com.sudoku.uiBase.BaseActivity;

public class SettingActivity extends BaseActivity {
    private ToggleButton toggleButtonhelp;   //显示冲突开关
    private ToggleButton toggleButtontips;   //高亮提示开关
    private ToggleButton toggleButtonsound;     //声音开关

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toggleButtonhelp = (ToggleButton)findViewById(R.id.tb_setting_help_conflict);
        toggleButtonhelp.setChecked(isHelpOpen);
        toggleButtonhelp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isHelpOpen = isChecked;
                toggleButtonhelp.setChecked(isChecked);
            }
        });

        toggleButtontips = (ToggleButton)findViewById(R.id.tb_setting_help_highlight);
        toggleButtontips.setChecked(isTipsOpen);
        toggleButtontips.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isTipsOpen = isChecked;
                toggleButtontips.setChecked(isChecked);
            }
        });

        toggleButtonsound = (ToggleButton)findViewById(R.id.tb_setting_sound);
        toggleButtonsound.setChecked(isSoundOpen);
        toggleButtonsound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSoundOpen = isChecked;
                toggleButtonsound.setChecked(isChecked);
            }
        });
    }
}
