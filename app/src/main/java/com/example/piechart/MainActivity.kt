package com.example.piechart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.piechart.ui.theme.PieChartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PieChartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .padding(
                                20.dp
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        PieChart(
                            modifier = Modifier.size(250.dp),
                            list = listOf(
                                PieValue(
                                    legend = "Android",
                                    percentage = 25f,
                                    Color(0xFF4CAF50)
                                ),
                                PieValue(
                                    legend = "Kotlin",
                                    percentage = 20f,
                                    Color(0xFF03A9F4)
                                ),
                                PieValue(
                                    legend = "Python",
                                    percentage = 15f,
                                    Color(0xFF9C27B0)
                                ),
                                PieValue(
                                    legend = "JavaScript",
                                    percentage = 25f,
                                    Color(0xFFFFC107)
                                ),
                                PieValue(
                                    legend = "Ruby",
                                    percentage = 15f,
                                    Color(0xFFE91E63)
                                ),
                            )
                        )
                    }
                }
            }
        }
    }
}

data class PieValue(
    val legend:String,
    val percentage: Float,
    val color:Color,
)

data class PieChartData(
    val legend: String,
    val startAngle: Float,
    val sweepAngle:Float,
    val color:Color,
)

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    list : List<PieValue>,
){

    var chartValues by remember {
        mutableStateOf<List<PieChartData>>(emptyList())
    }

    LaunchedEffect(key1 = list){
        val data : ArrayList<PieChartData> = ArrayList()
        var startAngle = -210f
        list.forEachIndexed { index , pieValue ->
            if(index > 0){
                startAngle += data[index - 1].sweepAngle
            }
            data.add(
                PieChartData(
                    legend = pieValue.legend,
                    startAngle = startAngle,
                    sweepAngle = pieValue.percentage.calculateSweepAngle(),
                    color = pieValue.color
                )
            )
        }
        chartValues = data
    }

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Canvas(
            modifier = modifier
                .onSizeChanged {
                    size = it
                },
        ){
            chartValues.forEach{
                drawArc(
                    color = it.color,
                    size = Size(size.width.toFloat(), size.height.toFloat()),
                    startAngle = it.startAngle,
                    sweepAngle = it.sweepAngle,
                    useCenter = true,
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        chartValues.forEach{ pieChartValue ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(
                                color = pieChartValue.color,
                                shape = RoundedCornerShape(3.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = pieChartValue.legend)
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}


fun Float.calculateSweepAngle():Float{
    return (360 * this) / 100
}