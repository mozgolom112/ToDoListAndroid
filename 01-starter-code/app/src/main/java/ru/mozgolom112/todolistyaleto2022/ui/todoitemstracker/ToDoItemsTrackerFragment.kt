package ru.mozgolom112.todolistyaleto2022.ui.todoitemstracker

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.mozgolom112.todolistyaleto2022.R
import ru.mozgolom112.todolistyaleto2022.adapters.ToDoItemAdapter
import ru.mozgolom112.todolistyaleto2022.databinding.FragmentToDoItemsTrackerBinding
import ru.mozgolom112.todolistyaleto2022.domain.ToDoItem
import ru.mozgolom112.todolistyaleto2022.ui.todoitemstracker.viewmodel.ToDoItemsTrackerViewModel
import ru.mozgolom112.todolistyaleto2022.ui.todoitemstracker.viewmodel.ToDoItemsTrackerViewModelFactory

class ToDoItemsTrackerFragment : Fragment() {

    private val toDoItemsTrackerViewModel: ToDoItemsTrackerViewModel by lazy {
        Log.i("TrackerFragment", "initTrackerViewModel")
        initTrackerViewModel()
    }

    private val toDoItemAdapter: ToDoItemAdapter by lazy {
        val infoClick = ToDoItemAdapter.InfoClickListener() { selectedItem ->
            toDoItemsTrackerViewModel.navigateToItemDetails(selectedItem)
        }
        val checkBoxClick = ToDoItemAdapter.CheckBoxStateClickListener() {selectedItem ->
            toDoItemsTrackerViewModel.changeItemState(selectedItem)
            val position = toDoItemAdapter.currentList.indexOf(selectedItem)
            toDoItemAdapter.notifyItemChanged(position) //для обновления анимации элемента в recycleview
        }
        ToDoItemAdapter(infoClick, checkBoxClick)
    }

    private fun initTrackerViewModel(): ToDoItemsTrackerViewModel {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ToDoItemsTrackerViewModelFactory(application)
        val trackerViewModel: ToDoItemsTrackerViewModel by viewModels { viewModelFactory }
        return trackerViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("ToDoItemsTrackerFragment", "onCreateView")
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
        var isListSumbitted = false
        toDoItemsTrackerViewModel.apply {
            navigateToDetails.observe(viewLifecycleOwner, Observer { hasNavigated ->
                if (hasNavigated == true) navigateToDetails(itemToDetails)
            })
            toDoItemsDatabase.observe(viewLifecycleOwner, Observer { items ->
                if (!isListSumbitted){
                    items?.let {
                        //TODO("Проверить, что при изменеии списка элементов, все отрабатывается корректно, например при удалении элемента из списка")
                        toDoItemAdapter.submitList(items)
                        isListSumbitted = true  //чтобы submit произошел лишь однажды
                    }
                }
            })
        }
    }

    private fun navigateToDetails(item: ToDoItem?) {
        //Если item == EMPTY_TODO_ITEM, то это означает что создается новый элемент
        //Если у нас есть элемент, то мы навигируемся и выгружаем детали
        Log.i("TrackerFragment", "${item?.description}")
        val action =
            ToDoItemsTrackerFragmentDirections.actionToDoItemsTrackerFragmentToToDoItemDetailFragment(
                item
            )
        findNavController().navigate(action)
        toDoItemsTrackerViewModel.doneNavigation()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("ToDoItemsDetailFragment", "onDestroyView ")
    }
}