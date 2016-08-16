package com.xgfe.android.components.xgrecyclerviewsample;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;
import com.xgfe.android.components.xgrecyclerview.datasources.SimpleDataSource;
import com.xgfe.android.components.xgrecyclerview.recyclerviews.XGRecyclerView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

@SampleName("list - 2 - Multiple Type List")
public class ListSample_MultipleType_Activity extends AppCompatActivity {
    private final static int TYPE_NORMAL = 1;
    private final static int TYPE_YELLOW = 2;

    XGRecyclerView recyclerView;
    SimpleDataSource<Data> dataSource;
    XGAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);
        recyclerView = (XGRecyclerView) findViewById(R.id.list_act_rv);

        adapter = XGAdapter.with(recyclerView.adapter())
                .itemAdapter(XGAdapter
                        .<Data>item(R.layout.item_list_with_redtext)
                        .bindText(R.id.list_item_tv, data -> data.text)
                        .build(), TYPE_NORMAL)
                .itemAdapter(XGAdapter
                        .<Data>item(R.layout.item_list_with_yellowtext)
                        .bindText(R.id.list_item_tv, data -> "yellow : " + data.text)
                        .build(), TYPE_YELLOW)
                .typeHandler((position, data) -> ((Data) data).isYellow ? TYPE_YELLOW : TYPE_NORMAL)
                .build();

        adapter.setDataSource(dataSource = new SimpleDataSource<>(adapter));

        //填充测试数据
        ArrayList<Data> list = new ArrayList<>();
        for (int i = 17; i < 51; i++) {
            list.add(new Data("this is data " + i, i % 4 == 0));
        }
        dataSource.setData(list);
    }

    private static class Data {
        public String text;
        public boolean isYellow = false;

        public Data(String text, boolean isYellow) {
            this.text = text;
            this.isYellow = isYellow;
        }
    }
}
