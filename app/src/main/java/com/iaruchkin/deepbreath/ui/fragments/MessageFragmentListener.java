package com.iaruchkin.deepbreath.ui.fragments;

public interface MessageFragmentListener {

    void onActionClicked(String fragmentTag);

    void onListClicked(String idF, String idW, String idA, String idC, int viewType);
}
