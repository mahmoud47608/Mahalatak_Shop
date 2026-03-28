package com.mahalatk.common.component.videopicker

import androidx.compose.runtime.Composable

/**
 * Remembers a video picker launcher that works on both Android and iOS.
 *
 * @param onVideoPicked callback with the video data as ByteArray
 * @return a lambda that launches the video picker when invoked
 */
@Composable
expect fun rememberVideoPickerLauncher(
    onVideoPicked: (ByteArray) -> Unit
): () -> Unit
