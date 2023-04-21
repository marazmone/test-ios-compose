import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    var selectedAccount by remember { mutableStateOf(accounts[0]) }

    Card(
        shape = RoundedCornerShape(32.dp),
        contentColor = Color.Gray,
        backgroundColor = Color(0xD22E2C37),
        modifier = modifier.wrapContentHeight(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 12.dp)
        ) {
            IconButton(
                onClick = {
                    println("click on menu")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    tint = Color.White,
                    contentDescription = "Menu"
                )
            }

            Text(
                text = "Search in mail",
                color = Color(0xFFBCBAC5),
                modifier = Modifier.weight(1f)
            )

            AccountSwitcher(
                accounts = accounts,
                currentAccount = selectedAccount,
                onAccountChanged = {
                    println(it)
                    selectedAccount = it
                }
            )
        }
    }
}