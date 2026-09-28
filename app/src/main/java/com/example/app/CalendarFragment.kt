package com.example.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class CalendarFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var calendarView: CalendarView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddEvent: FloatingActionButton
    private val eventsMap = mutableMapOf<String, MutableList<String>>() // Map to store events by date
    private var selectedDate: String? = null // Currently selected date

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        // Initialize views
        tabLayout = view.findViewById(R.id.tab_layout)
        calendarView = view.findViewById(R.id.calendar_view)
        recyclerView = view.findViewById(R.id.event_list)
        fabAddEvent = view.findViewById(R.id.fab_add_event)

        // Add Tabs to TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Daily"))
        tabLayout.addTab(tabLayout.newTab().setText("Weekly"))
        tabLayout.addTab(tabLayout.newTab().setText("Monthly"))

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = EventAdapter(emptyList())
        recyclerView.adapter = adapter

        // Handle Calendar Date Selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(requireContext(), "Selected date: $selectedDate", Toast.LENGTH_SHORT).show()
            displayEventsForDate(selectedDate, adapter)
        }

        // Handle Add Event Button
        fabAddEvent.setOnClickListener {
            selectedDate?.let {
                showAddEventDialog(it, adapter)
            } ?: Toast.makeText(requireContext(), "Please select a date first.", Toast.LENGTH_SHORT).show()
        }

        // Handle Tab Selection
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> Toast.makeText(requireContext(), "Switched to Daily View", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(requireContext(), "Switched to Weekly View", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(requireContext(), "Switched to Monthly View", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return view
    }

    private fun displayEventsForDate(date: String?, adapter: EventAdapter) {
        val events = eventsMap[date] ?: emptyList()
        if (events.isEmpty()) {
            Toast.makeText(requireContext(), "No events for $date", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Displaying events for $date", Toast.LENGTH_SHORT).show()
        }
        adapter.updateEvents(events)
    }

    private fun showAddEventDialog(date: String, adapter: EventAdapter) {
        val inputField = EditText(requireContext()).apply {
            hint = "Enter event details"
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add Event")
            .setView(inputField)
            .setPositiveButton("Add") { _, _ ->
                val event = inputField.text.toString()
                if (event.isNotBlank()) {
                    eventsMap.computeIfAbsent(date) { mutableListOf() }.add(event)
                    Toast.makeText(requireContext(), "Event added to $date", Toast.LENGTH_SHORT).show()
                    displayEventsForDate(date, adapter)
                } else {
                    Toast.makeText(requireContext(), "Event cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(requireContext(), "Event creation cancelled", Toast.LENGTH_SHORT).show()
            }
            .create()
        dialog.show()
    }

    // Adapter for RecyclerView
    class EventAdapter(private var events: List<String>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            return EventViewHolder(view)
        }

        override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
            holder.bind(events[position])
        }

        override fun getItemCount(): Int = events.size

        @SuppressLint("NotifyDataSetChanged")
        fun updateEvents(newEvents: List<String>) {
            events = newEvents
            notifyDataSetChanged()
        }

        class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(event: String) {
                itemView.findViewById<TextView>(android.R.id.text1).text = event
            }
        }
    }
}
