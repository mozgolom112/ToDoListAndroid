package ru.mozgolom112.todolistyaleto2022.todoitemstracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ru.mozgolom112.todolistyaleto2022.R
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabase
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabaseDao
import ru.mozgolom112.todolistyaleto2022.databinding.FragmentToDoItemsTrackerBinding
import java.util.*
const val EMPTY_ID = ""
class ToDoItemsTrackerFragment : Fragment() {

    private val toDoItemsTrackerViewModel: ToDoItemsTrackerViewModel by lazy {
        Log.i("TrackerFragment", "initTrackerViewModel")
        initTrackerViewModel()

    }

    private fun initTrackerViewModel(): ToDoItemsTrackerViewModel {
        val application = requireNotNull(this.activity).application
        Log.i("TrackerFragment", "application")
        val dataSource: ToDoListDatabaseDao = ToDoListDatabase.getInstance(application).toDoListDatabaseDao
        Log.i("TrackerFragment", "dataSource")
        val viewModelFactory = ToDoItemsTrackerViewModelFactory(dataSource, application)
        Log.i("TrackerFragment", "viewModelFactory")
        //Simple
        //return ViewModelProvider(this, viewModelFactory).get(ToDoItemsTrackerViewModel::class.java)

        //Cool
        val trackerViewModel: ToDoItemsTrackerViewModel by viewModels { viewModelFactory }
        Log.i("TrackerFragment", "finshedinitTrackerViewModel")
        return trackerViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentToDoItemsTrackerBinding = initBinding(inflater, container)
        fulfillBinding(binding)
        setObservers()
        return binding.root
    }

    private fun fulfillBinding(binding: FragmentToDoItemsTrackerBinding) {
        binding.apply {
            trackerViewModel = this@ToDoItemsTrackerFragment.toDoItemsTrackerViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentToDoItemsTrackerBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_to_do_items_tracker, container, false)

    private fun setObservers() {
        toDoItemsTrackerViewModel.apply {
            navigateToDetails.observe(viewLifecycleOwner, Observer { item ->
                Log.i("Tracker", "Navigate with")
                val toDoItemID: String = item?.id ?: return@Observer
                navigateToDetails(toDoItemID)
            })
            navigateToCreateNewToDoItem.observe(viewLifecycleOwner, Observer { hasNavigated ->
                if (hasNavigated){
                    navigateToDetails(EMPTY_ID)
                    doneNavigation()
                }
            })
        }
    }

    private fun navigateToDetails(toDoItemID: String) {
        val action = ToDoItemsTrackerFragmentDirections.actionToDoItemsTrackerFragmentToToDoItemDetailFragment(toDoItemID)
        findNavController().navigate(action)
    }
}