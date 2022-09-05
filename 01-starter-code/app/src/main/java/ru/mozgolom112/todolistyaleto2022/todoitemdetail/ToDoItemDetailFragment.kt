package ru.mozgolom112.todolistyaleto2022.todoitemdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.mozgolom112.todolistyaleto2022.R
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabase
import ru.mozgolom112.todolistyaleto2022.databinding.FragmentToDoItemDetailBinding
import ru.mozgolom112.todolistyaleto2022.todoitemstracker.ToDoItemsTrackerFragmentDirections
import ru.mozgolom112.todolistyaleto2022.todoitemstracker.ToDoItemsTrackerViewModelFactory

class ToDoItemDetailFragment : Fragment() {

    private val detailViewModel: ToDoItemDetailViewModel by lazy {
        initDetailViewModel()
    }

    private fun initDetailViewModel(): ToDoItemDetailViewModel {
        val toDoItemId = navArgs<ToDoItemDetailFragmentArgs>().value.toDoItemID
        val application = requireNotNull(this.activity).application
        val datasource = ToDoListDatabase.getInstance(application).toDoListDatabaseDao
        val viewModelFactory = ToDoItemDetailViewModelFactory(toDoItemId, datasource)
        val viewModel: ToDoItemDetailViewModel by viewModels { viewModelFactory }
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentToDoItemDetailBinding = initBinding(inflater, container)
        fulfillBinding(binding)
        setObservers()
        return binding.root
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentToDoItemDetailBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_to_do_item_detail, container, false)

    private fun fulfillBinding(binding: FragmentToDoItemDetailBinding) {

        binding.apply {
            detailViewModel = this@ToDoItemDetailFragment.detailViewModel
            lifecycleOwner = viewLifecycleOwner
            (spinPriority.editText as? AutoCompleteTextView)?.setAdapter(createSpinnerAdapter())

        }
    }

    private fun createSpinnerAdapter() : ArrayAdapter<String> {
        val items = listOf("Нет", "Низкий", "!! Высокий")
        val adapter = ArrayAdapter(requireContext(), R.layout.spin_list_item, items)

        return adapter
    }

    private fun setObservers() {
        detailViewModel.apply {
            navigateToTracker.observe(viewLifecycleOwner, Observer { hasNavigate ->
                    if (hasNavigate == true) {
                        navigateToToDoItemsTracker()
                    }
            })
        }
    }

    private fun navigateToToDoItemsTracker() {
        val action =
            ToDoItemDetailFragmentDirections.actionToDoItemDetailFragmentToToDoItemsTrackerFragment()
        findNavController().navigate(action)
        detailViewModel.doneNavigating()
    }
}