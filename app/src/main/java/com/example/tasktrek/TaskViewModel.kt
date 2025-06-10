package com.example.tasktrek

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
    private val _tasks = MutableLiveData<List<TaskItem>>()
    val tasks: LiveData<List<TaskItem>> = _tasks

    init {
        // Initialize with sample data
        _tasks.value = listOf(
            TaskItem(1, "Complete Project", "Finish the Android project", "Mar 1, 2024"),
            TaskItem(2, "Team Meeting", "Weekly sync with the team", "Mar 2, 2024"),
            TaskItem(3, "Code Review", "Review pull requests", "Mar 3, 2024")
        )
    }

    fun addTask(title: String, description: String, dueDate: String) {
        val currentTasks = _tasks.value.orEmpty().toMutableList()
        val newId = (currentTasks.maxOfOrNull { it.id } ?: 0) + 1
        currentTasks.add(TaskItem(newId, title, description, dueDate))
        _tasks.value = currentTasks
    }

    fun getTask(id: Int): TaskItem? {
        return _tasks.value?.find { it.id == id }
    }

    fun deleteTask(taskId: Int) {
        val currentTasks = _tasks.value.orEmpty().toMutableList()
        currentTasks.removeAll { it.id == taskId }
        _tasks.value = currentTasks
    }

    fun clearAllTasks() {
        _tasks.value = emptyList()
    }
} 