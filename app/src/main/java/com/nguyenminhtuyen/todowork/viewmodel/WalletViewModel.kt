package com.nguyenminhtuyen.todowork.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhtuyen.todowork.sui.security.*
import com.nguyenminhtuyen.todowork.sui.wallet.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WalletViewModel(app: Application) : AndroidViewModel(app) {
    private val _wallet = MutableStateFlow<SuiWallet?>(null)
    val wallet: StateFlow<SuiWallet?> = _wallet

    init {
        loadWallet()
    }

    private fun loadWallet() {
        viewModelScope.launch {
            val context = getApplication<Application>()
            if (hasWallet(context)) {
                val mnemonic = loadSeedPhrase(context)
                if (mnemonic != null) {
                    _wallet.value = importSuiWalletFromMnemonic(mnemonic)
                }
            }
        }
    }

    fun createWallet() {
        viewModelScope.launch {
            val context = getApplication<Application>()
            val newWallet = createSuiWallet()
            saveSeedPhrase(context, newWallet.mnemonic)
            _wallet.value = newWallet
            Toast.makeText(context, "Đã tạo ví mới!", Toast.LENGTH_SHORT).show()
        }
    }

    fun importWallet() {
        // TODO: mở một UI để nhập mnemonic, hoặc từ file
        Toast.makeText(getApplication(), "Chưa hỗ trợ nhập mnemonic.", Toast.LENGTH_SHORT).show()
    }

    fun deleteWallet() {
        viewModelScope.launch {
            val context = getApplication<Application>()
            deleteWallet(context)
            _wallet.value = null
            Toast.makeText(context, "Đã xóa ví", Toast.LENGTH_SHORT).show()
        }
    }

    fun exportSeedPhrase() {
        val context = getApplication<Application>()
        val phrase = _wallet.value?.mnemonic?.joinToString(" ")
        Toast.makeText(context, "Seed phrase: $phrase", Toast.LENGTH_LONG).show()
        // Gợi ý: mở một UI xác thực trước khi show ra!
    }

    fun copyAddressToClipboard(address: String) {
        val context = getApplication<Application>()
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Sui Address", address)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(context, "Địa chỉ ví đã được sao chép!", Toast.LENGTH_SHORT).show()
    }

    // Phương thức hiển thị Seed Phrase
    fun showMnemonicDialog() {
        // Lưu ý: Trước khi hiển thị, bạn cần thực hiện kiểm tra bảo mật (ví dụ: dùng biometric authentication).
        val context = getApplication<Application>()
        val mnemonic = _wallet.value?.mnemonic?.joinToString(" ")

        if (mnemonic != null) {
            Toast.makeText(context, "Seed Phrase: $mnemonic", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Không có seed phrase", Toast.LENGTH_SHORT).show()
        }
    }
}
