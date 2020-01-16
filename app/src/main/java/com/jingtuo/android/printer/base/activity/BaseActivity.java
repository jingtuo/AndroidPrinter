package com.jingtuo.android.printer.base.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.jingtuo.android.printer.R;

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private AppCompatTextView mTvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initToolBar();
        initView();
    }

    protected abstract int getLayoutId();

    /**
     *
     */
    protected void initToolBar() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mTvTitle = mToolbar.findViewById(R.id.tv_title);
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                if (isTitleCentered()) {
                    //移除ToolBar中自带的标题
                    actionBar.setTitle("");
                }
                actionBar.setDisplayHomeAsUpEnabled(isSecondaryPage());
                actionBar.setDisplayShowHomeEnabled(isSecondaryPage());
            }
        }
    }

    /**
     * 标题是否居中
     * @return
     */
    protected boolean isTitleCentered() {
        return true;
    }

    protected boolean isSecondaryPage() {
        return true;
    }

    protected void initView() {

    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        if (isTitleCentered()) {
            //标题居中，使用自定义标题代替
            if (mTvTitle != null) {
                mTvTitle.setText(title);
            }
            return;
        }
        super.onTitleChanged(title, color);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onClickHome();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onClickHome() {
        finish();
    }
}
