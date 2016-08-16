package com.xgfe.android.components.xgrecyclerview.decorations;

import com.xgfe.android.components.xgrecyclerview.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by luoruidong on 16/8/1.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final int mVerticalSpaceHeight;
    private final Drawable mDrawable;
    private int paddingLeft = 0, paddingRight = 0;

    public DividerItemDecoration(int mVerticalSpaceHeight, Context context) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        this.mDrawable = context.getResources().getDrawable(R.color.colorGrey300);
    }

    public DividerItemDecoration(Context context, @DrawableRes int id) {
        this.mDrawable = context.getResources().getDrawable(id);
        this.mVerticalSpaceHeight = mDrawable.getIntrinsicHeight();
    }

    public DividerItemDecoration(Context context, int verticalSpaceHeight, @DrawableRes int id) {
        mVerticalSpaceHeight = verticalSpaceHeight;
        mDrawable = context.getResources().getDrawable(id);
    }

    public DividerItemDecoration withPadding(int paddingLeft, int paddingRight) {
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        return this;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDrawable == null) return;
        final int left = parent.getPaddingLeft() + paddingLeft;
        final int right = parent.getWidth() - parent.getPaddingRight() - paddingRight;
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + mVerticalSpaceHeight;
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = mVerticalSpaceHeight;
    }
}
