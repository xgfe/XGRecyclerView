package com.xgfe.android.components.xgrecyclerview.recyclerviews;

import com.xgfe.android.components.xgrecyclerview.R;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by luoruidong on 16/7/25.
 */
public class XGRefresheRecyclerView extends SwipeRefreshLayout {
    private XGRecyclerView mRecyclerView;

    public XGRefresheRecyclerView(Context context) {
        super(context);
        init();
    }

    public XGRefresheRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mRecyclerView = new XGRecyclerView(getContext());
        mRecyclerView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                .MATCH_PARENT));
        addView(mRecyclerView);
    }

    public XGRecyclerView recyclerView() {
        return mRecyclerView;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        super.setOnRefreshListener(listener);
    }

    public void startRefreshing() {
        setRefreshing(true);
    }

    public void stopRefreshing() {
        setRefreshing(false);
    }
}
