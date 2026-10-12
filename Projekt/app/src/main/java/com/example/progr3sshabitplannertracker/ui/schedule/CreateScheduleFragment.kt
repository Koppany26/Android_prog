package com.example.progr3sshabitplannertracker.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.progr3sshabitplannertracker.ui.theme.Progr3SSHabitPlannerTrackerTheme

class CreateScheduleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Progr3SSHabitPlannerTrackerTheme {
                    CreateScheduleScreen(
                        onNavigateBack = { findNavController().navigateUp() }
                    )
                }
            }
        }
    }
}
