package com.jingtuo.android.printer.ui.my.user.add;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jingtuo.android.printer.data.PrinterDatabase;
import com.jingtuo.android.printer.data.dao.MyUserDao;
import com.jingtuo.android.printer.data.model.MyUserInfo;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddUserViewModel extends AndroidViewModel {

    private static final String TAG = AddUserViewModel.class.getSimpleName();

    private MutableLiveData<Boolean> mSaveResult;

    private MyUserDao myUserDao;

    public AddUserViewModel(@NonNull Application application) {
        super(application);
        mSaveResult = new MutableLiveData<>();
       PrinterDatabase database = Room.databaseBuilder(application.getApplicationContext(), PrinterDatabase.class, "printer")
            .build();
       myUserDao = database.myUserDao();
    }


    public LiveData<Boolean> saveResult(){
        return mSaveResult;
    }

    /**
     *
     * @param fullName
     * @param sex
     * @param mobileNo
     * @param address
     * @param postcode
     */
    public void save(String fullName, String sex, String mobileNo,
                        String address, String postcode) {
        MyUserInfo myUserInfo = new MyUserInfo();
        myUserInfo.setFullName(fullName);
        myUserInfo.setSex(sex);
        myUserInfo.setMobileNo(mobileNo);
        myUserInfo.setAddress(address);
        myUserInfo.setPostcode(postcode);
        myUserDao.insert(myUserInfo).subscribeOn(Schedulers.io())
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
}
