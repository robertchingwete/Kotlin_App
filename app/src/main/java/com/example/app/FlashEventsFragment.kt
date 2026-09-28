package com.example.app

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class FlashEventsFragment : Fragment() {

    private lateinit var listViewEvents: ListView
    private lateinit var fabAddEvent: View
    private val eventsList = mutableListOf<Pair<String, String>>() // List of events (title, time)
    private lateinit var eventsAdapter: ArrayAdapter<String>
    private lateinit var noEventsMessage: TextView // TextView to show when there are no events

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_flash_events, container, false)

        // Initialize views
        listViewEvents = view.findViewById(R.id.list_view_events)
        fabAddEvent = view.findViewById(R.id.fab_add_event)
        noEventsMessage = view.findViewById(R.id.no_events_message) // TextView to show "No events"

        // Set up ListView with an adapter
        eventsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, eventsList.map { it.first })
        listViewEvents.adapter = eventsAdapter

        // Initially hide the "No events" message if events exist
        updateNoEventsMessageVisibility()

        // Handle adding new event
        fabAddEvent.setOnClickListener { showAddEventDialog() }

        // Handle item clicks for edit and delete
        listViewEvents.setOnItemClickListener { _, _, position, _ ->
            showEventOptionsDialog(position)
        }

        return view
    }

    private fun showAddEventDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.event, null)
        val eventTitleInput = dialogView.findViewById<EditText>(R.id.et_event_title)
        val eventTimeInput = dialogView.findViewById<EditText>(R.id.et_event_time)

        AlertDialog.Builder(requireContext())
            .setTitle("Add New Event")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = eventTitleInput.text.toString()
                val time = eventTimeInput.text.toString()
                if (title.isNotEmpty() && time.isNotEmpty()) {
                    eventsList.add(Pair(title, time))
                    updateEventList()
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEventOptionsDialog(position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(eventsList[position].first)
            .setItems(arrayOf("Edit", "Delete")) { _, which ->
                when (which) {
                    0 -> showEditEventDialog(position)
                    1 -> deleteEvent(position)
                }
            }
            .show()
    }

    private fun showEditEventDialog(position: Int) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.event, null)
        val eventTitleInput = dialogView.findViewById<EditText>(R.id.et_event_title)
        val eventTimeInput = dialogView.findViewById<EditText>(R.id.et_event_time)

        // Pre-fill with current values
        eventTitleInput.setText(eventsList[position].first)
        eventTimeInput.setText(eventsList[position].second)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Event")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val title = eventTitleInput.text.toString()
                val time = eventTimeInput.text.toString()
                if (title.isNotEmpty() && time.isNotEmpty()) {
                    eventsList[position] = Pair(title, time)
                    updateEventList()
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteEvent(position: Int) {
        eventsList.removeAt(position)
        updateEventList()
    }

    private fun updateEventList() {
        eventsAdapter.clear()
        eventsAdapter.addAll(eventsList.map { it.first })
        eventsAdapter.notifyDataSetChanged()

        // Update the visibility of the "No events" message
        updateNoEventsMessageVisibility()
    }

    private fun updateNoEventsMessageVisibility() {
        // Show "No events available" message when events list is empty
        if (eventsList.isEmpty()) {
            noEventsMessage.visibility = View.VISIBLE
        } else {
            noEventsMessage.visibility = View.GONE
        }
    }
}
