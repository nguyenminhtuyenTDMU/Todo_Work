package com.nguyenminhtuyen.todowork.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nguyenminhtuyen.todowork.sui.security.*
import com.nguyenminhtuyen.todowork.sui.wallet.*
import com.nguyenminhtuyen.todowork.viewmodel.WalletViewModel

@Composable
fun WalletScreen(viewModel: WalletViewModel) {
    val wallet by viewModel.wallet.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Ví của tôi", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        if (wallet != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Địa chỉ Sui", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(wallet!!.address.take(10) + "...", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {
                            viewModel.copyAddressToClipboard(wallet!!.address)
                        }) {
                            Icon(Icons.Filled.ContentCopy, contentDescription = "Copy")
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.showMnemonicDialog() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) {
                        Text("Hiện Seed Phrase (bảo mật)", color = Color.White)
                    }

                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.deleteWallet() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Xóa ví", color = Color.White)
                    }
                }
            }
        } else {
            Text("Chưa có ví nào được tạo.")
            Spacer(Modifier.height(16.dp))
            Button(onClick = { viewModel.createWallet() }) {
                Text("Tạo ví mới")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { viewModel.importWallet() }) {
                Text("Nhập ví từ Seed Phrase")
            }
        }
    }
}
