package com.android.simplechat.ui.fragments.singlechat

import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.simplechat.R
import com.android.simplechat.databinding.FragmentSingleChatBinding
import com.android.simplechat.databinding.ToolbarInfoBinding
import com.android.simplechat.models.Common
import com.android.simplechat.models.User
import com.android.simplechat.ui.fragments.BaseFragment
import com.android.simplechat.utils.*
import com.google.firebase.database.DatabaseReference

class SingleChatFragment(private val common: Common) : BaseFragment(R.layout.fragment_single_chat) {

    override val binding: FragmentSingleChatBinding by viewBinding()

    private lateinit var listenerInfoToolbar : AppValueEventListener
    private lateinit var receivingUser: User
    private lateinit var toolbarInfo: ToolbarInfoBinding
    private lateinit var refUser: DatabaseReference
    private lateinit var adapter: SingleChatAdapter
    private lateinit var refMessages: DatabaseReference
    private lateinit var messagesListener: AppValueEventListener

    private var messages = emptyList<Common>()

    override fun onResume() {
        super.onResume()
        initToolbar()
        initRecycler()
    }

    private fun initToolbar() {
        toolbarInfo = APP_ACTIVITY.binding.toolbarInfo
        toolbarInfo.root.isVisible = true
        listenerInfoToolbar = AppValueEventListener {
            receivingUser = it.getUser()
            initInfoToolbar()
        }

        refUser = DB.child(NODE_USERS).child(common.id)
        refUser.addValueEventListener(listenerInfoToolbar)

        binding.bSendMessage.setOnClickListener {
            val message = binding.chatInputMessage.text.toString()
            if (message.isEmpty()) {
                showToast("Введите сообщение")
            } else sendMessage(message, common.id, TYPE_TEXT) {
                binding.chatInputMessage.setText("")
            }
        }
    }

    private fun initRecycler() {
        adapter = SingleChatAdapter()
        refMessages = DB.child(NODE_MESSAGES)
            .child(UID)
            .child(common.id)
        binding.chatRecycleView.adapter = adapter
        messagesListener = AppValueEventListener { snapshot ->
            messages = snapshot.children.map { it.getCommon() }
            adapter.setList(messages)
            binding.chatRecycleView.smoothScrollToPosition(adapter.itemCount)
        }
        refMessages.addValueEventListener(messagesListener)
    }

    private fun initInfoToolbar() {
        toolbarInfo.contactFullname.text = receivingUser.fullname.ifEmpty { common.fullname }
        toolbarInfo.toolbarImage.loadImage(receivingUser.photoUrl)
        toolbarInfo.contactStatus.text = receivingUser.state
    }

    override fun onPause() {
        super.onPause()
        toolbarInfo.root.isVisible = false
        refUser.removeEventListener(listenerInfoToolbar)
    }
}