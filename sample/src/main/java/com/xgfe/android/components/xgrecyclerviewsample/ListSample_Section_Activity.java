package com.xgfe.android.components.xgrecyclerviewsample;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;
import com.xgfe.android.components.xgrecyclerview.datasources.SectionDataSource;
import com.xgfe.android.components.xgrecyclerview.datasources.SimpleDataSource;
import com.xgfe.android.components.xgrecyclerview.decorations.PinnedSectionItemDecoration;
import com.xgfe.android.components.xgrecyclerview.recyclerviews.XGRecyclerView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SampleName("list - 6 - Section HeaderFooter Filter List")
public class ListSample_Section_Activity extends AppCompatActivity {
    XGRecyclerView recyclerView;
    Button btnFilter1, btnFilter2;
    XGAdapter adapter;
    SectionDataSource<Data, SubData> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_section_headerfooter_filter);
        btnFilter1 = (Button) findViewById(R.id.list_act_btn_filter1);
        btnFilter2 = (Button) findViewById(R.id.list_act_btn_filter2);
        recyclerView = (XGRecyclerView) findViewById(R.id.list_act_rv);
        recyclerView.addItemDecoration(new PinnedSectionItemDecoration());

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
                        .bindText(R.id.list_item_tv, data -> data.title)
                        .build(),SectionDataSource.TYPE_SECTION)
                .itemAdapter(XGAdapter
                        .<SubData>item(R.layout.item_list_with_yellowtext)
                        .bindText(R.id.list_item_tv, data -> data.text)
                        .build())
                .build();

        adapter.setDataSource(dataSource = new SectionDataSource<>(adapter,data -> data.sub));

        //添加两种过滤器
        SimpleDataSource.Filter<Data> filterSection = data -> !data.title.contains("3");
        SimpleDataSource.Filter<SubData> filterSub = data -> data.text.contains("2");

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

        btnFilter1.setOnClickListener(v -> dataSource.revertSectionFilter(filterSection));
        btnFilter2.setOnClickListener(v -> dataSource.revertSubFilter(filterSub));


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
