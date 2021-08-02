package com.example.todoapp.todolist.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoapp.databinding.FragmentTodoListBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TodoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null

    private val todoViewModel: TodoViewModel by sharedViewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todoAdapter = TodoAdapter()
        binding.todoListRecyclerView.apply {
            adapter = todoAdapter
        }

        todoViewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is State.Loading -> {
                    with(binding) {
                        progressBar.visibility = View.VISIBLE
                        todoContainer.visibility = View.GONE
                        retryErrorContainer.root.visibility = View.GONE
                    }
                }
                is State.ShowRetryError -> {
                    with(binding) {
                        progressBar.visibility = View.GONE
                        retryErrorContainer.root.visibility = View.VISIBLE
                    }
                }
                is State.ShowTodos -> {
                    todoAdapter.addAll(state.todoList)
                    with(binding) {
                        progressBar.visibility = View.GONE
                        todoContainer.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}