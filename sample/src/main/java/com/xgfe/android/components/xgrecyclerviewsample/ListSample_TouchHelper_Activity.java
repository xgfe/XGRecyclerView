package com.xgfe.android.components.xgrecyclerviewsample;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;
import com.xgfe.android.components.xgrecyclerview.datasources.TouchHelperDataSource;
import com.xgfe.android.components.xgrecyclerview.recyclerviews.XGRecyclerView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;

@SampleName("list - 9 - TouchHelper List")
public class ListSample_TouchHelper_Activity extends AppCompatActivity {
    XGRecyclerView recyclerView;
    XGAdapter adapter;
    TouchHelperDataSource<Data> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);
        recyclerView = (XGRecyclerView) findViewById(R.id.list_act_rv);

        //添加adapter

        adapter = XGAdapter.with(recyclerView.adapter()).itemAdapter(XGAdapter.<Data>item(R.layout
                .item_list_with_redtext).bindText(R.id.list_item_tv, data -> data.text).build()).build();

        adapter.setDataSource(dataSource = new TouchHelperDataSource<>(adapter));
        new ItemTouchHelper(dataSource.getCallback()).attachToRecyclerView(recyclerView);

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
