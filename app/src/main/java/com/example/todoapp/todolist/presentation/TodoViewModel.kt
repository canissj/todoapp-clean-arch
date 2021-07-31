package com.example.todoapp.todolist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.TodoRepository
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo
import kotlinx.coroutines.launch

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> = _state

    fun getTodos() {
        viewModelScope.launch {
            _state.value = State.Loading
            when (val result = todoRepository.getAllTodos()) {
                is ResultOf.Success -> {
                    _state.value = State.ShowTodos(result.value)
                }
                is ResultOf.Failure -> {
                    _state.value = State.Error
                }
            }
        }
    }
}

sealed class State {
    object Loading : State()
    object Error : State()
    data class ShowTodos(val todoList: List<Todo>) : State()
}
