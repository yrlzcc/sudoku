package shirley.com.sudoku.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import shirley.com.sudoku.R;

/**
 * Created by Administrator on 2016/4/18.
 */
public class BordGridView extends GridView {
    private float childPaintWidth = 1.0f;
    private float paintWidth = 8.0f;
    private int paintColor;
    private Paint paint;
    private int childPaintColor;
    private Paint childPaint;
    private int selectPaintColor;
    private Paint selectPaint;
    private float selectPaintWidth = 5.0f;
    private int selection = -1;
    private int[] referencePosition;

    public BordGridView(Context context) {
        super(context);
        initPaint();
        // TODO Auto-generated constructor stub
    }

    public BordGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BordGridView);//TypedArray是一个数组容器
        paintWidth = a.getFloat(R.styleable.BordGridView_mainBorderWidth, 8.0f);
        childPaintWidth = a.getFloat(R.styleable.BordGridView_subBorderWidth, 1.0f);
        selectPaintWidth = a.getFloat(R.styleable.BordGridView_selectBorderWidth, 5.0f);
        paintColor = a.getColor(R.styleable.BordGridView_mainBorderColor, getResources().getColor(R.color.item_main_border_color));
        childPaintColor = a.getColor(R.styleable.BordGridView_subBorderColor, getResources().getColor(R.color.item_sub_border_color));
        selectPaintColor = a.getColor(R.styleable.BordGridView_selectBorderColor, getResources().getColor(R.color.item_select_border_color));
        a.recycle();
        initPaint();
    }

    public BordGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(paintWidth);
        paint.setColor(paintColor);//设置画笔的颜色
        childPaint = new Paint();
        childPaint.setStyle(Paint.Style.STROKE);
        childPaint.setStrokeWidth(childPaintWidth);
        childPaint.setColor(childPaintColor);//设置画笔的颜色
        selectPaint = new Paint();
        selectPaint.setStyle(Paint.Style.STROKE);
        selectPaint.setStrokeWidth(selectPaintWidth);
        selectPaint.setColor(selectPaintColor);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int column = 9;//计算出一共有多少列，假设有3列
        int childCount = getChildCount();//子view的总数
//            System.out.println("子view的总数childCount==" + childCount);
        View selectview = null;

        for (int i = 0; i < childCount; i++) {//遍历子view
            View cellView = getChildAt(i);//获取子view
            cellView.setSelected(true);
//            if (i == selection) {
//                //绘制边框
//                canvas.drawRect(cellView.getLeft(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), selectPaint);
//                continue;
//            }
            int row = i / column;
            int col = i % column;
            canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), childPaint);
            canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), childPaint);
            if ((col + 1) % 3 == 0) {
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), paint);

            }
            if ((row + 1) % 3 == 0) {
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), paint);
            }
            if (row == 0) {
                canvas.drawLine(cellView.getLeft(), cellView.getTop(), cellView.getRight(), cellView.getTop(), paint);
            }
            if (col == 0) {
                canvas.drawLine(cellView.getLeft(), cellView.getTop(), cellView.getLeft(), cellView.getBottom(), paint);
            }
        }
//        if(selectview != null){
//            canvas.drawRect(selectview.getLeft(), selectview.getTop(), selectview.getRight(), selectview.getBottom(), selectPaint);
//        }

    }

    public void setSelection(int selection) {
        this.selection = selection;
    }
}
