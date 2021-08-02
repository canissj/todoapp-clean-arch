package com.example.todoapp.todolist.presentation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.todoapp.databinding.AddTodoDialogBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddTodoDialog : DialogFragment() {

    private var _binding: AddTodoDialogBinding? = null

    private val todoViewModel: TodoViewModel by sharedViewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddTodoDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addTodoOkBtn.setOnClickListener {
            todoViewModel.addNewTodo(binding.addTodoInputText.text?.toString() ?: "")
            dismiss()
        }
    }

    companion object {
        fun newInstance(): AddTodoDialog {
            return AddTodoDialog()
        }
    }
}