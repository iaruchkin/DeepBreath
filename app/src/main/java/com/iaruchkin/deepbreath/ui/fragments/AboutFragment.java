package com.iaruchkin.deepbreath.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.iaruchkin.deepbreath.R;
import com.iaruchkin.deepbreath.common.MvpAppCompatFragment;
import com.iaruchkin.deepbreath.presentation.presenter.AboutPresenter;
import com.iaruchkin.deepbreath.presentation.view.AboutView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.iaruchkin.deepbreath.ui.MainActivity.ABOUT_TAG;

public class AboutFragment extends MvpAppCompatFragment implements AboutView {

    private View mDeveloper;
    private View mGithub;
    private View mAqiLink;
    private View mWeatherLink;
    private View mIconsLink;
    private View mLogoLink;

    @InjectPresenter
    AboutPresenter presenter;

    @ProvidePresenter
    AboutPresenter providePresenter() {
        return new AboutPresenter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        setupToolbar(view);
        findViews(view);
        setupUx();

        return view;
    }

    @Override
    public void openURL(@NonNull String url){
        Intent intent = new Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse(url));

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), getString(R.string.error_no_browser), Toast.LENGTH_SHORT).show();
        }    }

    @Override
    public void composeEmail() {

        Log.i(ABOUT_TAG, "composeEmail");

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(String.format("mailto:%s", getString(R.string.email_adress)))); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT,  getString(R.string.subject_email));
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), R.string.error_no_email_app, Toast.LENGTH_LONG).show();
        }
    }

    private void setupUx(){
        mDeveloper.setOnClickListener(v -> presenter.sendMessage());
        mGithub.setOnClickListener(v -> presenter.openLink(getString(R.string.github_link)));
        mAqiLink.setOnClickListener(v -> presenter.openLink(getString(R.string.aqi_link)));
        mWeatherLink.setOnClickListener(v -> presenter.openLink(getString(R.string.weather_link)));
        mIconsLink.setOnClickListener(v -> presenter.openLink(getString(R.string.icons_link)));
        mLogoLink.setOnClickListener(v -> presenter.openLink(getString(R.string.logo_link)));

    }

    private void setupToolbar(View view) {
        setHasOptionsMenu(true);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getContext()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setTitle(getString(R.string.action_about));
        }
    }

    private void findViews(View view) {
        mDeveloper = view.findViewById(R.id.developer);
        mGithub = view.findViewById(R.id.github);
        mAqiLink = view.findViewById(R.id.aqi_link);
        mWeatherLink = view.findViewById(R.id.weather_link);
        mIconsLink = view.findViewById(R.id.icons_link);
        mLogoLink = view.findViewById(R.id.logo_link);

    }

}

