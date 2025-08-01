package com.sanjeetlittle.to_doo.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanjeetlittle.to_doo.data.models.Priority
import com.sanjeetlittle.to_doo.data.models.ToDoTask
import com.sanjeetlittle.to_doo.data.repositories.DataStoreRepository
import com.sanjeetlittle.to_doo.data.repositories.ToDoRepository
import com.sanjeetlittle.to_doo.util.Action
import com.sanjeetlittle.to_doo.util.Constants.MAX_TITLE_LENGTH
import com.sanjeetlittle.to_doo.util.RequestState
import com.sanjeetlittle.to_doo.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: ToDoRepository, private val dataStoreRepository: DataStoreRepository): ViewModel() {

    var action by mutableStateOf(Action.NO_ACTION)
        private set

    var id by mutableStateOf(0)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var priority by mutableStateOf(Priority.LOW)
        private set

    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED)
        private set

    var searchTextState by mutableStateOf("")
        private set

    private val  _searchedTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks

    private val  _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    init {
        getAllTasks()
        readSortState()
    }

    fun searchDatabase(searchQuery: String){
        _searchedTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = "%$searchQuery%").collect{
                    _searchedTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception){
            _searchedTasks.value = RequestState.Error(e)
        }
        searchAppBarState = SearchAppBarState.TRIGGERED
    }

    val lowPriorityTasks: StateFlow<List<ToDoTask>> = repository.sortByLowPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val highPriorityTasks: StateFlow<List<ToDoTask>> = repository.sortByHighPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        emptyList()
    )

    private fun readSortState(){
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                    .map { Priority.valueOf(it) }
                    .collect{
                        _sortState.value = RequestState.Success(it)
                    }
            }
        } catch (e: Exception){
            _sortState.value = RequestState.Error(e)
        }
    }

    fun persistSortState(priority: Priority){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persisSortState(priority = priority)
        }
    }

    private fun getAllTasks(){
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTasks.collect{
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception){
            _allTasks.value = RequestState.Error(e)
        }
    }

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    fun getSelectedTask(taskId: Int){
        viewModelScope.launch {
            repository.getSelectedTask(taskId).collect{
                _selectedTask.value = it
            }
        }
    }

    private fun addTask(){
        viewModelScope.launch(Dispatchers.IO){
            val toDoTask = ToDoTask(
                title = title,
                description = description,
                priority = priority
            )
            repository.addTask(toDoTask)
        }
        searchTextState = ""
        searchAppBarState = SearchAppBarState.CLOSED
    }

    private fun updateTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id,
                title = title,
                description = description,
                priority = priority
            )
            repository.updateTask(toDoTask)
        }
    }

    private fun deleteTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id,
                title = title,
                description = description,
                priority = priority
            )
            repository.deleteTask(toDoTask)
        }
    }

    private fun deleteAllTasks(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTask()
        }
    }

    fun handleDatabaseActions(action: Action){
        when (action){
            Action.ADD -> {
                addTask()
            }
            Action.UPDATE -> {
                updateTask()
            }
            Action.DELETE -> {
                deleteTask()
            }
            Action.DELETE_ALL -> {
                deleteAllTasks()
            }
            Action.UNDO -> {
                addTask()
            }
            else -> {}
        }
    }

    fun updateTaskFields(selectedTask: ToDoTask?){
        if (selectedTask != null){
            id = selectedTask.id
            title = selectedTask.title
            description = selectedTask.description
            priority = selectedTask.priority
        } else {
            id = 0
            title = ""
            description = ""
            priority = Priority.LOW
        }
    }

    fun updateTitle(newTitle: String){
        if (newTitle.length <= MAX_TITLE_LENGTH){
            title = newTitle
        }
    }

    fun updateAction(newAction: Action){
        action = newAction
    }

    fun updateDescription(newDescription: String){
        description = newDescription
    }

    fun updatePriority(newPriority: Priority){
        priority = newPriority
    }

    fun updateAppBarState(newState: SearchAppBarState){
        searchAppBarState = newState
    }
    fun updateSearchText(newText: String){
        searchTextState = newText
    }

    fun validateFields(): Boolean{
        return title.isNotEmpty() && description.isNotEmpty()
    }
}