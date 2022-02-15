package com.example.feature_add.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model_todo.TodoRepo
import com.example.model_todo.local.TodoDatabase
import com.example.model_todo.response.Todo
import kotlinx.coroutines.launch

class AddViewModel(app: Application) : AndroidViewModel(app) {

    private val todoRepo by lazy {
        TodoRepo(TodoDatabase.getDatabase(app, viewModelScope).todoDao())
    }

    fun addTodo(todo: Todo) {
        viewModelScope.launch { todoRepo.insert(todo) }
    }
}