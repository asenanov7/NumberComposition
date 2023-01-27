package com.example.numberomposition.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.numberomposition.R
import com.example.numberomposition.databinding.FragmentGameBinding
import com.example.numberomposition.domain.entity.GameResult
import com.example.numberomposition.domain.entity.GameSettings
import com.example.numberomposition.domain.entity.Level
import com.example.numberomposition.presentation.viewmodels.ViewModelGameFragment
import com.example.numberomposition.presentation.viewmodels.ViewModelGameFragmentFactory
import kotlinx.coroutines.internal.artificialFrame
import kotlin.properties.Delegates

class GameFragment:Fragment() {

    private lateinit var level: Level
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = ViewModelGameFragmentFactory(level)
        viewModelGameFragment = ViewModelProvider(this, viewModelFactory)[ViewModelGameFragment::class.java]

        gameSettings = viewModelGameFragment.gameSettingsLD.value ?:throw Exception("gameSettings == null")
        minPercentRightAnswers = gameSettings.minPercentOfRightAnswers
        minRightAnswers = gameSettings.minCountOfRightAnswers

        setupOptionsOnClickListener()
        setupObservers()

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

            if (it>=minRightAnswers){
                binding.textViewCounterOfRightAnswers.setTextColor(
                    ContextCompat.getColor(requireContext(), android.R.color.holo_green_light))
            }else{
                binding.textViewCounterOfRightAnswers.setTextColor(
                    ContextCompat.getColor(requireContext(), android.R.color.holo_red_light))
            }
        }

        viewModelGameFragment.counterOfPercentRightAnswersLD.observe(viewLifecycleOwner){
            counterPercentRightAnswers = it

            val textCounterOfRightPercent = String.format(resources.getString(R.string.rightAnswersPercent),
                it, minPercentRightAnswers) //%s,%s
            binding.textViewPercentOfRightAnswers.text=textCounterOfRightPercent
            if (it>=minPercentRightAnswers){
                binding.textViewPercentOfRightAnswers.setTextColor(
                    ContextCompat.getColor(requireContext(), android.R.color.holo_green_light))
            }else{
                binding.textViewPercentOfRightAnswers.setTextColor(
                    ContextCompat.getColor(requireContext(), android.R.color.holo_red_light))
            }

            val progressPercent = it / (100.toDouble().div(100))
            binding.progressBar.progress = progressPercent.toInt()
            binding.progressBar.secondaryProgress = minPercentRightAnswers
        }

        viewModelGameFragment.screenShouldBeFinishedLD.observe(viewLifecycleOwner) {
            if (it) {
                val winner = counterRightAnswers>=minRightAnswers && counterPercentRightAnswers>=minPercentRightAnswers
                launchResultFragment(GameResult(winner, counterRightAnswers, countOfQuestions, gameSettings))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
