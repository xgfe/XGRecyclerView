package com.xgfe.android.components.xgrecyclerviewsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ListView listView;

    Class<?>[] SAMPLE_CLASSES = {ListSample_Simple_Base_Activity.class, ListSample_Simple_Helper_Activity.class,
            ListSample_MultipleType_Activity.class, ListSample_Selectable_Activity.class,
            ListSample_Collapsible_Activity.class, ListSample_Loadable_Activity.class, ListSample_Section_Activity
            .class, ListSample_Menu_Activity.class, ListSample_Refresh_Activity.class,
            ListSample_TouchHelper_Activity.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        final ArrayList<KV> samples = new ArrayList<>();
        for (Class<?> clazz : SAMPLE_CLASSES) {
            SampleName sampleName = clazz.getAnnotation(SampleName.class);
            if (sampleName == null) {
                throw new RuntimeException("Sample Activity 需要加上 @SampleName 注解");
            }
            samples.add(new KV(sampleName.value(), clazz));
        }
        Collections.sort(samples);

        ArrayAdapter<KV> arrayAdapter = new ArrayAdapter<KV>(this, android.R.layout.simple_list_item_1, samples);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(MainActivity.this, samples.get(i).v));
            }
        });
    }

    class KV implements Comparable<KV> {
        String k;
        Class<?> v;

        public KV(String k, Class<?> v) {
            this.k = k;
            this.v = v;
        }

        @Override
        public int compareTo(KV kv) {
            return k.compareTo(kv.k);
        }

        @Override
        public String toString() {
            return k.toString();
        }
    }
}
