package com.norton.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.norton.demo.R;
import com.norton.demo.adapter.ListAdapter;
import com.swl.tvlibrary.custom.MemoryListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/12/20.
 */

public class ListViewTestActivity extends Activity {

    private Context context = this;
    private MemoryListView listView1;
    private MemoryListView listView2;
    private MemoryListView listView3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_test);

        listView1 = (MemoryListView) findViewById(R.id.listview_1);
        listView2 = (MemoryListView) findViewById(R.id.listview_2);
        listView3 = (MemoryListView) findViewById(R.id.listview_3);

        List<String> stringList = new ArrayList<>();
        for (int i =0 ; i < 49; i++) {
            stringList.add("测试item：" + i);
        }

        ListAdapter adapter1 = new ListAdapter(this,stringList);
        ListAdapter adapter2 = new ListAdapter(this,stringList);
        ListAdapter adapter3 = new ListAdapter(this,stringList);
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);
        listView3.setAdapter(adapter3);

        listView1.requestFocus();
        listView1.setSelectionTriggerItemSelect(0);
    }
}
