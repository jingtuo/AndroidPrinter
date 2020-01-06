package com.jingtuo.android.printer.base.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * @author JingTuo
 * @param <A> {@link SimpleAdapter}
 * @param <I>
 */
public class ViewHolder<A, I> {

    private View view;

    private int viewType;

    private int position;

    private int childPosition;

    private I data;

    private A adapter;

    public ViewHolder(ViewGroup parent, A adapter, int viewType) {
        this.adapter = adapter;
        this.view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        this.viewType = viewType;
        this.view.setTag(this);
        initView(view);
    }

    protected int getLayoutId() {
        return android.R.layout.simple_list_item_1;
    }

    protected void initView(View view) {
    }

    public View getView() {
        return view;
    }

    public void setView(int position, I data) {
        this.position = position;
        this.data = data;
    }

    public void setView(int position, int childPosition, I data) {
        this.position = position;
        this.childPosition = childPosition;
        this.data = data;
    }

    public int getPosition() {
        return position;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public I getData() {
        return data;
    }

    public int getViewType() {
        return viewType;
    }

    public A getAdapter() {
        return adapter;
    }


}
