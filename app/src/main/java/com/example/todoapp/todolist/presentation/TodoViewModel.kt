package com.example.todoapp.todolist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.ResultOf
import com.example.core.domain.Todo
import com.example.core.usecases.AddTodo
import com.example.core.usecases.GetAllTodos
import com.example.core.usecases.GetToken
import com.example.core.usecases.SignIn
import kotlinx.coroutines.launch

class TodoViewModel(
    private val signIn: SignIn,
    private val getAllTodos: GetAllTodos,
    private val addTodo: AddTodo,
    private val getToken: GetToken
) : ViewModel() {

    private val cacheTodoList = mutableListOf<Todo>()

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> = _state

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

    init {
        start()
    }

    private fun start() {
        if (!getToken().isNullOrEmpty()) {
            getTodos()
            return
        }

        viewModelScope.launch {
            _state.value = State.Loading
            when (val sigInResult = signIn("user1")) {
                is ResultOf.Success -> {
                    if (sigInResult.value) {
                        getTodos()
                    }
                }
                is ResultOf.Failure -> {
                    showRetryError()
                }
            }
        }
    }

    private fun showRetryError() {
        _state.value = State.ShowRetryError
    }

    fun addNewTodo(todo: Todo) {
        viewModelScope.launch {
            _state.value = State.Loading
            when (val result = addTodo(todo)) {
                is ResultOf.Success -> {
                    cacheTodoList.add(result.value)
                    _state.value = State.ShowTodos(cacheTodoList)
                }
                is ResultOf.Failure -> {
                    _toastMessage.value = "Could not add new todo"
                }
            }
        }
    }

    fun getTodos() {
        viewModelScope.launch {
            _state.value = State.Loading
            when (val result = getAllTodos()) {
                is ResultOf.Success -> {
                    _state.value = State.ShowTodos(result.value)
                    cacheTodoList.clear()
                    cacheTodoList.addAll(result.value)
                }
                is ResultOf.Failure -> {
                    _state.value = State.ShowRetryError
                }
            }
        }
    }
}

sealed class State {
    object Loading : State()
    object ShowRetryError : State()
    data class ShowTodos(val todoList: List<Todo>) : State()
}
