import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SearchBar(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .padding(horizontal = 16.dp),
            )
        }
    }
}

expect fun getPlatformName(): String