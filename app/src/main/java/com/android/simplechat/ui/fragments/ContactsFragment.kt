package com.android.simplechat.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.simplechat.R
import com.android.simplechat.databinding.FragmentContactsBinding
import com.android.simplechat.databinding.ItemContactBinding
import com.android.simplechat.models.Common
import com.android.simplechat.ui.fragments.singlechat.SingleChatFragment
import com.android.simplechat.utils.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var adapter: FirebaseRecyclerAdapter<Common, ContactViewHolder>
    private lateinit var refContacts: DatabaseReference
    private lateinit var refUsers: DatabaseReference
    private lateinit var refUsersListener: AppValueEventListener
    private var mapListener = hashMapOf<DatabaseReference, AppValueEventListener>()

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Контакты"
        initRecycler()
    }

    private fun initRecycler() {
        refContacts = DB.child(NODE_PHONES_CONTACTS).child(UID)

        val options = FirebaseRecyclerOptions.Builder<Common>()
            .setQuery(refContacts, Common::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<Common, ContactViewHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContactViewHolder(
                ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            override fun onBindViewHolder(
                holder: ContactViewHolder, position: Int, model: Common
            ) {
                refUsers = DB.child(NODE_USERS).child(model.id)
                refUsersListener = AppValueEventListener {
                    val contact = it.getCommon()
                    holder.binding.fullname.text = contact.fullname.ifEmpty { model.fullname }
                    holder.bind(contact)
                    holder.itemView.setOnClickListener { replaceFragment(SingleChatFragment(model)) }
                }
                refUsers.addValueEventListener(refUsersListener)
                mapListener[refUsers] = refUsersListener
            }
        }
        binding.recycler.adapter = adapter
        adapter.startListening()
    }

    override val binding: FragmentContactsBinding by viewBinding()

    class ContactViewHolder(
        var binding: ItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(common: Common) = with(binding) {
            state.text = common.state
            photo.loadImage(common.photoUrl)
        }
    }

    override fun onPause() {
        super.onPause()
        adapter.stopListening()
        mapListener.forEach { it.key.removeEventListener(it.value) }
    }
}