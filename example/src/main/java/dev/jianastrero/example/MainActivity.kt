package dev.jianastrero.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.jianastrero.logstronaut.logger.logD
import dev.jianastrero.example.ui.theme.LogstronautTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        testLog()
        CoroutineScope(Dispatchers.Default).launch {
            nestedFunc1()
        }
        nestedFunc1()

        setContent {
            LogstronautTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LogstronautTheme {
        Greeting("Android")
    }
}

fun testLog() {
    "Hello World".logD()
}

fun nestedFunc1() {
    nestedFunc2()
}

fun nestedFunc2() {
    nestedFunc3()
}

fun nestedFunc3() {
    listOf(
        "Hello World",
        24,
        12.5,
        false,
        13.64f,
        mapOf(
            "key" to "value",
            "key2" to 25,
            12 to 69.420f,
            69.420 to listOf("Hello world", "Hello world 2"),
        ),
    ).logD()
}
