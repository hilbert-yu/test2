package com.example.fit5046_a3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046_a3.model.Workout
import com.example.fit5046_a3.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WorkoutRepository(application)
    fun getWorkouts(): Flow<List<Workout>> {
        return repository.getWorkoutsByUser(1)
    }
    fun insertWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.insert(workout)
        }
    }

    fun updateWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.update(workout)
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.delete(workout)
        }
    }
}
