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

class ViewModelGameFragment(level: Level) : ViewModel() {

    private val repository = RepositoryImpl

    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)

    private val _questionLD = MutableLiveData<Question>()
    val questionLD: LiveData<Question>
        get() = _questionLD

    private val _gameSettingsLD = MutableLiveData<GameSettings>()
    val gameSettingsLD: LiveData<GameSettings>
        get() = _gameSettingsLD

    private val _screenShouldBeFinishedLD = MutableLiveData<Boolean>()
    val screenShouldBeFinishedLD: LiveData<Boolean>
        get() = _screenShouldBeFinishedLD

    private val _timerLD = MutableLiveData<Int>()
    val timerLD: LiveData<Int>
        get() = _timerLD

    private val _rightAnswerLD = MutableLiveData<Int>()
    val rightAnswerLD:LiveData<Int>
        get() = _rightAnswerLD

    private val _counterOfQuestionsLD = MutableLiveData(0)
    val counterOfQuestionsLD:LiveData<Int>
        get() = _counterOfQuestionsLD

    private val _counterOfRightAnswersLD = MutableLiveData(0)
    val counterOfRightAnswersLD:LiveData<Int>
        get() = _counterOfRightAnswersLD

    private val _counterOfPercentRightAnswersLD = MutableLiveData(0)
    val counterOfPercentRightAnswersLD:LiveData<Int>
        get() = _counterOfPercentRightAnswersLD

    private fun getGameSettings(level: Level) {
        _gameSettingsLD.value = getGameSettingsUseCase(level)
    }

    private fun launchTimer() {
        _screenShouldBeFinishedLD.value = false

        _gameSettingsLD.value?.let {
            _timerLD.value = it.gameTimeInSeconds
            thread {
                while (_timerLD.value != 1) {
                    Log.d("lesson", "launchTimer:${_timerLD.value} ")
                    Thread.sleep(1000)
                    _timerLD.postValue((_timerLD.value?.toInt()?.minus(1))
                        ?: throw java.lang.RuntimeException("_timerLD.value = ${_timerLD.value}"))
                }
                _screenShouldBeFinishedLD.postValue(true)
            }
        }
    }

    private fun generateQuestion() {
        _counterOfQuestionsLD.value = _counterOfQuestionsLD.value?.plus(1)

        _questionLD.value = generateQuestionUseCase(
            _gameSettingsLD.value?.maxSumValue?:
            throw java.lang.RuntimeException("viewModelGameFragment, gameSettingsLD.value?.maxSumValue == null"),
            6
        )
    }

    private fun recalculatePercentageOfCorrectAnswers(){
        val rightAnswers = _counterOfRightAnswersLD.value?.toDouble()?:throw Exception("_counterOfRightAnswersLD.value == null")
        val questions = _counterOfQuestionsLD.value?.toDouble()?:throw Exception("_counterOfQuestionsLD.value == null")
        _counterOfPercentRightAnswersLD.value = (rightAnswers/questions*100).toString().substringBefore(".").toInt()
    }

    fun answer(answer:String){
        //Получение праивльного ответа и установка его в ЛайвДату
        _rightAnswerLD.value = _questionLD.value?.sum?.minus(_questionLD.value?.visibleNumber?:
        throw java.lang.RuntimeException("viewModelGameFragment, _questionLD.value?.visibleNumber == null"))

        //Проверка на правильность ответа от пользователя,
        //если ответ верен, изменить значение количества правильных ответов
        if (_rightAnswerLD.value?.equals(answer.toInt()) == true){
            _counterOfRightAnswersLD.value = _counterOfRightAnswersLD.value?.plus(1)
        }

        //Пересчет процентов правильных ответов, в зависимости от количества вопросов и правильных ответов
        recalculatePercentageOfCorrectAnswers()

        //Создание нового вопроса, увелечение количества вопросов в ЛД на 1,
        // и установка нового вопроса в ЛД с актульным вопросомвопросом
        generateQuestion()
    }

    override fun onCleared() {
        super.onCleared()
        _timerLD.value=1
    }

    init {
        getGameSettings(level)
        launchTimer()
        generateQuestion()
    }

}

