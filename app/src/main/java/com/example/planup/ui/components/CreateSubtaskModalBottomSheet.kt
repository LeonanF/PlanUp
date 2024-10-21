package com.example.planup.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*


@Composable
fun CreateSubtaskModalBottomSheet() {
    val dueDate = remember { mutableStateOf("") }
    val showDatePickerDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = dueDate.value,
            onValueChange = {},
            label = { Text("Data de entrega", color = Color.White) },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(0.dp, 10.dp)
                .clickable { showDatePickerDialog.value = true },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFF35383F),
                focusedContainerColor = Color(0xFF1F222A),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            readOnly = true
        )

        if (showDatePickerDialog.value) {
            ShowDatePickerDialog(
                onDismiss = { showDatePickerDialog.value = false },
                onDateSelected = { selectedDate ->
                    dueDate.value = selectedDate
                    showDatePickerDialog.value = false
                }
            )
        }

    }
}

@Composable
fun ShowDatePickerDialog(onDismiss: () -> Unit, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        val formattedDate = String.format(Locale.US, "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
        onDateSelected(formattedDate)
    }, year, month, day).apply {
        setOnDismissListener { onDismiss() }
        show()
    }
}