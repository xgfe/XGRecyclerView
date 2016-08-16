package com.xgfe.android.components.xgrecyclerview.datasources;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luoruidong on 16/8/1.
 */
public class SimpleDataSource<DATA> implements XGDataSource {
    protected XGAdapter mAdapter;
    protected InnerDataRender<DATA> render = new InnerDataRender<>();
    protected List<DATA> data = new ArrayList<>();
    private HashMap<Integer, Filter<DATA>> filters = new HashMap<>();
    private int filtersIdCounter = 0;
    private boolean animationEnable = true;

    public SimpleDataSource(XGAdapter adapter) {
        mAdapter = adapter;
        afterConstruct(mAdapter);
    }

    protected void afterConstruct(XGAdapter adapter) {
    }


    public void setAdapter(XGAdapter adapter) {
        mAdapter = adapter;
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public boolean isAnimationEnable() {
        return animationEnable;
    }

    public void setAnimationEnable(boolean animationEnable) {
        this.animationEnable = animationEnable;
    }

    public void setData(List<DATA> data) {
        this.data = data;
        render();
    }

    public List<DATA> getDatas() {
        return data;
    }

    public List<DATA> getDatas(Filter<DATA> filter) {
        ArrayList<DATA> output = new ArrayList<>();
        for (DATA item : data) {
            if (filter.apply(item)) {
                output.add(item);
            }
        }
        return output;
    }

    public int addFilter(Filter<DATA> filter) {
        filtersIdCounter++;
        filters.put(filtersIdCounter, filter);
        render();
        return filtersIdCounter;
    }

    public boolean hasFilter(int id) {
        return filters.containsKey(id);
    }

    public boolean hasFilter(Filter<DATA> filter) {
        return filters.containsValue(filter);
    }

    public void removeFilter(int id) {
        filters.remove(id);
        render();
    }

    public void removeFilter(Filter<DATA> filter) {
        for (Map.Entry<Integer, Filter<DATA>> entry : filters.entrySet()) {
            if (entry.getValue() == filter) {
                filters.remove(entry.getKey());
                break;
            }
        }

        render();
    }

    public void revertFilter(Filter<DATA> filter) {
        if (hasFilter(filter)) {
            removeFilter(filter);
        } else {
            addFilter(filter);
        }
    }

    protected final List<DATA> applyFilter(List<DATA> input) {
        int len = filters.size();
        if (len > 0) {
            ArrayList<DATA> output = new ArrayList<>();
            for (DATA item : input) {
                dataAdder:
                {
                    for (Filter<DATA> filter : filters.values()) {
                        if (!filter.apply(item)) {
                            break dataAdder;
                        }
                    }
                    output.add(item);
                }
            }
            return output;
        } else {
            return new ArrayList<>(input);
        }
    }

    public final void render() {
        render(false);
    }

    public final void render(Object data) {
        mAdapter.notifyChanged(displayData.indexOf(data));
    }

    public final void render(boolean stopAnimation) {
        render(data, stopAnimation);
    }

    protected List<DATA> processData(List<DATA> source) {
        return applyFilter(source);
    }

    protected final void render(List<DATA> source, boolean stopAnimation) {
        List<DATA> newDisplayData = processData(source);
        if (animationEnable && !stopAnimation) {
            render.render(mAdapter, displayData, newDisplayData, new InnerDataRender.DataRenderHandler() {
                @Override
                public void dataRender(List data) {
                    displayData = data;
                }

                @Override
                public void afterRender() {

                }
            });
        } else {
            displayData = new ArrayList<>(newDisplayData);
            mAdapter.notifyDataSetChanged();
        }
    }

    protected List<DATA> displayData = new ArrayList<>();

    public DATA getTypedData(int position) {
        return displayData.get(position);
    }

    @Override
    public int getDataType(int position) {
        return XGAdapter.TYPE_NORMAL;
    }

    @Override
    public int getDataCount() {
        return displayData.size();
    }

    @Override
    public Object getData(int position) {
        return getTypedData(position);
    }

    @Override
    public void onCreateViewHolder(XGAdapter.ViewHolder holder, int viewType) {

    }

    @Override
    public void onBindViewHolder(XGAdapter.ViewHolder holder, Object data) {

    }

    /**
     * Filter
     */
    public interface Filter<OBJ> {
        boolean apply(OBJ data);
    }

    /**
     * InnerDataRender
     */
    private static class InnerDataRender<DATA> {
        private final static int RENDER_ANIM_DELAY = 500;

        public interface DataRenderHandler {
            void dataRender(List data);

            void afterRender();
        }

        private boolean isRendering = false;
        private Handler mHandler = new Handler();

        public boolean render(final XGAdapter adapter, final List<DATA> before, final List<DATA> after, final
        DataRenderHandler listener) {
            if (isRendering) {
                return false;
            }
            isRendering = true;
            final List<DATA> mixList = new ArrayList<>(before);

            SparseIntArray removedInfo = calculateRemoveList(before, after, mixList);
            final Runnable stepEnd = () -> {
                isRendering = false;
                adapter.notifyDataSetChanged();
                listener.afterRender();
            };

            final Runnable stepAdd = () -> {
                SparseIntArray addedInfo = calculateAddList(before, after, mixList);
                if (addedInfo.size() == 0) {
                    listener.dataRender(after);
                    stepEnd.run();
                } else {
                    listener.dataRender(after);
                    addRender(adapter, addedInfo);
                    mHandler.postDelayed(stepEnd, RENDER_ANIM_DELAY);
                }
            };

            if (removedInfo.size() == 0) {
                stepAdd.run();
            } else {
                listener.dataRender(mixList);
                removeRender(adapter, removedInfo);
                mHandler.postDelayed(stepAdd, RENDER_ANIM_DELAY);
            }

            return true;
        }

        private SparseIntArray calculateRemoveList(List<DATA> before, List<DATA> after, List<DATA> handle) {
            int isLastRemoved = -1;
            SparseIntArray removedInfo = new SparseIntArray();
            int lenBefore = before.size(), lenAfter = after.size(), offsetAfter = 0;
            for (int i = 0; i < lenBefore; i++) {
                DATA item = before.get(i);
                boolean isItInAfter = false;
                for (int j = offsetAfter; j < lenAfter; j++) {
                    if (item.equals(after.get(j))) {
                        isItInAfter = true;
                        offsetAfter = j + 1;
                        break;
                    }
                }
                if (!isItInAfter) {
                    handle.remove(item);
                    if (isLastRemoved >= 0) {
                        removedInfo.put(isLastRemoved, removedInfo.get(isLastRemoved) + 1);
                    } else {
                        isLastRemoved = i;
                        removedInfo.put(isLastRemoved, 1);
                    }
                } else {
                    isLastRemoved = -1;
                }
            }
            return removedInfo;
        }

        private void removeRender(XGAdapter adapter, SparseIntArray removedInfo) {
            int removeCounter = 0;
            for (int i = 0; i < removedInfo.size(); i++) {
                int key = removedInfo.keyAt(i);
                int obj = removedInfo.get(key);
                adapter.notifyRangeRemoved(key + removeCounter, obj);
                removeCounter -= obj;
            }
        }

        private SparseIntArray calculateAddList(List<DATA> before, List<DATA> after, List<DATA> mixList) {
            SparseIntArray addInfo = new SparseIntArray();
            int isLastAdded = -1, lastAddPos = 0, lenMix = mixList.size(), mixOffset = 0;
            for (DATA item : after) {
                boolean isItInAdder = true;
                for (int j = mixOffset; j < lenMix; j++) {
                    if (item.equals(mixList.get(j))) {
                        isLastAdded = -1;
                        mixOffset = j + 1;
                        lastAddPos++;
                        isItInAdder = false;
                        break;
                    }
                }
                if (isItInAdder) {
                    if (isLastAdded < 0) {
                        isLastAdded = lastAddPos;
                        addInfo.put(isLastAdded, 1);
                    } else {
                        addInfo.put(isLastAdded, addInfo.get(isLastAdded) + 1);
                    }
                }
            }
            return addInfo;
        }

        private void addRender(XGAdapter adapter, SparseIntArray addedInfo) {
            int addCounter = 0;
            for (int i = 0; i < addedInfo.size(); i++) {
                int key = addedInfo.keyAt(i);
                int obj = addedInfo.get(key);
                adapter.notifyRangeInserted(key + addCounter, obj);
                addCounter += obj;
            }
        }
    }
}
