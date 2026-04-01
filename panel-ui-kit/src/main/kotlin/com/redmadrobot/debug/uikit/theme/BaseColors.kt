@file:Suppress("MagicNumber")

package com.redmadrobot.debug.uikit.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal object BaseColors {
    // Purple
    val Purple10 = Color(0xFF21005D)
    val Purple20 = Color(0xFF381E72)
    val Purple30 = Color(0xFF4A4458)
    val Purple40 = Color(0xFF6750A4)
    val Purple80 = Color(0xFFD0BCFF)
    val Purple90 = Color(0xFFE8DEF8)
    val Purple95 = Color(0xFFF3EDF7)
    val Purple99 = Color(0xFFFEF7FF)

    // Neutral
    val Neutral10 = Color(0xFF1C1B1F)
    val Neutral20 = Color(0xFF313033)
    val Neutral30 = Color(0xFF49454F)
    val Neutral40 = Color(0xFF605D66)
    val Neutral50 = Color(0xFF79747E)
    val Neutral60 = Color(0xFF938F99)
    val Neutral80 = Color(0xFFCAC4D0)
    val Neutral87 = Color(0xFFE6E1E5)
    val Neutral90 = Color(0xFFE6E0E9)
    val Neutral92 = Color(0xFFECE6F0)
    val Neutral94 = Color(0xFFF4EFF4)
    val Neutral95 = Color(0xFFE7E0EC)
    val Neutral99 = Color(0xFFFFFBFE)

    // Neutral variant (dark surfaces)
    val NeutralVariant20 = Color(0xFF2B2930)
    val NeutralVariant30 = Color(0xFF36343B)
    val NeutralVariant40 = Color(0xFF414046)

    // Secondary
    val Secondary40 = Color(0xFF625B71)
    val Secondary80 = Color(0xFFCCC2DC)
    val Secondary90 = Color(0xFFE8DEF8)

    // Tertiary
    val Tertiary20 = Color(0xFF492532)
    val Tertiary30 = Color(0xFF633B48)
    val Tertiary40 = Color(0xFF7D5260)
    val Tertiary80 = Color(0xFFEFB8C8)
    val Tertiary90 = Color(0xFFFFD8E4)

    // Error
    val Error20 = Color(0xFF601410)
    val Error30 = Color(0xFF8C1D18)
    val Error40 = Color(0xFFB3261E)
    val Error80 = Color(0xFFF2B8B5)
    val Error90 = Color(0xFFF9DEDC)

    // Status / accent
    val Teal = Color(0xFF03DAC6)
    val Green = Color(0xFF1B6E2D)
    val GreenLight = Color(0xFFD6F5E0)
    val GreenDark = Color(0xFF1B3D2A)
    val GreenDarkText = Color(0xFF7DD99E)
    val Orange = Color(0xFFE65100)
    val OrangeLight = Color(0xFFFFF3E0)
    val OrangeDark = Color(0xFF3D2E10)
    val OrangeDarkText = Color(0xFFFFB74D)

    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)
}

@Suppress("LongMethod")
@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    DebugPanelTheme {
        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        ) {
            // Purple
            ColorElement(BaseColors.Purple10, "Purple10")
            ColorElement(BaseColors.Purple20, "Purple20")
            ColorElement(BaseColors.Purple30, "Purple30")
            ColorElement(BaseColors.Purple40, "Purple40")
            ColorElement(BaseColors.Purple80, "Purple80")
            ColorElement(BaseColors.Purple90, "Purple90")
            ColorElement(BaseColors.Purple95, "Purple95")
            ColorElement(BaseColors.Purple99, "Purple99")

            // Neutral
            ColorElement(BaseColors.Neutral10, "Neutral10")
            ColorElement(BaseColors.Neutral20, "Neutral20")
            ColorElement(BaseColors.Neutral30, "Neutral30")
            ColorElement(BaseColors.Neutral40, "Neutral40")
            ColorElement(BaseColors.Neutral50, "Neutral50")
            ColorElement(BaseColors.Neutral60, "Neutral60")
            ColorElement(BaseColors.Neutral80, "Neutral80")
            ColorElement(BaseColors.Neutral87, "Neutral87")
            ColorElement(BaseColors.Neutral90, "Neutral90")
            ColorElement(BaseColors.Neutral92, "Neutral92")
            ColorElement(BaseColors.Neutral94, "Neutral94")
            ColorElement(BaseColors.Neutral95, "Neutral95")
            ColorElement(BaseColors.Neutral99, "Neutral99")

            // Neutral variant
            ColorElement(BaseColors.NeutralVariant20, "NV20")
            ColorElement(BaseColors.NeutralVariant30, "NV30")
            ColorElement(BaseColors.NeutralVariant40, "NV40")

            // Secondary
            ColorElement(BaseColors.Secondary40, "Secondary40")
            ColorElement(BaseColors.Secondary80, "Secondary80")
            ColorElement(BaseColors.Secondary90, "Secondary90")

            // Tertiary
            ColorElement(BaseColors.Tertiary20, "Tertiary20")
            ColorElement(BaseColors.Tertiary30, "Tertiary30")
            ColorElement(BaseColors.Tertiary40, "Tertiary40")
            ColorElement(BaseColors.Tertiary80, "Tertiary80")
            ColorElement(BaseColors.Tertiary90, "Tertiary90")

            // Error
            ColorElement(BaseColors.Error20, "Error20")
            ColorElement(BaseColors.Error30, "Error30")
            ColorElement(BaseColors.Error40, "Error40")
            ColorElement(BaseColors.Error80, "Error80")
            ColorElement(BaseColors.Error90, "Error90")

            // Status / accent
            ColorElement(BaseColors.Teal, "Teal")
            ColorElement(BaseColors.Green, "Green")
            ColorElement(BaseColors.GreenLight, "GreenLight")
            ColorElement(BaseColors.GreenDark, "GreenDark")
            ColorElement(BaseColors.GreenDarkText, "GreenDkTxt")
            ColorElement(BaseColors.Orange, "Orange")
            ColorElement(BaseColors.OrangeLight, "OrangeLight")
            ColorElement(BaseColors.OrangeDark, "OrangeDark")
            ColorElement(BaseColors.OrangeDarkText, "OrangeDkTxt")

            // Black & White
            ColorElement(BaseColors.White, "White")
            ColorElement(BaseColors.Black, "Black")
        }
    }
}

@Composable
private fun ColorElement(color: Color, label: String, modifier: Modifier = Modifier) {
    val textColor = if (color.luminance() > 0.5f) Color.Black else Color.White

    Box(
        modifier = modifier
            .size(size = 100.dp)
            .background(color = color),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 10.sp,
        )
    }
}
