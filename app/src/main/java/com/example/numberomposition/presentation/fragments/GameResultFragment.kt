package com.example.numberomposition.presentation.fragments

import android.os.Bundle
import android.text.Editable.Factory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.numberomposition.R
import com.example.numberomposition.databinding.FragmentResultBinding
import com.example.numberomposition.domain.entity.GameResult
import com.example.numberomposition.presentation.fragments.ChooseLevelFragment.Companion.GAME_FRAGMENT
import com.example.numberomposition.presentation.fragments.WelcomeFragment.Companion.CHOOSE_LEVEL_FRAGMENT

class GameResultFragment:Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding:FragmentResultBinding? = null
    private val binding:FragmentResultBinding
    get() = _binding?:throw Exception("FragmentResultBinding==null")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseArgs()

        binding.buttonRepeat.setOnClickListener {
            retryGame()
        }

        //Слушатель клика у активити на кнопку назад
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                retryGame()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs(){
        gameResult = arguments?.getParcelable<GameResult>(KEY_GAME_RESULT) as GameResult
    }

    private fun retryGame(){
        requireActivity().supportFragmentManager.popBackStack(GAME_FRAGMENT, 1)
    }

    companion object{
        private const val KEY_GAME_RESULT ="gameResult"

        fun getInstance(gameResult: GameResult):GameResultFragment{
            return GameResultFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }

}