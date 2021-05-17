package com.iaruchkin.deepbreath.presentation.presenter;

import androidx.annotation.NonNull;

import com.iaruchkin.deepbreath.presentation.view.AboutView;

import moxy.InjectViewState;
import moxy.MvpPresenter;

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
