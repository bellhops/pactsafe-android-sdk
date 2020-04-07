package com.pactsafe.pactsafeandroidsdk.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pactsafe.pactsafeandroidsdk.PSApp
import com.pactsafe.pactsafeandroidsdk.R
import com.pactsafe.pactsafeandroidsdk.data.ApplicationPreferences
import com.pactsafe.pactsafeandroidsdk.models.EventType
import com.pactsafe.pactsafeandroidsdk.models.PSGroup
import com.pactsafe.pactsafeandroidsdk.models.PSSigner
import com.pactsafe.pactsafeandroidsdk.models.PSSignerID
import com.pactsafe.pactsafeandroidsdk.util.SIGNER
import com.pactsafe.pactsafeandroidsdk.util.injector
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class PSClickWrapActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()
    protected lateinit var ALERT_TYPE: ClickWrapType

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

        if (intent.hasExtra(TYPE)) {
            ALERT_TYPE = intent.extras?.get(TYPE) as ClickWrapType
        }
    }

    abstract fun onPreLoaded(psGroup: PSGroup)
    abstract fun onContractLinkClicked(title: String, url: String)
    abstract fun onAcceptanceComplete(checked: Boolean)
    abstract fun onSendAgreedComplete(downloadUrl: String)
    abstract fun onSignedStatusFetched(status: Map<String, Boolean>)

    fun fetchSignedStatus(signer: PSSignerID) {
        disposables.add(
            PSApp.fetchSignedStatus(signer)
                .subscribe({
                    onSignedStatusFetched(it)
                }, {
                    Timber.e(it, "There was an error fetching the data. ${it.localizedMessage}")
                })
        )
    }

    fun sendAgreed(signer: PSSigner, et: String) {
        disposables.add(
            PSApp.sendAgreed(signer, et)
                .subscribe({
                    if (et == EventType.AGREED) {
                        onSendAgreedComplete(it.headers()["X-Download-URL"] ?: "")
                    }
                }, {
                    Timber.e("There was an error: ${it.localizedMessage}")
                })
        )

    }

    fun showTermsIntercept(type: ClickWrapType, contracts: Map<String, Boolean>, signer: PSSigner) {
        if (type == ClickWrapType.CHECKBOX) {
            checkboxDialogFullScreen(contracts, signer)
        } else {
            alertModal(contracts, signer)
        }
    }

    private fun alertModal(contracts: Map<String, Boolean>, signer: PSSigner) {
        AlertDialog.Builder(this).apply {
            setMessage(PSApp.updatedTermsLanguage(contracts))
            setTitle("Updated Terms")
            setPositiveButton(R.string.agreed) { _, _ ->
                sendAgreed(signer, EventType.AGREED)
            }
            setNegativeButton(R.string.disagreed) { di, _ ->
                sendAgreed(signer, EventType.DISAGREED)
                di.dismiss()
            }
            setCancelable(false)
        }
            .show()
    }

    private fun checkboxDialogFullScreen(contracts: Map<String, Boolean>, signer: PSSigner) {
        val dialogFragment = PSDialogFragment(contracts).apply {
            arguments = Bundle().apply { putParcelable(SIGNER, signer) }
        }
        val fragTran = supportFragmentManager.beginTransaction()
        dialogFragment.show(fragTran, "dialog")
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
        PSApp.endSubscriptions()
    }

    enum class ClickWrapType {
        CHECKBOX,
        ALERT
    }

    companion object {

        const val TYPE = "type"

        inline fun <reified T> create(context: Context, clazz: Class<T>, type: ClickWrapType) =
            Intent(context, clazz).apply {
                putExtra(
                    TYPE, type
                )
            }
    }
}