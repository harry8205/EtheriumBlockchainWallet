package com.example.blockchainwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mintnft.utils.Constants
import com.example.mintnft.utils.PreferenceManager
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.web3j.crypto.Keys
import org.web3j.crypto.Wallet
import org.web3j.protocol.Web3j
import java.security.Provider
import java.security.Security

const val TAG = "testTag"
class MainActivity : AppCompatActivity() {
private lateinit var preferenceManager: PreferenceManager
    private lateinit var web3j: Web3j
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferenceManager = PreferenceManager(this)

        setupBouncyCastle()
        //creating wallet
        createWallet()

    }
    private fun setupBouncyCastle() {
        val provider: Provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        if (provider.javaClass.equals(BouncyCastleProvider::class.java)) {
            // BC with same package name, shouldn't happen in real life.
            return
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }
    private fun createWallet() {

        val ecKeyPair = Keys.createEcKeyPair()
        val privateKeyInDec = ecKeyPair.privateKey
        val publicKeyInDec = ecKeyPair.publicKey
        val sPublicKeyInHex = publicKeyInDec.toString(16)
        val sPrivateKeyInHex = privateKeyInDec.toString(16)
        val aWallet = Wallet.createLight("shoaib", ecKeyPair)
        val sAddress = aWallet.address
        Log.d(
            TAG,
            "ECKeyPair:\nPUBLIC KEY: $sPublicKeyInHex \nPRIVATE KEY: ${sPrivateKeyInHex}\nADDRESS: 0x$sAddress"
        )


            preferenceManager.putString(Constants.KEY_PRIVATE_ADDRESS_CUSTOMER, sPrivateKeyInHex)
            preferenceManager.putString(Constants.KEY_PUBLIC_ADDRESS_CUSTOMER, sPublicKeyInHex)
            preferenceManager.putString(Constants.KEY_WALLET_ADDRESS_CUSTOMER, "0x$sAddress")

    }

}