package com.example.todoapp.todolist.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoapp.databinding.FragmentFirstBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TodoListFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val todoViewModel: TodoViewModel by sharedViewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
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

                }
                is State.Error -> {

                }
                is State.ShowTodos -> {
                    todoAdapter.addAll(state.todoList)
                }
            }
        })

        todoViewModel.getTodos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}