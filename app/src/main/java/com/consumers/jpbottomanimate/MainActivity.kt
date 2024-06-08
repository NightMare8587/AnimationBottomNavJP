package com.consumers.jpbottomanimate

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.consumers.jpbottomanimate.ui.theme.JPbottomAnimateTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

private val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JPbottomAnimateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyApp(innerPadding)
                }
            }
        }
    }
}

@Composable
fun MyApp(innerPadding: PaddingValues) {
    var selectedIndex by remember { mutableStateOf(1) }
    // initial setting offset to -120F
    // if we set 0F then it will go to centre
    val circleOffsetX = remember { Animatable(-120f) }
    val coroutineScope = rememberCoroutineScope()
    var itemWidth by remember { mutableStateOf(0f) }
    val offsetX by animateFloatAsState(
        targetValue = selectedIndex * itemWidth.absoluteValue,
        animationSpec = tween(durationMillis = 500)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1F))
            Row(modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { layoutCoordinates ->
                    // Calculate the width of each item in the row
                    itemWidth = layoutCoordinates.size.width / 4f
                }) {
                Box(modifier = Modifier
                    .weight(1f)
                    .clickable {
                        coroutineScope.launch {
                            // itemWidth is the width of items in the bottom row
                            // calculating the target value to animate dividing by 2
                            // cannot give 0 because 0 will lead to centre that's why negative
                            circleOffsetX.animateTo(-itemWidth/2, animationSpec = tween(1750))
                        }
                        selectedIndex = 1
                        Log.i(TAG, "MyApp: circleOffSetValue ${circleOffsetX.value}")
                    }
                    .onGloballyPositioned { layoutCoordinates ->
                        val position = layoutCoordinates.positionInRoot()
                        Log.i(TAG, "MyApp: Position ${position.x}, ${position.y}")
                    }, contentAlignment = Alignment.Center
                ) {
                    if (selectedIndex == 1) {
                        Log.i(TAG, "MyApp: clickableOffset ${circleOffsetX.value}")
                    }
                    Text("Text 1", textAlign = TextAlign.Center)
                }
                Box(modifier = Modifier
                    .weight(1f)
                    .clickable {
                        coroutineScope.launch {
                            // here it is 0
                            // it will always be in centre
                            circleOffsetX.animateTo(0F, animationSpec = tween(1750))
                        }
                        selectedIndex = 2
                        Log.i(TAG, "MyApp: circleOffSetValue ${circleOffsetX.value}")
                    }
                    .onGloballyPositioned { layoutCoordinates ->
                        val position = layoutCoordinates.positionInRoot()
                        Log.i(TAG, "MyApp1: Position ${position.x}, ${position.y}")

                    }, contentAlignment = Alignment.Center
                ) {
                    if (selectedIndex == 2) {
                        Log.i(TAG, "MyApp: clickableOffset ${circleOffsetX.value}")
                    }
                    Text("Text 2", textAlign = TextAlign.Center)
                }
                Box(modifier = Modifier
                    .weight(1f)
                    .clickable {
                        coroutineScope.launch {
                            // giving position value in target to animate to third Icon
                            circleOffsetX.animateTo(itemWidth/2, animationSpec = tween(1750))
                        }
                        selectedIndex = 3
                        Log.i(TAG, "MyApp: circleOffSetValue ${circleOffsetX.value}")
                    }
                    .onGloballyPositioned { layoutCoordinates ->
                        val position = layoutCoordinates.positionInRoot()
                        Log.i(TAG, "MyApp2: Position ${position.x}, ${position.y}")
                    }, contentAlignment = Alignment.Center
                ) {

                    if (selectedIndex == 3) {
                        Log.i(TAG, "MyApp: clickableOffset ${circleOffsetX.value}")
                    }
                    Text("Text 3", textAlign = TextAlign.Center)
                }
            }
            Ring(circleOffsetX.value)
        }
    }
}

// this function draws a ring over selected item in bottom nav
@Composable
fun Ring(value: Float) {
    Canvas(
        modifier = Modifier
            .size(30.dp)
            .offset(value.dp,0.dp)
    ) {
        val diameter = size.minDimension
        val radius = diameter / 2
        val centre = size.center

        drawCircle(
            color = Color.Blue,
            radius = radius - 20F / 2,
            center = centre,
            style = Stroke(width = 10F)
        )
    }
}