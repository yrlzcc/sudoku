package shirley.com.sudoku.utils;

/**
 * Created by Administrator on 2016/4/18.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import shirley.com.sudoku.R;

public class DialogUtils extends Dialog {
    private static int default_width = 310; // 默认宽度
    private static int default_height = 150;// 默认高度

    private static int default_new_height = 100;// 新增默认高度不含标题

    public interface OnDialogSelectId {// 0为点击取消的button，1为确认的button

        void onClick(int whichButton);
    }

    private OnDialogSelectId mOnDialogSelectId;
    private Context context;
    private static String title;
    private String content;// 所需要分享的内容说明文字
    private int isConfirm = 0;// 1 为不显示确认button，2为不显示取消button;默认为0都顯示
    private static String sharePhotoUrl;
    private TextView mTitle;
    private TextView mContent;
    private Button mCancel;
    private Button mConfirm;
    private Button mConfirmOne;

    /**
     * 不需要cancel监听
     *
     * @param context
     * @param title
     * @param content
     * @param alertDo
     */
    public DialogUtils(Context context, String title, String content,
                       final OnDialogSelectId alertDo) {
        this(context, default_width, default_height,
                R.layout.dialog_utils, title, content, null, alertDo, 0);
    }

    /**
     * 不需要cancel监听
     *
     * @param context
     * @param content
     * @param alertDo
     */
    public DialogUtils(Context context, String content,
                       final OnDialogSelectId alertDo,int isConfirm) {
        this(context, default_width, default_new_height,
                R.layout.dialog_utils, title, content, null, alertDo, isConfirm);
    }

    /**
     * 默认布局
     *
     * @param context
     * @param title
     * @param content
     * @param cancelListener
     * @param alertDo
     */
    public DialogUtils(Context context, String title, String content,
                       OnCancelListener cancelListener, final OnDialogSelectId alertDo) {
        this(context, default_width, default_height,
                R.layout.dialog_utils, title, content, cancelListener,
                alertDo, 0);

    }

    /**
     * 设置是否显示确认、取消键,没有oncancel
     *
     * @param context
     * @param title
     * @param content
     * @param alertDo
     * @param isConfirm      1 为不显示确认button，2为不显示取消button;默认为0都顯示
     */
    public DialogUtils(Context context, String title, String content,
                       final OnDialogSelectId alertDo, int isConfirm) {
        this(context, default_width, default_height,
                R.layout.dialog_utils, title, content, null, alertDo,
                isConfirm);
    }

    /**
     * 自定义取消和确定键的值
     *
     * @param context
     * @param title
     * @param content
     * @param cancelListener
     * @param alertDo
     * @param isConfirm      1 为不显示确认button，2为不显示取消button;默认为0都顯示
     */
    public DialogUtils(Context context, String title, String content,
                       final OnDialogSelectId alertDo, int isConfirm, String cancelListener, String sure) {
        this(context, default_width, default_height,
                R.layout.dialog_utils, title, content, null, alertDo,
                isConfirm, cancelListener, sure);
    }

    /**
     * 设置是否显示确认、取消键
     *
     * @param context
     * @param title
     * @param content
     * @param cancelListener
     * @param alertDo
     * @param isConfirm      1 为不显示确认button，2为不显示取消button;默认为0都顯示
     */
    public DialogUtils(Context context, String title, String content,
                       OnCancelListener cancelListener, final OnDialogSelectId alertDo,
                       int isConfirm) {
        this(context, default_width, default_height,
                R.layout.dialog_utils, title, content, cancelListener,
                alertDo, isConfirm);

    }

    /**
     * 自定义布局
     *
     * @param context
     * @param width
     * @param height
     * @param layout
     * @param title
     * @param content
     * @param cancelListener
     * @param alertDo
     */

    public DialogUtils(Context context, int width, int height, int layout,
                       String title, String content, OnCancelListener cancelListener,
                       final OnDialogSelectId alertDo, int isConfirm) {
        super(context, R.style.CustomDialog);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = Utils.dip2px(context, width);
        window.setAttributes(params);

        this.isConfirm = isConfirm;
        this.title = title;
        this.content = content;
        this.context = context;

        setOnCancelListener(cancelListener);
        mOnDialogSelectId = alertDo;

    }

    /**
     * 自定义布局&取消和确认键
     *
     * @param context
     * @param width
     * @param height
     * @param layout
     * @param title
     * @param content
     * @param cancelListener
     * @param alertDo
     */

    public DialogUtils(Context context, int width, int height, int layout,
                       String title, String content, OnCancelListener cancelListener,
                       final OnDialogSelectId alertDo, int isConfirm, String cancel, String sure) {
        super(context, R.style.CustomDialog);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = Utils.dip2px(context, width);
        window.setAttributes(params);

        this.isConfirm = isConfirm;
        this.title = title;
        this.content = content;
        this.context = context;

        Button cancelBt = (Button) findViewById(R.id.dialog_utils_dialog_cancel);
        Button sureBt = (Button) findViewById(R.id.dialog_utils_dialog_confirm);
        cancelBt.setText(cancel);
        sureBt.setText(sure);

        setOnCancelListener(cancelListener);
        mOnDialogSelectId = alertDo;

    }

    public void setCancelText(String text) {
        if (mCancel == null) {
            mCancel = (Button) findViewById(R.id.dialog_utils_dialog_cancel);
        }
        mCancel.setText(text);
    }

    public void setConfirmText(String text) {
        if (mConfirm == null) {
            mConfirm = (Button) findViewById(R.id.dialog_utils_dialog_confirm);
        }
        mConfirm.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = (TextView) findViewById(R.id.dialog_utils_title);
        mTitle.setText(this.title);
        mContent = (TextView) findViewById(R.id.dialog_utils_content);
        mContent.setText(this.content);
        mContent.setGravity(gravity);
        mCancel = (Button) findViewById(R.id.dialog_utils_dialog_cancel);
        mConfirm = (Button) findViewById(R.id.dialog_utils_dialog_confirm);
        mConfirmOne = (Button) findViewById(R.id.dialog_utils_dialog);
        if (this.isConfirm == 0) {
            mCancel.setVisibility(View.VISIBLE);
            mConfirm.setVisibility(View.VISIBLE);
        } else if (this.isConfirm == 1) {
            mCancel.setVisibility(View.GONE);
            mConfirm.setVisibility(View.GONE);
            mConfirmOne.setVisibility(View.VISIBLE);
        } else if (this.isConfirm == 2) {
            mCancel.setVisibility(View.GONE);
            mConfirm.setVisibility(View.GONE);
            mConfirmOne.setVisibility(View.VISIBLE);
        }

        mCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mOnDialogSelectId.onClick(0);
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mOnDialogSelectId.onClick(1);
            }
        });

        mConfirmOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnDialogSelectId.onClick(1);
            }
        });
    }

    /**
     * 设置内容显示的对齐方式
     *
     * @param gravity
     */
    private int gravity = Gravity.CENTER;

    public void setContentGravity(int gravity) {
        this.gravity = gravity;
    }

}
