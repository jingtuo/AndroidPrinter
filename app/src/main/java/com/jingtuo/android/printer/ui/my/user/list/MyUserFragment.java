package com.jingtuo.android.printer.ui.my.user.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jingtuo.android.printer.R;
import com.jingtuo.android.printer.base.fragment.BaseFragment;
import com.jingtuo.android.printer.data.model.MyUserInfo;
import com.jingtuo.android.printer.ui.my.user.edit.EditUserActivity;

import java.util.List;

public class MyUserFragment extends BaseFragment {

    private MyUserViewModel myUserViewModel;

    private ListView listView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_user;
    }

    @Override
    protected void initView(View view) {
        listView = view.findViewById(R.id.list_view);
        ConstraintLayout clNoData = view.findViewById(R.id.cl_no_data);
        listView.setEmptyView(clNoData);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyUserAdapter adapter = (MyUserAdapter) parent.getAdapter();
                MyUserInfo myUserInfo = adapter.getItem(position);
                Intent intent = new Intent(parent.getContext(), EditUserActivity.class);
                intent.putExtra("id", myUserInfo.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myUserViewModel = ViewModelProviders.of(this).get(MyUserViewModel.class);
        myUserViewModel.myUserList().observe(this, new Observer<List<MyUserInfo>>() {
            @Override
            public void onChanged(List<MyUserInfo> myUserList) {
                listView.setAdapter(new MyUserAdapter(myUserList));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        myUserViewModel.loadMyUserList();
    }
}