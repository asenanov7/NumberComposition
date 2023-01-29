package com.example.numberomposition.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.numberomposition.R
import com.example.numberomposition.databinding.FragmentResultBinding

class GameResultFragment:Fragment() {

    private val args by navArgs<GameResultFragmentArgs>()

    private var _binding:FragmentResultBinding? = null
    private val binding:FragmentResultBinding
    get() = _binding?:throw Exception("FragmentResultBinding==null")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDrawable()
        bindingTextViews()

        binding.buttonRepeat.setOnClickListener { retryGame() }

    }

    private fun setDrawable(){
        if (args.gameResult.winner) {
            binding.imageViewSmile.setImageResource(R.drawable.icons8_smiling_face_with_smiling_eyes_96)
        }else {
            binding.imageViewSmile.setImageResource(R.drawable.icons8_sad_but_relieved_face_96)
        }
    }

    private fun bindingTextViews(){
        val neededPercent = args.gameResult.gameSettings.minPercentOfRightAnswers.toString()
        binding.textViewNeededPercent.text =
            String.format(getString(R.string.neededPercentOfRightAnswers), neededPercent)

        val neededPoints = args.gameResult.gameSettings.minCountOfRightAnswers.toString()
        binding.textViewCountOfNeededRightAnswers.text =
            String.format(getString(R.string.neededPoints), neededPoints)

        val percent = (args.gameResult.countOfRightAnswers.toDouble()/
                (args.gameResult.countOfQuestions-1).toDouble()*100).toString().substringBefore(".")
        binding.textViewYourPercent.text =
            String.format(getString(R.string.yourPercentOfRightAnswers), percent)

        val points = args.gameResult.countOfRightAnswers.toString()
        binding.textViewYourPoints.text =
            String.format(getString(R.string.yourPoints), points)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retryGame(){
        findNavController().popBackStack()
    }

}