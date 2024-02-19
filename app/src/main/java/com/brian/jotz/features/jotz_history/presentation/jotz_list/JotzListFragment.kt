package com.brian.jotz.features.jotz_history.presentation.jotz_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.brian.jotz.R
import com.brian.jotz.data.local.JotItem
import com.brian.jotz.data.utils.JotItemsAdapter
import com.brian.jotz.data.utils.getFullDateFromLong
import com.brian.jotz.databinding.FragmentJotzListBinding
import com.brian.jotz.features.jotz_history.domain.viewmodel.JotzViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class JotzListFragment : Fragment() {
    private lateinit var jotListFragmentBinding: FragmentJotzListBinding
    private val jotzListFragmentArgs: JotzListFragmentArgs by navArgs()
    private val jotzViewModel: JotzViewModel by viewModels()
    private lateinit var jotzItemsAdapter: JotItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        jotListFragmentBinding =
            FragmentJotzListBinding.inflate(inflater, container, false)
        return jotListFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jotListFragmentBinding.addJotFab.setOnClickListener {
            findNavController().navigate(
                JotzListFragmentDirections.actionJotzListFragmentToAddJotzFragment(
                    jotzListFragmentArgs.userId,
                    null
                )
            )
        }

        jotzItemsAdapter = JotItemsAdapter({ item, view ->
            if (item is JotItem) {
                view.findViewById<TextView>(R.id.jotListTitle).text =
                    item.date?.getFullDateFromLong() ?: "N/A"
                view.setOnClickListener {
                    item.id?.let { it1 ->
                        findNavController().navigate(
                            JotzListFragmentDirections.actionJotzListFragmentToJotzDetailFragment(
                                it1,
                                jotzListFragmentArgs.userId
                            )
                        )
                    }
                }
            }
        }, R.layout.jot_list_item)

        collectLatestStates()
        jotzViewModel.getAllJotItems(jotzListFragmentArgs.userId)
    }


    private fun collectLatestStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                jotzViewModel.jotItemUiState.collect {
                    jotListFragmentBinding.progressBar.isVisible = it.isLoading == true
                    jotListFragmentBinding.noDataFoundText.isVisible =
                        it.isLoading != true && it.jotItemList?.isEmpty() == true
                    if (!it.jotItemList.isNullOrEmpty()) {
                        jotListFragmentBinding.jotzListRecyclerView.layoutManager =
                            LinearLayoutManager(requireActivity())
                        jotListFragmentBinding.jotzListRecyclerView.adapter = jotzItemsAdapter
                        jotzItemsAdapter.setData(it.jotItemList)
                    }

                    Log.d("Added", "Item Added ${it.jotItemList}")
                }
            }
        }
    }
}