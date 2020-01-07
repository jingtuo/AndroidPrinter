package com.jingtuo.android.printer.ui.my.user.edit;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.jingtuo.android.printer.R;
import com.jingtuo.android.printer.base.activity.BaseActivity;
import com.jingtuo.android.printer.data.model.MyUserInfo;


/**
 * 添加我的用户
 *
 * @author JingTuo
 */
public class EditUserActivity extends BaseActivity implements View.OnClickListener {

    TextInputEditText mEtFullName;

    RadioGroup mRgSex;

    TextInputEditText mEtMobileNo;

    TextInputEditText mEtAddress;

    TextInputEditText mEtPostcode;

    EditUserViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_user;
    }

    @Override
    protected void initView() {
        super.initView();
        mEtFullName = findViewById(R.id.et_full_name);
        mRgSex = findViewById(R.id.rg_sex);
        mEtMobileNo = findViewById(R.id.et_mobile_no);
        mEtAddress = findViewById(R.id.et_address);
        mEtPostcode = findViewById(R.id.et_postcode);
        findViewById(R.id.btn_save).setOnClickListener(this);

        mViewModel = ViewModelProviders.of(this).get(EditUserViewModel.class);

        mViewModel.saveResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    //保存成功
                    Snackbar.make(mEtFullName, R.string.save_success, Snackbar.LENGTH_SHORT).show();
                    finish();
                } else {
                    Snackbar.make(mEtFullName, R.string.save_failure, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        mViewModel.myUserInfo().observe(this, new Observer<MyUserInfo>() {
            @Override
            public void onChanged(MyUserInfo myUserInfo) {
                mEtFullName.setText(myUserInfo.getFullName());
                if ("1".equals(myUserInfo.getSex())) {
                    mRgSex.check(R.id.rb_sex_men);
                } else {
                    mRgSex.check(R.id.rb_sex_women);
                }
                mEtMobileNo.setText(myUserInfo.getMobileNo());
                mEtAddress.setText(myUserInfo.getAddress());
                mEtPostcode.setText(myUserInfo.getPostcode());
            }
        });

        mViewModel.deleteResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    //删除成功
                    Snackbar.make(mEtFullName, R.string.delete_success, Snackbar.LENGTH_SHORT).show();
                    finish();
                } else {
                    Snackbar.make(mEtFullName, R.string.delete_failure, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);
        mViewModel.loadMyUserInfo(id);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            //保存
            if (mEtFullName.getText() == null || TextUtils.isEmpty(mEtFullName.getText().toString().trim())) {
                Snackbar.make(v, R.string.please_enter_full_name, Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (mEtMobileNo.getText() == null || TextUtils.isEmpty(mEtMobileNo.getText().toString().trim())) {
                Snackbar.make(v, R.string.please_enter_mobile_no, Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (mEtAddress.getText() == null || TextUtils.isEmpty(mEtAddress.getText().toString().trim())) {
                Snackbar.make(v, R.string.please_enter_address, Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (mEtPostcode.getText() == null || TextUtils.isEmpty(mEtPostcode.getText().toString().trim())) {
                Snackbar.make(v, R.string.please_enter_postcode, Snackbar.LENGTH_SHORT).show();
                return;
            }
            mViewModel.save(mEtFullName.getText().toString().trim(),
                    mRgSex.getCheckedRadioButtonId() == R.id.rb_sex_men ? "1" : "2",
                    mEtMobileNo.getText().toString().trim(),
                    mEtAddress.getText().toString().trim(),
                    mEtPostcode.getText().toString().trim());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_user) {
            mViewModel.deleteUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
