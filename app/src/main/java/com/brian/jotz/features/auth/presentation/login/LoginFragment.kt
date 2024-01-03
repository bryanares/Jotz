package com.brian.jotz.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.brian.jotz.R
import com.brian.jotz.databinding.FragmentLoginBinding
import com.brian.jotz.features.auth.domain.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {


    private lateinit var binding: FragmentLoginBinding
//    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    //call login method from ViewModel to login user, and navigate to home fragment if successful
    // if user doesn't have an account and wants to register, navigate to registration fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectLatestStates()
        binding.loginBt.setOnClickListener {
            authViewModel.login(
                binding.emailLogin.editText?.text.toString(),
                binding.passwordLogin.editText?.text.toString()
            )
        }
        binding.signupTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

    }

    //collect latest states, and navigate to home fragment if successful
    private fun collectLatestStates (){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                authViewModel.authUiState.collectLatest { state ->
                    binding.loginBt.isEnabled = !state.isLoading
                    if (state.isSuccessful) {
                        findNavController().navigate(
                            LoginFragmentDirections.actionLoginFragmentToJotzListFragment(
                                state.userId!!
                            )
                        )
                    }

                    if (state.error != null) {
                        Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                    }
                    authViewModel.resetState()
                }
            }
        }

    }

}