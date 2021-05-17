package com.iaruchkin.deepbreath.common;

import androidx.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import moxy.MvpPresenter;
import moxy.MvpView;

public abstract class BasePresenter<View extends MvpView> extends MvpPresenter<View> {

    private CompositeDisposable compositeDisposable;

    protected void disposeOnDestroy(@NonNull Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
    }

    @Override
    public void onDestroy() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
        super.onDestroy();
    }
}
