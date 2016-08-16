package com.xgfe.android.components.xgrecyclerview.datasources;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;

/**
 * Created by luoruidong on 16/8/12.
 */
public class TouchHelperDataSource<DATA> extends SimpleDataSource<DATA> {

    private ItemTouchHelper.Callback mCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return enableDrag;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return enableSwipe;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView
                .ViewHolder target) {
            int pos1 = mAdapter.getDataPosition(viewHolder.getAdapterPosition()),
                    pos2 = mAdapter.getDataPosition(target.getAdapterPosition());
            DATA d1 = displayData.get(pos1), d2 = displayData.get(pos2);
            Collections.swap(displayData, pos1, pos2);
            Collections.swap(data, data.indexOf(d1), data.indexOf(d2));
            mAdapter.notifyItemMoved(pos1, pos2);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = mAdapter.getDataPosition(viewHolder.getAdapterPosition());
            DATA d = displayData.get(pos);
            displayData.remove(d);
            data.remove(d);
            mAdapter.notifyRangeRemoved(pos, 1);
        }
    };

    private boolean enableDrag = true, enableSwipe = true;

    public void enableDrag(boolean enableDrag) {
        this.enableDrag = enableDrag;
    }

    public void enableSwipe(boolean enableSwipe) {
        this.enableSwipe = enableSwipe;
    }

    public boolean isDragEnable() {
        return enableDrag;
    }

    public boolean isSwipeEnable() {
        return enableSwipe;
    }

    public ItemTouchHelper.Callback getCallback() {
        return mCallback;
    }

    public TouchHelperDataSource(XGAdapter adapter) {
        super(adapter);
    }
}