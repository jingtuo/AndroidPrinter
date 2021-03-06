package com.jingtuo.android.printer.ui.home;

import android.app.Application;
import android.print.PrintAttributes;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.jingtuo.android.printer.data.PrinterDatabase;
import com.jingtuo.android.printer.data.dao.MyUserDao;
import com.jingtuo.android.printer.data.model.MyUserInfo;
import com.jingtuo.android.printer.data.source.MediaSizeSource;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    private MutableLiveData<List<PrintAttributes.MediaSize>> mSystemSizes;

    private MyUserDao myUserDao;

    private final MutableLiveData<List<MyUserInfo>> mMyUserListLiveData = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        mSystemSizes = new MutableLiveData<>();
        Single.create(new MediaSizeSource())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<PrintAttributes.MediaSize>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<PrintAttributes.MediaSize> mediaSizes) {
                        mSystemSizes.setValue(mediaSizes);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
        PrinterDatabase database = Room.databaseBuilder(application.getApplicationContext(), PrinterDatabase.class, "printer")
                .build();
        myUserDao = database.myUserDao();
    }

    public LiveData<List<MyUserInfo>> myUserList() {
        return mMyUserListLiveData;
    }

    public LiveData<List<PrintAttributes.MediaSize>> getSystemSizes() {
        return mSystemSizes;
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