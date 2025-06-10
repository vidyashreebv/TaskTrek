package com.example.tasktrek

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TravelAdapter(
    private val travels: List<TravelItem>,
    private val onTravelClick: (TravelItem) -> Unit,
    private val onDeleteClick: (TravelItem) -> Unit
) : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    class TravelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val destinationTextView: TextView = view.findViewById(R.id.travel_destination)
        val descriptionTextView: TextView = view.findViewById(R.id.travel_description)
        val dateTextView: TextView = view.findViewById(R.id.travel_date)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_travel, parent, false)
        return TravelViewHolder(view)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val travel = travels[position]
        holder.destinationTextView.text = travel.destination
        holder.descriptionTextView.text = travel.description
        holder.dateTextView.text = travel.date
        holder.itemView.setOnClickListener { onTravelClick(travel) }
        holder.deleteButton.setOnClickListener { onDeleteClick(travel) }
    }

    override fun getItemCount() = travels.size
} 