package com.example.feature_todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.feature_todo.adapter.TodoAdapter
import com.example.feature_todo.databinding.FragmentListBinding
import com.example.model_todo.response.Todo
import com.example.model_todo.util.FilterOption
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val todoAdapter by lazy { TodoAdapter(::editClicked, ::todoClicked) }
    private val todoViewModel by viewModels<TodoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentListBinding.inflate(
        inflater, container, false
    ).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        todoViewModel.todos.observe(viewLifecycleOwner) { todos -> todoAdapter.submitList(todos) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listFragmentAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() = with(binding) {
        rvTodos.adapter = todoAdapter
        val swipeToDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val todo = todoAdapter.getTodoAt(viewHolder.adapterPosition)
                todoViewModel.deleteTodo(todo.id)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(rvTodos)

        chipCompleted.setOnCheckedChangeListener { _, isChecked ->
            // Responds to chip checked/unchecked
            todoViewModel.updateFilter(if (isChecked) FilterOption.COMPLETED else FilterOption.ALL)
        }

        fabAdd.setOnClickListener {
            findNavController().navigate(
                resId = com.example.todo.R.id.addGraph,
            )
        }
    }

    private fun editClicked(todo: Todo) {
        findNavController().navigate(
            resId = com.example.todo.R.id.editGraph,
            args = bundleOf("todoId" to todo.id)
        )
    }

    private fun todoClicked(todo: Todo) {
        findNavController().navigate(
            resId = com.example.todo.R.id.detailsGraph,
            args = bundleOf(
                "todoId" to todo.id,
                "todoTitle" to todo.title,
                "todoContent" to todo.content,
                "todoComplete" to todo.isComplete,
            )
        )

    }

    private fun listFragmentAnimation() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
            duration = 2000.toLong()
        }
    }
}