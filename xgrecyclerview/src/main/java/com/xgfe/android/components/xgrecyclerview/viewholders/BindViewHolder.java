package com.xgfe.android.components.xgrecyclerview.viewholders;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by luoruidong on 16/7/21.
 */
public class BindViewHolder extends XGAdapter.ViewHolder {
    private SparseArray<View> viewHolder = new SparseArray<>();

    public BindViewHolder(View itemView, int... ids) {
        super(itemView);
        for (int id : ids) {
            viewHolder.put(id, itemView.findViewById(id));
        }
    }

    public <T> T get(int id) {
        return (T) viewHolder.get(id);
    }

    public <T> T get(int id, Class<T> c) {
        return (T) viewHolder.get(id);
    }
}
