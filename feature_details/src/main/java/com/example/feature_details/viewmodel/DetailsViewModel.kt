package com.example.feature_details.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model_todo.TodoRepo
import com.example.model_todo.local.TodoDatabase
import com.example.model_todo.response.Todo
import kotlinx.coroutines.launch

class DetailsViewModel (app: Application) : AndroidViewModel(app) {

    private val todoRepo by lazy {
        TodoRepo(TodoDatabase.getDatabase(app, viewModelScope).todoDao())
    }

    fun checkCompleted(id: Int, checked: Boolean) {
        viewModelScope.launch {
            todoRepo.setComplete(id, checked)
        }
    }

    suspend fun getTodo(todoId: Int): Todo {
        return todoRepo.getTodo(todoId)
    }

    fun deleteTodo(todoId: Int) {
        viewModelScope.launch {
            todoRepo.deleteTodo(todoId)
        }
    }

}