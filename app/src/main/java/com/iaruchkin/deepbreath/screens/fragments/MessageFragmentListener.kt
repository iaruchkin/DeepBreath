package com.iaruchkin.deepbreath.screens.fragments

import android.location.Location

interface MessageFragmentListener {

    fun onActionClicked(fragmentTag: String?)
    fun onStationInfoShow(location: Location)
    fun onListClicked(idF: String?, idW: String?, idA: String?, idC: String?, viewType: Int)

}