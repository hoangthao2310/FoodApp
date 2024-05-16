package com.example.foodapp.view.cart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.OnItemCartClickListener
import com.example.foodapp.adapter.CartAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentCartBinding
import com.example.foodapp.model.Cart
import com.example.foodapp.model.CartAdmin
import com.example.foodapp.viewmodel.CartViewModel

class CartFragment : BaseFragment<FragmentCartBinding>() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cart: Cart
    private lateinit var cartAdmin: CartAdmin
    override fun getLayout(container: ViewGroup?): FragmentCartBinding =
        FragmentCartBinding.inflate(layoutInflater, container, false)

    @SuppressLint("SetTextI18n")
    override fun initViews() {
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        cartAdmin = data as CartAdmin

        binding.layoutButtonIntoMoney.visibility = View.INVISIBLE
        cartViewModel.getCartDetail(cartAdmin.adminId.toString())
        cartViewModel.getCartFirebase.observe(viewLifecycleOwner){listItemCart ->
            if(listItemCart != null){
                binding.layoutButtonIntoMoney.visibility = View.VISIBLE
                val onItemClickListener = object : OnItemCartClickListener{
                    override fun onItemIncreaseClick(data: Any?) {
                        cart = data as Cart
                        cartViewModel.updateQuantity(cart)
                    }

                    override fun onItemReduceClick(data: Any?) {
                        cart = data as Cart
                        cartViewModel.updateQuantity(cart)
                    }

                    override fun onDeleteFood(data: Any?) {
                        val cart = data as Cart
                        cartViewModel.deleteCartDetail(cart.foodId.toString())
                        val position = cartAdapter.listCart.indexOf(cart)
                        cartAdapter.removeItem(position)
                    }
                }

                var intoMoney = 0.0
                for(item in listItemCart){
                    intoMoney += item.intoMoney!!
                    binding.tvTotalPrice.text = intoMoney.toString() + "đ"
                }

                cartAdapter = CartAdapter(listItemCart, onItemClickListener)
                binding.rcvCart.adapter = cartAdapter
                binding.progressBarCart.visibility = View.INVISIBLE
            }
        }

        binding.btnOrder.setOnClickListener {
            callback.showFragment(CartFragment::class.java, OrderFragment::class.java, 0, 0, data, true)
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
            attachToRecyclerView(binding.rcvCart)
        }
    }

    private fun dipToPx(dipValue: Float, context: Context): Int{
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }
}