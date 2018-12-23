package com.norton.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.norton.demo.R;
import com.norton.demo.adapter.GridViewAdapter;
import com.norton.demo.adapter.ListAdapter;
import com.swl.tvlibrary.custom.MemoryListView;
import com.swl.tvlibrary.custom.PageScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/12/20.
 */

public class GridViewTestActivity extends Activity {

    private Context context = this;
    private MemoryListView listView1;
    private PageScrollGridView gridView1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview_test);

        listView1 = (MemoryListView) findViewById(R.id.listview_1);
        gridView1 = (PageScrollGridView) findViewById(R.id.gridView_1);

        List<String> stringList = new ArrayList<>();
        for (int i =0 ; i < 200; i++) {
            stringList.add("测试item:" + i);
        }

        ListAdapter adapter1 = new ListAdapter(this,stringList);
        listView1.setAdapter(adapter1);

        GridViewAdapter adapter = new GridViewAdapter(this,stringList);
        gridView1.setAdapter(adapter);

        listView1.requestFocus();
        listView1.setSelectionTriggerItemSelect(0);
    }
}
