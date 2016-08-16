package com.xgfe.android.components.xgrecyclerviewsample;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;
import com.xgfe.android.components.xgrecyclerview.XGItemBuilder;
import com.xgfe.android.components.xgrecyclerview.datasources.MenuDataSource;
import com.xgfe.android.components.xgrecyclerview.datasources.SectionDataSource;
import com.xgfe.android.components.xgrecyclerview.recyclerviews.XGRecyclerView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

@SampleName("list - 7 - Menu List")
public class ListSample_Menu_Activity extends AppCompatActivity {
    XGRecyclerView recyclerView;
    XGAdapter adapter;
    SectionDataSource<Data, SubData> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);
        recyclerView = (XGRecyclerView) findViewById(R.id.list_act_rv);

        //添加adapter
        adapter = XGAdapter.with(recyclerView.adapter())
                .itemAdapter(XGAdapter
                        .<Data>item(XGItemBuilder.LAYOUT_TITLE)
                        .bindText(data -> data.title)
                        .build(),SectionDataSource.TYPE_SECTION)
                .itemAdapter(XGAdapter
                        .<SubData>item(XGItemBuilder.LAYOUT_TEXT)
                        .bindText(data -> data.text)
                        .build())
                .build();

        adapter.setDataSource(dataSource = new MenuDataSource<>(adapter,data -> data.sub));
        //填充测试数据
        ArrayList<Data> list = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            ArrayList<SubData> sub = new ArrayList<>();
            for (int j = 1 + i; j < 10 + i; j++) {
                sub.add(new SubData("this is subdata " + (i * 10 + j)));
            }
            list.add(new Data("this is section " + i, sub));
        }
        dataSource.setSectionData(list);
    }

    private static class Data {
        public String title;
        public List<SubData> sub;

        public Data(String title, List<SubData> sub) {
            this.title = title;
            this.sub = sub;
        }
    }

    private static class SubData {
        public String text;

        public SubData(String text) {
            this.text = text;
        }
    }

}
