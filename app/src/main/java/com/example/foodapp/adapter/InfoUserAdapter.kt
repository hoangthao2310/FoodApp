package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.databinding.ItemInfoUserOrderBinding
import com.example.foodapp.model.Location

class InfoUserAdapter(
    var listLocation: ArrayList<Location>,
    private val itemClick: OnItemClickListener
): RecyclerView.Adapter<InfoUserAdapter.InfoUserViewHolder>() {

    inner class InfoUserViewHolder(private val binding: ItemInfoUserOrderBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(location: Location){
            binding.tvAddressName.text = location.addressName
            binding.tvAddress.text = location.address
            binding.tvUserName.text = location.contactPersonName
            binding.tvPhoneNumber.text = location.contactPhoneNumber
        }

        init {
            binding.layoutItemLocation.setOnClickListener {
                itemClick.onItemClick(listLocation[adapterPosition])
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InfoUserAdapter.InfoUserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemInfoUserOrderBinding.inflate(inflater, parent, false)
        return InfoUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoUserAdapter.InfoUserViewHolder, position: Int) {
        holder.bind(listLocation[position])
    }

    override fun getItemCount(): Int {
        return listLocation.size
    }
}