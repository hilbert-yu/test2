package com.example.fit5046_a3.screens_2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit5046_a3.R
import com.example.fit5046_a3.viewmodel.UserViewModel

@Composable
fun ProfileScreen(userViewModel: UserViewModel, userEmail: String) {
    // Observe the user profile data as LiveData
    val userProfile by userViewModel.getUserProfile(userEmail).observeAsState()

    // State to control the visibility of the edit dialog
    val showEditDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // Show profile details dynamically based on the user's data
        userProfile?.let { user ->
            Text(
                text = "Profile Photo",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            ProfileAvatar()
            Spacer(modifier = Modifier.height(10.dp))
            ProfileItem("Email Address", user.email)
            ProfileItem("User Name", user.userName)
            ProfileItem("Gender", user.gender)
            ProfileItem("Date of Birth", user.dateOfBirth)
            ProfileItem("Weight", "${user.weight} kg")
            ProfileItem("Height", "${user.height} cm")

            // Show the dialog if needed
            if (showEditDialog.value) {
                EditProfileDialog(
                    userName = user.userName,
                    weight = user.weight,
                    height = user.height,
                    onConfirm = { newUserName, newWeight, newHeight ->
                        // Update the user's details in the database
                        val updatedUser = user.copy(
                            userName = newUserName,
                            weight = newWeight,
                            height = newHeight
                        )
                        userViewModel.updateUserProfile(updatedUser)
                        showEditDialog.value = false // Close the dialog
                    },
                    onCancel = {
                        showEditDialog.value = false // Close the dialog
                    }
                )
            }
        } ?: run {
            Text("Loading user profile...")
        }

        Spacer(modifier = Modifier.weight(1f)) // Spacer to push content to top

        Button(
            onClick = {
                showEditDialog.value = true // Open the edit dialog
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Change details")
        }
    }
}


@Composable
fun ProfileAvatar() {
    // Load user's avatar image
    val avatar: Painter = painterResource(id = R.drawable.user_avatar)

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = avatar,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = value,
            fontSize = 18.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
//        Divider(color = Color.Gray, thickness = 0.5.dp)
        Divider(modifier = Modifier.padding(vertical = 6.dp))


    }

}


@Composable
fun EditProfileDialog(
    userName: String,
    weight: Double,
    height: Double,
    onConfirm: (String, Double, Double) -> Unit,
    onCancel: () -> Unit
) {
    var newUserName by remember { mutableStateOf(userName) }
    var newWeight by remember { mutableStateOf(weight.toString()) }
    var newHeight by remember { mutableStateOf(height.toString()) }

    // Error message states
    var weightError by remember { mutableStateOf<String?>(null) }
    var heightError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(text = "Edit Profile")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = newUserName,
                    onValueChange = { newUserName = it },
                    label = { Text("User Name") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (weightError != null) {
                    Text(
                        text = weightError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                OutlinedTextField(
                    value = newWeight,
                    onValueChange = { newWeight = it },
                    label = { Text("Weight (kg)") },
                    isError = weightError != null
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (heightError != null) {
                    Text(
                        text = heightError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                OutlinedTextField(
                    value = newHeight,
                    onValueChange = { newHeight = it },
                    label = { Text("Height (cm)") },
                    isError = heightError != null
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                // Parse new weight and height to validate them
                val parsedWeight = newWeight.toDoubleOrNull() ?: -1.0
                val parsedHeight = newHeight.toDoubleOrNull() ?: -1.0

                // Validate weight and height
                var isValid = true
                if (parsedWeight <= 0) {
                    weightError = "Weight must be greater than 0"
                    isValid = false
                } else {
                    weightError = null
                }

                if (parsedHeight <= 0) {
                    heightError = "Height must be greater than 0"
                    isValid = false
                } else {
                    heightError = null
                }

                // If both values are valid, proceed to confirm
                if (isValid) {
                    onConfirm(newUserName, parsedWeight, parsedHeight)
                }
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}

