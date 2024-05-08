package com.example.fit5046_a3

//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.viewModels
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.ui.Modifier
//import com.example.com.example.fit5046_a3.viewmodel.UserViewModel
//
////class MainActivity : ComponentActivity() {
////    @RequiresApi(Build.VERSION_CODES.O)
////    override fun onCreate(savedInstanceState: Bundle?) {
//////        val viewModel: CountryViewModel by viewModels()
////        super.onCreate(savedInstanceState)
////        setContent {
////            LogWorkoutScreen(123456)
////
//////            MainScreen(viewModel = viewModel)
////        }
////    }
////}
//
//
//class MainActivity : ComponentActivity() {
//    private val viewModel: UserViewModel by viewModels()
//    @RequiresApi(64)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    //Greeting("Android")
//                    FormEntry(viewModel)
//                }
//
//        }
//    }
//}


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.fit5046_a3.navigation.MainNavigation
import com.example.fit5046_a3.navigation.NavigationViewModel
import com.example.fit5046_a3.ui.theme.FIT5046_A3Theme

class MainActivity : ComponentActivity() {

    private val viewModel1: NavigationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FIT5046_A3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation(navViewModel = viewModel1)
                }
            }
        }
    }
}