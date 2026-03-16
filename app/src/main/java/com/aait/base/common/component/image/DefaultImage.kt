package com.aait.base.common.component.image

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import coil.transform.Transformation
import com.aait.base.common.component.utilis.noRippleClickable
import com.mahalatak.R
import kotlinx.coroutines.delay

/**
 * Enhanced image component with loading states, error handling, and retry functionality
 *
 * @param modifier Optional modifier for this component
 * @param url Image URL to load
 * @param placeholder Drawable resource for placeholder while loading
 * @param error Drawable resource for error state
 * @param fallback Drawable resource for fallback state
 * @param contentDescription Accessibility description
 * @param contentScale How to scale the image content
 * @param crossfadeDuration Duration of crossfade animation in milliseconds
 * @param transformations List of Coil image transformations
 * @param shape Optional shape to clip the image
 * @param showLoadingState Whether to show loading indicator
 * @param showErrorState Whether to show error state UI
 * @param retryEnabled Whether to enable retry on error
 * @param retryText Text for retry button (defaults to localized "Retry")
 * @param alpha Alpha transparency value
 * @param imageLoader Optional custom ImageLoader instance
 * @param onClick Callback for image clicks
 * @param onLoading Callback when loading starts
 * @param onSuccess Callback when loading succeeds
 * @param onError Callback when loading fails
 */
@Composable
fun DefaultImage(
    modifier: Modifier = Modifier,
    url: String?,
    placeholder: Int? = null,
    error: Int? = placeholder,
    fallback: Int? = placeholder,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    crossfadeDuration: Int = 300,
    transformations: List<Transformation> = emptyList(),
    shape: Shape? = null,
    showLoadingState: Boolean = true,
    showErrorState: Boolean = true,
    retryEnabled: Boolean = false,
    retryText: String? = null,
    alpha: Float = 1f,
    imageLoader: ImageLoader? = null,
    onClick: (() -> Unit)? = null,
    onLoading: (() -> Unit)? = null,
    onSuccess: (() -> Unit)? = null,
    onError: ((Throwable?) -> Unit)? = null
) {
    val context = LocalContext.current
    var retryCount by remember { mutableIntStateOf(0) }
    var imageState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }

    // Animated alpha for smooth loading
    val animatedAlpha by animateFloatAsState(
        targetValue = if (imageState is AsyncImagePainter.State.Success) alpha else 0.7f,
        animationSpec = tween(crossfadeDuration),
        label = "ImageAlpha"
    )

    val imageRequest = remember(url, retryCount) {
        ImageRequest.Builder(context)
            .data(url)
            .apply {
                if (crossfadeDuration > 0) crossfade(crossfadeDuration)
                if (transformations.isNotEmpty()) transformations(transformations)
            }
            .build()
    }

    Box(
        modifier = modifier
            .let { mod ->
                if (onClick != null) {
                    mod.noRippleClickable { onClick() }
                } else mod
            }
            .let { mod ->
                shape?.let { mod.clip(it) } ?: mod
            },
        contentAlignment = Alignment.Center
    ) {
        if (imageLoader != null) {
            AsyncImage(
                model = imageRequest,
                contentDescription = contentDescription,
                imageLoader = imageLoader,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(animatedAlpha),
                placeholder = placeholder?.let { painterResource(it) },
                error = error?.let { painterResource(it) },
                fallback = fallback?.let { painterResource(it) },
                onLoading = { onLoading?.invoke() },
                onSuccess = { onSuccess?.invoke() },
                onError = { onError?.invoke(it.result.throwable) },
                contentScale = contentScale
            )
        } else {
            AsyncImage(
                model = imageRequest,
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(animatedAlpha),
                placeholder = placeholder?.let { painterResource(it) },
                error = error?.let { painterResource(it) },
                fallback = fallback?.let { painterResource(it) },
                onLoading = {
                    onLoading?.invoke()
                },
                onSuccess = { result ->
                    imageState = result
                    onSuccess?.invoke()
                },
                onError = { result ->
                    imageState = result
                    onError?.invoke(result.result.throwable)
                },
                contentScale = contentScale
            )
        }

        // Loading State
        if (showLoadingState && imageState is AsyncImagePainter.State.Loading) {
            LoadingIndicator()
        }

        // Error State with Retry
        if (showErrorState && imageState is AsyncImagePainter.State.Error && retryEnabled) {
            ErrorState(
                retryText = retryText,
                onRetry = {
                    retryCount++
                }
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 2.dp
        )
    }
}

