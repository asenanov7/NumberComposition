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
import com.example.numberomposition.presentation.viewmodels.ViewModelGameFragment
import com.example.numberomposition.presentation.viewmodels.ViewModelGameFragmentFactory
import kotlinx.coroutines.internal.artificialFrame

class GameFragment:Fragment() {

    private lateinit var level: Level
    private lateinit var viewModelGameFragment: ViewModelGameFragment

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

        setupOptionsOnClickListener()

        val viewModelFactory = ViewModelGameFragmentFactory(level)
        viewModelGameFragment = ViewModelProvider(this, viewModelFactory)[ViewModelGameFragment::class.java]


        viewModelGameFragment.timerLD.observe(viewLifecycleOwner) {
            binding.textViewTimer.text = it.toString()
        }
        viewModelGameFragment.screenShouldBeFinishedLD.observe(viewLifecycleOwner) {
            if (it) {
                launchResultFragment(GameResult(true,20,202,viewModelGameFragment.gameSettingsLD.value!!))
            }
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
        viewModelGameFragment.counterOfRightAnswersLD.observe(viewLifecycleOwner) {
            val minRightAnswers = viewModelGameFragment.gameSettingsLD.value?.minCountOfRightAnswers
            val textCounterOfRight = String.format(resources.getString(R.string.rightAnswers),
                it, minRightAnswers)   //%s,%s
            binding.textViewCounterOfRightAnswers.text = textCounterOfRight
        }
        viewModelGameFragment.counterOfPercentRightAnswersLD.observe(viewLifecycleOwner){
            val minPercentOfRightAnswers = viewModelGameFragment.gameSettingsLD.value?.minPercentOfRightAnswers
                ?:throw java.lang.RuntimeException("viewModelGameFragment.gameSettingsLD.value?.minPercentOfRightAnswers==null")
            val textCounterOfRightPercent = String.format(resources.getString(R.string.rightAnswersPercent),
            it, minPercentOfRightAnswers) //%s,%s
            binding.textViewPercentOfRightAnswers.text=textCounterOfRightPercent

            val progressPercent = it / (100.toDouble().div(100))
            binding.progressBar.progress = progressPercent.toInt()
            binding.progressBar.secondaryProgress = minPercentOfRightAnswers
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
