package com.nguyenminhtuyen.todowork.sui.wallet

import org.bitcoinj.crypto.MnemonicCode
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.security.SecureRandom
import java.security.MessageDigest

fun ensureBouncyCastleProvider() {
    // Kiểm tra xem BouncyCastle đã có trong hệ thống chưa
    val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
    if (provider == null) {
        // Nếu chưa, thêm vào hệ thống
        Security.addProvider(BouncyCastleProvider())
    } else if (provider.javaClass != BouncyCastleProvider::class.java) {
        // Nếu có nhưng không phải BouncyCastle, thay thế
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.addProvider(BouncyCastleProvider())
    }
}

data class SuiWallet(
    val mnemonic: List<String>,
    val privateKey: ByteArray,
    val publicKey: ByteArray,
    val address: String
)

fun createSuiWallet(): SuiWallet {
    // Bước 1: Generate mnemonic
    val entropy = ByteArray(16) // 128 bits = 12 từ
    SecureRandom().nextBytes(entropy)
    val mnemonic = MnemonicCode.INSTANCE.toMnemonic(entropy)

    // Bước 2: Tạo seed từ mnemonic
    val seed = MnemonicCode.toSeed(mnemonic, "")

    // Bước 3: Lấy 32 byte đầu từ seed làm private key (tạm theo Sui SDK đang làm)
    val privateKey = seed.copyOfRange(0, 32)

    // Bước 4: Sinh public key từ private key (dùng Ed25519)
    val keyPair = ed25519KeypairFromPrivateKey(privateKey)
    val publicKey = keyPair.first

    // Bước 5: Hash public key để tạo địa chỉ Sui
    val address = suiAddressFromPublicKey(publicKey)

    return SuiWallet(mnemonic, privateKey, publicKey, address)
}

fun importSuiWalletFromMnemonic(mnemonic: List<String>): SuiWallet {
    // Bước 1: Tạo seed từ mnemonic nhập vào
    val seed = MnemonicCode.toSeed(mnemonic, "")

    // Bước 2: Lấy 32 byte đầu từ seed làm private key
    val privateKey = seed.copyOfRange(0, 32)

    // Bước 3: Sinh public key từ private key
    val keyPair = ed25519KeypairFromPrivateKey(privateKey)
    val publicKey = keyPair.first

    // Bước 4: Hash public key để tạo địa chỉ Sui
    val address = suiAddressFromPublicKey(publicKey)

    return SuiWallet(mnemonic, privateKey, publicKey, address)
}


fun ed25519KeypairFromPrivateKey(privateKey: ByteArray): Pair<ByteArray, ByteArray> {
    // Dùng Ed25519 để sinh keypair – bạn có thể thay bằng TweetNacl hoặc lib khác
    val kpGen = org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator()
    kpGen.init(org.bouncycastle.crypto.KeyGenerationParameters(SecureRandom(), 256))

    val keyPair = kpGen.generateKeyPair()
    val pubKey = (keyPair.public as org.bouncycastle.crypto.params.Ed25519PublicKeyParameters).encoded
    val privKey = (keyPair.private as org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters).encoded

    return Pair(pubKey, privKey)
}

fun suiAddressFromPublicKey(publicKey: ByteArray): String {
    // Prefix 0x00 (for Ed25519), sau đó hash với Blake2b rồi lấy 32 bytes
    val prefixByte = byteArrayOf(0x00)
    val toHash = prefixByte + publicKey

    val digest = MessageDigest.getInstance("SHA3-256") // hoặc dùng Blake2b nếu cần đúng
    val hashed = digest.digest(toHash)

    return "0x" + hashed.take(32).joinToString("") { "%02x".format(it) }
}
