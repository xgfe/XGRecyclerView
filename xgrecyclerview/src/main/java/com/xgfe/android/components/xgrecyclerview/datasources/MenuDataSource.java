package com.xgfe.android.components.xgrecyclerview.datasources;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;

import android.support.v4.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoruidong on 16/7/25.
 */
public class MenuDataSource<SectionItem, SubItem> extends SectionDataSource<SectionItem, SubItem> {
    MenuFilter<SectionItem, SubItem> menuFilter = new MenuFilter<>();

    public MenuDataSource(XGAdapter adapter, OnGetSubList<SectionItem, SubItem> onGetSubList) {
        super(adapter, onGetSubList);
    }

    @Override
    protected List<Pair<SectionItem, SubItem>> processData(List<Pair<SectionItem, SubItem>> source) {
        List<Pair<SectionItem, SubItem>> list = super.processData(source);
        List<Pair<SectionItem, SubItem>> newList = new ArrayList<>(list);
        for (Pair<SectionItem, SubItem> pair : list) {
            if (!menuFilter.apply(pair)) {
                newList.remove(pair);
            }
        }
        return newList;
    }

    @Override
    public void onCreateViewHolder(XGAdapter.ViewHolder holder, int viewType) {
        super.onCreateViewHolder(holder, viewType);
        if (viewType == TYPE_SECTION) {
            holder.itemView.setTag(holder);
            holder.itemView.setOnClickListener(mMenuClickListener);
        }
    }

    private View.OnClickListener mMenuClickListener = v -> {
        Object data = ((XGAdapter.ViewHolder) v.getTag()).getData();
        menuFilter.currentSectionData = data == menuFilter.currentSectionData ? null : data;
        render();
    };

    private static class MenuFilter<SectionItem, SubItem> implements SimpleDataSource.Filter<Pair<SectionItem,
            SubItem>> {

        public Object currentSectionData;

        @Override
        public boolean apply(Pair<SectionItem, SubItem> data) {
            if (currentSectionData == null) {
                return data.second == null;
            } else {
                return data.second == null || data.first == currentSectionData;
            }
        }
    }

    ;
}
