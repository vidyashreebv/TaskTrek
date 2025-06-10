package com.example.tasktrek

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: List<TaskItem>,
    private val onTaskClick: (TaskItem) -> Unit,
    private val onDeleteClick: (TaskItem) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.task_title)
        val descriptionTextView: TextView = view.findViewById(R.id.task_description)
        val dueDateTextView: TextView = view.findViewById(R.id.task_due_date)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.title
        holder.descriptionTextView.text = task.description
        holder.dueDateTextView.text = task.dueDate
        holder.itemView.setOnClickListener { onTaskClick(task) }
        holder.deleteButton.setOnClickListener { onDeleteClick(task) }
    }

    override fun getItemCount() = tasks.size
}