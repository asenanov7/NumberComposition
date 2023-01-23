package com.example.numberomposition.domain.usecases

import com.example.numberomposition.domain.Repository
import com.example.numberomposition.domain.entity.GameSettings
import com.example.numberomposition.domain.entity.Level

class GetGameSettingsUseCase(private val repository: Repository) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }

}