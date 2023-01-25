package com.example.numberomposition.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.numberomposition.R
import com.example.numberomposition.databinding.FragmentGameBinding
import com.example.numberomposition.domain.entity.GameResult
import com.example.numberomposition.domain.entity.GameSettings
import com.example.numberomposition.domain.entity.Level

class GameFragment:Fragment() {

    private lateinit var level:Level

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

        binding.textViewInvisibleNumber.setOnClickListener {
            launchResultFragment(
                GameResult(true, 20, 20,
                    GameSettings(20,20,20,30)))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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