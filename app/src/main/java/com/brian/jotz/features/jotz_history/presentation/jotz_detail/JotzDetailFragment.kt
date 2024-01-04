package com.brian.jotz.features.jotz_history.presentation.jotz_detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brian.jotz.R
import com.brian.jotz.data.database.entities.Jotz
import com.brian.jotz.data.utils.getFullDateFromLong
import com.brian.jotz.databinding.FragmentJotzDetailBinding
import com.brian.jotz.features.jotz_history.domain.viewmodel.JotzViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JotzDetailFragment : Fragment() {
    private lateinit var jotzDetailBinding: FragmentJotzDetailBinding
    private val jotzDetailFragmentArgs : JotzDetailFragmentArgs by navArgs()
    private val jotzDetailViewModel : JotzViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        jotzDetailBinding = FragmentJotzDetailBinding.inflate(inflater, container, false)

        return jotzDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectLatestStates()
        jotzDetailViewModel.getSingleJotItem(
            jotzDetailFragmentArgs.userId,
            jotzDetailFragmentArgs.jotId
        )
        jotzDetailBinding.editJotFab.setOnClickListener {
            findNavController().navigate(
                JotzDetailFragmentDirections.actionJotzDetailFragmentToAddJotzFragment(
                    jotzDetailFragmentArgs.userId,
                    jotzDetailFragmentArgs.jotId
                )
            )
        }
    }

    private fun collectLatestStates(){

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                jotzDetailViewModel.jotItemUiState.collect{state ->

                    if (state.singleJotItem != null){
                        val jotItem = state.singleJotItem

                        jotzDetailBinding.jotDetailDate.text =
                            "Date : ${jotItem?.date?.getFullDateFromLong() ?: "N/A"}"
                        jotzDetailBinding.jotDetailTitle.text =
                            "Title : ${jotItem?.title ?: "N/A"}"
                        jotzDetailBinding.jotDetailBody.text =
                            "Body : ${jotItem?.body ?: "N/A"}"
                    }

                }
            }
        }
    }

    //    private var _binding: FragmentJotzDetailBinding? = null
//    private val binding get() = _binding!!
//    private val navigationArgs: JotzDetailFragmentArgs by navArgs()
//    lateinit var jotz: Jotz
//    private val viewModel: JotzViewModel by activityViewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        _binding = FragmentJotzDetailBinding.inflate(inflater, container, false)
//        val view = binding.root
//        binding.deleteJotBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_jotzDetailFragment_to_jotzListFragment)
//        }
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val id = navigationArgs.jotId
//        viewModel.retrieveJotz(id).observe(this.viewLifecycleOwner) { selectedJotz ->
//            jotz = selectedJotz
//            bind(jotz)
//        }
//    }
//
//    private fun bind(jotz: Jotz) {
//        binding.apply {
//            jotDetailTitle.text = jotz.jotzTitle.toString()
//            jotDetailBody.text = jotz.jotzBody.toString()
//            deleteJotBtn.setOnClickListener {
//                showConfirmationDialog()
//            }
//            editJotFab.setOnClickListener {
//                editJotz()
//            }
//        }
//    }
//
//    //delete Jotz Dialog
//    private fun showConfirmationDialog() {
//        MaterialAlertDialogBuilder(requireContext())
//            .setTitle(getString(android.R.string.dialog_alert_title))
//            .setMessage("Are you sure you want to delete Jot?")
//            .setCancelable(false)
//            .setNegativeButton("No") { _, _ -> }
//            .setPositiveButton("Yes") { _, _ ->
//                deleteJotz()
//                Log.d("DetailFrag", "Item Deleted")
//            }
//            .show()
//    }
//
//    //delete current item
//    private fun deleteJotz() {
//        viewModel.deleteJotz(jotz)
//        findNavController().navigateUp()
//    }
//
//    //edit current item(Jotz)
//    private fun editJotz() {
//        val action = JotzDetailFragmentDirections.actionJotzDetailFragmentToAddJotzFragment(
//            getString(R.string.edit_fragment_title), jotz.id
//        )
//        this.findNavController().navigate(action)
//    }
}