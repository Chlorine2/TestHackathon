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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.compose.rally.ui.accounts.AccountsScreen
import com.example.compose.rally.ui.accounts.SingleAccountScreen
import com.example.compose.rally.ui.bills.BillsScreen
import com.example.compose.rally.ui.bills.SingleBillScreen
import com.example.compose.rally.ui.credits.CreditsScreen
import com.example.compose.rally.ui.credits.SingleCreditScreen
import com.example.compose.rally.ui.deposits.DepositsScreen
import com.example.compose.rally.ui.deposits.SingleDepositScreen
import com.example.compose.rally.ui.overview.OverviewScreen

@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Overview.route,
        modifier = modifier
    ) {
        composable(route = Overview.route) {
            OverviewScreen(
                onClickSeeAllAccounts = {
                    navController.navigateSingleTopTo(Accounts.route)
                },
                onClickSeeAllBills = {
                    navController.navigateSingleTopTo(Bills.route)
                },
                 onClickSeeAllDeposits = {
                     navController.navigateSingleTopTo(Deposits.route)
//
                 },
                onClickSeeAllCredits = {
                    navController.navigateSingleTopTo(Credits.route)
//
                },
                onAccountClick = { accountType ->
                    navController.navigateToSingleAccount(accountType)
                },
                onBillClick = { billType ->
                    navController.navigateToSingleBill(billType)
                },
                onDepositClick = { depositType ->
                    navController.navigateToSingleDeposit(depositType)
                },
                onCreditClick = { depositType ->
                    navController.navigateToSingleCredit(depositType)
                }
            )
        }
        composable(route = Accounts.route) {
            AccountsScreen(
                onAccountClick = { accountType ->
                    navController.navigateToSingleAccount(accountType)
                }
            )
        }
        composable(route = Bills.route) {
            BillsScreen(
                onBillClick = { billType ->
                    navController.navigateToSingleBill(billType)
                },

            )
        }
        composable(route = Deposits.route){
            DepositsScreen(
                onDepositClick = { depositsType ->
                    navController.navigateToSingleDeposit(depositsType)
                },
            )
        }
        composable(route = Credits.route){
            CreditsScreen(
                onDepositClick = { creditType ->
                    navController.navigateToSingleCredit(creditType)
                },
            )
        }
        composable(
            route = SingleAccount.routeWithArgs,
            arguments = SingleAccount.arguments,
            deepLinks = SingleAccount.deepLinks
        ) { navBackStackEntry ->
            val accountType =
                navBackStackEntry.arguments?.getString(SingleAccount.accountTypeArg)
            SingleAccountScreen(accountType, navController)
        }
        composable(
            route = SingleBill.routeWithArgs,
            arguments = SingleBill.arguments,
            deepLinks = SingleBill.deepLinks
        ) { navBackStackEntry ->
            val billType =
                navBackStackEntry.arguments?.getString(SingleBill.accountTypeArg)
            SingleBillScreen(
                billType, navController
            )
        }
        composable(
            route = SingleDeposit.routeWithArgs,
            arguments = SingleDeposit.arguments,
            deepLinks = SingleDeposit.deepLinks
        ) { navBackStackEntry ->
            val depositType =
                navBackStackEntry.arguments?.getString(SingleDeposit.accountTypeArg)
            SingleDepositScreen(
                depositType, navController
            )
        }
        composable(
            route = SingleCredit.routeWithArgs,
            arguments = SingleCredit.arguments,
            deepLinks = SingleCredit.deepLinks
        ) { navBackStackEntry ->
            val depositType =
                navBackStackEntry.arguments?.getString(SingleCredit.accountTypeArg)
            SingleCreditScreen(
                depositType, navController
            )
        }
    }

}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

private fun NavHostController.navigateToSingleAccount(accountType: String) {
    this.navigateSingleTopTo("${SingleAccount.route}/$accountType")
}

private fun NavHostController.navigateToSingleBill(billType: String) {
    this.navigateSingleTopTo("${SingleBill.route}/$billType")
}

private fun NavHostController.navigateToSingleDeposit(depositType: String) {
    this.navigateSingleTopTo("${SingleDeposit.route}/$depositType")
}

private fun NavHostController.navigateToSingleCredit(depositType: String) {
    this.navigateSingleTopTo("${SingleCredit.route}/$depositType")
}