package io.garrit.android.demo.tododemo

import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.garrit.android.demo.tododemo.ui.theme.TodoDemoTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    var isChecked: MutableState<Boolean> = mutableStateOf(false)
)

var i = 0

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val list = remember {
                mutableStateListOf(Task(title = "Hello"), Task(title = "World"))
            }

            TodoDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(list = list)
                }
            }
        }
    }
}

@Composable
fun MainScreen(list: MutableList<Task>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        TextInputView(list = list)
        ListView(list = list)
    }
}



@Composable
fun TextInputView(list: MutableList<Task>) {
    var title by rememberSaveable {
        mutableStateOf("")
    }
    var mainText by rememberSaveable {
        mutableStateOf("")
    }
    var additionalInfo by rememberSaveable {
        mutableStateOf("")
    }


    val inputList:List<String> = listOf("add title to note", "add text to note", "any additional info")



    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = title, onValueChange = {
            title = it
        }, label = {Text("Title on note ")}
        )
        OutlinedTextField(value = mainText, onValueChange = {
            mainText = it
        }, label = {Text("MainText on note  ")}
        )
        OutlinedTextField(value = additionalInfo, onValueChange = {
            additionalInfo = it
        }, label = {Text("AdditionalInfo on note")}
        )
        Button(onClick = {
            if(title.length >= 3 && title.length <= 50 && mainText.length < 120){
                list.add(Task(title = "hello"))
                title = ""
                mainText = ""
                additionalInfo = ""
            }


        }) {
            Text("Add")
        }
    }
}

@Composable
fun ListView(list: List<Task>) {
    LazyColumn {
        items(list) { task ->
            RowView(task)
        }
    }
}

@Composable
fun RowView(task: Task) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isChecked.value,
            onCheckedChange = {
                task.isChecked.value = !task.isChecked.value
            }
        )
        Text(task.title)
    }
}

@Preview(showBackground = true)
@Composable
fun RowViewPreview() {



    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 100.dp)
    ) {
        Text(text = "m",

            )
        Text(
            text = "Ffasfdasfdsafsa",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Button() { }
    }




    /*
    val list = mutableStateListOf(Task(title =  "hello"), Task(title = " tjao"))
    TodoDemoTheme {
        LazyColumn() {
            items(list) { task ->
                RowView(task)
            }
        }
    }*/
}