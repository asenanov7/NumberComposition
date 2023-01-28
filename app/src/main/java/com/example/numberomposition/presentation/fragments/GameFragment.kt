package com.example.numberomposition.presentation.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.numberomposition.R
import com.example.numberomposition.databinding.FragmentGameBinding
import com.example.numberomposition.domain.entity.GameResult
import com.example.numberomposition.domain.entity.GameSettings
import com.example.numberomposition.presentation.viewmodels.ViewModelGameFragment
import com.example.numberomposition.presentation.viewmodels.ViewModelGameFragmentFactory
import kotlin.properties.Delegates

class GameFragment:Fragment() {

    private val args by navArgs<GameFragmentArgs>()
    private lateinit var viewModelGameFragment: ViewModelGameFragment
    private lateinit var gameSettings:GameSettings

    private var minRightAnswers by Delegates.notNull<Int>()
    private var minPercentRightAnswers by Delegates.notNull<Int>()
    private var counterRightAnswers by Delegates.notNull<Int>()
    private var counterPercentRightAnswers by Delegates.notNull<Int>()
    private var countOfQuestions by Delegates.notNull<Int>()

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw Exception("FragmentGameBinding==null")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = ViewModelGameFragmentFactory(args.level)
        viewModelGameFragment = ViewModelProvider(this, viewModelFactory)[ViewModelGameFragment::class.java]

        gameSettings = viewModelGameFragment.gameSettingsLD.value ?:throw Exception("gameSettings == null")
        minPercentRightAnswers = gameSettings.minPercentOfRightAnswers
        minRightAnswers = gameSettings.minCountOfRightAnswers

        setupOptionsOnClickListener()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservers(){
        viewModelGameFragment.timerLD.observe(viewLifecycleOwner) {
            binding.textViewTimer.text = it.toString()
        }

        viewModelGameFragment.questionLD.observe(viewLifecycleOwner) {
            binding.textViewSum.text = it.sum.toString()
            binding.textViewVisibleNumber.text = it.visibleNumber.toString()

            binding.textViewOption1.text = it.options[0].toString()
            binding.textViewOption2.text = it.options[1].toString()
            binding.textViewOption3.text = it.options[2].toString()
            binding.textViewOption4.text = it.options[3].toString()
            binding.textViewOption5.text = it.options[4].toString()
            binding.textViewOption6.text = it.options[5].toString()
        }

        viewModelGameFragment.counterOfQuestionsLD.observe(viewLifecycleOwner){
            countOfQuestions=it
        }

        viewModelGameFragment.counterOfRightAnswersLD.observe(viewLifecycleOwner) {
            counterRightAnswers = it
            val textCounterOfRight = String.format(resources.getString(R.string.rightAnswers),
                it, minRightAnswers)   //%s,%s
            binding.textViewCounterOfRightAnswers.text = textCounterOfRight

            checkEnoughOfRightAnswers()
        }

        viewModelGameFragment.counterOfPercentRightAnswersLD.observe(viewLifecycleOwner){
            counterPercentRightAnswers = it

            val textCounterOfRightPercent = String.format(resources.getString(R.string.rightAnswersPercent),
                it, minPercentRightAnswers) //%s,%s
            binding.textViewPercentOfRightAnswers.text=textCounterOfRightPercent

            checkEnoughOfPercentRightAnswers()

            binding.progressBar.setProgress(it, true)
            binding.progressBar.secondaryProgress = minPercentRightAnswers
        }

        viewModelGameFragment.screenShouldBeFinishedLD.observe(viewLifecycleOwner) {
            if (it) {
                val winner = counterRightAnswers>=minRightAnswers && counterPercentRightAnswers>=minPercentRightAnswers
                launchResultFragment(GameResult(winner, counterRightAnswers, countOfQuestions, gameSettings))
            }
        }
    }

    private fun checkEnoughOfPercentRightAnswers(){
        val colorGreen = ContextCompat.getColor(requireContext(), android.R.color.holo_green_light)
        val colorRed =  ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)

        if (counterPercentRightAnswers>=minPercentRightAnswers){
            binding.textViewPercentOfRightAnswers.setTextColor(colorGreen)
            binding.progressBar.progressTintList = ColorStateList.valueOf(colorGreen)
        }else{
            binding.textViewPercentOfRightAnswers.setTextColor(colorRed)
            binding.progressBar.progressTintList = ColorStateList.valueOf(colorRed)
        }
    }

    private fun checkEnoughOfRightAnswers(){
        val colorGreen = ContextCompat.getColor(requireContext(), android.R.color.holo_green_light)
        val colorRed =  ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)

        if (counterRightAnswers>=minRightAnswers){
            binding.textViewCounterOfRightAnswers.setTextColor(colorGreen)
        }else{
            binding.textViewCounterOfRightAnswers.setTextColor(colorRed)
        }
    }

    private fun setupOptionsOnClickListener(){
        with(binding){
            textViewOption1.setOnClickListener{viewModelGameFragment.answer(textViewOption1.text.toString())}
            textViewOption2.setOnClickListener{viewModelGameFragment.answer(textViewOption2.text.toString())}
            textViewOption3.setOnClickListener{viewModelGameFragment.answer(textViewOption3.text.toString())}
            textViewOption4.setOnClickListener{viewModelGameFragment.answer(textViewOption4.text.toString())}
            textViewOption5.setOnClickListener{viewModelGameFragment.answer(textViewOption5.text.toString())}
            textViewOption6.setOnClickListener{viewModelGameFragment.answer(textViewOption6.text.toString())}
        }
    }

    private fun launchResultFragment(gameResult: GameResult){
        findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameResultFragment(gameResult))
    }

}
