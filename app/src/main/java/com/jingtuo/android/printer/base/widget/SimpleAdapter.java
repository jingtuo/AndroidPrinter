package com.jingtuo.android.printer.base.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * <pre>
 *  封装BaseAdapter,增加内容:
 *  1,setData(),getData();
 *  2,ViewHolder
 *  </pre>
 * @author JingTuo
 * @param <I>
 */
public abstract class SimpleAdapter<I> extends BaseAdapter {

    protected List<I> data;

    public SimpleAdapter() {
    }

    public SimpleAdapter(List<I> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public I getItem(int position) {
        return position >= 0 && position < getCount() ? data.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder<SimpleAdapter<I>, I> holder;
        View view;
        int viewType = getViewType(position);
        if (convertView == null) {
            holder = onCreateViewHolder(parent, viewType);
            view = holder.getView();
        } else {
            //noinspection unchecked
            holder = (ViewHolder<SimpleAdapter<I>, I>) convertView.getTag();
            if (viewType == holder.getViewType()) {
                view = convertView;
            } else {
                holder = onCreateViewHolder(parent, viewType);
                view = holder.getView();
            }
        }
        holder.setView(position, getItem(position));
        return view;
    }

    public List<I> getData() {
        return data;
    }

    public void setData(List<I> data) {
        this.data = data;
    }

    protected abstract ViewHolder<SimpleAdapter<I>, I> onCreateViewHolder(ViewGroup parent, int viewType);

    protected int getViewType(int position) {
        return 0;
    }

}
