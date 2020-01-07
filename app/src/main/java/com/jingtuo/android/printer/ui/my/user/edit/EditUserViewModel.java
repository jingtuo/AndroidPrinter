package com.jingtuo.android.printer.ui.my.user.edit;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.jingtuo.android.printer.data.PrinterDatabase;
import com.jingtuo.android.printer.data.dao.MyUserDao;
import com.jingtuo.android.printer.data.model.MyUserInfo;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditUserViewModel extends AndroidViewModel {

    private static final String TAG = EditUserViewModel.class.getSimpleName();

    private MutableLiveData<MyUserInfo> mMyUserInfo;

    private MutableLiveData<Boolean> mSaveResult;

    private MutableLiveData<Boolean> mDeleteResult;

    private MyUserDao myUserDao;

    public EditUserViewModel(@NonNull Application application) {
        super(application);
        mMyUserInfo = new MutableLiveData<>();
        mSaveResult = new MutableLiveData<>();
        mDeleteResult = new MutableLiveData<>();
        PrinterDatabase database = Room.databaseBuilder(application.getApplicationContext(), PrinterDatabase.class, "printer")
                .build();
        myUserDao = database.myUserDao();
    }

    public LiveData<MyUserInfo> myUserInfo() {
        return mMyUserInfo;
    }

    public LiveData<Boolean> saveResult() {
        return mSaveResult;
    }

    public LiveData<Boolean> deleteResult() {
        return mDeleteResult;
    }

    public void loadMyUserInfo(long id) {
        myUserDao.queryMyUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<MyUserInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(MyUserInfo myUserInfo) {
                        mMyUserInfo.setValue(myUserInfo);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * @param fullName
     * @param sex
     * @param mobileNo
     * @param address
     * @param postcode
     */
    public void save(String fullName, String sex, String mobileNo,
                     String address, String postcode) {
        MyUserInfo myUserInfo = mMyUserInfo.getValue();
        if (myUserInfo == null) {
            myUserInfo = new MyUserInfo();
        }
        myUserInfo.setFullName(fullName);
        myUserInfo.setSex(sex);
        myUserInfo.setMobileNo(mobileNo);
        myUserInfo.setAddress(address);
        myUserInfo.setPostcode(postcode);
        myUserDao.update(myUserInfo).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        mSaveResult.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage(), e);
                        mSaveResult.setValue(false);
                    }
                });
    }


    public void deleteUser() {
        MyUserInfo myUserInfo = mMyUserInfo.getValue();
        if (myUserInfo == null) {
            mDeleteResult.setValue(false);
            return;
        }
        myUserDao.delete(myUserInfo).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        mDeleteResult.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage(), e);
                        mDeleteResult.setValue(false);
                    }
                });
    }
}
