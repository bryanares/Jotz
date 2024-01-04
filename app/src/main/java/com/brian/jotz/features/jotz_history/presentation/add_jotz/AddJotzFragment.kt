package com.brian.jotz.features.jotz_history.presentation.add_jotz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brian.jotz.data.database.entities.Jotz
import com.brian.jotz.data.utils.getFullDateFromLong
import com.brian.jotz.databinding.FragmentAddJotzBinding
import com.brian.jotz.features.jotz_history.domain.viewmodel.JotzViewModel
import com.brian.jotz.features.jotz_history.presentation.jotz_detail.JotzDetailFragmentArgs
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddJotzFragment : Fragment() {
    private lateinit var addJotzBinding: FragmentAddJotzBinding
    private val addJotzFragmentArgs: AddJotzFragmentArgs by navArgs()
    private val jotzViewModel : JotzViewModel by viewModels()

    private var selectedDate : Long? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addJotzBinding = FragmentAddJotzBinding.inflate(inflater, container,false)

        return addJotzBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectStates()
        setViews()
    }

    private fun setViews() {
        addJotzBinding.jotDateEt.editText?.isFocusable = false
        addJotzBinding.jotDateEt.editText?.setOnClickListener {
            val dateValidator : CalendarConstraints.DateValidator =
                DateValidatorPointBackward.before(MaterialDatePicker.todayInUtcMilliseconds())

            val constraintBuilder =
                CalendarConstraints.Builder()
                    .setStart(MaterialDatePicker.todayInUtcMilliseconds())
                    .setValidator(dateValidator)

            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Date")
                    .setCalendarConstraints(constraintBuilder.build())
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.show(parentFragmentManager, "Tag")
            datePicker.addOnPositiveButtonClickListener { date ->

                selectedDate = date
                addJotzBinding.jotDateEt.editText?.setText(date.getFullDateFromLong())
            }
        }

        if (addJotzFragmentArgs.jotId == null){

            addJotzBinding.addJotBtn.setOnClickListener {
                var jotTitle =
                    addJotzBinding.addTitleET.editText?.text.toString()
                var jotBody =
                    addJotzBinding.addJotEt.editText?.text.toString()


                if (jotTitle != null && jotTitle != null && selectedDate != null) {

                    jotzViewModel.addJotItem(
                        addJotzFragmentArgs.userId,
                        selectedDate!!,
                        jotTitle,
                        jotBody
                    )
                }
            }
        }else {
            //update existing record
            addJotzBinding.editOrAddTextView.setText("Edit Jot Enty")
            jotzViewModel.getSingleJotItem(
                addJotzFragmentArgs.userId,
                addJotzFragmentArgs.jotId!!
            )

            addJotzBinding.addJotBtn.setOnClickListener {
                var jotTitle =
                    addJotzBinding.addTitleET.editText?.text.toString()
                var jotBody =
                    addJotzBinding.addTitleET.editText?.text.toString()
                jotzViewModel.updateJotItem(
                    addJotzFragmentArgs.userId,
                    addJotzFragmentArgs.jotId!!,
                    selectedDate,
                    jotTitle,
                    jotBody
                )
            }
        }
    }

    private fun collectStates(){
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                jotzViewModel.jotItemUiState.collect(){

                    if (it.singleJotItem != null){
                        addJotzBinding.jotDateEt.editText?.setText(it.singleJotItem.date?.getFullDateFromLong())
                        addJotzBinding.addTitleET.editText?.setText(it.singleJotItem.title.toString())
                        addJotzBinding.addJotEt.editText?.setText(it.singleJotItem.body.toString())
                    }
                    if (it.createdJotItem != null && addJotzFragmentArgs.jotId == null){
                        jotzViewModel.resetState()
                        findNavController().navigate(
                            AddJotzFragmentDirections.actionAddJotzFragmentToJotzDetailFragment(
                                addJotzFragmentArgs.userId,
                                it.createdJotItem.id!!
                            )
                        )
                    }
                    if (it.updatedJotItem != null && addJotzFragmentArgs.jotId != null){
                        jotzViewModel.resetState()
                        findNavController().navigate(
                            AddJotzFragmentDirections.actionAddJotzFragmentToJotzDetailFragment(
                                addJotzFragmentArgs.userId,
                                it.updatedJotItem.id!!
                            )
                        )
                    }
                }
            }
        }
    }
    //    private var _binding: FragmentAddJotzBinding? = null
//    private val binding get() = _binding!!
//    private val viewModel: JotzViewModel by activityViewModels()
//
//    //    private val viewModel: JotzViewModel by activityViewModels() {
////        JotzViewModelFactory((activity?.application as JotzApplication).database.jotzDao())
////    }
//    lateinit var jotz: Jotz
//    private val navigationArgs: JotzDetailFragmentArgs by navArgs()
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        _binding = FragmentAddJotzBinding.inflate(inflater, container, false)
//        val view = binding.root
//
//        return view
//    }
//
//    //check if entry is valid
//    private fun isJotzEntryValid(): Boolean {
//        return viewModel.isJotzEntryValid(
//            binding.addTitle.text.toString(),
//            binding.addBody.text.toString()
//        )
//    }
//
//    //add new jotz entry
//    private fun addNewJotz() {
//        if (isJotzEntryValid()) {
//            viewModel.addNewJotz(
//                binding.addTitle.text.toString(),
//                binding.addBody.text.toString(),
//            )
//        }
//        val action = AddJotzFragmentDirections.actionAddJotzFragmentToJotzListFragment("")
//        findNavController().navigate(action)
//    }
//
//    /** Edit Jotz Functionality below
//     * */
//
//    private fun bind(jotz: Jotz) {
//        binding.apply {
//            addTitle.setText(jotz.jotzTitle, TextView.BufferType.SPANNABLE)
//            addBody.setText(jotz.jotzBody, TextView.BufferType.SPANNABLE)
//            addJotBtn.setOnClickListener { updateJotz() }
//        }
//    }
//
//    private fun updateJotz() {
//        if (isJotzEntryValid()) {
//            viewModel.updateJotzEntry(
//                this.navigationArgs.jotId,
//                this.binding.addTitle.text.toString(),
//                this.binding.addBody.text.toString()
//            )
//
//            val action = AddJotzFragmentDirections.actionAddJotzFragmentToJotzListFragment("")
//            findNavController().navigate(action)
//        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val id = navigationArgs.jotId
//
//        if (id > 0) {
//            viewModel.retrieveJotz(id).observe(this.viewLifecycleOwner) { selectedJotz ->
//                jotz = selectedJotz
//                bind(jotz)
//            }
//        } else {
//            binding.addJotBtn.setOnClickListener {
//                addNewJotz()
//            }
//        }
//    }
}