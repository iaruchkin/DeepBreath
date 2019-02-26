package com.iaruchkin.deepbreath.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.iaruchkin.deepbreath.R;

import androidx.fragment.app.Fragment;
import io.reactivex.disposables.CompositeDisposable;

public class AqiFragment extends Fragment {
    static final String EXTRA_ITEM_URL = "extra:itemURL";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static final String TAG = AqiFragment.class.getSimpleName();
    private MessageFragmentListener listener;

    WebView mWebView;


    public static AqiFragment newInstance(String itemURL){
        AqiFragment fragmentFullNews = new AqiFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ITEM_URL, itemURL);
        fragmentFullNews.setArguments(bundle);
        return fragmentFullNews;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_error, container, false);
//        setView(getArguments().getString(EXTRA_ITEM_URL));

        return view;
    }

    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MessageFragmentListener){
            listener = (MessageFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

     private void setView(String itemURL){

        if (itemURL != null) {
        mWebView.loadUrl(itemURL);
        }
    }
}
