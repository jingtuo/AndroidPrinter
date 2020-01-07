package com.jingtuo.android.printer.ui.my.user.list;


import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingtuo.android.printer.R;
import com.jingtuo.android.printer.base.widget.SimpleAdapter;
import com.jingtuo.android.printer.base.widget.ViewHolder;
import com.jingtuo.android.printer.data.model.MyUserInfo;

import java.util.List;

/**
 * 我的用户-Adapter
 *
 * @author JingTuo
 */
public class MyUserAdapter extends SimpleAdapter<MyUserInfo> {

    public MyUserAdapter(List<MyUserInfo> data) {
        super(data);
    }

    @Override
    protected ViewHolder<SimpleAdapter<MyUserInfo>, MyUserInfo> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyUserViewHolder(parent, this, viewType);
    }

    class MyUserViewHolder extends ViewHolder<SimpleAdapter<MyUserInfo>, MyUserInfo> {

        private TextView tvFullName;

        private TextView tvPostcode;

        private TextView tvAddress;

        MyUserViewHolder(ViewGroup parent, SimpleAdapter<MyUserInfo> adapter, int viewType) {
            super(parent, adapter, viewType);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.my_user_item;
        }

        @Override
        protected void initView(View view) {
            super.initView(view);
            tvFullName = view.findViewById(R.id.tv_full_name);
            tvPostcode = view.findViewById(R.id.tv_postcode);
            tvAddress = view.findViewById(R.id.tv_address);
        }

        @Override
        public void setView(int position, MyUserInfo data) {
            super.setView(position, data);
            tvFullName.setText(data.getFullName());
            tvPostcode.setText(data.getPostcode());
            tvAddress.setText(data.getAddress());
        }
    }
}
