package com.example.tasktrek

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val bio: String = "",
    val imageUri: String = ""
)

sealed class ProfileValidation {
    object Success : ProfileValidation()
    data class Error(val message: String) : ProfileValidation()
}

class ProfileViewModel : ViewModel() {
    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile

    private val _validationResult = MutableLiveData<ProfileValidation>()
    val validationResult: LiveData<ProfileValidation> = _validationResult

    init {
        // Load saved profile
        loadSavedProfile()
    }

    private fun loadSavedProfile() {
        // For now, we'll use the last saved profile in memory
        if (_userProfile.value == null) {
            _userProfile.value = UserProfile()
        }
    }

    fun validateAndUpdateProfile(
        name: String,
        email: String,
        phone: String,
        location: String,
        bio: String,
        imageUri: String
    ): Boolean {
        // Validate name
        if (name.trim().length < 2) {
            _validationResult.value = ProfileValidation.Error("Name must be at least 2 characters long")
            return false
        }

        // Validate email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _validationResult.value = ProfileValidation.Error("Please enter a valid email address")
            return false
        }

        // Validate phone (if provided)
        if (phone.isNotEmpty() && !Patterns.PHONE.matcher(phone).matches()) {
            _validationResult.value = ProfileValidation.Error("Please enter a valid phone number")
            return false
        }

        // If all validations pass, update profile
        _userProfile.value = UserProfile(
            name = name.trim(),
            email = email.trim(),
            phone = phone.trim(),
            location = location.trim(),
            bio = bio.trim(),
            imageUri = imageUri
        )
        
        _validationResult.value = ProfileValidation.Success
        return true
    }

    fun getCurrentProfile(): UserProfile {
        return _userProfile.value ?: UserProfile()
    }

    fun clearAllData() {
        _userProfile.value = UserProfile() // Reset to empty profile
    }
} 