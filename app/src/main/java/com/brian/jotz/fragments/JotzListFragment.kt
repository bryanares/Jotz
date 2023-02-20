package com.brian.jotz.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.brian.jotz.*
import com.brian.jotz.databinding.FragmentJotzListBinding

class JotzListFragment : Fragment() {

    private var _binding : FragmentJotzListBinding? = null
    private val binding get() = _binding!!

    private val viewModel : JotzViewModel by activityViewModels {
        JotzViewModelFactory(
            (activity?.application as JotzApplication).database.jotzDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentJotzListBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.addJotFab.setOnClickListener{
            findNavController().navigate(R.id.action_jotzListFragment_to_addJotzFragment)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //navigate to jotzList Fragment
        val adapter = JotzListAdapter{
            val action = JotzListFragmentDirections.actionJotzListFragmentToJotzDetailFragment(it.id)
            this.findNavController().navigate(action)
            Log.d("JotzListfragment", "Item at ${it.id} clicked")
        }
        binding.jotzListRecyclerView.adapter = adapter
        viewModel.allJotz.observe(this.viewLifecycleOwner) { jotz ->
            jotz.let {
                adapter.submitList(it)
            }
        }
        binding.jotzListRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.addJotFab.setOnClickListener{
            val action = JotzListFragmentDirections.actionJotzListFragmentToAddJotzFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)

        }

    }
}