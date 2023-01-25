package com.example.numberomposition.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.numberomposition.R
import com.example.numberomposition.databinding.FragmentWelcomeBinding

class WelcomeFragment:Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding
    get() = _binding ?:throw Exception("FragmentWelcomeBinding == null")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAccept.setOnClickListener {
            launchChooseLevelFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchChooseLevelFragment(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, ChooseLevelFragment.getInstance())
            .addToBackStack(CHOOSE_LEVEL_FRAGMENT)
            .commit()
    }

    companion object{
        const val CHOOSE_LEVEL_FRAGMENT = "ChooseLevelFragment"

        fun getInstance():WelcomeFragment{
            return WelcomeFragment()
        }
    }
}