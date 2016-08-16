package com.xgfe.android.components.xgrecyclerview.datasources;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;
import com.xgfe.android.components.xgrecyclerview.viewholders.ButtonViewHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoruidong on 16/7/17.
 */
public class CollapsibleDataSource<DATA> extends SimpleDataSource<DATA> {
    private final static int TYPE_COLLAPSE_BTN = -6999;
    private final static String BTN_TEXT_COLLAPSED = "点击展开%d项数据";
    private final static String BTN_TEXT_UNCOLLAPSED = "点击收起%d项数据";
    private SimpleDataSource.Filter<DATA> collapsedFilter;
    private boolean collapsed = false;
    private int collapsedCount = -1;
    private String btnTextCollapsed = BTN_TEXT_COLLAPSED;
    private String btnTextUncollapsed = BTN_TEXT_UNCOLLAPSED;
    private Button btnView;
    private View.OnClickListener btnListener = v -> revertCollapsed();

    public CollapsibleDataSource(XGAdapter adapter) {
        super(adapter);
    }

    public CollapsibleDataSource(XGAdapter adapter, SimpleDataSource.Filter<DATA> collapsedFilter) {
        super(adapter);
        this.collapsedFilter = collapsedFilter;
    }

    @Override
    protected void afterConstruct(XGAdapter adapter) {
        super.afterConstruct(adapter);
        adapter.addAdapter(new XGAdapter.ItemAdapter() {
            @Override
            public void onBindViewHolder(XGAdapter.ViewHolder holder, Object data) {
            }

            @Override
            public XGAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (btnView != null) return new ButtonViewHolder(btnView);
                return new ButtonViewHolder(getLayoutInflater(), parent);
            }
        }, TYPE_COLLAPSE_BTN);
    }

    public void revertCollapsed() {
        setCollapsed(!isCollapsed());
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
        render();
    }

    public SimpleDataSource.Filter<DATA> getCollapsedFilter() {
        return collapsedFilter;
    }

    public void setCollapsedFilter(SimpleDataSource.Filter<DATA> collapsedFilter) {
        this.collapsedFilter = collapsedFilter;
    }

    public void setButton(Button btnView) {
        this.btnView = btnView;
    }

    public void setButtonText(String btnTextCollapsed, String btnTextUncollapsed) {
        this.btnTextCollapsed = btnTextCollapsed;
        this.btnTextUncollapsed = btnTextUncollapsed;
    }

    @Override
    protected List<DATA> processData(List<DATA> source) {
        List<DATA> com = super.processData(source);
        List<DATA> data = new ArrayList<>(com), add = new ArrayList<>();
        collapsedCount = 0;
        for (DATA item : com) {
            if (collapsedFilter.apply(item)) {
                collapsedCount++;
                data.remove(item);
                add.add(item);
            }
        }
        if (!collapsed) {
            data.addAll(add);
        }
        return data;
    }

    @Override
    public int getDataCount() {
        return super.getDataCount() + 1;
    }

    @Override
    public Object getData(int position) {
        if (position == getDataCount() - 1) {
            return new ButtonViewHolder.ButtonHolderControl(String.format(collapsed ? btnTextCollapsed :
                    btnTextUncollapsed, collapsedCount), true, btnListener);
        }
        return super.getData(position);
    }

    @Override
    public int getDataType(int position) {
        if (position == getDataCount() - 1) return TYPE_COLLAPSE_BTN;
        return super.getDataType(position);
    }
}
