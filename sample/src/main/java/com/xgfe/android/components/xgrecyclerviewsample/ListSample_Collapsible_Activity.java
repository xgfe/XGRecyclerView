package com.xgfe.android.components.xgrecyclerviewsample;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;
import com.xgfe.android.components.xgrecyclerview.datasources.CollapsibleDataSource;
import com.xgfe.android.components.xgrecyclerview.datasources.SimpleDataSource;
import com.xgfe.android.components.xgrecyclerview.recyclerviews.XGRecyclerView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

@SampleName("list - 4 - Collapsible HeaderFooter List")
public class ListSample_Collapsible_Activity extends AppCompatActivity {
    XGRecyclerView recyclerView;
    XGAdapter adapter;
    SimpleDataSource<Data> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);
        recyclerView = (XGRecyclerView) findViewById(R.id.list_act_rv);

        //添加header
        TextView ntv = new TextView(this);
        ntv.setText("this is header!!!");
        ntv.setBackgroundColor(0xff00ff00);

        //添加footer
        TextView ntv2 = new TextView(this);
        ntv2.setText("this is footer!!!");
        ntv2.setBackgroundColor(0xff0000ff);

        //添加adapter
        adapter = XGAdapter.with(recyclerView.adapter())
                .header(ntv)
                .footer(ntv2)
                .itemAdapter(XGAdapter
                        .<Data>item(R.layout.item_list_with_redtext)
                        .bindText(R.id.list_item_tv, data -> data.text)
                        .build())
                .build();

        adapter.setDataSource(dataSource = new CollapsibleDataSource<>(adapter,
                data -> data.text.contains("0")||data.text.contains("5")));

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
