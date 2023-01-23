package com.example.numberomposition.domain.usecases

import com.example.numberomposition.domain.Repository
import com.example.numberomposition.domain.entity.Question

class GenerateQuestionUseCase(private val repository: Repository) {

    operator fun invoke(maxSumValue:Int, countOfOptions:Int): Question {
        return repository.generateQuestionUseCase(maxSumValue, countOfOptions)
    }

}