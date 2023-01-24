package com.example.numberomposition.data

import com.example.numberomposition.domain.Repository
import com.example.numberomposition.domain.entity.GameSettings
import com.example.numberomposition.domain.entity.Level
import com.example.numberomposition.domain.entity.Question
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object RepositoryImpl : Repository {
    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_NUMBER = 1

    override fun generateQuestionUseCase(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE,maxSumValue+1) //Рандомное значение от двойки до максимального значения из параметров включительно
        val visibleNumber = Random.nextInt(MIN_ANSWER_NUMBER, sum) //Рандомное значение от 1 до $sum

        val rightAnswer = sum - visibleNumber
        val options = HashSet<Int>()
        options.add(rightAnswer)

        val from = max(rightAnswer-countOfOptions, MIN_ANSWER_NUMBER)
        val to = min(maxSumValue, rightAnswer+countOfOptions)

        while (options.size < countOfOptions){
            options.add(Random.nextInt(from, to))
        }
        return Question(sum,visibleNumber, options.toList())
    }


    override fun getGameSettings(level: Level): GameSettings {
        return when(level){
            Level.TEST -> GameSettings(10,3,50, 8)
            Level.EASY -> GameSettings(10,10, 70,60)
            Level.NORMAL -> GameSettings(20, 20, 80, 40)
            Level.HARD -> GameSettings(30,30,90, 40)
        }
    }
}