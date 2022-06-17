package com.android.simplechat.ui.fragments.singlechat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.simplechat.databinding.ItemMessageBinding
import com.android.simplechat.models.Common
import com.android.simplechat.utils.UID
import com.android.simplechat.utils.asTime

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var messages = emptyList<Common>()

    class SingleChatHolder(
        private val binding: ItemMessageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Common) = with(binding) {
            if (message.from == UID) {
                blocUserMessage.isVisible = true
                blocReceivedMessage.isVisible = false
                chatUserMessage.text = message.text
                chatUserMessageTime.text = message.timestamp.toString().asTime()
            } else {
                blocUserMessage.isVisible = false
                blocReceivedMessage.isVisible = true
                chatReceivedMessage.text = message.text
                chatReceivedMessageTime.text = message.timestamp.toString().asTime()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SingleChatHolder(
        ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        holder.bind(messages[position])

    }

    fun setList(list: List<Common>) {
        messages = list
        notifyDataSetChanged()
    }
}
