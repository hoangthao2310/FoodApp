package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.databinding.ItemSavedLocationBinding
import com.example.foodapp.model.Location

class LocationAdapter(
    var listLocation: ArrayList<Location>,
    private val itemClick: OnItemClickListener
): RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    inner class LocationViewHolder(private val binding: ItemSavedLocationBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(location: Location){
            binding.tvAddressName.text = location.addressName
            binding.tvAddress.text = location.address
        }

        init {
            binding.layoutItemLocation.setOnClickListener {
                itemClick.onItemClick(listLocation[adapterPosition])
            }
            binding.btnDelete.setOnClickListener {
                itemClick.onItemDeleteClick(listLocation[adapterPosition])
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationAdapter.LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemSavedLocationBinding.inflate(inflater, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationAdapter.LocationViewHolder, position: Int) {
        holder.bind(listLocation[position])
    }

    override fun getItemCount(): Int {
        return listLocation.size
    }
    fun removeItem(position: Int) {
        listLocation.removeAt(position)
        notifyItemRemoved(position)
    }
}