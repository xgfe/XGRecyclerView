package com.xgfe.android.components.xgrecyclerview.datasources;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;

/**
 * Created by luoruidong on 16/8/1.
 */
public interface XGDataSource {
    int getDataType(int position);

    int getDataCount();

    Object getData(int position);

    void onCreateViewHolder(XGAdapter.ViewHolder holder, int viewType);

    void onBindViewHolder(XGAdapter.ViewHolder holder, Object data);
}
