package com.iaruchkin.deepbreath.screens.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.network.dtos.findCityDTO.Station
import com.iaruchkin.deepbreath.utils.LocationUtils
import java.util.*


class AutocompleteAdapter(context: Context, items: List<Station>) :
        ArrayAdapter<Station>(
                context,
                R.layout.autocomplete_list_item,
                items
        ) {

    //region Private fields


    private var isActive = true
    private val filter: KNoFilter
    private var mItems = mutableListOf<Station>()


    //endregion
    //region Public methods


    init {
        filter = KNoFilter()
        mItems.addAll(items)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val item = getItem(position)

        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.autocomplete_list_item, parent, false)
        }
        (view?.findViewWithTag<View>("autoCompleteItem") as TextView).text = item?.name
        val distance = (LocationUtils.locationDistance(item?.getCoordinates(), context)/1000)
        (view.findViewWithTag<View>("autoCompleteItemDistance") as TextView).text =
                String.format("%.2f км от вас", distance)

        if (isActive) {
            view.findViewWithTag<View>("autoCompleteItem").visibility = View.VISIBLE
            view.findViewWithTag<View>("nothingFoundImage").visibility = View.GONE
            view.findViewWithTag<View>("nothingFoundText").visibility = View.GONE
        } else {
            view.findViewWithTag<View>("autoCompleteItem").visibility = View.GONE
            view.findViewWithTag<View>("nothingFoundImage").visibility = View.VISIBLE
            view.findViewWithTag<View>("nothingFoundText").visibility = View.VISIBLE
        }

        return view
    }

    override fun getFilter(): Filter {
        return filter
    }

    override fun isEnabled(position: Int): Boolean {
        return isActive
    }


    //endregion
    //region Inner classes


    private inner class KNoFilter : Filter() {

        //region Public methods


        override fun convertResultToString(resultValue: Any): String? {
            return (resultValue as Station).name
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val results = FilterResults()
            if (constraint != null) {
                val suggestions: ArrayList<Station> = ArrayList()
                mItems.forEach {
                    if (it.name?.toLowerCase(Locale.getDefault())
                                    ?.contains(
                                            constraint.toString().toLowerCase(Locale.getDefault())
                                    ) == true
                    ) {
                        suggestions.add(it)
                    }
                }
                results.values = suggestions
                results.count = suggestions.size
            }
            return results
        }

        override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults?
        ) {
            clear()
            isActive = if (results != null && results.count > 0) {
                addAll((results.values as ArrayList<Station>))
                true
            } else {
                addAll(listOf(Station()))
                false
            }
            notifyDataSetChanged()
        }


        //endregion

    }


    //endregion

}