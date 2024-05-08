package com.example.fit5046_a3.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046_a3.model.Workout
import com.example.fit5046_a3.viewmodel.WorkoutViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Date formatter for dd/MM/yyyy
@RequiresApi(Build.VERSION_CODES.O)
val customDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

@RequiresApi(Build.VERSION_CODES.O)
fun sortWorkoutsByDate(workouts: List<Workout>): List<Workout> {
    // Sort workouts using the correct date formatter
    return workouts.sortedBy { workout ->
        LocalDate.parse(workout.workoutDate, customDateFormatter)
    }.reversed()
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewWorkouts(drawerState: DrawerState) {
    // Access the WorkoutViewModel
    val workoutViewModel: WorkoutViewModel = viewModel()
    // Collect the workouts as a State object
    val workouts by workoutViewModel.getWorkouts().collectAsState(initial = emptyList())

    // Sort workouts by date using the custom date formatter
    val sortedWorkouts = sortWorkoutsByDate(workouts)

    Scaffold(
        topBar = {
            val coroutineScope = rememberCoroutineScope()
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = "About") },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { drawerState.open() }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "")
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Past Workouts",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(sortedWorkouts.size) { index ->
                    val workout = sortedWorkouts[index]
                    WorkoutRecordItem(workout)
                    if (index < sortedWorkouts.size - 1) {
                        Divider(color = Color.Gray, thickness = 1.dp)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun WorkoutRecordItem(workout: Workout) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text("Workout Type: ${workout.workoutType}")
            Spacer(modifier = Modifier.size(10.dp))
            Text("Date: ${workout.workoutDate}")
            Spacer(modifier = Modifier.size(10.dp))
            Text("Time: ${workout.startTime} - ${workout.endTime}")
            Spacer(modifier = Modifier.size(20.dp))
        }
        // Image (can be replaced or adjusted as needed)
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_camera),
            contentDescription = "Workout Image",
            modifier = Modifier.size(80.dp)
        )
    }
}

