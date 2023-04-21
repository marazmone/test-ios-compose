import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AccountSwitcher(
    accounts: List<Account>,
    currentAccount: Account,
    onAccountChanged: (Account) -> Unit,
    modifier: Modifier = Modifier
) {
    val imageSize = 36.dp
    val imageSizePx = with(LocalDensity.current) { imageSize.toPx() }

    val currentAccountIndex = accounts.indexOf(currentAccount)
    var nextAccountIndex by remember { mutableStateOf<Int?>(null) }

    var delta by remember(currentAccountIndex) { mutableStateOf(0f) }
    val draggableState = rememberDraggableState(onDelta = { delta = it })

    val targetAnimation = remember { Animatable(0f) }

    LaunchedEffect(key1 = currentAccountIndex) {
        snapshotFlow { delta }
            .filter { nextAccountIndex == null }
            .filter { it.absoluteValue > 1f }
            .throttleFirst(300)
            .map { delta ->
                if (delta < 0) { // Scroll down (Bottom -> Top)
                    if (currentAccountIndex < accounts.size - 1) 1 else 0
                } else { // Scroll up (Top -> Bottom)
                    if (currentAccountIndex > 0) -1 else 0
                }
            }
            .filter { it != 0 }
            .collect { change ->
                nextAccountIndex = currentAccountIndex + change

                targetAnimation.animateTo(
                    change.toFloat(),
                    animationSpec = tween(easing = LinearEasing, durationMillis = 200)
                )

                onAccountChanged(accounts[nextAccountIndex!!])
                nextAccountIndex = null
                targetAnimation.snapTo(0f)
            }
    }

    Box(modifier = Modifier.size(imageSize)) {
        nextAccountIndex?.let { index ->
            Image(
                painter = painterResource(accounts[index].image),
                contentScale = ContentScale.Crop,
                contentDescription = "Account image",
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = abs(targetAnimation.value)
                        scaleY = abs(targetAnimation.value)
                    }
                    .clip(CircleShape)
            )
        }

        Image(
            painter = painterResource(accounts[currentAccountIndex].image),
            contentScale = ContentScale.Crop,
            contentDescription = "Account image",
            modifier = Modifier
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Vertical,
                )
                .graphicsLayer {
                    this.translationY = targetAnimation.value * imageSizePx * -1.5f
                }
                .clip(CircleShape)
        )
    }
}