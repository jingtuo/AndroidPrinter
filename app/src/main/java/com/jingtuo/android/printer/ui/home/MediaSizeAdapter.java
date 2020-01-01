package com.jingtuo.android.printer.ui.home;

import android.print.PrintAttributes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckedTextView;

import com.jingtuo.android.printer.R;
import com.jingtuo.android.printer.util.PrinterUtils;

import java.util.List;

public class MediaSizeAdapter extends BaseAdapter {

    private List<PrintAttributes.MediaSize> mData;

    public void setData(List<PrintAttributes.MediaSize> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData!=null ? mData.size() : 0;
    }

    @Override
    public PrintAttributes.MediaSize getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MediaSizeViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.media_size_drop_down_item, null);
            viewHolder = new MediaSizeViewHolder(convertView);
        } else {
            viewHolder = (MediaSizeViewHolder)convertView.getTag();
        }
        PrintAttributes.MediaSize mediaSize = mData.get(position);
        viewHolder.setView(mediaSize);
        return convertView;
    }

    class MediaSizeViewHolder {

        private View view;

        private TextView tvName;

        private TextView tvSize;

        public MediaSizeViewHolder(View view) {
            this.view = view;
            this.view.setTag(this);
            initView(view);
        }

        private void initView(View view) {
            tvName = view.findViewById(R.id.tv_name);
            tvSize = view.findViewById(R.id.tv_size);
        }

        public void setView(PrintAttributes.MediaSize item) {
            PrintAttributes.MediaSize mediaSize = item;
            tvName.setText(item.getLabel(tvName.getContext().getPackageManager()));
            tvSize.setText(String.format("%s(mm) x %s(mm)",
                    PrinterUtils.formatNumber(PrinterUtils.convertToMill(item.getWidthMils())),
                    PrinterUtils.formatNumber(PrinterUtils.convertToMill(item.getHeightMils()))));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }
}
