package com.xgfe.android.components.xgrecyclerviewsample;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;
import com.xgfe.android.components.xgrecyclerview.datasources.SimpleDataSource;
import com.xgfe.android.components.xgrecyclerview.recyclerviews.XGRecyclerView;
import com.xgfe.android.components.xgrecyclerview.recyclerviews.XGRefresheRecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

@SampleName("list - 8 - Refresh List")
public class ListSample_Refresh_Activity extends AppCompatActivity {
    XGRefresheRecyclerView refreshView;
    XGRecyclerView recyclerView;
    XGAdapter adapter;
    SimpleDataSource<Data> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_refresh);
        refreshView = (XGRefresheRecyclerView) findViewById(R.id.list_act_rv);
        refreshView.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R
                .color.colorAccent));
        ;
        recyclerView = refreshView.recyclerView();

        //添加adapter
        adapter = XGAdapter.with(recyclerView.adapter()).itemAdapter(XGAdapter.<Data>item(R.layout
                .item_list_with_redtext).bindText(R.id.list_item_tv, data -> data.text).build()).build();

        adapter.setDataSource(dataSource = new SimpleDataSource<>(adapter));

        refreshView.setOnRefreshListener(() -> new Handler().postDelayed(() -> refreshView.stopRefreshing(), 5000));

        //填充测试数据
        ArrayList<Data> list = new ArrayList<>();
        for (int i = 18; i < 44; i++) {
            list.add(new Data("this is data " + i));
        }
        dataSource.setData(list);
    }

    private static class Data {
        public String text;

        public Data(String text) {
            this.text = text;
        }
    }
}
