package com.aait.base.common.component.utilis

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    maxRating: Int = 5,
    onRatingChanged: (Int) -> Unit = {}
) {
    Row(modifier = modifier) {
        for (i in 1..maxRating) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (i <= rating) Color(0xFFFFC107) else Color.LightGray,
                modifier = Modifier
                    .size(32.dp)
                    .noRippleClickable { onRatingChanged(i) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRatingBar() {
    var rating by remember { mutableStateOf(4) } // Example: 4 out of 5
    RatingBar(
        rating = rating,
        onRatingChanged = { rating = it }
    )
}
