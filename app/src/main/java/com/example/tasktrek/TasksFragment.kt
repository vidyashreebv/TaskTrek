package com.example.tasktrek

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TasksFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.tasks_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize adapter with all required parameters
        adapter = TaskAdapter(
            tasks = emptyList(),
            onTaskClick = { task ->
                val action = TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment(task.id)
                findNavController().navigate(action)
            },
            onDeleteClick = { task ->
                showDeleteConfirmation(task)
            }
        )
        recyclerView.adapter = adapter

        // Observe task list changes
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapter = TaskAdapter(
                tasks = tasks,
                onTaskClick = { task ->
                    val action = TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment(task.id)
                    findNavController().navigate(action)
                },
                onDeleteClick = { task ->
                    showDeleteConfirmation(task)
                }
            )
            recyclerView.adapter = adapter
        }

        view.findViewById<FloatingActionButton>(R.id.fab_add_task).setOnClickListener {
            val action = TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment(-1)
            findNavController().navigate(action)
        }
    }

    private fun showDeleteConfirmation(task: TaskItem) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteTask(task.id)
                Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}