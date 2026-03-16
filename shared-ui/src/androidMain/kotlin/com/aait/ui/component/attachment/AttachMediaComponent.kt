package com.aait.ui.component.attachment

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
import com.aait.ui.component.utilis.DashedBox
import com.aait.ui.theme.PaddingDimensions

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
