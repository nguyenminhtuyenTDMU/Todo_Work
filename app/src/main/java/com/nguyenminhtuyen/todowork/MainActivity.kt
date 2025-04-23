package com.nguyenminhtuyen.todowork

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nguyenminhtuyen.todowork.ui.screen.*
import com.nguyenminhtuyen.todowork.viewmodel.TodoViewModel
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import com.nguyenminhtuyen.todowork.sui.wallet.ensureBouncyCastleProvider
import com.nguyenminhtuyen.todowork.viewmodel.WalletViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ensureBouncyCastleProvider()
        setContent {
            val viewModel: TodoViewModel = viewModel()
            val walletViewModel: WalletViewModel = viewModel()

            var screen by remember { mutableStateOf("home") }

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(selected = screen == "home", onClick = { screen = "home" }, label = { Text("Home") }, icon = {Icon(Icons.Filled.Home, contentDescription = "Home")})
                        NavigationBarItem(selected = screen == "wallet", onClick = { screen = "wallet" }, label = { Text("Ví") }, icon = {Icon(Icons.Filled.AccountBalanceWallet, contentDescription = "Wallet")})
                        NavigationBarItem(selected = screen == "history", onClick = { screen = "history" }, label = { Text("Lịch sử") }, icon = {Icon(Icons.Filled.History, contentDescription = "History")})
                    }
                }
            ) { innerPadding ->
                when (screen) {
                    "home" -> TodoListScreen(viewModel, Modifier.padding(innerPadding))
                    "wallet" -> WalletScreen(viewModel = walletViewModel)
                    "history" -> HistoryScreen(viewModel, Modifier.padding(innerPadding))
                }
            }
        }
    }
}
