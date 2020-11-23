package com.amanpandey.upmedia.ui

import NewsRepository.Repository
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.amanpandey.upmedia.R
import db.ArticleDatabase
import kotlinx.android.synthetic.main.activity_main.*

class News : AppCompatActivity() {

    lateinit var viewModel: newsActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newsRepository = Repository(ArticleDatabase(this))
        val viewModelProviderFactory = ProviderFactory(application,newsRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(newsActivityViewModel::class.java)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}