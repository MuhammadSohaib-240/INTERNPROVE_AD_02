package com.example.todolist.ui.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.ui.view.components.AddTaskOverlay
import com.example.todolist.ui.view.components.Task
import com.example.todolist.viewmodel.TaskListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(viewModel: TaskListViewModel = viewModel()) {

    val tasks by viewModel.tasks.observeAsState(listOf())
    var showAddTaskDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "To-Do List",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showAddTaskDialog = true },
                        modifier = Modifier
                            .background(Color(0xff051956), shape = RoundedCornerShape(50.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Task",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xff3451a1)
                )
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xff3451a1))
                    .padding(paddingValues)
            ) {
                item {
                    Text(
                        text = "TASKS LIST",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(tasks) { taskData ->
                    Task(
                        id = taskData.id,
                        title = taskData.title,
                        isChecked = taskData.isChecked,
                        onCheckedChange = { isChecked ->
                            viewModel.updateTask(taskData.copy(isChecked = isChecked))
                        },
                        onDeleteTask = { taskId ->
                            viewModel.deleteTask(taskId) // Pass delete function
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (showAddTaskDialog) {
                AddTaskOverlay(
                    onTaskAdded = { ->
                        showAddTaskDialog = false
                    },
                    onCancel = {
                        showAddTaskDialog = false
                    }
                )
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 488, heightDp = 700)
@Composable
fun TaskListScreenPreview() {
    TaskListScreen()
}
