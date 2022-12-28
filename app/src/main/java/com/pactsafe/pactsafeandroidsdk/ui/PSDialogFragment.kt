package com.pactsafe.pactsafeandroidsdk.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.pactsafe.pactsafeandroidsdk.PSApp
import com.pactsafe.pactsafeandroidsdk.R
import com.pactsafe.pactsafeandroidsdk.models.EventType
import com.pactsafe.pactsafeandroidsdk.models.PSSigner
import com.pactsafe.pactsafeandroidsdk.util.SIGNER
import io.reactivex.disposables.CompositeDisposable

class PSDialogFragment(private val contracts: Map<String, Boolean>, private val useBrowser: Boolean) :
    DialogFragment() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogFullScreen)
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.view_click_wrap, container, false)
        val alertText: TextView = view.findViewById(R.id.txt_alert_message)
        val psCheckBoxView: PSCheckBoxView = view.findViewById(R.id.ps_checkbox)
        val submitButton: Button = view.findViewById(R.id.btn_submit)
        val cancelButton: TextView = view.findViewById(R.id.txt_cancel)

        val signer: PSSigner? = arguments?.getParcelable(SIGNER)

        psCheckBoxView.setContracts(contracts)
        psCheckBoxView.setUseOSBrowser(useBrowser)
        alertText.text = PSApp.updatedTermsLanguage(contracts)

        compositeDisposable.add(psCheckBoxView.getCheckedSubscription().subscribe({
            submitButton.isEnabled = it
        }, {
            Log.e(TAG, "There was an error subscribing to checkbox")
        }))

        submitButton.setOnClickListener {
            (requireActivity() as PSClickWrapActivity).sendAgreed(signer ?: PSSigner(), EventType.AGREED)
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
    companion object {
        const val TAG = "PSDialogFragment"
    }
}