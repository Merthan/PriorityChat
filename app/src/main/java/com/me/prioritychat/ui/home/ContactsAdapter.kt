package com.me.prioritychat.ui.home

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.adwardstark.mtextdrawable.MaterialTextDrawable
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.me.prioritychat.R
import com.me.prioritychat.databinding.ItemContactCardBinding
import kotlinx.coroutines.NonDisposableHandle.parent

var selectedContact = Contact("Empty","",0,1)

class ContactsAdapter(private val contacts: List<Contact>,val navController: NavController) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactCardBinding.inflate(inflater, parent, false)
        return ContactViewHolder(binding,navController)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
    }

    override fun getItemCount(): Int = contacts.size

    inner class ContactViewHolder(private val binding: ItemContactCardBinding,val navController: NavController) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            // Bind the contact data to the views using View Binding
            binding.tvContactName.text = contact.name
            binding.tvLastMessage.text = contact.lastMessage ?: ""

            val firstTwoLetters = contact.name.take(2).toUpperCase()
            //val colorGenerator = ColorGenerator.MATERIAL // You can use other color generators as well
            //val color = colorGenerator.getColor(contact.name) // Assign a color based on the name


            /*val textDrawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize(32) // Adjust the font size as needed
                .toUpperCase()
                .endConfig()
                .buildRound(firstTwoLetters, color)*/


            binding.root.setOnClickListener {
                navController.navigate(R.id.action_navigation_home_to_chatFragment)
                selectedContact = contact
                //val action = NavigationHomeDirections.actionContactsFragmentToChatFragment(contact.name, contact.lastMessage)
                //itemView.findNavController().navigate(action)

            }

            MaterialTextDrawable.with(binding.root.context)
                .text(firstTwoLetters)
                .into(binding.ivContactImage)


            //binding.ivContactImage.setImageDrawable(textDrawable)

            // Set unread count and visibility based on the value
            if (contact.unreadCount > 0) {
                binding.tvUnreadCount.setImageDrawable(AvatarGenerator.AvatarBuilder(binding.root.context)
                    .setLabel(contact.unreadCount.toString())
                    .setBackgroundColor(Color.GRAY)
                    .setAvatarSize(60)
                    .setTextSize(15)
                    .toCircle()
                    .build())

                binding.tvUnreadCount.visibility = android.view.View.VISIBLE
            } else {
                binding.tvUnreadCount.visibility = android.view.View.GONE
            }
        }
    }
}
/*
data class Contact(
    val name: String,
    //val imageResId: Int,
    var lastMessage: String? = null,
    var lastMessageTime: Long? = null,
    var unreadCount: Int = 0
)*/
data class Contact(
    val name: String,
    val lastMessage: String?,
    val lastMessageTime: Long,
    val unreadCount: Int
)
