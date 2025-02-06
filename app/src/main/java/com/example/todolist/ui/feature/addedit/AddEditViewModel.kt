package com.example.todolist.ui.feature.addedit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.todolist.data.ToDoRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.todolist.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditViewModel(
    private val id: Long? = null,
    private val repository: ToDoRepository,
) : ViewModel() {

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf<String?>(null)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        id?.let {
            viewModelScope.launch {
                val toDo = repository.getBy(it)
                title = toDo?.title ?: ""
                description = toDo?.description
            }
        }
    }

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.TitleChanged -> {
                title = event.title
            }
            is AddEditEvent.DescriptionChanged -> {
                description = event.description
            }
            AddEditEvent.Save -> {
                saveToDo()
            }
        }
    }

    private fun saveToDo() {
        viewModelScope.launch {
            if (title.isBlank()) {
                _uiEvent.send(UiEvent.ShowSnackbar("O título não pode estar em branco!"))
                return@launch
            }
            if (id != null) {
                repository.insert(title, description, id)
                _uiEvent.send(UiEvent.ShowSnackbar("Tarefa editada!"))
            } else {
                repository.insert(title, description)
                _uiEvent.send(UiEvent.ShowSnackbar("Tarefa criada!"))
            }
            _uiEvent.send(UiEvent.NavigateBack)
        }
    }
}