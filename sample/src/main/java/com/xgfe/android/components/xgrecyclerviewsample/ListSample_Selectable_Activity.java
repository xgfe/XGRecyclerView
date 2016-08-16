package com.xgfe.android.components.xgrecyclerviewsample;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;
import com.xgfe.android.components.xgrecyclerview.datasources.SimpleDataSource;
import com.xgfe.android.components.xgrecyclerview.decorations.StripedItemDecoration;
import com.xgfe.android.components.xgrecyclerview.recyclerviews.XGRecyclerView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;
import java.util.List;

@SampleName("list - 3 - Selectable List")
public class ListSample_Selectable_Activity extends AppCompatActivity {
    XGRecyclerView recyclerView;
    XGAdapter adapter;
    SimpleDataSource<Data> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);
        recyclerView = (XGRecyclerView) findViewById(R.id.list_act_rv);
        recyclerView.setDividerEnable(false);
        recyclerView.addItemDecoration(new StripedItemDecoration(this));
        recyclerView.setItemAnimator(new LightItemAnimator());
        //添加adapter

        adapter = XGAdapter.with(recyclerView.adapter()).itemAdapter(XGAdapter.<Data>item(R.layout
                .item_list_with_redtext).bindClickListener(data1 -> v -> {
            data1.isSelected = !v.isSelected();
            dataSource.render(data1);
        }).bindText(R.id.list_item_tv, data -> data.text).bindSelected(data -> data.isSelected).build()).build();

        adapter.setDataSource(dataSource = new SimpleDataSource<>(adapter));

        //填充测试数据
        ArrayList<Data> list = new ArrayList<>();
        for (int i = 8; i < 44; i++) {
            Data data = new Data("this is data " + i);
            if (i == 12 || i == 20 || i == 21) {
                data.isSelected = true;
            }
            list.add(data);
        }

        dataSource.setData(list);
    }

    private static class Data {
        public String text;
        public boolean isSelected;

        public Data(String text) {
            this.text = text;
        }
    }

    private static class LightItemAnimator extends DefaultItemAnimator {
        ArrayList<AnimationInfo> penddingAnima = new ArrayList<>();

        @Override
        public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder
                newHolder, @NonNull ItemHolderInfo preInfo, @NonNull ItemHolderInfo postInfo) {
            if (oldHolder != newHolder)return super.animateChange(oldHolder, newHolder, preInfo, postInfo);

            //

            SelectHolderInfo pInfo = (SelectHolderInfo) preInfo, tInfo = (SelectHolderInfo) postInfo;
            if (pInfo.isSelected == tInfo.isSelected) return false;

            if (penddingAnima.size() != 0) {
                List<AnimationInfo> copy = new ArrayList<>(penddingAnima);
                for (AnimationInfo info : copy) {
                    if (info.holder == newHolder) {
                        info.cancel();
                        penddingAnima.remove(info);
                        Log.e("AnimationInfo", "cancel len = " + penddingAnima.size()+" "+newHolder.toString());
                    }
                }
            }
            //
            AnimationInfo animationInfo = new AnimationInfo();

            animationInfo.animation = new ScaleAnimation(0.6f, 1.0f, 0.6f, 1.0f);
            animationInfo.animation.setDuration(400);
            animationInfo.animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    penddingAnima.remove(animationInfo);
                    Log.e("AnimationInfo", "end len = " + penddingAnima.size()+" "+newHolder.toString());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animationInfo.holder = newHolder;
            animationInfo.holder.itemView.startAnimation(animationInfo.animation);
            penddingAnima.add(animationInfo);
            Log.e("AnimationInfo", "add len = " + penddingAnima.size()+" "+newHolder.toString());

            return true;
        }

        private static class AnimationInfo {
            public Animation animation;
            public RecyclerView.ViewHolder holder;

            public void cancel() {
                animation.setAnimationListener(null);
                animation.cancel();
            }
        }

        private static class SelectHolderInfo extends ItemHolderInfo {
            public boolean isSelected;
        }

        @Override
        public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull List<Object>
                payloads) {
            return true;
        }

        @NonNull
        @Override
        public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state, @NonNull RecyclerView
                .ViewHolder viewHolder, int changeFlags, @NonNull List<Object> payloads) {
            SelectHolderInfo info = (SelectHolderInfo) super.recordPreLayoutInformation(state, viewHolder,
                    changeFlags, payloads);
            info.isSelected = viewHolder.itemView.isSelected();
            return info;
        }

        @NonNull
        @Override
        public ItemHolderInfo recordPostLayoutInformation(@NonNull RecyclerView.State state, @NonNull RecyclerView
                .ViewHolder viewHolder) {
            SelectHolderInfo info = (SelectHolderInfo) super.recordPostLayoutInformation(state, viewHolder);
            info.isSelected = viewHolder.itemView.isSelected();
            return info;
        }

        @Override
        public ItemHolderInfo obtainHolderInfo() {
            return new SelectHolderInfo();
        }


        @Override
        public void endAnimation(RecyclerView.ViewHolder item) {
            super.endAnimation(item);
            for (AnimationInfo info : penddingAnima) {
                if (info.holder == item) {
                    info.cancel();
                    penddingAnima.remove(info);
                    break;
                }
            }
        }

        @Override
        public void endAnimations() {
            super.endAnimations();
            for (AnimationInfo info : penddingAnima) {
                info.cancel();
            }
            penddingAnima.clear();
        }

        @Override
        public boolean isRunning() {
            return super.isRunning() || !penddingAnima.isEmpty();
        }
    }

}
