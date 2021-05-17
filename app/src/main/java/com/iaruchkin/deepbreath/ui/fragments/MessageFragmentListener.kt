package com.iaruchkin.deepbreath.ui.fragments

interface MessageFragmentListener {

    fun onActionClicked(fragmentTag: String?)
    fun onStationInfoShow(location: List<Double>)
    fun onListClicked(idF: String?, idW: String?, idA: String?, idC: String?, viewType: Int)

}