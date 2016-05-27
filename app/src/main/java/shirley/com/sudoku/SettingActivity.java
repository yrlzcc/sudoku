package shirley.com.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import sdw.sea.erd.normal.spot.SpotManager;
import shirley.com.sudoku.uiBase.BaseActivity;
import shirley.com.sudoku.utils.AdUtils;
import shirley.com.sudoku.utils.Utils;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private ToggleButton toggleButtonhelp;   //显示冲突开关
    private ToggleButton toggleButtontips;   //高亮提示开关
    private ToggleButton toggleButtonsound;     //声音开关
    private ToggleButton toggleButtoncomplete;  //自动填充所有
    private TextView tv_setting_current_version; //当前版本
    private RelativeLayout rl_auto_fill;  //自动填充layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tv_setting_current_version = (TextView)findViewById(R.id.tv_setting_current_version);
        String version = Utils.getVersion(this);
        tv_setting_current_version.setText(version);

        findViewById(R.id.imagebutton_left).setOnClickListener(this);
        findViewById(R.id.imagebutton_right).setVisibility(View.GONE);

        toggleButtonhelp = (ToggleButton)findViewById(R.id.tb_setting_help_conflict);
        toggleButtonhelp.setChecked(isConflictHelpOpen);
        toggleButtonhelp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isConflictHelpOpen = isChecked;
                toggleButtonhelp.setChecked(isChecked);
            }
        });

        toggleButtontips = (ToggleButton)findViewById(R.id.tb_setting_help_highlight);
        toggleButtontips.setChecked(isHighlightTipsOpen);
        toggleButtontips.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isHighlightTipsOpen = isChecked;
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

        rl_auto_fill = (RelativeLayout)findViewById(R.id.rl_auto_fill);
        toggleButtoncomplete = (ToggleButton)findViewById(R.id.tb_setting_complete);
        if(!isDebug){
            rl_auto_fill.setVisibility(View.GONE);
        }
        toggleButtoncomplete.setChecked(false);
        isAutoFill = false;
        toggleButtoncomplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AdUtils.openAd(SettingActivity.this);
                isAutoFill = isChecked;
                toggleButtonsound.setChecked(isChecked);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imagebutton_left:
                finish();
                break;
            default:break;
        }
    }
}
