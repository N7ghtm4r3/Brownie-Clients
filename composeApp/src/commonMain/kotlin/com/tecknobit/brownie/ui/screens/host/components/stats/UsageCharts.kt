@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.brownie.ui.screens.host.components.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipScope
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.cpu
import brownie.composeapp.generated.resources.cpu_tooltip
import brownie.composeapp.generated.resources.memory
import brownie.composeapp.generated.resources.memory_tooltip
import brownie.composeapp.generated.resources.storage
import brownie.composeapp.generated.resources.storage_tooltip
import com.tecknobit.brownie.ui.screens.host.data.CpuUsage
import com.tecknobit.brownie.ui.screens.host.data.MemoryUsage
import com.tecknobit.brownie.ui.screens.host.data.StorageUsage
import com.tecknobit.brownie.ui.screens.host.data.StorageUsage.Companion.asText
import com.tecknobit.brownie.ui.theme.AppTypography
import com.tecknobit.brownie.ui.theme.green
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.brownie.ui.theme.yellow
import com.tecknobit.equinoxcore.annotations.Wrapper
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Component used to display the [CpuUsage] stats
 *
 * @param usage The stats related to the CPU of the host
 */
@Wrapper
@Composable
fun CpuUsageChart(
    usage: CpuUsage
) {
    GaugeChart(
        usageTitle = Res.string.cpu,
        usagePercent = usage.percentValue,
        richToolTipContent = {
            RichTooltip {
                Text(
                    text = stringResource(
                        resource = Res.string.cpu_tooltip,
                        usage.clock
                    )
                )
            }
        }
    )
}

/**
 * Component used to display the [MemoryUsage] stats
 *
 * @param usage The stats related to the RAM memory of the host
 */
@Wrapper
@Composable
fun MemoryUsageChart(
    usage: MemoryUsage
) {
    GaugeChart(
        usageTitle = Res.string.memory,
        usagePercent = usage.percentValue,
        richToolTipContent = {
            RichTooltip {
                Text(
                    text = stringResource(
                        resource = Res.string.memory_tooltip,
                        usage.usageValue, usage.totalValue
                    )
                )
            }
        }
    )
}

/**
 * Component used to display the [StorageUsage] stats
 *
 * @param usage The stats related to the storage unit of the host
 */
@Wrapper
@Composable
fun StorageUsageChart(
    usage: StorageUsage
) {
    GaugeChart(
        usageTitle = Res.string.storage,
        usagePercent = usage.percentValue,
        richToolTipContent = {
            RichTooltip {
                Text(
                    text = stringResource(
                        resource = Res.string.storage_tooltip,
                        usage.type.asText(), usage.usageValue, usage.totalValue
                    )
                )
            }
        }
    )
}

/**
 * Custom chart component used to display the stats related to a host
 *
 * @param modifier The modifier to apply to the component
 * @param usageTitle The representative title of the usage info
 * @param usagePercent The percent value of the usage stat
 * @param richToolTipContent The representative tooltip displayed to show additional info about the
 * statistic
 *
 * [Credits](https://proandroiddev.com/creating-a-custom-gauge-speedometer-in-jetpack-compose-bd3c3d72074b)
 */
@Composable
fun GaugeChart(
    modifier: Modifier = Modifier,
    usageTitle: StringResource,
    usagePercent: Double,
    richToolTipContent: @Composable TooltipScope.() -> Unit,
) {
    val meterValue = getMeterValue(
        usagePercent = usagePercent
    )
    val trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val indicatorColor = MaterialTheme.colorScheme.onSurface
    val chartColor = if (usagePercent <= 49)
        green()
    else if (usagePercent in 50.0..80.0)
        yellow()
    else
        red()
    Box(
        modifier = modifier
            .size(200.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val sweepAngle = 240f
            val fillSwipeAngle = (meterValue / 100f) * sweepAngle
            val height = size.height
            val width = size.width
            val startAngle = 150f
            val arcHeight = height - 20.dp.toPx()

            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset((width - height + 60f) / 2f, (height - arcHeight) / 2f),
                size = Size(arcHeight, arcHeight),
                style = Stroke(
                    width = 55f,
                    cap = StrokeCap.Round
                )
            )

            drawArc(
                brush = SolidColor(chartColor),
                startAngle = startAngle,
                sweepAngle = fillSwipeAngle.toFloat(),
                useCenter = false,
                topLeft = Offset((width - height + 60f) / 2f, (height - arcHeight) / 2),
                size = Size(arcHeight, arcHeight),
                style = Stroke(
                    width = 55f,
                    cap = StrokeCap.Round
                )
            )
            val centerOffset = Offset(width / 2f, height / 2.09f)
            drawCircle(indicatorColor, 24f, centerOffset)
            val needleAngle = (meterValue / 100f) * sweepAngle + startAngle
            val needleLength = 160f
            val needleBaseWidth = 10f
            val needlePath = Path().apply {
                val topX = centerOffset.x + needleLength * cos(
                    needleAngle.toRadians()
                )
                val topY = centerOffset.y + needleLength * sin(
                    needleAngle.toRadians()
                )
                val baseLeftX = centerOffset.x + needleBaseWidth * cos(
                    (needleAngle - 90).toRadians()
                )
                val baseLeftY = centerOffset.y + needleBaseWidth * sin(
                    (needleAngle - 90).toRadians()
                )
                val baseRightX = centerOffset.x + needleBaseWidth * cos(
                    (needleAngle + 90).toRadians()
                )
                val baseRightY = centerOffset.y + needleBaseWidth * sin(
                    (needleAngle + 90).toRadians()
                )
                moveTo(topX, topY)
                lineTo(baseLeftX, baseLeftY)
                lineTo(baseRightX, baseRightY)
                close()
            }
            drawPath(
                color = indicatorColor,
                path = needlePath
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                val state = rememberTooltipState()
                val scope = rememberCoroutineScope()
                Text(
                    text = stringResource(usageTitle),
                    style = AppTypography.titleMedium
                )
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                        positioning = TooltipAnchorPosition.Above
                    ),
                    state = state,
                    tooltip = { richToolTipContent() }
                ) {
                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(20.dp)
                            .clickable {
                                scope.launch {
                                    state.show()
                                }
                            },
                        imageVector = Icons.Default.Info,
                        contentDescription = null
                    )
                }
            }
            Text(
                text = "$usagePercent%",
                style = AppTypography.bodyMedium
            )
        }
    }
}

/**
 * Method used to get the value pointed by the [GaugeChart] indicator
 *
 * @param usagePercent The percent value of the usage stat
 *
 * @return the value of the indicator as [Double]
 */
private fun getMeterValue(
    usagePercent: Double,
): Double {
    return if (usagePercent < 0)
        0.0
    else if (usagePercent > 100)
        100.0
    else
        usagePercent
}

/**
 * Method used to convert a [Double] value to the corresponding radians value
 *
 * @return the value converted to the corresponding radians as [Double]
 */
fun Double.toRadians(): Float = (this * (PI.toFloat() / 180f)).toFloat()
