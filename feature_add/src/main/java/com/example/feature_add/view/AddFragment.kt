package com.example.feature_add.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.feature_add.databinding.FragmentAddBinding
import com.example.feature_add.viewmodel.AddViewModel
import com.example.model_todo.response.Todo
import kotlinx.coroutines.launch

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentAddBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initObservers() {
        confirmDiscard()
        enableOrDisableSaveButton()
    }

    private fun initViews() = with(binding) {
        fabSave.setOnClickListener {
            val todo = Todo(
                title = addTitle.text.toString(),
                content = addContent.text.toString()
            )
            lifecycleScope.launch {
                viewModel.addTodo(todo)
                navigateBack()
            }
        }
    }

    /** Helper function that navigates to the todoGraph */
    private fun navigateBack() {
        findNavController().navigate(com.example.todo.R.id.todoGraph)
    }

    private fun confirmDiscard() = with(binding) {
        this.discardButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Confirm discard")
                .setMessage("Are you sure you want to discard your changes?")
                .setPositiveButton("Confirm") { _, _ ->
                    navigateBack()
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .show()
        }
    }

    private fun enableOrDisableSaveButton() = with(binding) {
        this.addTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let { if (s.isNotEmpty()) this@with.fabSave.isEnabled = true }
                s?.let { if (s.isEmpty()) this@with.fabSave.isEnabled = false }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}