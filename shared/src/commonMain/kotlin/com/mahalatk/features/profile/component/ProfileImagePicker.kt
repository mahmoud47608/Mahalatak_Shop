package com.mahalatk.features.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mahalatk.common.component.imagepicker.toImageBitmap
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_camera
import mahalatk.shared.generated.resources.ic_profile
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileImagePicker(
    imageUrl: String = "",
    imageBytes: ByteArray? = null,
    label: String,
    errorText: String? = null,
    editable: Boolean = false,
    onPickImage: () -> Unit = {},
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MahalatkTheme.iconBackground),
                contentAlignment = Alignment.Center,
            ) {
                if (imageBytes != null) {
                    val bitmap = imageBytes.toImageBitmap()
                    if (bitmap != null) {
                        Image(
                            painter = BitmapPainter(bitmap),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    }
                } else if (imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Image(
                        painter = painterResource(Res.drawable.ic_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 14.dp),
                    )
                }
            }

            if (editable) {
                IconButton(
                    onClick = onPickImage,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(40.dp)
                        .offset(x = 1.dp, y = 1.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_camera),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Text(
            text = label,
            style = MahalatkTheme.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MahalatkTheme.black,
            modifier = Modifier.padding(top = 8.dp),
        )

        if (errorText != null) {
            Text(
                text = errorText,
                style = MahalatkTheme.bodySmall,
                color = MahalatkTheme.error,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}
