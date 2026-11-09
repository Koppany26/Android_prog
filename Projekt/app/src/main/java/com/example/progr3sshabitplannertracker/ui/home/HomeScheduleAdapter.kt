package com.example.progr3sshabitplannertracker.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.progr3sshabitplannertracker.R
import com.example.progr3sshabitplannertracker.data.model.ScheduleResponse
import com.example.progr3sshabitplannertracker.databinding.ItemScheduleBinding
import java.time.format.DateTimeFormatter

class HomeScheduleAdapter(
    private val onStatusClick: ((ScheduleResponse) -> Unit)? = null,
    private val onItemClick: ((ScheduleResponse) -> Unit)? = null
) : ListAdapter<ScheduleResponse, HomeScheduleAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onStatusClick, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: ItemScheduleBinding,
        private val onStatusClick: ((ScheduleResponse) -> Unit)?,
        private val onItemClick: ((ScheduleResponse) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScheduleResponse) {
            binding.tvHabitName.text = item.habit?.name ?: "Unknown Habit"
            
            // Format start and end time safely
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val startStr = item.startTime?.format(formatter) ?: ""
            val endStr = item.endTime?.format(formatter) ?: ""
            
            val timeText = if (startStr.isNotEmpty() && endStr.isNotEmpty()) {
                "$startStr - $endStr"
            } else if (startStr.isNotEmpty()) {
                startStr
            } else {
                "Time not set"
            }
            binding.tvTime.text = timeText

            // Set Check icon or empty circle based on status
            val isCompleted = item.status == "Completed" || item.progress?.any { it.progressValue > 0 } == true
            if (isCompleted) {
                binding.ivStatus.setImageResource(R.drawable.ic_check_circle_green)
            } else {
                binding.ivStatus.setImageResource(R.drawable.ic_circle_empty)
            }

            binding.ivStatus.setOnClickListener {
                onStatusClick?.invoke(item)
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ScheduleResponse>() {
        override fun areItemsTheSame(oldItem: ScheduleResponse, newItem: ScheduleResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScheduleResponse, newItem: ScheduleResponse): Boolean {
            return oldItem == newItem
        }
    }
}
