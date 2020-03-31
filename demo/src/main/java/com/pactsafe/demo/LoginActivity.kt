package com.pactsafe.demo

import android.os.Bundle
import com.pactsafe.pactsafeandroidsdk.models.PSGroup
import com.pactsafe.pactsafeandroidsdk.ui.PSClickWrapActivity
import kotlinx.android.synthetic.main.activity_login_alert.*

class LoginActivity : PSClickWrapActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_alert)

        edit_username.textChangedListener {
            enableLoginBtn()
        }

        edit_password.textChangedListener {
            enableLoginBtn()
        }

        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onPreLoaded(psGroup: PSGroup) {
        println("THE LOGIN ACTIVITY HAS GOTTEN THE PRELOAD")
    }

    override fun onContractLinkClicked(url: String) {
        println("CONTRACT LINK: $url")
    }

    override fun onAcceptanceComplete(checked: Boolean) {
        println("IS CHECKED : $checked")
        btn_login.isEnabled = checked
    }


    private fun enableLoginBtn() {
        btn_login.isEnabled = edit_username.isValidEmail() && edit_password.isValidPassword()
    }


}