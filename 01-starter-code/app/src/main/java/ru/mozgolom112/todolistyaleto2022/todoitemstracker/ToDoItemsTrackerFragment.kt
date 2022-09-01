package ru.mozgolom112.todolistyaleto2022.todoitemstracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
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

    private val toDoItemAdapter: ToDoItemAdapter by lazy {
        ToDoItemAdapter()
    }

    private fun initTrackerViewModel(): ToDoItemsTrackerViewModel {
        val application = requireNotNull(this.activity).application
        val dataSource: ToDoListDatabaseDao = ToDoListDatabase.getInstance(application).toDoListDatabaseDao
        val viewModelFactory = ToDoItemsTrackerViewModelFactory(dataSource, application)
        //Simple
        //return ViewModelProvider(this, viewModelFactory).get(ToDoItemsTrackerViewModel::class.java)
        //Cool
        val trackerViewModel: ToDoItemsTrackerViewModel by viewModels { viewModelFactory }
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
            recycleToDoItemList.apply {
                adapter = toDoItemAdapter
                layoutManager = customManager()
            }
        }
    }

    private fun customManager(): RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

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
            toDoItems.observe(viewLifecycleOwner, Observer { items ->
                items?.let{
                    toDoItemAdapter.addHeaderAndSubmitList(it)
                }
            })
        }
    }

    private fun navigateToDetails(toDoItemID: String) {
        val action = ToDoItemsTrackerFragmentDirections.actionToDoItemsTrackerFragmentToToDoItemDetailFragment(toDoItemID)
        findNavController().navigate(action)
    }
}