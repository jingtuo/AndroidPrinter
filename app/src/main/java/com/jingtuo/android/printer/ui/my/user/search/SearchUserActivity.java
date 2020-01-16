package com.jingtuo.android.printer.ui.my.user.search;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jingtuo.android.printer.R;
import com.jingtuo.android.printer.base.activity.BaseActivity;
import com.jingtuo.android.printer.data.model.MyUserInfo;
import com.jingtuo.android.printer.ui.my.user.edit.EditUserActivity;
import com.jingtuo.android.printer.ui.my.user.list.MyUserAdapter;

import java.util.List;

public class SearchUserActivity extends BaseActivity {

    private MyUserAdapter mAdapter;

    private SearchUserViewModel mViewModel;

    private AppCompatEditText etSearch;

    private AppCompatImageButton ibClose;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_user;
    }

    @Override
    protected void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        etSearch = toolbar.findViewById(R.id.et_search);
        ibClose = toolbar.findViewById(R.id.ib_close);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    ibClose.setVisibility(View.GONE);
                } else {
                    ibClose.setVisibility(View.VISIBLE);
                }
                mViewModel.search(s.toString().trim());
            }
        });
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        ListView listView = findViewById(R.id.list_view);
        mAdapter = new MyUserAdapter();
        listView.setAdapter(mAdapter);
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
        mViewModel = ViewModelProviders.of(this).get(SearchUserViewModel.class);
        mViewModel.myUserList().observe(this, new Observer<List<MyUserInfo>>() {
            @Override
            public void onChanged(List<MyUserInfo> myUserList) {
                mAdapter.setData(myUserList);
                mAdapter.notifyDataSetChanged();
            }
        });
        mViewModel.search("");
    }
}
