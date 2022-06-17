package com.android.simplechat.ui.fragments

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.simplechat.R
import com.android.simplechat.databinding.FragmentChatsBinding
import com.android.simplechat.utils.APP_ACTIVITY

class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private val binding: FragmentChatsBinding by viewBinding()

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Чаты"
    }
}