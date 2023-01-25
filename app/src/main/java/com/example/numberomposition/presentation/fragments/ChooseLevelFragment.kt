package com.example.numberomposition.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.numberomposition.R
import com.example.numberomposition.databinding.FragmentChooseLevelBinding
import com.example.numberomposition.domain.entity.Level

class ChooseLevelFragment:Fragment() {

    private var _binding:FragmentChooseLevelBinding? = null
    private val binding:FragmentChooseLevelBinding
    get() = _binding?:throw Exception("FragmentChooseLevelBinding == null")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            textViewTestLevel.setOnClickListener { launchGameFragment(Level.TEST) }
            textViewEasyLevel.setOnClickListener { launchGameFragment(Level.EASY) }
            textViewNormalLevel.setOnClickListener { launchGameFragment(Level.NORMAL) }
            textViewHardLevel.setOnClickListener { launchGameFragment(Level.HARD) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFragment(level:Level){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, GameFragment.getInstance(level))
            .addToBackStack(GAME_FRAGMENT)
            .commit()
    }

    companion object{
        const val GAME_FRAGMENT = "GameFragment"

        fun getInstance():ChooseLevelFragment{
            return ChooseLevelFragment()
        }
    }
}