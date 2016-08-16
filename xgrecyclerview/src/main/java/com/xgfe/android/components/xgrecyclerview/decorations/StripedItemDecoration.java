package com.xgfe.android.components.xgrecyclerview.decorations;

import com.xgfe.android.components.xgrecyclerview.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoruidong on 16/8/11.
 */
public class StripedItemDecoration extends RecyclerView.ItemDecoration {
    public StripedItemDecoration(Context context) {
        this(context, R.color.colorWhite, R.color.colorGrey200);
    }

    public StripedItemDecoration(Context context, @DrawableRes int id1, @DrawableRes int id2) {
        mDivider = context.getResources().getDrawable(id1);
        mDivider2 = context.getResources().getDrawable(id2);
    }

    private Drawable mDivider, mDivider2;
    private List<Integer> type = new ArrayList<>();

    public StripedItemDecoration forType(int type) {
        this.type.add(type);
        return this;
    }


    public StripedItemDecoration forType(List<Integer> type) {
        this.type.addAll(type);
        return this;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        RecyclerView.Adapter adapter = parent.getAdapter();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);
            if (position == RecyclerView.NO_POSITION) {
                continue;
            }
            final int viewType = adapter.getItemViewType(position);
            if (type.size() != 0 && !type.contains(viewType)) {
                continue;
            }
            final int top = child.getTop() + Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + child.getHeight();
            if (position % 2 == 1) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            } else {
                mDivider2.setBounds(left, top, right, bottom);
                mDivider2.draw(c);
            }
        }
    }
}