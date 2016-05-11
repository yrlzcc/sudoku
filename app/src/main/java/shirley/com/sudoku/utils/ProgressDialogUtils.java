package shirley.com.sudoku.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import shirley.com.sudoku.R;

/**
 * Created by Administrator on 2016/5/10.
 */
public class ProgressDialogUtils extends Dialog {

    private static int default_width = 120; // 默认宽度
    private static int default_height = 88;// 默认高度

    private Context context;
    private String content;

    private TextView mContent;
    private ProgressBar group_add_progressbar;

    public ProgressDialogUtils(Context context, String content,
                               OnCancelListener cancelListener) {
        super(context, R.style.CustomDialog);
        setContentView(R.layout.dialog_progressdialog_utils);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = Utils.dip2px(context, default_width);
        window.setAttributes(params);
        if (cancelListener != null)
            setOnCancelListener(cancelListener);

        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = (TextView) findViewById(R.id.contenttv);
        mContent.setText(content);
        group_add_progressbar = (ProgressBar) findViewById(R.id.group_add_progressbar);

    }

    public void setContent(String content) {
        mContent.setText(content);
    }
}

