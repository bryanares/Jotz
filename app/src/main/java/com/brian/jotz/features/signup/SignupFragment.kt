package com.brian.jotz.features.signup

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
import com.brian.jotz.databinding.FragmentSignupBinding
import com.brian.jotz.domain.viewmodels.JotzViewModel
import com.brian.jotz.features.auth.domain.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var _binding : FragmentSignupBinding
    private val binding get() = _binding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //call signup method from viewmodel to signup user, and navigate to home fragment if successful
        binding.signupBt.setOnClickListener {
            authViewModel.signup(
                binding.emailSignup.editText?.text.toString(),
                binding.passwordSignup.editText?.text.toString(),
                binding.confirmPasswordSignup.editText?.text.toString(),
                binding.nameSignup.editText?.text.toString()
            )
        }
        collectLatestStates()
    }

    //
    private fun collectLatestStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                authViewModel.authUiState.collectLatest { state ->
                    binding.signupBt.isEnabled = !state.isLoading
                    if (state.isSuccessful) {
                        findNavController().navigate(
                            SignupFragmentDirections.actionSignupFragmentToJotzListFragment(
                                state.userId!!
                            )
                        )
                    }
                    state.error?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                    authViewModel.resetState()
                }
            }
        }
    }
}