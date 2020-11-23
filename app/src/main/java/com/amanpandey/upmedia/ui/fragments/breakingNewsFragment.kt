package com.amanpandey.upmedia.ui.fragments

import Adapter.newsadapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amanpandey.upmedia.R
import com.amanpandey.upmedia.ui.News
import com.amanpandey.upmedia.ui.newsActivityViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import utilPackage.ConstantUsed.Companion.QUERY_PAGE_SIZE
import utilPackage.ResourceUsed


class breakingNewsFragment : Fragment(R.layout.fragment_breaking_news){

    lateinit var viewModel: newsActivityViewModel
    lateinit var newsAdapter: newsadapter

    val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as News).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFRagment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is ResourceUsed.Success ->{
                    hideProgressBar()
                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPage = newsResponse.totalResults/ QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPage

                        if(isLastPage) {
                            rvBreakingNews.setPadding(0,0,0,0)
                        }

                    }
                }

                is ResourceUsed.Error ->{
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity,"An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }

                is ResourceUsed.Loading ->{
                    showProgessBar()
                }
            }
        })
    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgessBar(){
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastpage = !isLoading && !isLastPage
            val isAlLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val  shouldPaginate = isNotLoadingAndNotLastpage && isAlLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                viewModel.getBreakingNews("id")
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView(){
        newsAdapter = newsadapter()
        rvBreakingNews.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@breakingNewsFragment.scrollListener)
        }
    }
}