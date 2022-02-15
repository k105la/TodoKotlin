package com.example.feature_todo.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.model_todo.response.Todo

object TodoDiffUtil : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
}