@Composable
private fun ErrorState(
    retryText: String? = null,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(android.R.drawable.ic_menu_report_image),
                contentDescription = "Error",
                tint = Color.Gray,
                modifier = Modifier.size(32.dp)
            )

            TextButton(
                onClick = onRetry,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = retryText ?: stringResource(id = R.string.retry),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Profile/Avatar specific image component
 */
@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    url: String?,
    size: androidx.compose.ui.unit.Dp = 40.dp,
    placeholder: Int? = null,
    contentDescription: String? = "Profile Image",
    onClick: (() -> Unit)? = null
) {
    DefaultImage(
        modifier = modifier.size(size),
        url = url,
        placeholder = placeholder,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        shape = RoundedCornerShape(size / 2), // Circular
        onClick = onClick
    )
}

/**
 * Card image component with rounded corners
 */
@Composable
fun CardImage(
    modifier: Modifier = Modifier,
    url: String?,
    aspectRatio: Float = 16f / 9f,
    cornerRadius: androidx.compose.ui.unit.Dp = 12.dp,
    placeholder: Int? = null,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null
) {
    DefaultImage(
        modifier = modifier.aspectRatio(aspectRatio),
        url = url,
        placeholder = placeholder,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        shape = RoundedCornerShape(cornerRadius),
        onClick = onClick
    )
}

/**
 * Full width banner image
 */
@Composable
fun BannerImage(
    modifier: Modifier = Modifier,
    url: String?,
    height: androidx.compose.ui.unit.Dp = 200.dp,
    placeholder: Int? = null,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null
) {
    DefaultImage(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        url = url,
        placeholder = placeholder,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        onClick = onClick
    )
}

/**
 * Gallery image with loading shimmer effect
 */
@Composable
fun GalleryImage(
    modifier: Modifier = Modifier,
    url: String?,
    placeholder: Int? = null,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null
) {
    var isLoading by remember { mutableStateOf(true) }

    Box(modifier = modifier) {
        DefaultImage(
            modifier = Modifier.fillMaxSize(),
            url = url,
            placeholder = placeholder,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            showLoadingState = false, // Custom loading
            onLoading = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = { isLoading = false },
            onClick = onClick
        )

        // Custom shimmer loading effect
        if (isLoading) {
            ShimmerEffect(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Shimmer loading effect
 */
@Composable
private fun ShimmerEffect(
    modifier: Modifier = Modifier
) {
    var shimmerAlpha by remember { mutableFloatStateOf(0.3f) }

    LaunchedEffect(Unit) {
        while (true) {
            shimmerAlpha = if (shimmerAlpha == 0.3f) 0.7f else 0.3f
            delay(800)
        }
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = shimmerAlpha,
        animationSpec = tween(800),
        label = "ShimmerAlpha"
    )

    Box(
        modifier = modifier
            .background(Color.Gray.copy(alpha = animatedAlpha))
    )
}

/**
 * Extension functions for common use cases
 */
fun Modifier.defaultImageClickable(onClick: () -> Unit): Modifier {
    return this.clickable { onClick() }
}

/**
 * Preview composables for testing
 */
@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun DefaultImagePreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Regular image
        DefaultImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            url = "https://images.unsplash.com/photo-1519167758481-83f550bb49b3",
            contentDescription = "Sample image"
        )

        // Profile image
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileImage(
                url = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d",
                size = 60.dp
            )
            ProfileImage(
                url = "https://images.unsplash.com/photo-1494790108755-2616b2e58e6c",
                size = 80.dp
            )
        }

        // Card image
        CardImage(
            modifier = Modifier.fillMaxWidth(),
            url = "https://images.unsplash.com/photo-1511285560929-80b456fea0bc"
        )
    }
}