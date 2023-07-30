package com.me.prioritychat.ui.home

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.me.prioritychat.R
import com.me.prioritychat.databinding.FragmentHomeBinding

fun Context.toast(message:String?)= Toast.makeText(this,message?:"Empty",Toast.LENGTH_SHORT).show()
// Token: dSvtWRGgQ2KnBVb5bZgoTE:APA91bHTCO1vYL-f4cEYwFnOklzeOvD4PNYXV84EZALSNVPoBIlpgN-wmh5-ldI-xOQ5lM5jLn3FmjycsVABKkbYqUeZDUoe8AUwigjsr7XrGyrLQimCVa9outpAYrgfpu91HYMN0TV4
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            context?.toast("notification permission granted")
        } else {
            context?.toast("notification permission NOT granted")
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        requireContext()?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(it, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    // FCM SDK (and your app) can post notifications.
                } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                    // TODO: display an educational UI explaining to the user the features that will be enabled
                    //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                    //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                    //       If the user selects "No thanks," allow the user to continue without notifications.
                } else {
                    // Directly ask for the permission
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }

    }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it
        }
        askNotificationPermission()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    lateinit var recyclerView : RecyclerView

    private fun setup() {
        recyclerView = binding.recyclerViewContacts
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ContactsAdapter(getContacts(),findNavController())
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("Token", token)
            context?.toast("Token $token")
        })
    }

    private fun getContacts(): List<Contact> {

        val contacts = listOf(
            Contact("Jane Smith", "Regarding the new project proposal...", System.currentTimeMillis(), 3),
            Contact("Chen Li (Chinese -> English)", "Request for financial performance analysis.", System.currentTimeMillis() - 600000, 3),
            Contact("Ahmet Can (Turkish -> English)", "Congratulations on your CEO anniversary!", System.currentTimeMillis() - 660000, 0),
            Contact("Aisha Khan", "Confirmation for the upcoming board meeting.", System.currentTimeMillis() - 10000, 2),
            Contact("Yuki T. (Japanese -> English)", "Sent the invitation to the annual company gala.", System.currentTimeMillis() - 540000, 3),
            Contact("Michael Smith", "Monthly report is ready for review.", System.currentTimeMillis() - 60000, 1),
            Contact("Priya Patel", "Reminder: Important client meeting at 2 PM.", System.currentTimeMillis() - 120000, 1),
            Contact("Robert Wilson", "Congratulations on the successful merger!", System.currentTimeMillis() - 180000, 0),
            Contact("Nhi Ng. (Vietnamese -> English)", "Update: Company's quarterly earnings.", System.currentTimeMillis() - 240000, 2),
            Contact("David Martinez", "Request for a brief meeting tomorrow.", System.currentTimeMillis() - 300000, 0),
            Contact("Sophia Lee", "Update: Progress on the marketing campaign.", System.currentTimeMillis() - 360000, 1),
            Contact("William Clark", "New product launch schedule.", System.currentTimeMillis() - 420000, 0),
            Contact("Lakshmi Iyer", "Reminder: Employee town hall at 4 PM.", System.currentTimeMillis() - 480000, 2),
        )
        return contacts
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}