package com.example.todolist.ui.view.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

@Composable
fun Task(
    id: Int,
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteTask: (Int) -> Unit // Add this parameter for deleting the task
) {
    // Create a state to manage the swipe offset
    val swipeOffset = remember { Animatable(0f) }
    val swipeThreshold = 150f
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .height(75.dp)
            .offset { IntOffset(swipeOffset.value.roundToInt(), 0) } // Apply offset for swipe effect
            .background(
                color = Color(0xff051956),
                shape = RoundedCornerShape(12.dp)
            )
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        // Check if swipe offset passes the threshold
                        if (swipeOffset.value > swipeThreshold) {
                            coroutineScope.launch {
                                // Animate task swipe out
                                swipeOffset.animateTo(1000f) {
                                    onDeleteTask(id) // Call delete function when fully swiped
                                }
                            }
                        } else {
                            coroutineScope.launch {
                                // Animate task back to original position if swipe is not enough
                                swipeOffset.animateTo(0f)
                            }
                        }
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume() // Consume the gesture
                        val newOffset = (swipeOffset.value + dragAmount).coerceAtLeast(0f)
                        coroutineScope.launch {
                            swipeOffset.snapTo(newOffset) // Update offset for swipe effect
                        }
                    }
                )
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color.White,
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.Black
            ),
            modifier = Modifier
                .padding(start = 8.dp)
        )

        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.White,
            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
            modifier = Modifier
                .padding(start = 12.dp, end = 18.dp)
                .align(Alignment.CenterVertically)
        )
    }
}
