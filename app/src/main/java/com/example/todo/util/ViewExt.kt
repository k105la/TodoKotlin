package com.example.todo.util

import androidx.recyclerview.widget.RecyclerView

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener { event.invoke(layoutPosition, itemViewType) }
    return this
}