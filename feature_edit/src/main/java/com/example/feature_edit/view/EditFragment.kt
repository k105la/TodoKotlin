package com.example.feature_edit.view

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
import com.example.feature_edit.databinding.FragmentEditBinding
import com.example.feature_edit.viewmodel.EditViewModel
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch

class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EditViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentEditBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editFragmentAnimation()
    }
    private fun initObservers() {
        confirmDiscard()
        confirmDelete()
        enableOrDisableSaveButton()
    }

    private fun initViews() = with(binding) {
        val todoId = arguments?.getInt("todoId")!!
        lifecycleScope.launch {
            val todo = viewModel.getTodo(todoId)
            topAppBar.setBackgroundColor(todo.color)
            editTitle.setText(todo.title)
            editContent.setText(todo.content)
            fabSave.setOnClickListener {
                viewModel.updateTodo(todoId, editTitle.text.toString(), editContent.text.toString())
                navigateBack()
            }
        }
    }

    /** Helper function that navigates to the todoGraph */
    private fun navigateBack() {
        findNavController().navigate(com.example.todo.R.id.todoGraph)
    }

    private fun confirmDelete() = with(binding) {
        val todoId = arguments?.getInt("todoId")!!
        this.deleteButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Confirm delete")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Confirm") { _, _ ->
                    viewModel.deleteTodo(todoId)
                    navigateBack()
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .show()
        }
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
        this.editTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let { if (s.isNotEmpty()) this@with.fabSave.isEnabled = true }
                s?.let { if (s.isEmpty()) this@with.fabSave.isEnabled = false }
            }
        })
    }

    private fun editFragmentAnimation() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 2000.toLong()
        }
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
            duration = 2000.toLong()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}