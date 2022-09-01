package ru.mozgolom112.todolistyaleto2022.todoitemstracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.mozgolom112.todolistyaleto2022.R
import ru.mozgolom112.todolistyaleto2022.databinding.FragmentToDoItemsTrackerBinding

class ToDoItemsTrackerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentToDoItemsTrackerBinding = initBinding(inflater, container)
        binding.btnHello.setOnClickListener(){
            findNavController().navigate(R.id.actionToDoItemsTrackerFragmentToToDoItemDetailFragment)
        }
        return binding.root
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentToDoItemsTrackerBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_to_do_items_tracker, container, false)

}