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

class TravelDetailFragment : Fragment() {

    private lateinit var destinationEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var dateEditText: TextInputEditText
    private lateinit var saveButton: Button
    private val args: TravelDetailFragmentArgs by navArgs()
    private val viewModel: TravelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_travel_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        destinationEditText = view.findViewById(R.id.travel_destination_edit_text)
        descriptionEditText = view.findViewById(R.id.travel_description_edit_text)
        dateEditText = view.findViewById(R.id.travel_date_edit_text)
        saveButton = view.findViewById(R.id.save_travel_button)

        // Load existing travel if editing
        if (args.travelId != -1) {
            viewModel.getTravel(args.travelId)?.let { travel ->
                destinationEditText.setText(travel.destination)
                descriptionEditText.setText(travel.description)
                dateEditText.setText(travel.date)
            }
        }

        dateEditText.setOnClickListener {
            showDatePicker()
        }

        saveButton.setOnClickListener {
            val destination = destinationEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val date = dateEditText.text.toString()

            if (destination.isNotEmpty() && date.isNotEmpty()) {
                viewModel.addTravel(destination, description, date)
                findNavController().navigateUp()
            }
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select travel date")
            .build()

        datePicker.addOnPositiveButtonClickListener { timeInMillis ->
            val date = Date(timeInMillis)
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            dateEditText.setText(dateFormat.format(date))
        }

        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }
} 