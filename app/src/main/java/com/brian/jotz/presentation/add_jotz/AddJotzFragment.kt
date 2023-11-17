package com.brian.jotz.presentation.add_jotz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brian.jotz.data.database.entities.Jotz
import com.brian.jotz.databinding.FragmentAddJotzBinding
import com.brian.jotz.domain.viewmodels.JotzViewModel
import com.brian.jotz.presentation.jotz_detail.JotzDetailFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddJotzFragment : Fragment() {


    private var _binding: FragmentAddJotzBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JotzViewModel by activityViewModels()

    //    private val viewModel: JotzViewModel by activityViewModels() {
//        JotzViewModelFactory((activity?.application as JotzApplication).database.jotzDao())
//    }
    lateinit var jotz: Jotz
    private val navigationArgs: JotzDetailFragmentArgs by navArgs()


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
        if (isJotzEntryValid()) {
            viewModel.addNewJotz(
                binding.addTitle.text.toString(),
                binding.addBody.text.toString(),
            )
        }
        val action = AddJotzFragmentDirections.actionAddJotzFragmentToJotzListFragment("")
        findNavController().navigate(action)
    }

    /** Edit Jotz Functionality below
     * */

    private fun bind(jotz: Jotz) {
        binding.apply {
            addTitle.setText(jotz.jotzTitle, TextView.BufferType.SPANNABLE)
            addBody.setText(jotz.jotzBody, TextView.BufferType.SPANNABLE)
            addJotBtn.setOnClickListener { updateJotz() }
        }
    }

    private fun updateJotz() {
        if (isJotzEntryValid()) {
            viewModel.updateJotzEntry(
                this.navigationArgs.jotId,
                this.binding.addTitle.text.toString(),
                this.binding.addBody.text.toString()
            )

            val action = AddJotzFragmentDirections.actionAddJotzFragmentToJotzListFragment("")
            findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.jotId

        if (id > 0) {
            viewModel.retrieveJotz(id).observe(this.viewLifecycleOwner) { selectedJotz ->
                jotz = selectedJotz
                bind(jotz)
            }
        } else {
            binding.addJotBtn.setOnClickListener {
                addNewJotz()
            }
        }
    }
}