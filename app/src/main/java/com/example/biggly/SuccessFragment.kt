package com.example.biggly

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.biggly.databinding.FragmentSuccessBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SuccessFragment : Fragment() {

    private var _binding: FragmentSuccessBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSuccessBinding.inflate(inflater, container, false)


        showNotifications();
        binding.fileSent.setOnClickListener{
            //findNavController().popBackStack()
            findNavController().navigate(R.id.action_Success_to_secondFragment)
        }

        return binding.root

    }

    private fun showNotifications() {
        val notifyID = 1
        val CHANNEL_ID = "my_channel_01" // The id of the channel.

        val name: CharSequence =
            getString(R.string.app_name) // The user-visible name of the channel.

        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(CHANNEL_ID, name, importance) as NotificationChannel
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(requireActivity())
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(requireActivity().resources.getString(R.string.app_name))
            .setContentText(requireActivity().resources.getString(R.string.upload_successfully1))
            .setChannelId(CHANNEL_ID)

            .setAutoCancel(true)


        // Add as notification
        val manager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        manager!!.createNotificationChannel(mChannel);
        manager!!.notify(0, builder.build())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}