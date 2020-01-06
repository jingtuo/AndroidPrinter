package com.jingtuo.android.printer.ui.my.user;

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

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyUserViewModel extends AndroidViewModel {

    private static final String TAG = MyUserViewModel.class.getSimpleName();

    private final MutableLiveData<List<MyUserInfo>> mMyUserListLiveData = new MutableLiveData<>();

    private MyUserDao myUserDao;

    public MyUserViewModel(@NonNull Application application) {
        super(application);
        PrinterDatabase database = Room.databaseBuilder(application.getApplicationContext(), PrinterDatabase.class, "printer")
            .build();
        myUserDao = database.myUserDao();
    }

    public LiveData<List<MyUserInfo>> myUserList() {
        return mMyUserListLiveData;
    }

    /**
     *
     */
    public void loadMyUserList() {
        myUserDao.queryMyUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<MyUserInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<MyUserInfo> myUserList) {
                        mMyUserListLiveData.setValue(myUserList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                });

    }
}