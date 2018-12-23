package com.norton.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.norton.demo.R;
import com.swl.tvlibrary.custom.adapter.BaseAdapterImpl;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter implements BaseAdapterImpl {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> categoryColumn;

    public GridViewAdapter(Context context, List<String> pBean) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        if (pBean != null) {
            this.categoryColumn = pBean;
        } else {
            this.categoryColumn = new ArrayList<>();
        }
    }

    @Override
    public View getView(int index, View convertView, ViewGroup group) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gridview_item, null);
            holder = new ViewHolder();
            holder.Channel_name = (TextView) convertView.findViewById(R.id.tc_catagory_name);
            holder.widget33 = (LinearLayout) convertView.findViewById(R.id.widget33);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.Channel_name.setText(categoryColumn.get(index));
        return convertView;
    }

    @Override
    public int getCount() {
        return categoryColumn.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryColumn.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void setSelectPosition(int position) {

    }

    @Override
    public void setSecondPosition(int position) {

    }

    private class ViewHolder {
        private TextView Channel_name;
        private LinearLayout widget33;
    }

}
