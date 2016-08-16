package com.xgfe.android.components.xgrecyclerview;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by luoruidong on 16/8/2.
 */
public class XGAdapterBuilder {
    private XGAdapter mAdapter;

    XGAdapterBuilder(XGAdapter adapter) {
        mAdapter = adapter;
    }

    public <DATA> XGItemBuilder<DATA, XGAdapterBuilder> item(int layoutId) {
        return new XGItemBuilder<>(this, layoutId);
    }

    public <DATA> XGItemBuilder<DATA, XGAdapterBuilder> item(int layoutId, int type) {
        return new XGItemBuilder<>(this, layoutId, type);
    }

    public XGAdapterBuilder itemAdapter(XGAdapter.ItemAdapter adapter) {
        mAdapter.addAdapter(adapter);
        return this;
    }

    public XGAdapterBuilder itemAdapter(XGAdapter.ItemAdapter adapter, int type) {
        mAdapter.addAdapter(adapter, type);
        return this;
    }

    public XGAdapterBuilder header(final int layout) {
        mAdapter.addHeader(new XGAdapter.ItemAdapter() {
            @Override
            public void onBindViewHolder(XGAdapter.ViewHolder holder, Object o) {

            }

            @Override
            public XGAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new XGAdapter.ViewHolder(getLayoutInflater().inflate(layout, parent, false));
            }
        });
        return this;
    }

    public XGAdapterBuilder header(final View view) {
        mAdapter.addHeader(new XGAdapter.ItemAdapter() {
            @Override
            public void onBindViewHolder(XGAdapter.ViewHolder holder, Object o) {

            }

            @Override
            public XGAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new XGAdapter.ViewHolder(view);
            }
        });
        return this;
    }

    public XGAdapterBuilder footer(final int layout) {
        mAdapter.addFooter(new XGAdapter.ItemAdapter() {
            @Override
            public void onBindViewHolder(XGAdapter.ViewHolder holder, Object o) {

            }

            @Override
            public XGAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new XGAdapter.ViewHolder(getLayoutInflater().inflate(layout, parent, false));
            }
        });
        return this;
    }

    public XGAdapterBuilder footer(final View view) {
        mAdapter.addFooter(new XGAdapter.ItemAdapter() {
            @Override
            public void onBindViewHolder(XGAdapter.ViewHolder holder, Object o) {

            }

            @Override
            public XGAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new XGAdapter.ViewHolder(view);
            }
        });
        return this;
    }

    public XGAdapterBuilder typeHandler(XGAdapter.OnItemTypeHandler handler) {
        mAdapter.setTypeHandler(handler);
        return this;
    }

    public XGAdapter build() {
        return mAdapter;
    }
}

