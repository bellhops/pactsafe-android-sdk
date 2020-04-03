package com.pactsafe.pactsafeandroidsdk.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pactsafe.pactsafeandroidsdk.PSApp
import com.pactsafe.pactsafeandroidsdk.data.ApplicationPreferences
import com.pactsafe.pactsafeandroidsdk.models.PSGroup
import com.pactsafe.pactsafeandroidsdk.models.PSSigner
import com.pactsafe.pactsafeandroidsdk.util.injector
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class PSClickWrapActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appPreferences: ApplicationPreferences = injector()

        /*
        Here we want to ensure that if the group pre-loads in time for onCreate,
        then we don't need to setup a disposable for observation.
         */
        if (PSApp.hasPreloaded) {
            println("LOADED ALREADY")
            appPreferences.group?.let { onPreLoaded(it) }
        } else {
            println("NEED TO SETUP DISPOSABLE TO LISTEN")
            disposables.add(PSApp.hasPreloadedObservable.subscribe({
                onPreLoaded(it)
            }, {
                Timber.e(it, "Failed to Load data.")
            }))
        }
    }

    abstract fun onPreLoaded(psGroup: PSGroup)
    abstract fun onContractLinkClicked(title: String, url: String)
    abstract fun onAcceptanceComplete(checked: Boolean)
    abstract fun onSendAgreedComplete(downloadUrl: String)


    fun fetchSignedStatus(signerId: String) {

    }

    fun sendAgreed(signer: PSSigner) {
        disposables.add(PSApp.sendAgreed(signer)
            .subscribe({
                onSendAgreedComplete(it.headers()["X-Download-URL"] ?: "")
            }, {
                Timber.e("There was an error: ${it.localizedMessage}")
            })
        )

    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
        PSApp.endSubscriptions()
    }
}