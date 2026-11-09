package com.example.progr3sshabitplannertracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
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

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_schedule, null)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)
        val spinnerStatus = dialogView.findViewById<Spinner>(R.id.spinnerStatus)
        val etNotes = dialogView.findViewById<EditText>(R.id.etNotes)
        val btnSave = dialogView.findViewById<View>(R.id.btnSave)
        val btnDelete = dialogView.findViewById<View>(R.id.btnDelete)

        tvTitle.text = "Edit: $habitName"

        val statuses = listOf("Planned", "Completed", "Skipped")
        val spinnerAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, statuses)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = spinnerAdapter
        
        val currentStatusPos = statuses.indexOf(schedule.status).takeIf { it >= 0 } ?: 0
        spinnerStatus.setSelection(currentStatusPos)

        etNotes.setText(schedule.notes ?: "")

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        btnSave.setOnClickListener {
            val selectedStatus = spinnerStatus.selectedItem.toString()
            val newNotes = etNotes.text.toString().trim()
            viewModel.updateSchedule(schedule.id, selectedStatus, newNotes)
            Toast.makeText(context, "Schedule updated!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            val confirmView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_confirm, null)
            val btnConfirm = confirmView.findViewById<View>(R.id.btnConfirmDelete)
            val btnCancel = confirmView.findViewById<View>(R.id.btnCancelDelete)

            val confirmDialog = AlertDialog.Builder(context)
                .setView(confirmView)
                .create()

            btnConfirm.setOnClickListener {
                viewModel.deleteSchedule(schedule.id)
                Toast.makeText(context, "Schedule deleted!", Toast.LENGTH_SHORT).show()
                confirmDialog.dismiss()
                dialog.dismiss()
            }

            btnCancel.setOnClickListener {
                confirmDialog.dismiss()
            }

            confirmDialog.show()
        }

        dialog.show()
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
