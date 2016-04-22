package shirley.com.shudu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import shirley.com.shudu.utils.Constans;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViewById(R.id.menu_level1).setOnClickListener(this);
        findViewById(R.id.menu_level2).setOnClickListener(this);
        findViewById(R.id.menu_level3).setOnClickListener(this);
        findViewById(R.id.menu_level4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int level = 1;
        switch (v.getId()){
            case R.id.menu_level1:
                level = Constans.LEVEL1;
                break;
            case R.id.menu_level2:
                level = Constans.LEVEL2;
                break;
            case R.id.menu_level3:
                level = Constans.LEVEL3;
                break;
            case R.id.menu_level4:
                level = Constans.LEVEL4;
                break;
            default:break;
        }
        Intent intent = new Intent(MenuActivity.this,MainActivity.class);
        intent.putExtra("level",level);
        startActivity(intent);
    }
}
