package com.iaruchkin.deepbreath.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.room.entities.FavoritesEntity


class FavoriteItemAdapter(
        private val mClickHandler: AdapterOnClickHandler?
) :
        RecyclerView.Adapter<FavoriteItemAdapter.FavoritesViewHolder>() {

    //region Private fields


    private val mFavoritesItemList = mutableListOf<FavoritesEntity?>()


    //endregion
    //region Public methods


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val itemListView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_favorites_forecast, parent, false)

        return FavoritesViewHolder(itemListView)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val item = mFavoritesItemList[position]
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return mFavoritesItemList.size
    }

    fun replaceItems(favorites: List<FavoritesEntity?>) {
        mFavoritesItemList.clear()
        mFavoritesItemList.addAll(favorites)
        notifyDataSetChanged()
    }


    //endregion
    //region Inner classes


    inner class FavoritesViewHolder(view: View) : ViewHolder(view), View.OnClickListener {

        //region Private fields


        private val aqiValue: TextView = view.findViewById(R.id.aqi_value)
        private val locationName: TextView = view.findViewById(R.id.location_desc)


        //endregion
        //region Public methods


        fun bind(filterItem: FavoritesEntity) {

            aqiValue.text = filterItem.aqi.toString()
            locationName.text = filterItem.locationName

        }

        override fun onClick(p0: View?) {

        }

        init {

            view.setOnClickListener(this)
        }
    }


    //endregion
    //region Interfaces


    interface AdapterOnClickHandler {
        fun onFilterSelected(filterId: String)
        fun onFilterRemove(filterId: String)
        fun onFilterEdit(pos: Int)
        fun onFilterClearSelection()
    }


    //endregion

}