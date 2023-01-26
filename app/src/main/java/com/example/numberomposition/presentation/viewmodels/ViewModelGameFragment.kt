package com.example.numberomposition.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.numberomposition.data.RepositoryImpl
import com.example.numberomposition.domain.entity.GameSettings
import com.example.numberomposition.domain.entity.Level
import com.example.numberomposition.domain.entity.Question
import com.example.numberomposition.domain.usecases.GenerateQuestionUseCase
import com.example.numberomposition.domain.usecases.GetGameSettingsUseCase
import kotlin.concurrent.thread

class ViewModelGameFragment(var level: Level) : ViewModel() {

    private val repository = RepositoryImpl

    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)

    private var _questionLD = MutableLiveData<Question>()
    val questionLD: LiveData<Question>
        get() = _questionLD

    private var _gameSettingsLD = MutableLiveData<GameSettings>()
    val gameSettingsLD: LiveData<GameSettings>
        get() = _gameSettingsLD

    private var _screenShouldBeFinished = MutableLiveData<Boolean>()
    val screenShouldBeFinished: LiveData<Boolean>
        get() = _screenShouldBeFinished

    private var _timerLD = MutableLiveData<Int>()
    val timerLD: LiveData<Int>
        get() = _timerLD

    private var _counterOfRightAnswers = MutableLiveData<Int>()
    val counterOfRightAnswers:LiveData<Int>
        get() = _counterOfRightAnswers

    private fun getGameSettings(level: Level) {
        _gameSettingsLD.value = getGameSettingsUseCase(level)
    }

    private fun launchTimer() {
        Log.d("lesson", "launchTimer()")

        _screenShouldBeFinished.value = false

        Log.d("lesson", " In Timer _gameSettingsLD.value = ${_gameSettingsLD.value}")
        _gameSettingsLD.value?.let {
            _timerLD.value = it.gameTimeInSeconds
            thread {
                while (_timerLD.value != 1) {
                    Thread.sleep(1000)
                    _timerLD.postValue((_timerLD.value?.toInt()?.minus(1))
                        ?: throw java.lang.RuntimeException("_timerLD.value = ${_timerLD.value}"))
                }
                _screenShouldBeFinished.postValue(true)
            }
        }
    }

    fun generateQuestion(answer:String) {
        _questionLD.value = generateQuestionUseCase(
            _gameSettingsLD.value?.maxSumValue?:
            throw java.lang.RuntimeException("GameFragment 47, viewModelGameFragment.gameSettingsLD.value?.maxSumValue == null"),
            6
        )
    }

    init {
        getGameSettings(level)
        launchTimer()
        generateQuestion()
    }


}

