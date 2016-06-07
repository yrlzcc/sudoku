package shirley.com.sudoku.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import shirley.com.sudoku.R;
import shirley.com.sudoku.model.GridItem;
import shirley.com.sudoku.utils.Constans;
import shirley.com.sudoku.utils.ModeData;


public class GridItemAdapter extends BaseAdapter {

    private List<GridItem> gridItemList;
    private Context context;
    private LayoutInflater mInflater;
    private int selection;
    private boolean isHighlightTipsOpen = true;  //高亮提示开关
    private boolean isConflictHelpOpen = true;    //帮助开关
    private int mode = 1;

    public GridItemAdapter(Context context, List<GridItem> objects, int selection) {
        gridItemList = objects;
        this.context = context;
        this.selection = selection;
    }

    @Override
    public int getCount() {
        return gridItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return gridItemList.get(position);
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
            convertView = mInflater.inflate(R.layout.layout_item_griditem, null);
            holder = new ViewHolder();
            holder.tv_content = (TextView) convertView.findViewById(R.id.item_tv_gridcell);
            holder.item_gv_mark = (GridView) convertView.findViewById(R.id.item_gv_mark);
            holder.sl = (SquareLayout)convertView.findViewById(R.id.sl_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GridItem item = gridItemList.get(position);
        if (item.isMark && item.marknums != null) {
            holder.item_gv_mark.setAdapter(new GridMarkItemAdapter(context, item.marknums,mode));
            holder.item_gv_mark.setEnabled(false);
            holder.item_gv_mark.setClickable(false);
            holder.item_gv_mark.setFocusable(false);
            holder.item_gv_mark.setVisibility(View.VISIBLE);
        } else {
            holder.item_gv_mark.setVisibility(View.GONE);
        }
        if(mode == Constans.MODE_NUM){
            setNumMode(holder, item, position);
        }
        else if(mode == Constans.MODE_COLOR){
            setColorMode(holder, item, position,convertView);
        }

        return convertView;
    }

    private void setNumMode(ViewHolder holder,GridItem item,int position){
        //不为0将数字显示，为0将格子初始化
        if (item.num != 0) {
            holder.tv_content.setText(String.valueOf(item.num));
        } else {
            holder.tv_content.setText(null);
        }
        if (item.isFix) {
            holder.tv_content.setEnabled(false);
            holder.tv_content.setTextColor(context.getResources().getColor(R.color.fix_textcolor));
        } else {
            holder.tv_content.setEnabled(true);
            holder.tv_content.setTextColor(context.getResources().getColor(R.color.fill_textcolor));
        }
        holder.tv_content.setBackgroundResource(R.color.item_back);
        if (isConflictHelpOpen && item.isSame) {
            holder.tv_content.setBackgroundResource(R.color.item_conflict_back);
        } else {
            if (isHighlightTipsOpen && item.isSelected) {
//                holder.tv_content.setBackgroundResource(R.color.item_select_reference_back);
                holder.tv_content.setBackgroundResource(R.drawable.shape_grid_back_normal);
            } else {
                holder.tv_content.setBackgroundResource(R.color.item_back);
//                holder.tv_content.setBackgroundResource(R.drawable.shape_btn_back_normal);
            }
            if (position == selection) {
//                holder.tv_content.setBackgroundResource(R.color.item_select_back);
                holder.tv_content.setBackgroundResource(R.drawable.shape_grid_back_press);
            }

        }
    }

    private void setColorMode(ViewHolder holder,GridItem item,int position,View v){
        holder.tv_content.setSelected(false);
        if(item.num != 0){
            holder.tv_content.setBackgroundResource(ModeData.modeData[item.num - 1][1]);
        }
        if (position == selection) {
//            holder.sl.setBackgroundResource(R.drawable.bg_item_select);
        }
 }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public void setHighLightState(boolean isHightOpen) {
        isHighlightTipsOpen = isHightOpen;
    }

    public void setConflictHelpState(boolean isConflictHelpOpen){
        this.isConflictHelpOpen = isConflictHelpOpen;
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    class ViewHolder {
        public TextView tv_content;
        public GridView item_gv_mark;
        public SquareLayout sl;
    }

}
