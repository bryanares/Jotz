package com.brian.jotz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.brian.jotz.JotzApplication
import com.brian.jotz.JotzViewModel
import com.brian.jotz.JotzViewModelFactory
import com.brian.jotz.R
import com.brian.jotz.data.Jotz
import com.brian.jotz.databinding.FragmentAddJotzBinding

class AddJotzFragment : Fragment() {


    private var _binding: FragmentAddJotzBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JotzViewModel by activityViewModels() {
        JotzViewModelFactory((activity?.application as JotzApplication).database.jotzDao())
    }
    lateinit var jotz: Jotz


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddJotzBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    //check if entry is valid
    private fun isJotzEntryValid(): Boolean {
        return viewModel.isJotzEntryValid(
            binding.addTitle.text.toString(),
            binding.addBody.text.toString()
        )
    }

    //add new jotz entry
    private fun addNewJotz() {
        if (isJotzEntryValid()){
            viewModel.addNewJotz(
                binding.addTitle.text.toString(),
                binding.addBody.text.toString(),
            )
        }
        val action = AddJotzFragmentDirections.actionAddJotzFragmentToJotzListFragment()
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addJotBtn.setOnClickListener {
            addNewJotz()
        }
    }
}