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
import kotlinx.coroutines.launch

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

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }




    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Note") },
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                label = { Text("Title for note") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = subtitle,
                onValueChange = { subtitle = it },
                label = { Text("Text for note") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {

                    if(title.length <= 3){
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "title must be longer than 3 letter",
                                actionLabel = "OK",
                                duration = SnackbarDuration.Short
                            )}}

                    if(title.length >= 50){
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "title must be shorter than 50 letters",
                                actionLabel = "OK",
                                duration = SnackbarDuration.Short
                            )}}

                    if(subtitle.length >= 120 ){
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "text should not be longer than 120 letters",
                                actionLabel = "OK",
                                duration = SnackbarDuration.Short
                            )}}

                    //Go back if the requirements are met
                    if(title.length > 3 && title.length < 50 && subtitle.length < 120){
                        todoList.add(Note(id = todoList.size, title = title, subtitle = subtitle))
                        navController.popBackStack()
                    }
            }) {
                Text("Add note")
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(navController: NavController, note: Note) {
    var title by remember { mutableStateOf(note.title) }
    var subtitle by remember { mutableStateOf(note.subtitle) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Note") },
                navigationIcon = {
                    IconButton(onClick = {

                        navController.popBackStack()
                    }) {
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                label = { Text("Edit title") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = subtitle,
                onValueChange = { subtitle = it },
                label = { Text("Edit text") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {

                    if(title.length <= 3  || title.length >= 50){
                        if(title.length <= 3){
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "title must be longer than 3 letters",
                                actionLabel = "OK",
                                duration = SnackbarDuration.Short
                            )}}else{
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "title must be shorter than 50 letters",
                                        actionLabel = "OK",
                                        duration = SnackbarDuration.Short
                                    )}}
                    }else{
                        note.title = title
                    }

                    if(subtitle.length >= 120 ){
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "text should not be longer than 120 letters",
                                actionLabel = "OK",
                                duration = SnackbarDuration.Short
                            )}}else{
                        note.subtitle = subtitle
                    }

                    //Go back if the requirements are met
                    if(title.length in 4..49 && subtitle.length < 120){
                        navController.popBackStack()
                    }
            }) {
                Text("Save changes")
            }
        }
    }
}