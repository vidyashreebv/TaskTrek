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

class TravelFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TravelAdapter
    private val viewModel: TravelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_travel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.travel_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize adapter with all required parameters
        adapter = TravelAdapter(
            travels = emptyList(),
            onTravelClick = { travel ->
                val action = TravelFragmentDirections.actionTravelFragmentToTravelDetailFragment(travel.id)
                findNavController().navigate(action)
            },
            onDeleteClick = { travel ->
                showDeleteConfirmation(travel)
            }
        )
        recyclerView.adapter = adapter

        // Observe travel list changes
        viewModel.travels.observe(viewLifecycleOwner) { travels ->
            adapter = TravelAdapter(
                travels = travels,
                onTravelClick = { travel ->
                    val action = TravelFragmentDirections.actionTravelFragmentToTravelDetailFragment(travel.id)
                    findNavController().navigate(action)
                },
                onDeleteClick = { travel ->
                    showDeleteConfirmation(travel)
                }
            )
            recyclerView.adapter = adapter
        }

        view.findViewById<FloatingActionButton>(R.id.fab_add_travel).setOnClickListener {
            val action = TravelFragmentDirections.actionTravelFragmentToTravelDetailFragment(-1)
            findNavController().navigate(action)
        }
    }

    private fun showDeleteConfirmation(travel: TravelItem) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Travel Plan")
            .setMessage("Are you sure you want to delete this travel plan?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteTravel(travel.id)
                Toast.makeText(context, "Travel plan deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}