package com.jingtuo.android.printer.ui.home;

import android.print.PrintAttributes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jingtuo.android.printer.data.source.MediaSizeSource;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    private MutableLiveData<List<PrintAttributes.MediaSize>> mSystemSizes;

    public HomeViewModel() {
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
    }

    public LiveData<List<PrintAttributes.MediaSize>> getSystemSizes() {
        return mSystemSizes;
    }
}