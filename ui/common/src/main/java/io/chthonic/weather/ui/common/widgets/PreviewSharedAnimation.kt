package io.chthonic.weather.ui.common.widgets

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import io.chthonic.weather.ui.common.theme.AppTheme

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedContentLambdaTargetStateParameter")
fun PreviewSharedAnimation(content: @Composable (SharedTransitionScope, AnimatedContentScope)-> Unit) {
    SharedTransitionLayout {
        AnimatedContent(targetState = null, label = "") {
            AppTheme() {
                content(this@SharedTransitionLayout, this)
            }
        }
    }
}