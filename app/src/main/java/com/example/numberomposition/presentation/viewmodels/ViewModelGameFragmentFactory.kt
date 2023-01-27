package com.example.numberomposition.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.numberomposition.domain.entity.Level

class ViewModelGameFragmentFactory(private val level:Level ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ViewModelGameFragment(level) as T
    }
}