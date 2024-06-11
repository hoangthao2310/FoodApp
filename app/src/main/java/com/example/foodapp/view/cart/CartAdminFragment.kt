package com.example.foodapp.view.cart

import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.CartAdminAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentCartAdminBinding
import com.example.foodapp.model.CartAdmin
import com.example.foodapp.viewmodel.CartViewModel
import com.google.firebase.auth.FirebaseUser

class CartAdminFragment : BaseFragment<FragmentCartAdminBinding>() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdminAdapter: CartAdminAdapter
    private lateinit var firebaseUser: FirebaseUser
    override fun getLayout(container: ViewGroup?): FragmentCartAdminBinding =
        FragmentCartAdminBinding.inflate(layoutInflater, container, false)
    override fun initViews() {
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        firebaseUser = data as FirebaseUser
        cartViewModel.getCartAdmin(firebaseUser.uid)
        cartViewModel.getCartAdmin.observe(viewLifecycleOwner){list ->
            val onItemClickListener = object : OnItemClickListener{
                override fun onItemClick(data: Any?) {
                    val cartAdmin = data as CartAdmin
                    callback.showFragment(CartAdminFragment::class.java, CartFragment::class.java,
                        0, 0, cartAdmin, true)
                }

                override fun onItemAddClick(data: Any?) {}

                override fun onItemEditClick(data: Any?) {}

                override fun onItemDeleteClick(data: Any?) {
                    val cartAdmin = data as CartAdmin
                    cartViewModel.getCartDetail(cartAdmin.adminId.toString())
                    cartViewModel.getCartFirebase.observe(viewLifecycleOwner){
                        for(cartId in it){
                            cartViewModel.deleteCartDetail(cartId.foodId.toString())
                        }
                    }
                    cartViewModel.deleteCartAdmin(cartAdmin.cartAdminId.toString())
                    val position = cartAdminAdapter.listCartAdmin.indexOf(cartAdmin)
                    cartAdminAdapter.removeItem(position)
                }
            }
            cartAdminAdapter = CartAdminAdapter(list, onItemClickListener)
            binding.rcvCartAdmin.adapter = cartAdminAdapter
            binding.progressBar.visibility = View.INVISIBLE
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
                viewHolder: ViewHolder
            ): Int {
                val dragFlags = 0
                val swiperFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swiperFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                target: ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {}

            override fun getSwipeThreshold(viewHolder: ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
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
                viewHolder: ViewHolder
            ){
                super.clearView(recyclerView, viewHolder)

                if(viewHolder.itemView.scrollX > limitScrollX){
                    viewHolder.itemView.scrollTo(limitScrollX, 0)
                }else if(viewHolder.itemView.scrollX < 0){
                    viewHolder.itemView.scrollTo(0,0)
                }
            }
        }).apply {
            attachToRecyclerView(binding.rcvCartAdmin)
        }
    }

    private fun dipToPx(dipValue: Float, context: Context): Int{
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }
}