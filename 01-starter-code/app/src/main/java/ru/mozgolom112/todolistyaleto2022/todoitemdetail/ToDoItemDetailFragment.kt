package ru.mozgolom112.todolistyaleto2022.todoitemdetail

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import ru.mozgolom112.todolistyaleto2022.R
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabase
import ru.mozgolom112.todolistyaleto2022.databinding.FragmentToDoItemDetailBinding
import java.text.SimpleDateFormat

val priority_items = listOf("Нет", "Низкий", "!! Высокий")

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
        setObservers(binding)
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

        val adapter = ArrayAdapter(requireContext(), R.layout.spin_list_item, priority_items)
        return adapter
    }

    private fun setObservers(binding: FragmentToDoItemDetailBinding) {
        detailViewModel.apply {
            navigateToTracker.observe(viewLifecycleOwner, Observer { hasNavigate ->
                    if (hasNavigate == true) {
                        navigateToToDoItemsTracker()
                    }
            })
            getFieldsFlag.observe(viewLifecycleOwner, Observer { getFields ->

                if (getFields){
                    val description: String = binding.txtInputToDo.text.toString()
                    val priority = getPriorityCode(binding.spin.text.toString())
                    Log.i("ToDoItemDetailFragment","Selected spin is $priority.Priority is ${priority_items[priority]}")
                    val dateDeadline = -1L
                    if (description == ""){
                        Log.i("ToDoItemDetailFragment","Description is empty")
                    }
                    getToDoItemFields(description, priority, dateDeadline)
                }
            })
            emptyDescriptionError.observe(viewLifecycleOwner, Observer { showError ->
                if (showError){
                    val error = "Пожалуйста, добавьте текст задачи. Он не может быть пустым"
                    showSnackBar(error)
                }
            })
            currentToDoItem.observe(viewLifecycleOwner, Observer { toDoItem ->
                Log.i("ToDoItemDetailFragment","currentToDoItem set ")
                toDoItem?.let {
                    Log.i("ToDoItemDetailFragment","currentToDoItem not empty ")
                    setCurrentValues(toDoItem, binding)
                }
            })
        }
    }

    private fun setCurrentValues(toDoItem: ToDoItem, binding: FragmentToDoItemDetailBinding) {
        binding.apply {
            txtInputToDo.setText(toDoItem.description)
            //Issues(Выпадает после этого только один item)
            spinPriority.editText?.setText(getPriorityString(toDoItem.priority))
            setDeadlineDate(this, toDoItem.dateDeadline)
        }
    }

    private fun setDeadlineDate(binding: FragmentToDoItemDetailBinding, millis: Long) {
        if (millis<0L){
            val formatter = SimpleDateFormat("dd/MM/yyyy");
            binding.txtDateOfDeadline.text = formatter.format(millis)
        }
    }

    private fun getPriorityString(priority_code: Int): String = priority_items[priority_code]

    private fun showSnackBar(error: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            error,
            Snackbar.LENGTH_SHORT
        ).show()
        detailViewModel.doneSnackBarEvent()
    }

    //Не совсем хорошо так делать на мой взгляд, но в этом есть смысл, так как мы можем тягать
    //новый список Кодов из базы и легко добавлять их в дальнейшем
    //Если нет. то можно использовать обычный spinner
    private fun getPriorityCode(text: String): Int = priority_items.indexOf(text)


    private fun navigateToToDoItemsTracker() {
        val action =
            ToDoItemDetailFragmentDirections.actionToDoItemDetailFragmentToToDoItemsTrackerFragment()
        findNavController().navigate(action)
        detailViewModel.doneNavigating()
    }
}