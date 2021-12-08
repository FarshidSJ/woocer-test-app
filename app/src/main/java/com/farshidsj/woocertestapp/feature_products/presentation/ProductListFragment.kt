package com.farshidsj.woocertestapp.feature_products.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.farshidsj.woocertestapp.databinding.FragmentProductListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private lateinit var productListAdapter: ProductListAdapter
    private val viewModel: ProductListViewModel by viewModels()
    private lateinit var args: Bundle
    private var consumerKey = ""
    private var consumerSecret = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductListBinding.inflate(layoutInflater, container, false)
        initArgs()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initAdapter()
        handleList()
    }

    private fun handleList() {
        viewModel.getProducts(consumerKey, consumerSecret)
        lifecycleScope.launchWhenStarted {
            viewModel.products.collect {
                productListAdapter.differ.submitList(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.showLoading.collect {
                showLoading(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is ProductListViewModel.UIEvent.ShowSnackbar -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initArgs() {
        arguments?.let { args = it }
        args.getString("consumer_key")?.let {
            consumerKey = it
        }
        args.getString("consumer_secret")?.let {
            consumerSecret = it
        }
    }

    private fun initViews() {
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initAdapter() {
        productListAdapter = ProductListAdapter()
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = productListAdapter
        }
    }

    private fun showLoading(showLoading: Boolean) {
        if (showLoading) {
            binding.lottieLoading.visibility = View.VISIBLE
        } else {
            binding.lottieLoading.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}