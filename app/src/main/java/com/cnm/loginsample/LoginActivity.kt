package com.cnm.loginsample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.data.OAuthLoginState
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        const val OAUTH_CLIENT_ID = "A9Aj8uOOQwx7odW4I9O8"
        const val OAUTH_CLIENT_SECRET = "471o3BAo4N"
        const val OAUTH_CLIENT_NAME = "Sample"
    }

    private val mOAuthLoginModule: OAuthLogin = OAuthLogin.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mOAuthLoginModule.init(
            this@LoginActivity,
            OAUTH_CLIENT_ID,
            OAUTH_CLIENT_SECRET,
            OAUTH_CLIENT_NAME
        )

        bt_login.setOnClickListener {
                signIn()
        }
        bt_logout.setOnClickListener {
            mOAuthLoginModule.logout(this@LoginActivity)
        }

    }

    fun successLogin() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun signIn() {

        if (mOAuthLoginModule.getState(this@LoginActivity) == OAuthLoginState.OK) {
            Log.d("not login", "Nhn status don't need login")
            successLogin()
            Log.e("1", "nhn Login Module State : " +
                    mOAuthLoginModule.getState(this@LoginActivity).toString() )
        } else {
            Log.d("login", "Nhn status need login")
            mOAuthLoginModule.startOauthLoginActivity(this@LoginActivity,
                @SuppressLint("HandlerLeak")
                object : OAuthLoginHandler() {
                    override fun run(p0: Boolean) {
                        if (p0) {
                            val accessToken = mOAuthLoginModule.getAccessToken(this@LoginActivity)
                            val refreshToken = mOAuthLoginModule.getRefreshToken(this@LoginActivity)
                            val expiresAt = mOAuthLoginModule.getExpiresAt(this@LoginActivity)
                            val tokenType = mOAuthLoginModule.getTokenType(this@LoginActivity)
                            Log.e("1", "nhn Login Access Token : $accessToken")
                            Log.e("1", "nhn Login refresh Token : $refreshToken")
                            Log.e("1", "nhn Login expiresAt : $expiresAt")
                            Log.e("1", "nhn Login Token Type : $tokenType")
                            Log.e("1", "nhn Login Module State : " +
                                    mOAuthLoginModule.getState(this@LoginActivity).toString() )
                            successLogin()
                        }
                        else{
                            val errorCode = mOAuthLoginModule.getLastErrorCode(this@LoginActivity).code
                            val errorDesc = mOAuthLoginModule.getLastErrorDesc(this@LoginActivity)
                            Toast.makeText(this@LoginActivity, "errorCode:$errorCode, errorDesc:$errorDesc", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }


}