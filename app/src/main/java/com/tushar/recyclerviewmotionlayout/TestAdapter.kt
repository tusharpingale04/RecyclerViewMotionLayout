package com.tushar.recyclerviewmotionlayout

import android.annotation.SuppressLint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView


@SuppressLint("ClickableViewAccessibility")
class TestAdapter(private val list: List<String>, val onClick: (OnClickEvents) -> Unit) :
    RecyclerView.Adapter<TestAdapter.MyViewHolder>() {

    private var lastSlidedItem: View? = null
    private val itemStateArray = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MyViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {

        private var tv: TextView = item.findViewById(R.id.myText)
        private val mute: ImageView = item.findViewById(R.id.mute)
        private val pin: ImageView = item.findViewById(R.id.pin)
        private val delete: ImageView = item.findViewById(R.id.delete)

        fun bind(position: Int) {
            val str = list[position]

            tv.text = str

            if (!itemStateArray.get(position, false)) {
                (item as MotionLayout).transitionToStart()
            } else {
                (item as MotionLayout).transitionToEnd()
            }

            item.addTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionTrigger(
                    p0: MotionLayout?,
                    p1: Int,
                    p2: Boolean,
                    p3: Float
                ) {

                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                    if(lastSlidedItem != null){
                        (lastSlidedItem as MotionLayout).transitionToStart()
                        lastSlidedItem = null
                    }
                }

                override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, p1: Int) {
                    if(lastSlidedItem == null && motionLayout?.progress!! == 1.0f){
                        lastSlidedItem = item
                        itemStateArray.put(position, true)
                    }else{
                        itemStateArray.put(position, false)
                    }
                }

            })

            item.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (item.progress == 0.0f){
                        onClick.invoke(OnClickEvents.OnItemClicked(position))
                    }
                }
                false
            }

            mute.clickWithDebounce {
                item.transitionToStart()
                onClick.invoke(OnClickEvents.MuteClick)
            }

            pin.clickWithDebounce {
                item.transitionToStart()
                onClick.invoke(OnClickEvents.PinClick)
            }

            delete.clickWithDebounce {
                item.transitionToStart()
                onClick.invoke(OnClickEvents.DeleteClick)
            }
        }
    }

    sealed class OnClickEvents {
        object MuteClick : OnClickEvents()
        object PinClick: OnClickEvents()
        object DeleteClick : OnClickEvents()
        data class OnItemClicked(val position: Int) : OnClickEvents()
    }
}