package com.example.numberomposition.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.numberomposition.R
import com.example.numberomposition.databinding.FragmentGameBinding
import com.example.numberomposition.domain.entity.GameResult
import com.example.numberomposition.domain.entity.GameSettings
import com.example.numberomposition.domain.entity.Level
import com.example.numberomposition.presentation.viewmodels.ViewModelGameFragment
import com.example.numberomposition.presentation.viewmodels.ViewModelGameFragmentFactory
import java.lang.Thread.sleep

class GameFragment:Fragment() {

    private lateinit var level:Level
    private lateinit var viewModelGameFragment:ViewModelGameFragment

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
    get() = _binding?:throw Exception("FragmentGameBinding==null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View  {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = ViewModelGameFragmentFactory(level)
        viewModelGameFragment = ViewModelProvider(this,viewModelFactory)[ViewModelGameFragment::class.java]

        viewModelGameFragment.timerLD.observe(viewLifecycleOwner){
            binding.textViewTimer.text = it.toString()
        }
        viewModelGameFragment.screenShouldBeFinished.observe(viewLifecycleOwner){
            if (it){
                launchResultFragment(GameResult(true,20,20,GameSettings(20,20,20,20)))
            }
        }
        viewModelGameFragment.questionLD.observe(viewLifecycleOwner){
            binding.textViewSum.text = it.sum.toString()
            binding.textViewVisibleNumber.text = it.visibleNumber.toString()

            binding.textViewOption1.text = it.options[0].toString()
            binding.textViewOption2.text = it.options[1].toString()
            binding.textViewOption3.text = it.options[2].toString()
            binding.textViewOption4.text = it.options[3].toString()
            binding.textViewOption5.text = it.options[4].toString()
            binding.textViewOption6.text = it.options[5].toString()
        }

        setupOptionsOnClickListener()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupOptionsOnClickListener(){
        with(binding){
            textViewOption1.setOnClickListener{viewModelGameFragment.generateQuestion()}
            textViewOption2.setOnClickListener{viewModelGameFragment.generateQuestion()}
            textViewOption3.setOnClickListener{viewModelGameFragment.generateQuestion()}
            textViewOption4.setOnClickListener{viewModelGameFragment.generateQuestion()}
            textViewOption5.setOnClickListener{viewModelGameFragment.generateQuestion()}
            textViewOption6.setOnClickListener{viewModelGameFragment.generateQuestion()}
        }
    }

    private fun parseArgs(){
        level = arguments?.getParcelable<Level>(KEY_LEVEL) as Level
    }

    private fun launchResultFragment(gameResult: GameResult){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, GameResultFragment.getInstance(gameResult))
            .addToBackStack(GAME_RESULT_FRAGMENT)
            .commit()
    }

    companion object{
        const val GAME_RESULT_FRAGMENT = "GameResultFragment"

        private const val KEY_LEVEL ="level"

        fun getInstance(level:Level): GameFragment{
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }

}