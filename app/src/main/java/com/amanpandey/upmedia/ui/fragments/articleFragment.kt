package com.amanpandey.upmedia.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.amanpandey.upmedia.R
import com.amanpandey.upmedia.ui.News
import com.amanpandey.upmedia.ui.newsActivityViewModel
import com.google.android.material.snackbar.Snackbar
import fragments.ArticleFRagmentArgs
import kotlinx.android.synthetic.main.fragment_article.*

class articleFragment : Fragment(R.layout.fragment_article) {
    lateinit var viewModel: newsActivityViewModel
    val args: ArticleFRagmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as News).viewModel

        val article = args.article
        webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        fab.setOnClickListener{
            viewModel.savedArticle(article)
            Snackbar.make(view,"ArticleSaved successfully", Snackbar.LENGTH_SHORT).show()
        }
    }
}