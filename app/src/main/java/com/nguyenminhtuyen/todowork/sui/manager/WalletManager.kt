package com.nguyenminhtuyen.todowork.sui.manager

import android.content.Context
import com.nguyenminhtuyen.todowork.sui.security.deleteWallet
import com.nguyenminhtuyen.todowork.sui.security.loadSeedPhrase
import com.nguyenminhtuyen.todowork.sui.security.saveSeedPhrase
import com.nguyenminhtuyen.todowork.sui.wallet.SuiWallet
import com.nguyenminhtuyen.todowork.sui.wallet.createSuiWallet
import com.nguyenminhtuyen.todowork.sui.wallet.importSuiWalletFromMnemonic

object WalletManager {

    fun createAndSaveWallet(context: Context): SuiWallet {
        val wallet = createSuiWallet()
        saveSeedPhrase(context, wallet.mnemonic)
        return wallet
    }

    fun loadWallet(context: Context): SuiWallet? {
        val mnemonic = loadSeedPhrase(context) ?: return null
        return importSuiWalletFromMnemonic(mnemonic)
    }

    fun deleteWalletData(context: Context) {
        deleteWallet(context)
    }
}
