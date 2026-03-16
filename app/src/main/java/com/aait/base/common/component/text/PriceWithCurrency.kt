package com.aait.base.common.component.text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.aait.base.R

@Composable
fun PriceWithCurrency(
    price: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = 14.sp,
        textDirection = TextDirection.Rtl
    )
) {
    Text(
        modifier = modifier,
        text = (price ?: "0.0") + "  " + stringResource(R.string.rsa_currency),
        style = style,
        color = color
    )
}
