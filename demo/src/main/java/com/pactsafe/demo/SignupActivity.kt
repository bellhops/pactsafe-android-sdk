package com.pactsafe.demo

import android.os.Bundle
import com.pactsafe.pactsafeandroidsdk.models.PSGroup
import com.pactsafe.pactsafeandroidsdk.ui.PSClickWrapActivity
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : PSClickWrapActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        edit_first_name.textChangedListener {
            enableLoginBtn()
        }
        edit_last_name.textChangedListener {
            enableLoginBtn()
        }
        edit_email.textChangedListener {
            enableLoginBtn()
        }
        edit_password.textChangedListener {
            enableLoginBtn()
        }
        edit_re_password.textChangedListener {
            enableLoginBtn()
        }


    }

    override fun onPreLoaded(psGroup: PSGroup) {
        println("THE SIGNUP ACTIVITY HAS GOTTEN THE PRELOAD")
    }

    override fun onAcceptanceComplete() {
        println("ACCEPTANCE DONE")
    }

    private fun enableLoginBtn() {
        btn_login.isEnabled = edit_first_name.text?.isNotEmpty() ?: false
                && edit_last_name.text?.isNotEmpty() ?: false
                && edit_password.text?.length == edit_re_password.text?.length
                && edit_password.isValidPassword()
                && edit_re_password.isValidPassword()
                && edit_email.isValidEmail()

    }
}