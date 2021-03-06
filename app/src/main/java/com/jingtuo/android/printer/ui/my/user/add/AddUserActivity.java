package com.jingtuo.android.printer.ui.my.user.add;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.jingtuo.android.printer.R;
import com.jingtuo.android.printer.base.activity.BaseActivity;


/**
 * 添加我的用户
 *
 * @author JingTuo
 */
public class AddUserActivity extends BaseActivity implements View.OnClickListener {

    TextInputEditText mEtFullName;

    RadioGroup mRgSex;

    TextInputEditText mEtMobileNo;

    TextInputEditText mEtAddress;

    TextInputEditText mEtPostcode;

    AddUserViewModel mViewModel;

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

        mViewModel = ViewModelProviders.of(this).get(AddUserViewModel.class);
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
}
