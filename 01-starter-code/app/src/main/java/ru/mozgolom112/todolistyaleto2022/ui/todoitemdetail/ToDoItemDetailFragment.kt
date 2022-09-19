package ru.mozgolom112.todolistyaleto2022.ui.todoitemdetail

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import ru.mozgolom112.todolistyaleto2022.R
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabase
import ru.mozgolom112.todolistyaleto2022.databinding.FragmentToDoItemDetailBinding
import ru.mozgolom112.todolistyaleto2022.ui.todoitemdetail.viewmodel.DetailErrors
import ru.mozgolom112.todolistyaleto2022.ui.todoitemdetail.viewmodel.ToDoItemDetailViewModel
import ru.mozgolom112.todolistyaleto2022.ui.todoitemdetail.viewmodel.ToDoItemDetailViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

val priority_items = listOf("Нет", "Низкий", "!! Высокий")
val NO_DEADLINE = -1L


class ToDoItemDetailFragment : Fragment() {

    private val detailViewModel: ToDoItemDetailViewModel by lazy {
        initDetailViewModel()
    }

    private fun initDetailViewModel(): ToDoItemDetailViewModel {
        val selectedToDoItem = navArgs<ToDoItemDetailFragmentArgs>().value.selectedToDoItem
        val application = requireNotNull(this.activity).application
        val datasource = ToDoListDatabase.getInstance(application).toDoListDatabaseDao
        val viewModelFactory = ToDoItemDetailViewModelFactory(selectedToDoItem, datasource)
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
            viewModel = this@ToDoItemDetailFragment.detailViewModel
            lifecycleOwner = viewLifecycleOwner
            (spinPriority.editText as? AutoCompleteTextView)?.setAdapter(createSpinnerAdapter())
        }
        setClickListeners(binding)
    }

    private fun createSpinnerAdapter(): ArrayAdapter<String> =
        ArrayAdapter(requireContext(), R.layout.spin_list_item, priority_items)

    private fun setClickListeners(binding: FragmentToDoItemDetailBinding) {
        binding.apply {
            switchSetDeadline.setOnClickListener {
                showDatePicker() { switchSetDeadline.isChecked = false }
            }
            txtDateOfDeadline.setOnClickListener {
                showDatePicker()
            }
        }
    }

    private fun FragmentToDoItemDetailBinding.showDatePicker(onCancelListener: () -> Unit = {}) {
        Log.i("DetailFragment", "Current state is ${switchSetDeadline.isChecked}")
        //timestamp = -1L
        if (switchSetDeadline.isChecked) {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    c.set(year, monthOfYear, dayOfMonth)
                    val timestamp = c.timeInMillis
                    detailViewModel.saveDeadlineDate(timestamp)
                }, year, month, day
            )
            dpd.setOnCancelListener() { onCancelListener() }
            dpd.show()
        } else {
            Log.i("Calendar", "Set NO_DEADLINE Timestamp")
            detailViewModel.saveDeadlineDate(NO_DEADLINE)
        }
    }

    private fun setObservers(binding: FragmentToDoItemDetailBinding) {
        detailViewModel.apply {
            navigateToTracker.observe(viewLifecycleOwner, Observer { hasNavigated ->
                if (hasNavigated == true) navigateToToDoItemsTracker()
            })
            getFieldsFlag.observe(viewLifecycleOwner, Observer { getFields ->

                if (getFields) {
                    val description: String = binding.txtInputToDo.text.toString()
                    val priority = getPriorityCode(binding.spin.text.toString())
                    Log.i(
                        "ToDoItemDetailFragment",
                        "Selected spin is $priority.Priority is ${priority_items[priority]}"
                    )
                    getToDoItemFields(description, priority)
                }
            })
            errorTextDescription.observe(viewLifecycleOwner, Observer { err ->
                showDialog(err)

            })
            currentToDoItem.observe(viewLifecycleOwner, Observer { toDoItem ->
                Log.i("ToDoItemDetailFragment", "currentToDoItem set ")
                toDoItem?.let {
                    Log.i("ToDoItemDetailFragment", "currentToDoItem not empty ")
                    setCurrentValues(it, binding)
                    Log.i("Calendar", "Set in observer Timestamp ${it.dateDeadline}")
                    saveDeadlineDate(it.dateDeadline)
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
        if (millis < 0L) {
            val formatter = SimpleDateFormat("dd/MM/yyyy");
            binding.txtDateOfDeadline.text = formatter.format(millis)
        }
    }

    private fun getPriorityString(priority_code: Int): String = priority_items[priority_code]

    private fun showDialog(error: DetailErrors) {
        when (error) {
            DetailErrors.EMPTY_DESCRIPTION_ERROR -> showSnackBar(error.text)
            DetailErrors.INSERT_UNIQUE_ITEM_ERROR -> showAlertDialog(error.text)
            DetailErrors.UPDATE_NON_EXISTENT_ITEM -> showAlertDialog(error.text)
            else -> return
        }
        detailViewModel.doneSnackBarEvent()
    }

    private fun showSnackBar(text: String) = Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        text,
        Snackbar.LENGTH_SHORT
    ).show()

    private fun showAlertDialog(text: String) = MaterialAlertDialogBuilder(requireContext())
        .setTitle("Ошибка при сохранении")
        .setMessage(text)
        .setPositiveButton("Да") { _, _ -> forceCreateNewItem() }
        .setNegativeButton("Нет") { _, _ -> navigateToToDoItemsTracker() }
        .show()


    private fun forceCreateNewItem() {
        detailViewModel.apply {
            //Опасный вызов фунции!!! Удаляем UUID элемента, который нам пришел
            forceRemoveItemID()
            onSaveItemClick()
        }
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