package com.xgfe.android.components.xgrecyclerview.recyclerviews;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;
import com.xgfe.android.components.xgrecyclerview.decorations.DividerItemDecoration;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by luoruidong on 16/8/2.
 */
public class XGRecyclerView extends RecyclerView {
    private LinearLayoutManager mLayoutManager;
    private XGAdapter mAdapter;
    private DividerItemDecoration mDivider;
    private boolean hasDivider = false;

    public XGAdapter adapter() {
        return mAdapter;
    }

    public XGRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public XGRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public XGRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        super.setLayoutManager(mLayoutManager);

        mAdapter = new XGAdapter();
        super.setAdapter(mAdapter);

        mDivider = new DividerItemDecoration((int) (1f * getResources().getDisplayMetrics().density), getContext());
        setDividerEnable(true);
    }

    public void setAdapter(XGAdapter.ItemAdapter itemAdapter) {
        setAdapter(itemAdapter, XGAdapter.TYPE_NORMAL);
    }

    public void setAdapter(XGAdapter.ItemAdapter itemAdapter, int type) {
        mAdapter.addAdapter(itemAdapter, type);
    }


    public void setDividerEnable(boolean enable) {
        if (!hasDivider && enable) {
            addItemDecoration(mDivider);
            hasDivider = true;
        } else if (hasDivider && !enable) {
            removeItemDecoration(mDivider);
            hasDivider = false;
        }
    }

    @Deprecated
    @Override
    public final void setAdapter(RecyclerView.Adapter adapter) {
        //You can't use this now. Your should use adapter from XGRecyclerView.Adapter
        throw new RuntimeException("You can't use this now. Your should use adapter from XGRecyclerView.Adapter");
    }

    @Deprecated
    @Override
    public final void setLayoutManager(LayoutManager layout) {
        //You can't use this now. It's must be a verticaled LinearLayoutManager
        throw new RuntimeException("You can't use this now. It's must be a verticaled LinearLayoutManager");
    }


}
