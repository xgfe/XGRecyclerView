package com.xgfe.android.components.xgrecyclerview;

import com.xgfe.android.components.xgrecyclerview.datasources.XGDataSource;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by luoruidong on 16/8/1.
 */
public class XGAdapter extends RecyclerView.Adapter<XGAdapter.ViewHolder> {
    public final static int TYPE_HEADER = -9999;
    public final static int TYPE_FOOTER = -9998;
    public final static int TYPE_NORMAL = -1;

    public interface OnItemTypeHandler<DATA> {
        int getItemViewType(int position, DATA data);
    }

    private SparseArray<ItemAdapter> adapterMap = new SparseArray<>();
    private XGDataSource mDataSource;
    private RecyclerView view;
    private OnItemTypeHandler mTypeHandler;
    private LayoutInflater layoutInflater;
    private int cacheCount;
    private boolean hasHeader = false, hasFooter = false;

    public void setDataSource(XGDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    public void addHeader(ItemAdapter itemAdapter) {
        addAdapter(itemAdapter, TYPE_HEADER);
    }

    public void addFooter(ItemAdapter itemAdapter) {
        addAdapter(itemAdapter, TYPE_FOOTER);
    }

    public void addAdapter(ItemAdapter itemAdapter) {
        addAdapter(itemAdapter, TYPE_NORMAL);
    }

    public void addAdapter(ItemAdapter itemAdapter, int type) {
        adapterMap.put(type, itemAdapter);
        if (view != null) itemAdapter.onAttachedToRecyclerView(view, layoutInflater);
        if (type == TYPE_HEADER) hasHeader = true;
        if (type == TYPE_FOOTER) hasFooter = true;
    }

    public void removeAdapter(int type) {
        adapterMap.remove(type);
        if (type == TYPE_HEADER) hasHeader = false;
        if (type == TYPE_FOOTER) hasFooter = false;
    }

    public boolean hasHeader() {
        return hasHeader;
    }

    public boolean hasFooter() {
        return hasFooter;
    }

    public void setTypeHandler(OnItemTypeHandler typeHandler) {
        mTypeHandler = typeHandler;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        view = recyclerView;
        layoutInflater = LayoutInflater.from(view.getContext());
        if (adapterMap.size() != 0) {
            for (int i = 0, len = adapterMap.size(); i < len; i++) {
                adapterMap.valueAt(i).onAttachedToRecyclerView(view, layoutInflater);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = find(viewType).onCreateViewHolder(parent, viewType);
        holder.setAdapter(this);
        mDataSource.onCreateViewHolder(holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int dataPos = getDataPosition(position);
        int type = getItemViewType(position);
        Object data;
        if (type == TYPE_HEADER || type == TYPE_FOOTER) {
            data = null;
        } else {
            data = mDataSource.getData(dataPos);
        }
        holder.onBindViewHolder(position, data);
        find(type).onBindViewHolder(holder, data);
        mDataSource.onBindViewHolder(holder, data);
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader && position == 0) return TYPE_HEADER;
        if (hasFooter && position == cacheCount - 1) return TYPE_FOOTER;
        int type = mDataSource.getDataType(getDataPosition(position));
        if (type == TYPE_NORMAL && mTypeHandler != null) {
            type = mTypeHandler.getItemViewType(position, mDataSource.getData(getDataPosition(position)));
        }
        return type;
    }

    @Override
    public int getItemCount() {
        if (mDataSource == null) return 0;
        cacheCount = mDataSource.getDataCount();
        if (hasHeader) cacheCount++;
        if (hasFooter) cacheCount++;
        return cacheCount;
    }

    private ItemAdapter find(int type) {
        ItemAdapter itemAdapter = adapterMap.get(type);
        if (itemAdapter == null) throw new IllegalArgumentException("Adapter for type = " + type + " not found!!!!");
        return itemAdapter;
    }

    public int getDataPosition(int position) {
        if (hasHeader) return position - 1;
        return position;
    }

    public void notifyChanged(int position) {
        if (hasHeader) position++;
        notifyItemChanged(position);
    }

    public void notifyRangeRemoved(int positionStart, int itemCount) {
        if (hasHeader) positionStart++;
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    public void notifyRangeInserted(int positionStart, int itemCount) {
        if (hasHeader) positionStart++;
        notifyItemRangeInserted(positionStart, itemCount);
    }

    /**
     * Adapter
     */
    public static abstract class ItemAdapter<VH extends ViewHolder, DATA> {
        private LayoutInflater layoutInflater;

        protected void onAttachedToRecyclerView(RecyclerView recyclerView, LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
        }

        protected LayoutInflater getLayoutInflater() {
            return layoutInflater;
        }

        public abstract void onBindViewHolder(VH holder, DATA data);

        public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);
    }

    /**
     * ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected Object data;
        protected XGAdapter mAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setFocusable(true);
            itemView.setClickable(true);
            if (itemView.getBackground() == null) {
                itemView.setBackgroundResource(R.drawable.list_background_ripple);
            }
        }

        protected void setAdapter(XGAdapter adapter) {
            mAdapter = adapter;
        }

        public Object getData() {
            return data;
        }

        public void onBindViewHolder(int position, Object data) {
            this.data = data;
        }

        @Deprecated
        private void notifyDataSetChanged() {
            mAdapter.notifyDataSetChanged();
        }
    }

    public static XGAdapterBuilder build() {
        return new XGAdapterBuilder(new XGAdapter());
    }

    public static XGAdapterBuilder with(XGAdapter adapter) {
        return new XGAdapterBuilder(adapter);
    }

    public static <DATA> XGItemBuilder<DATA, ItemAdapter> item(int layoutId) {
        return new XGItemBuilder<>(layoutId);
    }

}

