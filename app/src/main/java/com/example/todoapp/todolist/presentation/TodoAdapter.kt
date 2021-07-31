package com.example.todoapp.todolist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core.domain.Todo
import com.example.todoapp.databinding.TodoItemBinding

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private val todoList = mutableListOf<Todo>()

    class ViewHolder(private val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            binding.todoTitle.text = todo.name
            binding.todoCheckbox.isSelected = todo.isDone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todoList[position])
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun addAll(todoList: List<Todo>) {
        this.todoList.clear()
        this.todoList.addAll(todoList)
        notifyDataSetChanged()
    }
}