package com.example.feature_edit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model_todo.TodoRepo
import com.example.model_todo.local.TodoDatabase
import com.example.model_todo.response.Todo
import kotlinx.coroutines.launch

class EditViewModel(app: Application) : AndroidViewModel(app) {
    private val todoRepo by lazy {
        TodoRepo(TodoDatabase.getDatabase(app, viewModelScope).todoDao())
    }

    suspend fun getTodo(todoId: Int): Todo {
        return todoRepo.getTodo(todoId)
    }

    fun updateTodo(todoId: Int, title: String, content: String) {
        viewModelScope.launch {
            todoRepo.updateTodo(todoId, title, content)
        }
    }

    fun deleteTodo(todoId: Int) {
        viewModelScope.launch {
            todoRepo.deleteTodo(todoId)
        }
    }

}