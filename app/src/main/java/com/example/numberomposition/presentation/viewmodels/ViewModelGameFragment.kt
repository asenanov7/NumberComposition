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

    private var _questionLD = MutableLiveData<Question>()
    val questionLD: LiveData<Question>
        get() = _questionLD

    private var _gameSettingsLD = MutableLiveData<GameSettings>()
    val gameSettingsLD: LiveData<GameSettings>
        get() = _gameSettingsLD

    private var _screenShouldBeFinishedLD = MutableLiveData<Boolean>()
    val screenShouldBeFinishedLD: LiveData<Boolean>
        get() = _screenShouldBeFinishedLD

    private var _timerLD = MutableLiveData<Int>()
    val timerLD: LiveData<Int>
        get() = _timerLD

    private var _rightAnswerLD = MutableLiveData<Int>()
    val rightAnswerLD:LiveData<Int>
        get() = _rightAnswerLD

    private var _counterOfQuestionsLD = MutableLiveData(0)
    val counterOfQuestionsLD:LiveData<Int>
        get() = _counterOfQuestionsLD

    private var _counterOfRightAnswersLD = MutableLiveData(0)
    val counterOfRightAnswersLD:LiveData<Int>
        get() = _counterOfRightAnswersLD

    private var _counterOfPercentRightAnswersLD = MutableLiveData(0)
    val counterOfPercentRightAnswersLD:LiveData<Int>
        get() = _counterOfPercentRightAnswersLD

    private fun getGameSettings(level: Level) {
        _gameSettingsLD.value = getGameSettingsUseCase(level)
    }

    private fun launchTimer() {
        Log.d("lesson", "launchTimer()")

        _screenShouldBeFinishedLD.value = false

        Log.d("lesson", " In Timer _gameSettingsLD.value = ${_gameSettingsLD.value}")
        _gameSettingsLD.value?.let {
            _timerLD.value = it.gameTimeInSeconds
            thread {
                while (_timerLD.value != 1) {
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
        Log.d("lesson", "_counterOfQuestions.value = ${_counterOfQuestionsLD.value}")

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

        Log.d("lesson", "_counterOfPercentRightAnswersLD.value: ${_counterOfPercentRightAnswersLD.value}")
    }

    fun answer(answer:String){
        //Получение праивльного ответа и установка его в ЛайвДату
        _rightAnswerLD.value = _questionLD.value?.sum?.minus(_questionLD.value?.visibleNumber?:
        throw java.lang.RuntimeException("viewModelGameFragment, _questionLD.value?.visibleNumber == null"))
        Log.d("lesson", "_rightAnswer.value = ${_rightAnswerLD.value}")

        //Проверка на правильность ответа от пользователя,
        //если ответ верен, изменить значение количества правильных ответов
        if (_rightAnswerLD.value?.equals(answer.toInt()) == true){
            _counterOfRightAnswersLD.value = _counterOfRightAnswersLD.value?.plus(1)
            Log.d("lesson", "_counterOfRightAnswers.value = ${_counterOfRightAnswersLD.value} ")
        }

        //Пересчет процентов правильных ответов, в зависимости от количества вопросов и правильных ответов
        recalculatePercentageOfCorrectAnswers()

        //Создание нового вопроса, увелечение количества вопросов в ЛД на 1,
        // и установка нового вопроса в ЛД с актульным вопросомвопросом
        generateQuestion()
    }

    init {
        getGameSettings(level)
        launchTimer()
        generateQuestion()
    }

}

