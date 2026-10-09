package com.example.mywallet

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs

@Composable
fun SwipeArea(
    modifier: Modifier = Modifier,
    onSwipeUp: () -> Unit,
    onSwipeDown: () -> Unit,
    onSwipeEnd: () -> Unit,
    onDragUp: (Amount: Float) -> Unit,
    onDragDown: (Amount: Float) -> Unit,
    onDragCancel: () -> Unit,
) {
    val minDistance = 300f

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                var totalX = 0f
                var totalY = 0f
                var moveY = 0f
                detectDragGestures(
                    onDragStart = {
                        totalX = 0f
                        totalY = 0f
                    },
                    onDrag = { change, dragAmount ->

                        change.consume()

                        totalX += dragAmount.x
                        totalY += dragAmount.y
                        if(totalY < -minDistance){
                            moveY = -minDistance
                        } else if(totalY > minDistance){
                            moveY = minDistance
                        } else {
                            moveY = totalY
                        }
                        if(dragAmount.y != 0f) if (dragAmount.y < 0f) onDragUp(moveY) else onDragDown(moveY)
                    },
                    onDragEnd = {
                        if (
                            abs(totalY) >= minDistance &&
                            abs(totalY) > abs(totalX)
                        ) {
                            if (totalY < 0) {
                                onSwipeUp()
                            } else {
                                onSwipeDown()
                            }
                        } else {
                            onSwipeEnd()
                        }
                    },

                    onDragCancel = {
                        onDragCancel()
                    }
                )
            }
    )
}