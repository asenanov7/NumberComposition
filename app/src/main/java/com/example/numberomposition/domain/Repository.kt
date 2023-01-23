package com.example.numberomposition.domain

import com.example.numberomposition.domain.entity.GameSettings
import com.example.numberomposition.domain.entity.Level
import com.example.numberomposition.domain.entity.Question

interface Repository {
    fun getGameSettings(level: Level): GameSettings

    fun generateQuestionUseCase(maxSumValue:Int, countOfOptions:Int):Question
}