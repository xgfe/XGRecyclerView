package com.xgfe.android.components.xgrecyclerview.decorations;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;
import com.xgfe.android.components.xgrecyclerview.datasources.SectionDataSource;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by luoruidong on 16/8/1.
 */
public class PinnedSectionItemDecoration extends RecyclerView.ItemDecoration {
    private RecyclerView.Adapter mAdapter = null;
    private View mPinnedHeaderView = null;
    private int mHeaderPosition = -1;
    private int sectionType = SectionDataSource.TYPE_SECTION;

    private int mPinnedHeaderTop;
    private Rect mClipBounds;

    public PinnedSectionItemDecoration() {
    }

    public PinnedSectionItemDecoration(int sectionType) {
        this.sectionType = sectionType;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        createPinnedHeader(parent);

        if (mPinnedHeaderView != null) {
            final int headerEndAt = mPinnedHeaderView.getTop() + mPinnedHeaderView.getHeight();
            final View v = parent.findChildViewUnder(c.getWidth() / 2, headerEndAt + 1);

            if (isHeaderView(parent, v)) {
                mPinnedHeaderTop = v.getTop() - mPinnedHeaderView.getHeight();
            } else {
                mPinnedHeaderTop = 0;
            }

            mClipBounds = c.getClipBounds();
            mClipBounds.top = mPinnedHeaderTop + mPinnedHeaderView.getHeight();
            c.clipRect(mClipBounds);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mPinnedHeaderView != null) {
            c.save();

            mClipBounds.top = 0;
            c.clipRect(mClipBounds, Region.Op.UNION);
            c.translate(0, mPinnedHeaderTop);
            mPinnedHeaderView.draw(c);

            c.restore();
        }
    }

    private void createPinnedHeader(RecyclerView parent) {
        mAdapter = parent.getAdapter();
        if (!(parent.getLayoutManager() instanceof LinearLayoutManager))
            throw new IllegalArgumentException("Need LinearLayoutManager for recyclerView to apply " +
                    "PinnedSectionItemDecoration");
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) parent.getLayoutManager();
        final int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
        final int headerPosition = findPinnedHeaderPosition(firstVisiblePosition);

        if (firstVisiblePosition == 0 && (parent.getAdapter() instanceof XGAdapter) && ((XGAdapter) mAdapter)
                .hasHeader()) {
            mPinnedHeaderView = null;
            mHeaderPosition = -1;
        } else if (headerPosition >= 0 && mHeaderPosition != headerPosition) {
            mHeaderPosition = headerPosition;
            final int viewType = mAdapter.getItemViewType(headerPosition);

            final RecyclerView.ViewHolder pinnedViewHolder = mAdapter.createViewHolder(parent, viewType);
            mAdapter.bindViewHolder(pinnedViewHolder, headerPosition);
            mPinnedHeaderView = pinnedViewHolder.itemView;

            // read layout parameters
            ViewGroup.LayoutParams layoutParams = mPinnedHeaderView.getLayoutParams();

            int heightMode = View.MeasureSpec.getMode(layoutParams.height);
            int heightSize = View.MeasureSpec.getSize(layoutParams.height);

            if (heightMode == View.MeasureSpec.UNSPECIFIED) {
                heightMode = View.MeasureSpec.EXACTLY;
            }

            final int maxHeight = parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom();
            if (heightSize > maxHeight) {
                heightSize = maxHeight;
            }

            // measure & layout
            final int ws = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent
                    .getPaddingRight(), View.MeasureSpec.EXACTLY);
            final int hs = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode);
            mPinnedHeaderView.measure(ws, hs);
            mPinnedHeaderView.layout(0, 0, mPinnedHeaderView.getMeasuredWidth(), mPinnedHeaderView.getMeasuredHeight());
        }
    }

    private int findPinnedHeaderPosition(int fromPosition) {
        if (fromPosition > mAdapter.getItemCount()) {
            return -1;
        }

        for (int position = fromPosition; position >= 0; position--) {
            final int viewType = mAdapter.getItemViewType(position);
            if (isPinnedViewType(viewType)) {
                return position;
            }
        }
        return -1;
    }

    private boolean isPinnedViewType(int viewType) {
        return viewType == sectionType;
    }

    private boolean isHeaderView(RecyclerView parent, View v) {
        final int position = parent.getChildPosition(v);
        if (position == RecyclerView.NO_POSITION) {
            return false;
        }
        final int viewType = mAdapter.getItemViewType(position);

        return isPinnedViewType(viewType);
    }
}