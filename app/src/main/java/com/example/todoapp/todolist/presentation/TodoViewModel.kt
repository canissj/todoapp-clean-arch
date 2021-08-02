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
import com.example.todoapp.todolist.framework.DataSourceError
import com.example.todoapp.todolist.framework.SingleLiveEvent
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

    private val _toastMessage: SingleLiveEvent<String> = SingleLiveEvent()
    val toastMessage: SingleLiveEvent<String> = _toastMessage

    // we use always the same user for simplicity
    private val userName = "user1"

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
            when (val result = signIn(userName)) {
                is ResultOf.Success -> {
                    if (result.value) {
                        getTodos()
                    }
                }
                is ResultOf.Failure -> {
                    _state.value = State.ShowRetryError
                }
            }
        }
    }

    fun addNewTodo(todo: String) {
        if (todo.isEmpty()) {
            _toastMessage.value = "Cannot add an empty todo"
            return
        }
        viewModelScope.launch {
            _state.value = State.Loading
            when (val result = addTodo(Todo(name = todo, isDone = false))) {
                is ResultOf.Success -> {
                    cacheTodoList.add(result.value)
                    _state.value = State.ShowTodos(cacheTodoList)
                }
                is ResultOf.Failure -> {
                    handleAuthError(result)
                    _state.value = State.ShowTodos(cacheTodoList)
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
                    handleAuthError(result)
                    if (cacheTodoList.isNotEmpty()) {
                        _state.value = State.ShowTodos(cacheTodoList)
                    } else {
                        _state.value = State.ShowRetryError
                    }
                }
            }
        }
    }

    private fun handleAuthError(failure: ResultOf.Failure) {
        if (failure.throwable is DataSourceError.AuthenticationError) {
            viewModelScope.launch {
                signIn(userName)
            }
        }
    }
}

sealed class State {
    object Loading : State()
    object ShowRetryError : State()
    data class ShowTodos(val todoList: List<Todo>) : State()
}
