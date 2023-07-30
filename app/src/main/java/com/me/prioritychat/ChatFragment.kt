package com.me.prioritychat

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.me.prioritychat.databinding.FragmentItemListBinding
import com.me.prioritychat.databinding.ItemChatMessageBinding
import com.me.prioritychat.ui.home.Contact
import com.me.prioritychat.ui.home.selectedContact

/**
 * A fragment representing a list of Items.
 */

var dummyCounter = 0

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentItemListBinding
    private val chatAdapter = ChatAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSendMessageButton()
        (requireActivity() as MainActivity).supportActionBar?.title = selectedContact.name
        //(activity as MainActivity).setActionBarTitle(YOUR_TITLE)
        showRatingAlertDialog(view.context, selectedContact)
        chatAdapter.addMessage(ChatMessage("", selectedContact.lastMessage?:"",false))
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = chatAdapter
        val chatMessages = getChatData()
        //TODO: DeepL API calls for translating
        chatAdapter.setChatMessages(chatMessages)
    }

    private fun getChatData(): List<ChatMessage> {
        val messages = mutableListOf<ChatMessage>()
        when(dummyCounter){
            0,2,4,6 ->{
                messages.add(ChatMessage("Alice", "Hey", false))
                messages.add(ChatMessage("Bob", "Hey!", true))
                messages.add(ChatMessage("Alice", "How are you?", false))
                messages.add(ChatMessage("Bob", "I'm good! How about you?", true))
                messages.add(ChatMessage("Alice", "I'm doing well too, thanks!", false))
                messages.add(ChatMessage("Bob", "Great to hear!", true))
            }
            1,3,5,7 ->{
                messages.add(ChatMessage("Alice", "The proposal looks good", false))
                messages.add(ChatMessage("Bob", "Great, starting soon", true))
            }
            else -> {
                messages.add(ChatMessage("Alice", "Second", false))
                messages.add(ChatMessage("Bob", "HelloEND", true))
            }
        }
        dummyCounter++

        // Add more dummy messages as needed
        return messages
    }

    private fun setupSendMessageButton() {
        val sendButton = binding.sendButton
        val messageEditText = binding.messageEditText

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val message = ChatMessage("You", messageText, true)
                chatAdapter.addMessage(message)
                // Clear the message EditText after sending the message
                messageEditText.text.clear()
            }
        }
    }

    fun showRatingAlertDialog(context: Context, contact: Contact) {

        MaterialDialog(context).show {
            title(text = "Training Phase: Rate Priority")
            message(text = "\"${contact.lastMessage}\"")

            positiveButton(text = "Prioritize")
            negativeButton(text = "Ignore")
            neutralButton(text = "Cancel")
            cornerRadius(16f)
        }

        /*val alertDialogBuilder = AlertDialog.Builder(context)

        // Set the title and message for the dialog
        alertDialogBuilder.setTitle("Training Phase")
        alertDialogBuilder.setMessage(contact.lastMessage)

        // Create and set custom views for the title and message (optional)
        val customTitleView = TextView(context)
        customTitleView.text = "Training Phase"
        customTitleView.textSize = 18f
        customTitleView.setTextColor(context.resources.getColor(R.color.black))
        customTitleView.setPadding(16, 16, 16, 8)
        alertDialogBuilder.setCustomTitle(customTitleView)

        val customMessageView = TextView(context)
        customMessageView.text = contact.lastMessage
        customMessageView.textSize = 16f
        customMessageView.setTextColor(context.resources.getColor(R.color.black))
        customMessageView.setPadding(16, 0, 16, 16)
        alertDialogBuilder.setView(customMessageView)

        alertDialogBuilder.setPositiveButton("Prioritize") { dialog, _ ->
            // Handle the "Prioritize" option
            // Add your code here to handle the action
            dialog.dismiss()
        }

        alertDialogBuilder.setNegativeButton("Ignore") { dialog, _ ->
            // Handle the "Ignore" option
            // Add your code here to handle the action
            dialog.dismiss()
        }

        // Set the neutral button and its action
        alertDialogBuilder.setNeutralButton("Cancel") { dialog, _ ->
            // Handle the "Cancel" option
            // Add your code here to handle the action
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()*/
    }


}
data class ChatMessage(val sender: String, val message: String, val isSentByCurrentUser: Boolean)

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private val chatMessages = mutableListOf<ChatMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = chatMessages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = chatMessages.size

    inner class ChatViewHolder(private val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            //binding.senderTextView.text = message.sender
            binding.messageTextView.text = message.message

            val linear = binding.messageFrame
            if (message.isSentByCurrentUser) {
                //binding.senderTextView.visibility = View.GONE
                binding.messageTextView.setBackgroundResource(R.drawable.bg_chat_message_sent)
                //binding.messageFrame.foregroundGravity = Gravity.END
                //binding.messageFrame.gravity = Gravity.END
                linear.gravity = Gravity.END
                linear.foregroundGravity = Gravity.END
                //val params = FrameLayout.LayoutParams()

            } else {
               // binding.senderTextView.visibility = View.VISIBLE
                //binding.root.gravity = Gravity.START
                linear.gravity = Gravity.START
                linear.foregroundGravity = Gravity.START
                binding.messageTextView.setBackgroundResource(R.drawable.bg_chat_message_received)
            }
        }
    }

    fun setChatMessages(messages: List<ChatMessage>) {
        chatMessages.clear()
        chatMessages.addAll(messages)
        notifyDataSetChanged()
    }

    fun addMessage(message: ChatMessage) {
        chatMessages.add(message)
        notifyItemInserted(chatMessages.size - 1)
    }



}