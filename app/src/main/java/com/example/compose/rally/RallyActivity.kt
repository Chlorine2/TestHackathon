/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.rally

import RegistryModel
import android.content.ClipData
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.rally.data.Account
import com.example.compose.rally.data.Bill
import com.example.compose.rally.data.Credit
import com.example.compose.rally.data.Deposit
import com.example.compose.rally.data.UserData
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.theme.RallyTheme
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    private fun <T> SharedPreferences.saveAppState(itemList: List<T>, state : String) {
        val json = Gson().toJson(itemList)
        edit().putString(state, json).apply()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        val savedAccountsJson = sharedPreferences.getString(Accounts.route, null)
        UserData.accounts = if (savedAccountsJson != null) {
            Gson().fromJson(savedAccountsJson, Array<Account>::class.java).toMutableList()
        } else {
            UserData.accounts
        }

        val savedBillsJson = sharedPreferences.getString(Bills.route, null)
        UserData.bills = if (savedBillsJson != null) {
            Gson().fromJson(savedBillsJson, Array<Bill>::class.java).toMutableList()
        } else {
            UserData.bills
        }

        val savedDepositsJson = sharedPreferences.getString(Deposits.route, null)
        UserData.deposits = if (savedDepositsJson != null) {
            Gson().fromJson(savedDepositsJson, Array<Deposit>::class.java).toMutableList()
        } else {
            UserData.deposits
        }

        val savedCreditsJson = sharedPreferences.getString(Credits.route, null)
        UserData.credits = if (savedCreditsJson != null) {
            Gson().fromJson(savedCreditsJson, Array<Credit>::class.java).toMutableList()
        } else {
            UserData.credits
        }

        setContent {
            RallyApp()
        }
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        sharedPreferences.saveAppState(UserData.accounts, Accounts.route)
        sharedPreferences.saveAppState(UserData.credits, Credits.route)
        sharedPreferences.saveAppState(UserData.deposits, Deposits.route)
        sharedPreferences.saveAppState(UserData.bills, Bills.route)


    }

}


@Composable
fun RallyApp() {
    RallyTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen =
            rallyTabRowScreens.find { it.route == currentDestination?.route } ?: Overview

        if(sign_in().value) {

            Scaffold(
                topBar = {
                    RallyTabRow(
                        allScreens = rallyTabRowScreens,
                        onTabSelected = { newScreen ->
                            navController.navigateSingleTopTo(newScreen.route)
                        },
                        currentScreen = currentScreen
                    )
                }
            ) { innerPadding ->
                RallyNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}


@Composable
fun sign_in() : MutableState<Boolean> {

    val currentEmail = remember { mutableStateOf("") }
    val tr = remember { mutableStateOf(false) }
    val currentPassword = remember { mutableStateOf("") }
    val registry = remember {mutableStateOf(false)}
    val emailError = remember { mutableStateOf(false) }
    val passwordError = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val currentExeption = remember { mutableStateOf("") }



    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {


        OutlinedTextField(
            value = currentEmail.value,
            onValueChange = {
                currentEmail.value = it
            },
            label = { Text(text = "Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),

            shape = RoundedCornerShape(100),
            singleLine = true,
            isError = emailError.value
        )
        OutlinedTextField(
            value = currentPassword.value,
            onValueChange = {
                currentPassword.value = it
            },
            label = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),

            shape = RoundedCornerShape(100),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            isError = passwordError.value

        )
        val apiService = remember { ApiCustomService.ApiServiceBuilder2.apiService }
        val coroutineScope = rememberCoroutineScope()

        Text(text = currentExeption.value, color = Color.Red)
        Button(
            onClick = {
                emailError.value = currentEmail.value.length < 5 || currentEmail.value.length > 30
                passwordError.value = currentPassword.value.length < 5 || currentEmail.value.length > 30
                        || !currentPassword.value.matches(Regex("^[a-zA-Z0-9]+$"))
                coroutineScope.launch {

                if(!emailError.value && !passwordError.value) {
                    try {
                        if (registry.value) {
                            val response = withContext(Dispatchers.IO) {
                                apiService.Register(
                                    RegistryModel(
                                        username = currentEmail.value,
                                        password = currentPassword.value
                                    )
                                )
                            }
                            if (response.token != "") {
                                registry.value = !registry.value
                                currentEmail.value = ""
                                currentPassword.value = ""

                                Toast.makeText(
                                    context.applicationContext,
                                    "successful registry",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            val response = withContext(Dispatchers.IO) {
                                apiService.Authenticate(
                                    RegistryModel(
                                        username = currentEmail.value,
                                        password = currentPassword.value
                                    )
                                )
                            }
                            if (response.token != "") {
                                tr.value = true

                            }
                        }
                    } catch (e: Exception) {
                        Log.e("teg", "API call failed ${e.localizedMessage}", e)
                        if(e.localizedMessage?.contains("409") == true){
                            passwordError.value = true
                            emailError.value = true
                            currentExeption.value = "This name is already used"
                        }
                        if(e.localizedMessage?.contains("404") == true){
                            passwordError.value = true
                            emailError.value = true
                            currentExeption.value = "password or mail is wrong"
                        }
                    }
                }
            }
                      }, shape = RoundedCornerShape(100.dp),
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 10.dp)
                .fillMaxWidth()
                .requiredHeight(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface,),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 6.dp,
                pressedElevation = 8.dp,
                disabledElevation = 0.dp
            )
        ) {

            if(registry.value){
                Text(text = "Registry")
            }
            else{
                Text(text = "Sign In")
            }

        }
        Button(
            onClick = {
                emailError.value = false
                passwordError.value = false
                registry.value = !registry.value
                      currentEmail.value = ""
                      currentPassword.value = ""
                currentExeption.value = ""

            },
            shape = RoundedCornerShape(100.dp),
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 10.dp)
                .fillMaxWidth()
                .requiredHeight(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary,),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 6.dp,
                pressedElevation = 8.dp,
                disabledElevation = 0.dp
            )
        ) {
            if(registry.value){
                Text(text = "Sign In")
            }
            else{
                Text(text = "Registry")
            }

        }
    }
    return tr

}