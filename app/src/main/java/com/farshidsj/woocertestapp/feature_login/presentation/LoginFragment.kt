package com.farshidsj.woocertestapp.feature_login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.farshidsj.woocertestapp.R
import com.farshidsj.woocertestapp.databinding.FragmentLoginBinding
import com.farshidsj.woocertestapp.feature_login.domain.model.AuthenticationModel
import com.farshidsj.woocertestapp.core.utils.AppPreferences
import com.farshidsj.woocertestapp.core.utils.Constants
import com.farshidsj.woocertestapp.core.utils.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var appPreferences: AppPreferences

    private var nameInput = ""
    private var emailInput = ""
    private var consumerKeyInput = ""
    private var consumerSecretInput = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        activity?.let {
            appPreferences = AppPreferences(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
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

        GlobalScope.launch(Dispatchers.IO) {
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
                GlobalScope.launch(Dispatchers.IO) {
                    appPreferences.saveAuthForm(
                        authenticationModel = AuthenticationModel(
                            name = nameInput,
                            email = emailInput,
                            consumerKey = consumerKeyInput,
                            consumerSecret = consumerSecretInput
                        )
                    )
                }
                Utils.consumerKey = consumerKeyInput
                Utils.consumerSecret = consumerSecretInput
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

}