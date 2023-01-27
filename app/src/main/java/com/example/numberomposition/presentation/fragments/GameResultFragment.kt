package com.example.numberomposition.presentation.fragments

import android.os.Bundle
import android.text.Editable.Factory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
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

        setDrawable()
        bindingTextViews()

        binding.buttonRepeat.setOnClickListener { retryGame() }

        //Слушатель клика у активити на кнопку назад
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                retryGame()
            }
        })
    }

    private fun setDrawable(){
        val sadDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.icons8_sad_but_relieved_face_96 )
        val smileDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.icons8_smiling_face_with_smiling_eyes_96)

        if (gameResult.winner) {
            binding.imageViewSmile.setImageDrawable(smileDrawable)
        }else {
            binding.imageViewSmile.setImageDrawable(sadDrawable)
        }
    }

    private fun bindingTextViews(){
        val neededPercent = gameResult.gameSettings.minPercentOfRightAnswers.toString()
        binding.textViewNeededPercent.text =
            String.format(getString(R.string.neededPercentOfRightAnswers), neededPercent)

        val neededPoints = gameResult.gameSettings.minCountOfRightAnswers.toString()
        binding.textViewCountOfNeededRightAnswers.text =
            String.format(getString(R.string.neededPoints), neededPoints)

        val percent = (gameResult.countOfRightAnswers.toDouble()/
                (gameResult.countOfQuestions-1).toDouble()*100).toString().substringBefore(".")
        binding.textViewYourPercent.text =
            String.format(getString(R.string.yourPercentOfRightAnswers), percent)

        val points =gameResult.countOfRightAnswers.toString()
        binding.textViewYourPoints.text =
            String.format(getString(R.string.yourPoints), points)
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