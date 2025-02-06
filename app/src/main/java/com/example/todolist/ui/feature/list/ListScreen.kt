package com.example.todolist.ui.feature.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.data.ToDoDatabaseProvider
import com.example.todolist.data.ToDoRepositoryImpl
import com.example.todolist.domain.ToDo
import com.example.todolist.domain.todo1
import com.example.todolist.domain.todo2
import com.example.todolist.domain.todo3
import com.example.todolist.navigation.AddEditRoute
import com.example.todolist.ui.UiEvent
import com.example.todolist.ui.components.ToDoItem
import com.example.todolist.ui.theme.ToDoListTheme

@Composable
fun ListScreen(
    navigateToAddEditScreen: (id: Long?) -> Unit,
) {

    val context = LocalContext.current.applicationContext
    val database = ToDoDatabaseProvider.provide(context)
    val repository = ToDoRepositoryImpl(
        dao = database.toDoDao
    )
    val viewModel = viewModel<ListViewModel> {
        ListViewModel(repository = repository)
    }

    val toDos by viewModel.toDos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Navigate<*> -> {
                    when (uiEvent.route) {
                        is AddEditRoute -> {
                            navigateToAddEditScreen(uiEvent.route.id)
                        }
                    }
                }
                UiEvent.NavigateBack -> {

                }
                is UiEvent.ShowSnackbar -> {

                }
            }
        }
    }

    ListContent(
        todos = toDos,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ListContent(
    todos: List<ToDo>,
    onEvent: (ListEvent) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ListEvent.AddEdit(null))
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .consumeWindowInsets(paddingValues),
            contentPadding = PaddingValues(12.dp)
        ) {
            itemsIndexed(todos) { index, todo ->
                ToDoItem(
                    todo = todo,
                    onCompleteChange = {
                        onEvent(ListEvent.CompleteChanged(todo.id, it))
                    },
                    onItemClicked = {
                        onEvent(ListEvent.AddEdit(todo.id))
                    },
                    onDeleteClicked = {
                        onEvent(ListEvent.Delete(todo.id))
                    }
                )
                if (index < todos.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun ListScreenPreview() {
    ToDoListTheme {
        ListContent(
            todos = listOf(
                todo1,
                todo2,
                todo3,
            ),
            onEvent = {},
        )
    }
}