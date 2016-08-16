package com.xgfe.android.components.xgrecyclerview.datasources;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by luoruidong on 16/7/18.
 */
public class SectionDataSource<SectionItem, SubItem> extends SimpleDataSource<Pair<SectionItem, SubItem>> {
    public final static int TYPE_SECTION = -6999;
    private HashMap<SimpleDataSource.Filter, Integer> filterIdCache = new HashMap<>();
    private OnGetSubList<SectionItem, SubItem> mOnGetSubList;

    public interface OnGetSubList<SectionItem, SubItem> {
        List<SubItem> getSubList(SectionItem data);
    }

    public SectionDataSource(XGAdapter adapter,OnGetSubList<SectionItem, SubItem> onGetSubList) {
        super(adapter);
        mOnGetSubList = onGetSubList;
    }

    @Deprecated
    @Override
    public void setData(List<Pair<SectionItem, SubItem>> data) {
        super.setData(data);
    }

    public void setSectionData(List<SectionItem> datas) {
        List<Pair<SectionItem, SubItem>> data = new ArrayList<>();
        for (SectionItem item : datas) {
            data.add(new Pair<>(item, null));
            List<SubItem> sub = mOnGetSubList.getSubList(item);
            for (SubItem subitem : sub) {
                data.add(new Pair<>(item, subitem));
            }
        }
        super.setData(data);
    }

    public int addSectionFilter(SimpleDataSource.Filter<SectionItem> filter) {
        int id = super.addFilter(data1 -> filter.apply(data1.first));
        filterIdCache.put(filter, id);
        return id;
    }

    public void removeSectionFilter(SimpleDataSource.Filter<SectionItem> filter) {
        removeFilter(filterIdCache.get(filter));
        filterIdCache.remove(filter);
    }

    public boolean hasSectionFilter(SimpleDataSource.Filter<SectionItem> filter) {
        return filterIdCache.containsKey(filter);
    }

    public void revertSectionFilter(SimpleDataSource.Filter<SectionItem> filter) {
        if (hasSectionFilter(filter)) {
            removeSectionFilter(filter);
        } else {
            addSectionFilter(filter);
        }
    }

    public int addSubFilter(SimpleDataSource.Filter<SubItem> filter) {
        int id = super.addFilter(data1 -> {
            if (data1.second != null) return filter.apply(data1.second);
            return true;
        });
        filterIdCache.put(filter, id);
        return id;
    }

    public void removeSubFilter(SimpleDataSource.Filter<SubItem> filter) {
        removeFilter(filterIdCache.get(filter));
        filterIdCache.remove(filter);
    }

    public boolean hasSubFilter(SimpleDataSource.Filter<SubItem> filter) {
        return filterIdCache.containsKey(filter);
    }

    public void revertSubFilter(SimpleDataSource.Filter<SubItem> filter) {
        if (hasSubFilter(filter)) {
            removeSubFilter(filter);
        } else {
            addSubFilter(filter);
        }
    }

    @Override
    public Object getData(int position) {
        Pair<SectionItem, SubItem> data = getTypedData(position);
        if (data.second != null) return data.second;
        return data.first;
    }

    @Override
    public final int getDataType(int position) {
        if (getTypedData(position).second != null) return getDisplaySubDataViewType(position);
        return TYPE_SECTION;
    }

    protected int getDisplaySubDataViewType(int position) {
        return super.getDataType(position);
    }
}
