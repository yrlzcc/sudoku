package shirley.com.sudoku.view;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import shirley.com.sudoku.R;
import shirley.com.sudoku.model.GridItem;
import shirley.com.sudoku.utils.Constans;
import shirley.com.sudoku.utils.ModeData;


public class GridMarkItemAdapter extends BaseAdapter {

    private int[] gridItemList;
    private Context context;
    private LayoutInflater mInflater;
    private int mode = 1;

    public GridMarkItemAdapter(Context context, int[] objects,int mode) {
        gridItemList = objects;
        this.context = context;
        this.mode = mode;
    }

    @Override
    public int getCount() {
        if(gridItemList == null){
            return 0;
        }
        return gridItemList.length;
    }

    @Override
    public Object getItem(int position) {
        if(gridItemList == null){
            return null;
        }
        return gridItemList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(context);
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_item_markitem, null);
            holder = new ViewHolder();
            holder.tv_content = (TextView) convertView.findViewById(R.id.item_tv_gridcell);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int item = gridItemList[position];
        //不为0将数字显示，为0将格子初始化
        if (mode == Constans.MODE_NUM){
            if (item != 0) {
                holder.tv_content.setText(String.valueOf(item));
            } else {
                holder.tv_content.setText(null);
            }
    }
    else if(mode == Constans.MODE_COLOR){
            if (item != 0) {
                holder.tv_content.setBackgroundResource(ModeData.modeData[item-1][1]);
            } else {
                holder.tv_content.setText(null);
            }
        }
        return convertView;
    }

    class ViewHolder{
        public TextView tv_content;
    }

}
