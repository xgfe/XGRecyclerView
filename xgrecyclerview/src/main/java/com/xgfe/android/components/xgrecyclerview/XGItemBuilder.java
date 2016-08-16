package com.xgfe.android.components.xgrecyclerview;

import com.xgfe.android.components.xgrecyclerview.viewholders.BindViewHolder;

import android.graphics.drawable.Drawable;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoruidong on 16/8/2.
 */
public class XGItemBuilder<DATA, RETUEN> {
    public final static int LAYOUT_TITLE = android.R.layout.simple_list_item_1;
    public final static int LAYOUT_TEXT = R.layout.list_item_text_template;
    public final static int LAYOUT_BUTTON = R.layout.list_item_button_template;
    public final static int VIEW_ID_HOLDER_ROOT = -1;

    public interface OnBinderData<OBJ, V extends View> {
        void bind(V view, OBJ data);
    }

    public interface OnBinderDataBack<OBJ, C> {
        C bind(OBJ data);
    }

    private ArrayList<Pair<Integer, OnBinderData>> binders = new ArrayList<>();

    private XGAdapterBuilder parent;

    private int layout;
    private Integer type = null;

    public XGItemBuilder(XGAdapterBuilder parent, int layout, int type) {
        this.parent = parent;
        this.layout = layout;
        this.type = type;
    }

    XGItemBuilder(XGAdapterBuilder parent, int layout) {
        this.parent = parent;
        this.layout = layout;
    }

    XGItemBuilder(int layout) {
        this.layout = layout;
    }

    public <VV extends View> XGItemBuilder<DATA, RETUEN> bind(int id, OnBinderData<DATA, VV> call) {
        binders.add(Pair.create(id, call));
        return this;
    }

    public <VV extends View> XGItemBuilder<DATA, RETUEN> bind(OnBinderData<DATA, VV> call) {
        binders.add(Pair.create(VIEW_ID_HOLDER_ROOT, call));
        return this;
    }

    public XGItemBuilder<DATA, RETUEN> bindTextView(int id, OnBinderData<DATA, TextView> call) {
        binders.add(Pair.create(id, call));
        return this;
    }

    public XGItemBuilder<DATA, RETUEN> bindImageView(int id, OnBinderData<DATA, ImageView> call) {
        binders.add(Pair.create(id, call));
        return this;
    }

    public XGItemBuilder<DATA, RETUEN> bindText(int id, OnBinderDataBack<DATA, String> call) {
        return bindTextView(id, (view, data) -> view.setText(call.bind(data)));
    }

    public XGItemBuilder<DATA, RETUEN> bindText(OnBinderDataBack<DATA, String> call) {
        return bind((view, data) -> {
            if (view instanceof TextView) ((TextView) view).setText(call.bind(data));
        });
    }

    public XGItemBuilder<DATA, RETUEN> bindImage(int id, OnBinderDataBack<DATA, Drawable> call) {
        return bindImageView(id, (view, data) -> view.setImageDrawable(call.bind(data)));
    }

    public XGItemBuilder<DATA, RETUEN> bindClickListener(int id, OnBinderDataBack<DATA, View.OnClickListener>
            call) {
        return bind(id, (view, data) -> view.setOnClickListener(call.bind(data)));
    }

    public XGItemBuilder<DATA, RETUEN> bindClickListener(OnBinderDataBack<DATA, View.OnClickListener> call) {
        return bind((view, data) -> view.setOnClickListener(call.bind(data)));
    }

    public XGItemBuilder<DATA, RETUEN> bindSelected(int id, OnBinderDataBack<DATA, Boolean> call) {
        return bind(id, (view, data) -> view.setSelected(call.bind(data)));
    }

    public XGItemBuilder<DATA, RETUEN> bindSelected(OnBinderDataBack<DATA, Boolean> call) {
        return bind((view, data) -> view.setSelected(call.bind(data)));
    }

    public RETUEN build() {
        final List<Pair<Integer, OnBinderData>> binders = new ArrayList<>(this.binders);
        XGAdapter.ItemAdapter adapter;
        if (binders.size() != 0) {
            ArrayList<Integer> id = new ArrayList<>();
            for (Pair<Integer, OnBinderData> pair : binders) {
                if (pair.first != VIEW_ID_HOLDER_ROOT && !id.contains(pair.first)) {
                    id.add(pair.first);
                }
            }
            final int[] ids = new int[id.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = id.get(i);
            }
            adapter = new XGAdapter.ItemAdapter<BindViewHolder, Object>() {
                @Override
                public void onBindViewHolder(BindViewHolder holder, Object data) {
                    for (Pair<Integer, OnBinderData> pair : binders) {
                        pair.second.bind(pair.first == VIEW_ID_HOLDER_ROOT ? holder.itemView : holder.get(pair.first)
                                , data);
                    }
                }

                @Override
                public BindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    BindViewHolder holder = new BindViewHolder(getLayoutInflater().inflate(layout, parent, false), ids);
                    return holder;
                }
            };
        } else {
            adapter = new XGAdapter.ItemAdapter() {
                @Override
                public void onBindViewHolder(XGAdapter.ViewHolder holder, Object data) {

                }

                @Override
                public XGAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    BindViewHolder holder = new BindViewHolder(getLayoutInflater().inflate(layout, parent, false));
                    return holder;
                }
            };
        }
        if (parent == null) return (RETUEN) adapter;
        else if (type == null) return (RETUEN) parent.itemAdapter(adapter);
        else return (RETUEN) parent.itemAdapter(adapter, type);
    }


}
