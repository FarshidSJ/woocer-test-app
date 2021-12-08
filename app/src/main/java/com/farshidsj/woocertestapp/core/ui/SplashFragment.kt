package com.farshidsj.woocertestapp.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.farshidsj.woocertestapp.R
import com.farshidsj.woocertestapp.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {

    private lateinit var _binding: FragmentSplashBinding
    private lateinit var _navHostFragment: NavHostFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            delay(1000)
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
        /*_navHostFragment = childFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        _navHostFragment.navController.navigate(R.id.action_splashFragment_to_loginFragment)*/
    }

}