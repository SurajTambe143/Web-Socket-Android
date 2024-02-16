package com.example.websocket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.websocket.databinding.InMessageItemBinding
import com.example.websocket.databinding.OutMessageItemBinding

class MessageAdapter() : RecyclerView.Adapter<ViewHolder>() {

    private var listOfChat = mutableListOf<IOMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> InMessageViewHolder(
                InMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            1 -> OutMessageViewHolder(
                OutMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = listOfChat.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder){
            is OutMessageViewHolder -> holder.bind(listOfChat[position])
            is InMessageViewHolder -> holder.bind(listOfChat[position])
        }
    }

    inner class InMessageViewHolder(val binding: InMessageItemBinding) : ViewHolder(binding.root) {
        fun bind(data: IOMessage) {
            binding.itemMessage.text = data.message
        }
    }

    inner class OutMessageViewHolder(private val binding: OutMessageItemBinding) : ViewHolder(binding.root) {
        fun bind(data: IOMessage) {
            binding.itemMessage.text = data.message
        }
    }

    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
        return if (listOfChat[position].messageType) 1 else 0
    }

    fun setData(list: IOMessage) {
        listOfChat.addAll(listOf(list))
        notifyItemInserted(listOfChat.size-1)
    }
}