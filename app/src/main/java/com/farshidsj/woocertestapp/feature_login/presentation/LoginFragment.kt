package com.farshidsj.woocertestapp.feature_login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.farshidsj.woocertestapp.R
import com.farshidsj.woocertestapp.databinding.FragmentLoginBinding
import com.farshidsj.woocertestapp.feature_login.domain.model.AuthenticationModel
import com.farshidsj.woocertestapp.core.utils.AppPreferences
import com.farshidsj.woocertestapp.core.utils.Constants
import com.farshidsj.woocertestapp.core.utils.Utils
import com.farshidsj.woocertestapp.feature_products.presentation.ProductListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginFragment : Fragment() {

//    private lateinit var binding: FragmentLoginBinding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var appPreferences: AppPreferences
    private val viewModel: LoginViewModel by viewModels()

    private var nameInput = ""
    private var emailInput = ""
    private var consumerKeyInput = ""
    private var consumerSecretInput = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        activity?.let {
            appPreferences = AppPreferences(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupEventObserver()
    }

    private fun setupEventObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { event ->
                when(event) {
                    is LoginViewModel.UIEvent.ShowSnackbar -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkInputs(): Boolean {
        if (nameInput.isEmpty()) {
            Utils.showInputError(binding.tiName, Constants.ERROR_EMPTY_NAME)
            return false
        }

        if (emailInput.isEmpty()) {
            Utils.showInputError(binding.tiEmail, Constants.ERROR_EMPTY_EMAIL)
            return false
        }

        if (consumerKeyInput.isEmpty()) {
            Utils.showInputError(binding.tiConsumerKey, Constants.ERROR_EMPTY_CONSUMER_KEY)
            return false
        }

        if (consumerSecretInput.isEmpty()) {
            Utils.showInputError(binding.tiConsumerSecret, Constants.ERROR_EMPTY_CONSUMER_SECRET)
            return false
        }

        return true
    }

    private fun setupListeners() {
        binding.tiName.editText?.addTextChangedListener {
            nameInput = it.toString()
            Utils.hideInputError(binding.tiName)
        }

        binding.tiEmail.editText?.addTextChangedListener {
            emailInput = it.toString()
            Utils.hideInputError(binding.tiEmail)
        }

        binding.tiConsumerKey.editText?.addTextChangedListener {
            consumerKeyInput = it.toString()
            Utils.hideInputError(binding.tiConsumerKey)
        }

        binding.tiConsumerSecret.editText?.addTextChangedListener {
            consumerSecretInput = it.toString()
            Utils.hideInputError(binding.tiConsumerSecret)
        }
    }

    private fun initViews() {
        setupListeners()

        CoroutineScope(Dispatchers.IO).launch {
            appPreferences.getAuthForm().catch { e ->
                e.printStackTrace()
            }.collect {
                withContext(Dispatchers.Main) {
                    nameInput = it.name
                    binding.tiName.editText?.setText(nameInput)
                    emailInput = it.email
                    binding.tiEmail.editText?.setText(emailInput)
                    consumerKeyInput = it.consumerKey
                    binding.tiConsumerKey.editText?.setText(consumerKeyInput)
                    consumerSecretInput = it.consumerSecret
                    binding.tiConsumerSecret.editText?.setText(consumerSecretInput)
                }
            }
        }

        binding.btnProceed.setOnClickListener {
            if (checkInputs()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val authenticationModel = AuthenticationModel(
                        name = nameInput,
                        email = emailInput,
                        consumerKey = consumerKeyInput,
                        consumerSecret = consumerSecretInput
                    )
                    viewModel.performLogin(
                        authenticationModel,
                        Constants.USER_COLLECTION
                    )
                    appPreferences.saveAuthForm(
                        authenticationModel = authenticationModel
                    )
                }
                findNavController().navigate(
                    R.id.action_loginFragment_to_productListFragment,
                    Bundle().apply {
                        putString(Constants.CONSUMER_KEY, consumerKeyInput)
                        putString(Constants.CONSUMER_SECRET, consumerSecretInput)
                    }
                )

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}