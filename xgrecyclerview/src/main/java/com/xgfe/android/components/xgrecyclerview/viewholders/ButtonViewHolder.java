package com.xgfe.android.components.xgrecyclerview.viewholders;

import com.xgfe.android.components.xgrecyclerview.R;
import com.xgfe.android.components.xgrecyclerview.XGAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by luoruidong on 16/7/14.
 */
public class ButtonViewHolder extends XGAdapter.ViewHolder {
    private Button mButton;

    public ButtonViewHolder(LayoutInflater inflater, ViewGroup parent) {
        this(warp(inflater, parent));
    }

    public ButtonViewHolder(Button view) {
        super(view);
        mButton = view;
    }

    private static Button warp(LayoutInflater inflater, ViewGroup parent) {
        return (Button) inflater.inflate(R.layout.list_item_button_template, parent, false);
        //return new Button(context);
    }

    @Override
    public void onBindViewHolder(int position, Object data) {
        super.onBindViewHolder(position, data);
        if (data instanceof ButtonHolderControl) {
            ButtonHolderControl control = (ButtonHolderControl) data;
            mButton.setText(control.text);
            mButton.setEnabled(control.enable);
            mButton.setOnClickListener(control.listener);
        }
    }

    public static class ButtonHolderControl {
        public String text;
        public boolean enable;
        public View.OnClickListener listener;

        public ButtonHolderControl(String text, boolean enable, View.OnClickListener listener) {
            this.text = text;
            this.enable = enable;
            this.listener = listener;
        }
    }
}
