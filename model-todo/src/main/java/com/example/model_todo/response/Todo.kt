package com.example.model_todo.response

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String = "",
    val isComplete: Boolean = false,
    @ColorInt val color: Int = randomColor
) {

    companion object {
        val randomColor
            get() = Color.rgb(
                Random.nextInt(255),
                Random.nextInt(255),
                Random.nextInt(255)
            )
    }
}