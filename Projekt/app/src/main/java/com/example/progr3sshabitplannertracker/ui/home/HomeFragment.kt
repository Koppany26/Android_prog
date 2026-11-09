package com.example.progr3sshabitplannertracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progr3sshabitplannertracker.R
import com.example.progr3sshabitplannertracker.data.model.ScheduleResponse
import com.example.progr3sshabitplannertracker.databinding.FragmentHomeBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: HomeScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = HomeViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUi()
        setupObservers()
    }

    private fun setupUi() {
        // Set dynamic date in header
        val todayDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.ENGLISH)
        binding.tvSubtitle.text = todayDate.format(formatter)

        // Setup RecyclerView with status toggle and edit dialog listener
        adapter = HomeScheduleAdapter(
            onStatusClick = { schedule ->
                viewModel.toggleProgress(schedule)
            },
            onItemClick = { schedule ->
                showEditDialog(schedule)
            }
        )
        binding.recyclerViewSchedules.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSchedules.adapter = adapter

        // Fetch schedules
        val todayStr = try { LocalDate.now().toString() } catch (_: Exception) { "2025-10-26" }
        viewModel.getScheduleByDay(todayStr)
        
        // Add button
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createScheduleFragment)
        }
    }

    private fun showEditDialog(schedule: ScheduleResponse) {
        val context = requireContext()
        val habitName = schedule.habit?.name ?: "Schedule Item"

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 24)
        }

        val tvStatusLabel = TextView(context).apply {
            text = "Status:"
            textSize = 14f
        }
        layout.addView(tvStatusLabel)

        val spinnerStatus = Spinner(context)
        val statuses = listOf("Planned", "Completed", "Skipped")
        val spinnerAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, statuses)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = spinnerAdapter
        
        val currentStatusPos = statuses.indexOf(schedule.status).takeIf { it >= 0 } ?: 0
        spinnerStatus.setSelection(currentStatusPos)
        layout.addView(spinnerStatus)

        val tvNotesLabel = TextView(context).apply {
            text = "\nNotes:"
            textSize = 14f
        }
        layout.addView(tvNotesLabel)

        val etNotes = EditText(context).apply {
            hint = "Add notes..."
            setText(schedule.notes ?: "")
        }
        layout.addView(etNotes)

        AlertDialog.Builder(context)
            .setTitle("Edit Schedule: $habitName")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val selectedStatus = spinnerStatus.selectedItem.toString()
                val newNotes = etNotes.text.toString().trim()
                viewModel.updateSchedule(schedule.id, selectedStatus, newNotes)
                Toast.makeText(context, "Schedule updated!", Toast.LENGTH_SHORT).show()
            }
            .setNeutralButton("Delete") { _, _ ->
                AlertDialog.Builder(context)
                    .setTitle("Delete Schedule")
                    .setMessage("Are you sure you want to delete this schedule?")
                    .setPositiveButton("Delete") { _, _ ->
                        viewModel.deleteSchedule(schedule.id)
                        Toast.makeText(context, "Schedule deleted!", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupObservers() {
        viewModel.schedules.observe(viewLifecycleOwner) { schedules ->
            if (!schedules.isNullOrEmpty()) {
                adapter.submitList(schedules)
                binding.tvEmpty.visibility = View.GONE
            } else {
                adapter.submitList(emptyList())
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }
        
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
