package com.example.tasktrek

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var profileImage: ShapeableImageView
    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var locationEditText: TextInputEditText
    private lateinit var bioEditText: TextInputEditText
    
    // Add TextInputLayouts for error states
    private lateinit var nameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var phoneLayout: TextInputLayout
    
    private var currentImageUri: String = ""

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                profileImage.setImageURI(uri)
                currentImageUri = uri.toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        profileImage = view.findViewById(R.id.profile_image)
        nameEditText = view.findViewById(R.id.name_edit_text)
        emailEditText = view.findViewById(R.id.email_edit_text)
        phoneEditText = view.findViewById(R.id.phone_edit_text)
        locationEditText = view.findViewById(R.id.location_edit_text)
        bioEditText = view.findViewById(R.id.bio_edit_text)
        
        // Initialize layouts
        nameLayout = view.findViewById(R.id.name_layout)
        emailLayout = view.findViewById(R.id.email_layout)
        phoneLayout = view.findViewById(R.id.phone_layout)

        // Set up image picker
        profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        // Load saved profile
        loadProfile()

        // Observe validation results
        viewModel.validationResult.observe(viewLifecycleOwner) { result ->
            clearErrors()
            when (result) {
                is ProfileValidation.Success -> {
                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                }
                is ProfileValidation.Error -> {
                    showError(result.message)
                }
            }
        }

        // Set up save button
        view.findViewById<View>(R.id.save_profile_button).setOnClickListener {
            saveProfile()
        }

        // Add clear data button click handler
        view.findViewById<View>(R.id.clear_data_button).setOnClickListener {
            showClearDataConfirmationDialog()
        }
    }

    private fun loadProfile() {
        val currentProfile = viewModel.getCurrentProfile()
        nameEditText.setText(currentProfile.name)
        emailEditText.setText(currentProfile.email)
        phoneEditText.setText(currentProfile.phone)
        locationEditText.setText(currentProfile.location)
        bioEditText.setText(currentProfile.bio)
        
        if (currentProfile.imageUri.isNotEmpty()) {
            try {
                profileImage.setImageURI(Uri.parse(currentProfile.imageUri))
                currentImageUri = currentProfile.imageUri
            } catch (e: Exception) {
                // If there's an error loading the image, keep the default
                e.printStackTrace()
            }
        }
    }

    private fun saveProfile() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val phone = phoneEditText.text.toString()
        val location = locationEditText.text.toString()
        val bio = bioEditText.text.toString()

        viewModel.validateAndUpdateProfile(
            name = name,
            email = email,
            phone = phone,
            location = location,
            bio = bio,
            imageUri = currentImageUri
        )
    }

    private fun clearErrors() {
        nameLayout.error = null
        emailLayout.error = null
        phoneLayout.error = null
    }

    private fun showError(message: String) {
        when {
            message.contains("name", ignoreCase = true) -> nameLayout.error = message
            message.contains("email", ignoreCase = true) -> emailLayout.error = message
            message.contains("phone", ignoreCase = true) -> phoneLayout.error = message
            else -> Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun showClearDataConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Clear All Data")
            .setMessage("Are you sure you want to clear all your profile data? This action cannot be undone.")
            .setPositiveButton("Clear") { _, _ ->
                viewModel.clearAllData()
                Toast.makeText(context, "All data cleared", Toast.LENGTH_SHORT).show()
                loadProfile() // Reload the empty profile
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}