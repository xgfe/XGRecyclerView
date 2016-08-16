package com.xgfe.android.components.xgrecyclerviewsample;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;
import com.xgfe.android.components.xgrecyclerview.datasources.SimpleDataSource;
import com.xgfe.android.components.xgrecyclerview.recyclerviews.XGRecyclerView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

@SampleName("list - 1 - Simple Helper List")
public class ListSample_Simple_Helper_Activity extends AppCompatActivity {
    XGRecyclerView recyclerView;
    XGAdapter adapter;
    SimpleDataSource<Data> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);
        recyclerView = (XGRecyclerView) findViewById(R.id.list_act_rv);

        //添加adapter

        adapter = XGAdapter.with(recyclerView.adapter())
                .itemAdapter(XGAdapter
                        .<Data>item(R.layout.item_list_with_redtext)
                        .bindText(R.id.list_item_tv, data -> data.text)
                        .build())
                .build();

        /*
        //下面这种写法效果相同
        adapter = XGAdapter.with(recyclerView.adapter())
                .<Data>item(R.layout.item_list_with_redtext)
                        .bindText(R.id.list_item_tv, data -> data.text)
                        .build()
                .build();
         */
        adapter.setDataSource(dataSource = new SimpleDataSource<>(adapter));

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
