package com.example.foodapp.view.cart

import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.InfoUserAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentLocationOrderBinding
import com.example.foodapp.model.CartAdmin
import com.example.foodapp.model.Location
import com.example.foodapp.viewmodel.AccountViewModel
import com.example.foodapp.viewmodel.CartViewModel

class LocationOrderFragment : BaseFragment<FragmentLocationOrderBinding>() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var infoUserAdapter: InfoUserAdapter
    private lateinit var cartAdmin: CartAdmin
    override fun getLayout(container: ViewGroup?): FragmentLocationOrderBinding =
        FragmentLocationOrderBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        cartAdmin = data as CartAdmin
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]

        accountViewModel.getUserDetail()
        accountViewModel.getUser.observe(viewLifecycleOwner){
            accountViewModel.getLocation(it?.userId.toString())
        }
        accountViewModel.getLocation.observe(viewLifecycleOwner){ listLocation ->
            val onItemClickListener = object : OnItemClickListener{
                override fun onItemClick(data: Any?) {
                    val location = data as Location
                    accountViewModel.updateInfoUserOrder(location)
                    parentFragmentManager.popBackStack()
                }

                override fun onItemAddClick(data: Any?) {
                }

                override fun onItemEditClick(data: Any?) {
                }

                override fun onItemDeleteClick(data: Any?) {
                    val location = data as Location
                    accountViewModel.deleteLocation(location.locationId.toString())
                    val position = infoUserAdapter.listLocation.indexOf(location)
                    infoUserAdapter.removeItem(position)
                }
            }
            infoUserAdapter = InfoUserAdapter(listLocation, onItemClickListener)
            binding.rcvAddress.adapter = infoUserAdapter
            binding.proBarAddress.visibility = View.INVISIBLE
        }

        binding.layoutAddAddress.setOnClickListener {
            callback.showFragment(LocationOrderFragment::class.java, AddInfoUserOrderFragment::class.java, 0 ,0, null, true)
        }

        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }

        setItemTouchHelper()
    }

    private fun setItemTouchHelper(){
        ItemTouchHelper(object : ItemTouchHelper.Callback(){

            // todo, the limit swipe, same as the delete button in item 100dp
            private val limitScrollX = dipToPx(100f, requireContext())
            private var currentScrollX = 0
            private var currentScrollXWhenInActive = 0
            private var initXWhenInActive = 0f
            private var firstInActive = false

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swiperFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swiperFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    if(dX == 0f){
                        currentScrollX = viewHolder.itemView.scrollX
                        firstInActive = true

                    }

                    if(isCurrentlyActive){
                        //swipe with finger

                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if(scrollOffset > limitScrollX){
                            scrollOffset = limitScrollX
                        }else if(scrollOffset < 0){
                            scrollOffset = 0
                        }

                        viewHolder.itemView.scrollTo(scrollOffset, 0)
                    }else{
                        //swipe with auto animation
                        if(firstInActive){
                            firstInActive = false
                            currentScrollXWhenInActive = viewHolder.itemView.scrollX
                            initXWhenInActive = dX
                        }

                        if(viewHolder.itemView.scrollX < limitScrollX){
                            viewHolder.itemView.scrollTo((currentScrollXWhenInActive * dX / initXWhenInActive).toInt(), 0)
                        }
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ){
                super.clearView(recyclerView, viewHolder)

                if(viewHolder.itemView.scrollX > limitScrollX){
                    viewHolder.itemView.scrollTo(limitScrollX, 0)
                }else if(viewHolder.itemView.scrollX < 0){
                    viewHolder.itemView.scrollTo(0,0)
                }
            }
        }).apply {
            attachToRecyclerView(binding.rcvAddress)
        }
    }

    private fun dipToPx(dipValue: Float, context: Context): Int{
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }

}