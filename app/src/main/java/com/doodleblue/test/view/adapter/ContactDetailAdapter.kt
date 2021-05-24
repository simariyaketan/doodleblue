package com.doodleblue.test.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR

import androidx.recyclerview.widget.RecyclerView
import com.doodleblue.test.R
import com.doodleblue.test.databinding.ItemContactBinding
import com.doodleblue.test.model.ContactDetail

class ContactDetailAdapter : RecyclerView.Adapter<ContactDetailAdapter.ContactDetailViewHolder>(),
    View.OnClickListener {

    private lateinit var contacts: List<ContactDetail>
    private lateinit var binding: ItemContactBinding

    private lateinit var contactAdapterClickListner: ContactAdapterClickListner

    fun setContacts(contacts: List<ContactDetail>) {
        this.contacts = contacts
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactDetailViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()), R.layout.item_contact,
            parent, false
        )
        binding.relContactDetail.setOnClickListener(this)
        return ContactDetailViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: ContactDetailViewHolder, position: Int) {

        holder.onBind(contacts?.get(position), position)
    }

    override fun getItemCount(): Int {
        return contacts?.size ?: 0
    }

    class ContactDetailViewHolder(itemContactBinding: ItemContactBinding) :
        RecyclerView.ViewHolder(itemContactBinding.getRoot()) {
        private val binding: ItemContactBinding
        fun onBind(contact: ContactDetail?, position: Int) {
            binding.relContactDetail.tag = position
            binding.setVariable(BR.contact, contact)
            binding.executePendingBindings()
        }

        init {
            binding = itemContactBinding
        }
    }

    override fun onClick(v: View?) {
        val viewId = v?.id
        val position: Int = v?.tag as Int
        if (viewId == R.id.relContactDetail) {
            contactAdapterClickListner.AddContactToFavorite(position)
        }
    }

    fun setContactAdapterClickListner(contactAdapterClickListner: ContactAdapterClickListner) {
        this.contactAdapterClickListner = contactAdapterClickListner
    }

    interface ContactAdapterClickListner {
        fun AddContactToFavorite(position: Int)
    }
}