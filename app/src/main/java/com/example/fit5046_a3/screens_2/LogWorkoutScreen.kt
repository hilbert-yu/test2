package com.example.fit5046_a3.screens_2

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.fit5046_a3.model.Workout
import com.example.fit5046_a3.viewmodel.WorkoutViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LogWorkoutScreen(userId: Long, workoutViewModel : WorkoutViewModel) {
    // Access the WorkoutViewModel
    // val workoutViewModel: WorkoutViewModel = viewModel()

    // Sample workout types
    val workoutTypes = listOf(
        "Basketball", "Tennis", "Swimming", "Running", "Football",
        "Cycling", "Hiking", "Fitness training", "Other"
    )

    // States to hold UI input data
    var isExpanded by remember { mutableStateOf(false) }
    var selectedWorkoutType by remember { mutableStateOf(workoutTypes[0]) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance().timeInMillis) }
    var selectedStartTime by remember { mutableStateOf(Time(0, 0)) }
    var selectedEndTime by remember { mutableStateOf(Time(0, 0)) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePickerStart by remember { mutableStateOf(false) }
    var showTimePickerEnd by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var customWorkoutType by remember { mutableStateOf("") }

    // Date and Time Picker States
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
    )
    val timePickerStateStart = rememberTimePickerState()
    val timePickerStateEnd = rememberTimePickerState()

    // Date formatter for display
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)

    // Workout form layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Workout Form",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 24.sp
        )

        // Fitness Type Dropdown
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            modifier = Modifier.padding(top = 32.dp)
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .height(64.dp),
                readOnly = true,
                value = selectedWorkoutType,
                onValueChange = {},
                label = { Text("Please select workout type", fontSize = 16.sp) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                }
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                workoutTypes.forEach { workoutType ->
                    DropdownMenuItem(
                        text = { Text(workoutType, fontSize = 16.sp) },
                        onClick = {
                            selectedWorkoutType = workoutType
                            isExpanded = false
                        }
                    )
                }
            }
        }

        // Show TextField if "Other" is selected
        if (selectedWorkoutType == "Other") {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                value = customWorkoutType,
                onValueChange = { customWorkoutType = it },
                label = { Text("Enter your custom workout type") }
            )
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // Date Picker Button
        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Pick Workout Date",
                fontSize = 19.sp)
        }
        Text(
            modifier = Modifier
                .padding(top = 16.dp),
            fontSize = 20.sp,
            text = "Workout Date: ${dateFormatter.format(Date(selectedDate))}",
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // Time Picker Buttons
        Button(
            onClick = { showTimePickerStart = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Pick Start Time",
                fontSize = 19.sp)
        }
        Text(
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 20.sp,
            text = "Start Time: ${DateFormat.timeToString(selectedStartTime)}"
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Button(
            onClick = { showTimePickerEnd = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Pick End Time",
                fontSize = 19.sp)
        }
        Text(
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 20.sp,
            text = "End Time: ${DateFormat.timeToString(selectedEndTime)}"
        )

        Spacer(modifier = Modifier.weight(1f))

        // Submit Button
        Button(
            onClick = {
                // If the selectedWorkoutType is Other, use the WorkoutType which is entered by user
                val workoutTypeToSubmit = if (selectedWorkoutType == "Other") customWorkoutType else selectedWorkoutType

                // Check whether the start time is later than the end time
                if (selectedStartTime.hour > selectedEndTime.hour ||
                    (selectedStartTime.hour == selectedEndTime.hour && selectedStartTime.minute > selectedEndTime.minute)) {
                    errorMessage = "Workout start time should not be later than the end time.\n\nPlease enter again."
                    showErrorDialog = true
                } else {
                    // Create a new Workout object
                    val workout = Workout(
                        userId = userId,
                        workoutType = workoutTypeToSubmit,
                        workoutDate = dateFormatter.format(Date(selectedDate)),
                        startTime = DateFormat.timeToString(selectedStartTime),
                        endTime = DateFormat.timeToString(selectedEndTime)
                    )

                    Log.d("Workout", "Attempting to insert workout: $workout")
                    // Insert a workout record using the ViewModel
                    workoutViewModel.insertWorkout(workout)

                    // show confirm dialog
                    showConfirmationDialog = true
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Submit Workout",
                fontSize = 19.sp)
        }

        // DatePicker and TimePicker dialogs
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                            selectedDate = datePickerState.selectedDateMillis ?: Calendar.getInstance().timeInMillis
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimePickerStart) {
            TimePickerDialog(
                onDismissRequest = { showTimePickerStart = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedStartTime = Time(timePickerStateStart.hour, timePickerStateStart.minute)
                            showTimePickerStart = false
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePickerStart = false }) { Text("Cancel") }
                }
            ) {
                TimePicker(state = timePickerStateStart)
            }
        }

        if (showTimePickerEnd) {
            TimePickerDialog(
                onDismissRequest = { showTimePickerEnd = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedEndTime = Time(timePickerStateEnd.hour, timePickerStateEnd.minute)
                            showTimePickerEnd = false
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePickerEnd = false }) { Text("Cancel") }
                }
            ) {
                TimePicker(state = timePickerStateEnd)
            }
        }

        // Confirmation dialog
        if (showConfirmationDialog) {
            Dialog(
                onDismissRequest = { showConfirmationDialog = false }
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Workout Submitted!",
                            fontSize = 22.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        TextButton(
                            onClick = { showConfirmationDialog = false }
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }

        if (showErrorDialog) {
            Dialog(
                onDismissRequest = { showErrorDialog = false }
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMessage,
                            fontSize = 22.sp,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        TextButton(
                            onClick = { showErrorDialog = false }
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}

data class Time(val hour: Int, val minute: Int)

object DateFormat {
    fun timeToString(time: Time): String {
        val hourStr = if (time.hour < 10) "0${time.hour}" else time.hour.toString()
        val minuteStr = if (time.minute < 10) "0${time.minute}" else time.minute.toString()
        return "$hourStr:$minuteStr"
    }
}

