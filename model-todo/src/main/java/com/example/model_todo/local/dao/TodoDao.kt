package com.example.model_todo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.model_todo.response.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    // The flow always holds/caches latest version of data. Notifies its observers when the
    // data has changed.
    @Query("SELECT * FROM todo")
    fun getTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM todo WHERE isComplete IN (:complete)")
    fun getTodos(complete: Boolean): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: Todo)

    @Query("SELECT * FROM todo WHERE id=:id ")
    suspend fun getTodo(id: Int) : Todo

    @Query("UPDATE todo SET isComplete = :complete WHERE id=:id")
    suspend fun setComplete(id: Int, complete: Boolean)

    @Query("UPDATE todo SET title = :title, content = :content WHERE id=:id")
    suspend fun updateTodo(id: Int, title: String, content: String)

    @Query("DELETE FROM todo WHERE id = :id")
    suspend fun deleteTodo(id: Int)

}