package com.example.biggly

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.biggly.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var isOPen: Boolean = false
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)


        var handler :Handler = Handler();
       handler.postDelayed(Runnable {
           handler.removeCallbacksAndMessages(this)
//           lifecycleScope.launchWhenResumed {
//               findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//           }

           if(isOPen){
               startActivity(Intent(requireActivity(), MainActivity::class.java))

           }else {
               startActivity(Intent(requireActivity(), LaunchScreens::class.java))
           }

           requireActivity().finish()
        },2000)

        if(requireActivity()!= null) {
            //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            var sharedPreferences =
                requireActivity().getSharedPreferences("sharedpreference", Context.MODE_PRIVATE)

            isOPen = sharedPreferences.getBoolean("IS_OPEN", false);

        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}