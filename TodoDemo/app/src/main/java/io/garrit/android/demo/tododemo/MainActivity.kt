package io.garrit.android.demo.tododemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

data class Note(
    val id: Int,
    var title: String,
    var subtitle: String,
    val check: MutableState<Boolean> = mutableStateOf(false)
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NoteApp()
        }
    }
}

@Composable
fun NoteApp() {
    val navController = rememberNavController()
    val ListNote = remember { mutableStateListOf<Note>() }

    NavHost(navController = navController, startDestination = "todoList") {
        composable("todoList") { ListScreen(navController, ListNote) }
        composable("addTodo") { AddNoteScreen(navController, ListNote) }
        composable("editTodo/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull()
            val todoItem = ListNote.find { it.id == itemId }
            todoItem?.let { EditNoteScreen(navController, it) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController, todoList: MutableList<Note>) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Notes app") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addTodo") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(todoList) { item ->
                ListItem(
                    leadingContent = {
                        Checkbox(
                            checked = item.check.value,
                            onCheckedChange = {
                                item.check.value = !item.check.value
                            })},
                    headlineContent = { Text(item.title) },
                    supportingContent = { Text(item.subtitle)},
                    trailingContent = {
                        Row {
                            IconButton(
                                onClick = { navController.navigate("editTodo/${item.id}") }
                            ) {
                                Icon(Icons.Filled.Edit, contentDescription = "Edit note")
                            }
                            IconButton(
                                onClick = { todoList.remove(item) }
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete Note")
                            }
                        }
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(navController: NavController, todoList: MutableList<Note>) {
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Todo") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (title.isNotBlank() && subtitle.isNotBlank()) {
                                todoList.add(Note(id = todoList.size, title = title, subtitle = subtitle))
                                navController.popBackStack()
                            }
                        }) {

                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Todo") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = subtitle,
                onValueChange = { subtitle = it },
                label = { Text("Details") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (title.isNotBlank() && subtitle.isNotBlank()) {
                    todoList.add(Note(id = todoList.size, title = title, subtitle = subtitle))
                    navController.popBackStack()
                }
            }) {
                Text("Add Todo")
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(navController: NavController, todoItem: Note) {
    var title by remember { mutableStateOf(todoItem.title) }
    var subtitle by remember { mutableStateOf(todoItem.subtitle) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Todo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {

                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title For the Note") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = subtitle,
                onValueChange = { subtitle = it },
                label = { Text("Edit text") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (title.isNotBlank() && subtitle.isNotBlank()) {
                    todoItem.title = title
                    todoItem.subtitle = subtitle
                    navController.popBackStack()
                }
            }) {
                Text("Save Todo")
            }
        }
    }
}