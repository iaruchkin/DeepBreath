package com.iaruchkin.deepbreath.screens.adapter

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.room.entities.FavoritesEntity


class FavoriteItemAdapter(
        private val mClickHandler: AdapterOnClickHandler?
) : RecyclerView.Adapter<FavoriteItemAdapter.FavoritesViewHolder>() {

    private val mFavoritesItemList = mutableListOf<FavoritesEntity?>()

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

    inner class FavoritesViewHolder(val view: View) : ViewHolder(view) {

        private val locationName: TextView = view.findViewById(R.id.rLocationDesc)
        private val favoriteButton: ImageView = view.findViewById(R.id.rFavoriteButton)

        fun bind(item: FavoritesEntity) {
            locationName.text = item.locationName
            favoriteButton.setOnClickListener {
                mClickHandler?.onBookmarkRemove(item.id)
            }
            view.setOnClickListener {
                mClickHandler?.onBookmarkOpen(item.getCoordinates())
            }
        }

    }

    interface AdapterOnClickHandler {
        fun onBookmarkOpen(location: Location)
        fun onBookmarkRemove(id: String)
    }

}