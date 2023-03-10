package com.example.numberomposition.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.numberomposition.R
import com.example.numberomposition.databinding.ActivityMainBinding
import com.example.numberomposition.databinding.FragmentWelcomeBinding
import com.example.numberomposition.presentation.fragments.WelcomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}