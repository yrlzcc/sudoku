package shirley.com.shudu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import shirley.com.shudu.R;

/**
 * Created by Administrator on 2016/4/18.
 */
    public class BordGridView extends GridView {
        private float childPaintWidth = 1.0f;
        private float paintWidth = 8.0f;
        private int paintColor;
        private int childPaintColor;
        private Paint paint;
        private Paint childPaint;
        public BordGridView(Context context) {
            super(context);
            initPaint();
            // TODO Auto-generated constructor stub
        }

        public BordGridView(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BordGridView);//TypedArray是一个数组容器
            paintWidth = a.getFloat(R.styleable.BordGridView_mainBorderWidth,8.0f);
            childPaintWidth = a.getFloat(R.styleable.BordGridView_subBorderWidth,1.0f);
            paintColor = a.getColor(R.styleable.BordGridView_mainBorderColor, 0);
            childPaintColor = a.getColor(R.styleable.BordGridView_subBorderColor,0);
            a.recycle();
            initPaint();
        }

        public BordGridView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            initPaint();
        }

        private void initPaint(){
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(paintWidth);
            paint.setColor(Color.parseColor("#cc000000"));//设置画笔的颜色
            childPaint = new Paint();
            childPaint.setStyle(Paint.Style.STROKE);
            childPaint.setStrokeWidth(childPaintWidth);
            childPaint.setColor(Color.parseColor("#33000000"));//设置画笔的颜色
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            int column = 9;//计算出一共有多少列，假设有3列
            int childCount = getChildCount();//子view的总数
//            System.out.println("子view的总数childCount==" + childCount);

            for (int i = 0; i < childCount; i++) {//遍历子view
                int row = i/column;
                int col = i%column;
                View cellView = getChildAt(i);//获取子view
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), childPaint);
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), childPaint);
                if((col+1)%3==0) {
                    canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), paint);

                }
                if((row+1)%3 == 0) {
                    canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), paint);
                }
                if(row == 0){
                    canvas.drawLine(cellView.getLeft(), cellView.getTop(), cellView.getRight(), cellView.getTop(), paint);
                }
                if(col == 0){
                    canvas.drawLine(cellView.getLeft(), cellView.getTop(), cellView.getLeft(), cellView.getBottom(), paint);
                }
            }
        }
    }
