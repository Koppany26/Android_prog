package com.example.progr3sshabitplannertracker.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.progr3sshabitplannertracker.data.model.HabitResponse
import com.example.progr3sshabitplannertracker.data.model.ScheduleResponse
import com.example.progr3sshabitplannertracker.utils.DateTimeUtils

@Composable
fun HomeScreen(
    onAddHabit: () -> Unit,
    onCreateSchedule: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadToday()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = "Today's Plan", style = MaterialTheme.typography.headlineMedium)
            Text(text = uiState.today, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onAddHabit, modifier = Modifier.weight(1f)) {
                    Text("Add Habit")
                }
                Button(onClick = onCreateSchedule, modifier = Modifier.weight(1f)) {
                    Text("New Schedule")
                }
            }
        }

        uiState.error?.let { message ->
            item {
                Text(text = "Error: $message", color = MaterialTheme.colorScheme.error)
            }
        }

        item {
            Text(text = "Today's Schedule", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 8.dp))
        }

        if (uiState.schedules.isEmpty()) {
            item {
                Text(text = "Nothing scheduled today", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            items(uiState.schedules, key = { it.id }) { schedule ->
                ScheduleCard(schedule)
            }
        }

        item {
            Text(text = "Habits", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 8.dp))
        }

        if (uiState.habits.isEmpty()) {
            item {
                Text(text = "No habits yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            items(uiState.habits, key = { it.id }) { habit ->
                HabitCard(habit)
            }
        }
    }
}

@Composable
private fun ScheduleCard(schedule: ScheduleResponse) {
    val categoryName = schedule.habit?.category?.name ?: "Other"
    val startTime = DateTimeUtils.formatTime(schedule.startTime?.toString()).ifBlank { "--:--" }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.width(64.dp)) {
                Text(text = startTime, style = MaterialTheme.typography.bodyLarge)
                Text(text = "${schedule.durationMinutes ?: 0} min", style = MaterialTheme.typography.bodySmall)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = schedule.habit?.name ?: "Unknown habit", style = MaterialTheme.typography.titleMedium)
                Text(text = categoryName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                schedule.notes?.takeIf { it.isNotBlank() }?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = it, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun HabitCard(habit: HabitResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = habit.name, style = MaterialTheme.typography.titleMedium)
            habit.description?.takeIf { it.isNotBlank() }?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = "Goal: ${habit.goal}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
        }
    }
}
