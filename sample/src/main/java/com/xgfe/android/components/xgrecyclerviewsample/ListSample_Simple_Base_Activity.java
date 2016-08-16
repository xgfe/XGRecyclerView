package com.xgfe.android.components.xgrecyclerviewsample;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;
import com.xgfe.android.components.xgrecyclerview.datasources.SimpleDataSource;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

@SampleName("list - 0 - Simple Base List")
public class ListSample_Simple_Base_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    XGAdapter adapter;
    SimpleDataSource<Data> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_simple);
        recyclerView = (RecyclerView) findViewById(R.id.list_act_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //添加adapter
        adapter = new XGAdapter();
        adapter.addAdapter(new MyItemAdapter());
        dataSource = new SimpleDataSource<>(adapter);
        adapter.setDataSource(dataSource);

        recyclerView.setAdapter(adapter);

        //填充测试数据
        ArrayList<Data> list = new ArrayList<>();
        for (int i = 18; i < 44; i++) {
            list.add(new Data("this is data " + i));
        }
        dataSource.setData(list);
    }

    private static class MyItemAdapter extends XGAdapter.ItemAdapter<MyViewHolder, Data> {
        @Override
        public void onBindViewHolder(MyViewHolder holder, Data data) {
            holder.tv.setText(data.text);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(getLayoutInflater().inflate(R.layout.item_list_with_redtext, parent, false));
        }
    }

    private static class MyViewHolder extends XGAdapter.ViewHolder {
        private TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.list_item_tv);
        }
    }

    private static class Data {
        public String text;

        public Data(String text) {
            this.text = text;
        }
    }
}
