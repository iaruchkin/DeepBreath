package com.iaruchkin.deepbreath.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.iaruchkin.deepbreath.presentation.view.AboutView;

import androidx.annotation.NonNull;

@InjectViewState
public class AboutPresenter extends MvpPresenter<AboutView> {

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void sendMessage() {
        getViewState().composeEmail();
    }

    public void openLink(@NonNull final String url) {
        getViewState().openURL(url);
    }
}
