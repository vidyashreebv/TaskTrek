package com.example.tasktrek

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskDetailFragment : Fragment() {

    private lateinit var titleEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var dueDateEditText: TextInputEditText
    private lateinit var saveButton: Button
    private val args: TaskDetailFragmentArgs by navArgs()
    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleEditText = view.findViewById(R.id.task_title_edit_text)
        descriptionEditText = view.findViewById(R.id.task_description_edit_text)
        dueDateEditText = view.findViewById(R.id.task_due_date_edit_text)
        saveButton = view.findViewById(R.id.save_task_button)

        // Load existing task if editing
        if (args.taskId != -1) {
            viewModel.getTask(args.taskId)?.let { task ->
                titleEditText.setText(task.title)
                descriptionEditText.setText(task.description)
                dueDateEditText.setText(task.dueDate)
            }
        }

        dueDateEditText.setOnClickListener {
            showDatePicker()
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val dueDate = dueDateEditText.text.toString()

            if (title.isNotEmpty() && dueDate.isNotEmpty()) {
                viewModel.addTask(title, description, dueDate)
                findNavController().navigateUp()
            }
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select due date")
            .build()

        datePicker.addOnPositiveButtonClickListener { timeInMillis ->
            val date = Date(timeInMillis)
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            dueDateEditText.setText(dateFormat.format(date))
        }

        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }
} 