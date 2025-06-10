package com.example.tasktrek

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TravelViewModel : ViewModel() {
    private val _travels = MutableLiveData<List<TravelItem>>()
    val travels: LiveData<List<TravelItem>> = _travels

    init {
        // Initialize with sample data
        _travels.value = listOf(
            TravelItem(1, "Paris, France", "Visit the Eiffel Tower", "Jun 15, 2024"),
            TravelItem(2, "Tokyo, Japan", "Cherry blossom season", "Apr 1, 2024"),
            TravelItem(3, "New York, USA", "Business conference", "May 20, 2024")
        )
    }

    fun addTravel(destination: String, description: String, date: String) {
        val currentTravels = _travels.value.orEmpty().toMutableList()
        val newId = (currentTravels.maxOfOrNull { it.id } ?: 0) + 1
        currentTravels.add(TravelItem(newId, destination, description, date))
        _travels.value = currentTravels
    }

    fun getTravel(id: Int): TravelItem? {
        return _travels.value?.find { it.id == id }
    }

    fun deleteTravel(travelId: Int) {
        val currentTravels = _travels.value.orEmpty().toMutableList()
        currentTravels.removeAll { it.id == travelId }
        _travels.value = currentTravels
    }

    fun clearAllTravels() {
        _travels.value = emptyList()
    }
} 