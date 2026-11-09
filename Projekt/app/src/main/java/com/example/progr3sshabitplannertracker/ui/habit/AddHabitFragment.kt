package com.example.progr3sshabitplannertracker.ui.habit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.progr3sshabitplannertracker.data.model.HabitCategory
import com.example.progr3sshabitplannertracker.databinding.FragmentAddHabitBinding

class AddHabitFragment : Fragment() {

    private var _binding: FragmentAddHabitBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddHabitViewModel by viewModels()

    private var categoriesList: List<HabitCategory> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchCategories()

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoriesList = categories
            val categoryNames = categories.map { it.name }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoryNames
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = adapter
        }

        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etHabitName.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()
            val goal = binding.etGoal.text.toString().trim()
            val selectedPosition = binding.spinnerCategory.selectedItemPosition

            val categoryId = if (selectedPosition >= 0 && selectedPosition < categoriesList.size) {
                categoriesList[selectedPosition].id.toInt()
            } else {
                1
            }

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a habit name!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addHabit(name, description, categoryId, if (goal.isEmpty()) "Daily habit" else goal)
            }
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Habit created successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
