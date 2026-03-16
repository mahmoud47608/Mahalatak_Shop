package com.aait.base.common.component.attachment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.aait.base.common.component.utilis.DashedBox
import com.aait.base.ui.theme.PaddingDimensions

/**
 * A reusable component for attaching media (image, video, or file).
 *
 * @param modifier Optional modifier for styling
 * @param type The type of attachment (IMAGE, VIDEO, or FILE)
 * @param title Custom title for the component. If null, uses the default title based on type.
 * @param errorMessageResId Optional error message resource ID (displayed in red)
 * @param hasError Whether to show error state on the dashed box
 * @param onAttachClicked Callback when the dashed box is clicked
 */
@Composable
fun AttachMediaComponent(
    modifier: Modifier = Modifier,
    type: AttachmentType,
    title: String? = null,
    errorMessageResId: Int? = null,
    hasError: Boolean = errorMessageResId != null,
    onAttachClicked: () -> Unit
) {
    Column(
        modifier = modifier.wrapContentWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title ?: stringResource(type.defaultTitleRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(PaddingDimensions.small))
        Row(verticalAlignment = Alignment.CenterVertically) {
            DashedBox(
                onClicked = onAttachClicked,
                hasError = hasError
            ) {
                Image(
                    painter = painterResource(type.iconRes),
                    contentDescription = stringResource(type.defaultTitleRes)
                )
            }
            errorMessageResId?.let {
                Spacer(modifier = Modifier.width(PaddingDimensions.small))
                Text(
                    text = stringResource(errorMessageResId),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Convenience composable for attaching images.
 */
@Composable
fun AttachImageComponent(
    modifier: Modifier = Modifier,
    title: String? = null,
    errorMessageResId: Int? = null,
    hasError: Boolean = errorMessageResId != null,
    onPickImageClicked: () -> Unit
) {
    AttachMediaComponent(
        modifier = modifier,
        type = AttachmentType.IMAGE,
        title = title,
        errorMessageResId = errorMessageResId,
        hasError = hasError,
        onAttachClicked = onPickImageClicked
    )
}

/**
 * Convenience composable for attaching videos.
 */
@Composable
fun AttachVideoComponent(
    modifier: Modifier = Modifier,
    title: String? = null,
    errorMessageResId: Int? = null,
    hasError: Boolean = errorMessageResId != null,
    onPickVideoClicked: () -> Unit
) {
    AttachMediaComponent(
        modifier = modifier,
        type = AttachmentType.VIDEO,
        title = title,
        errorMessageResId = errorMessageResId,
        hasError = hasError,
        onAttachClicked = onPickVideoClicked
    )
}

/**
 * Convenience composable for attaching files.
 */
@Composable
fun AttachFileComponent(
    modifier: Modifier = Modifier,
    title: String? = null,
    errorMessageResId: Int? = null,
    hasError: Boolean = errorMessageResId != null,
    onPickFileClicked: () -> Unit
) {
    AttachMediaComponent(
        modifier = modifier,
        type = AttachmentType.FILE,
        title = title,
        errorMessageResId = errorMessageResId,
        hasError = hasError,
        onAttachClicked = onPickFileClicked
    )
}
