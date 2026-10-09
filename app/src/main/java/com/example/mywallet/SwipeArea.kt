package com.example.mywallet

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs

suspend fun PointerInputScope.detectDragGesturesInitial(
    onDragStart: (Offset) -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDragCancel: () -> Unit = {},
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit
) {
    awaitEachGesture {
        val slop = viewConfiguration.touchSlop
        val down = awaitFirstDown(
            requireUnconsumed = false,
            pass = PointerEventPass.Initial
        )
        var total = Offset.Zero
        var dragging = false

        while (true) {
            val event = awaitPointerEvent(PointerEventPass.Initial)
            val change = event.changes.firstOrNull { it.id == down.id }
            if (change == null) {
                if (dragging) onDragCancel()
                return@awaitEachGesture
            }

            // 指を離した
            if (!change.pressed) {
                if (dragging) {
                    change.consume() // 子のクリック誤発火を防ぐ
                    onDragEnd()
                }
                return@awaitEachGesture
            }

            val delta = change.positionChange()
            if (!dragging) {
                total += delta
                if (total.getDistance() > slop) {
                    dragging = true
                    onDragStart(down.position)
                    change.consume() // ここで子のタップがキャンセルされる
                    onDrag(change, total)
                }
            } else {
                change.consume()
                onDrag(change, delta)
            }
        }
    }
}

@Composable
fun SwipeArea(
    modifier: Modifier = Modifier,
    onSwipeUp: () -> Unit,
    onSwipeDown: () -> Unit,
    onSwipeEnd: () -> Unit,
    onDragUp: (amount: Float, color: Color) -> Unit,
    onDragDown: (amount: Float, color: Color) -> Unit,
    onDragCancel: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val minDistance = 300f

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                var totalX = 0f
                var totalY = 0f
                var moveY = 0f
                var color = Color.White

                detectDragGesturesInitial(
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
                            color = Color.Red
                        } else if(totalY > minDistance){
                            moveY = minDistance
                            color = Color.Green
                        } else {
                            moveY = totalY
                            color = Color.White
                        }
                        if(dragAmount.y != 0f) if (dragAmount.y < 0f) onDragUp(moveY,color) else onDragDown(moveY,color)
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
            },
        content = content
    )
}