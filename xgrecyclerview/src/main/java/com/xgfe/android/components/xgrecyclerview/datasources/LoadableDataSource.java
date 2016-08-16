package com.xgfe.android.components.xgrecyclerview.datasources;

import com.xgfe.android.components.xgrecyclerview.XGAdapter;
import com.xgfe.android.components.xgrecyclerview.viewholders.ButtonViewHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

/**
 * Created by luoruidong on 16/7/11.
 */
public class LoadableDataSource<DATA> extends SimpleDataSource<DATA> {
    public final static int TYPE_LOADABLE_BTN = -7999;
    private final static String BTN_TEXT_LOAD = "点击继续加载";
    private final static String BTN_TEXT_END = "";
    private LoadableDataListener loadableDataListener;
    private boolean isLoadable = true;
    private String btnText = BTN_TEXT_LOAD;
    private Button btnView;
    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (loadableDataListener != null) {
                loadableDataListener.onLoad();
            }
        }
    };

    public LoadableDataSource(XGAdapter adapter) {
        super(adapter);
    }

    public LoadableDataSource(XGAdapter adapter, LoadableDataListener loadableDataListener) {
        super(adapter);
        this.loadableDataListener = loadableDataListener;
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
        }, TYPE_LOADABLE_BTN);
    }

    public LoadableDataListener getLoadableDataListener() {
        return loadableDataListener;
    }

    public void setLoadableDataListener(LoadableDataListener loadableDataListener) {
        this.loadableDataListener = loadableDataListener;
    }

    public boolean isLoadable() {
        return isLoadable;
    }

    public void setLoadable(boolean loadable) {
        setLoadableOnly(loadable);
        render(true);
    }

    public void setLoadable(boolean loadable, String message) {
        setLoadableOnly(loadable, message);
        render(true);
    }

    public void setLoadableOnly(boolean loadable) {
        setLoadableOnly(loadable, null);
    }

    public void setLoadableOnly(boolean loadable, String message) {
        isLoadable = loadable;
        if (message == null) {
            btnText = loadable ? BTN_TEXT_LOAD : BTN_TEXT_END;
        } else {
            btnText = message;
        }
    }

    public void setButton(Button btnView) {
        this.btnView = btnView;
    }

    public void addData(List<DATA> list) {
        data.addAll(list);
        render();
    }

    @Override
    public int getDataCount() {
        return super.getDataCount() + (btnText.isEmpty() ? 0 : 1);
    }

    @Override
    public Object getData(int position) {
        if (position == super.getDataCount()) {
            return new ButtonViewHolder.ButtonHolderControl(btnText, isLoadable, btnListener);
        }
        return super.getData(position);
    }

    @Override
    public int getDataType(int position) {
        if (position == super.getDataCount()) return TYPE_LOADABLE_BTN;
        return super.getDataType(position);
    }

    public interface LoadableDataListener {
        void onLoad();
    }
}
