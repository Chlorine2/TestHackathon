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

import android.content.ClipData
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
