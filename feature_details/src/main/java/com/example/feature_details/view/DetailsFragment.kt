package com.example.feature_details.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.feature_details.databinding.FragmentDetailsBinding
import com.example.feature_details.viewmodel.DetailsViewModel
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentDetailsBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initObservers() {
        confirmDelete()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsFragmentAnimation()
    }

    private fun initViews() = with(binding) {
        lifecycleScope.launch {
            val todo = viewModel.getTodo(arguments?.getInt("todoId")!!)
            tvTitle.text = todo.title
            tvContent.text = todo.content
            cbCompleted.isChecked = todo.isComplete
            topAppBar.setBackgroundColor(todo.color)
            cbCompleted.setOnClickListener {
                viewModel.checkCompleted(todo.id, cbCompleted.isChecked)
            }
        }
        discardButton.setOnClickListener { navigateBack() }
    }

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

    private fun detailsFragmentAnimation() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 1500.toLong()
        }
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 1500.toLong()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}