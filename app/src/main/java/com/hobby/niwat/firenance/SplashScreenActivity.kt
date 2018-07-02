package com.hobby.niwat.firenance

import android.content.Intent
import android.os.Bundle
import android.view.Window

class SplashScreenActivity : AbstractFirestoreActivity() {

	private val SLEEP_TIME = 3000L
	private val RC_SIGN_IN = 101

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		setContentView(R.layout.activity_splash_screen)

		Thread(Runnable {
			try {
				Thread.sleep(SLEEP_TIME)
			} catch (e: Exception) {

			}
			if (!isLogin()) {
				startSignIn()
			} else {
				startActivity(Intent(this, MainActivity::class.java))
				finish()
			}
		}).start()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode) {
			RC_SIGN_IN -> {
				if (resultCode == RESULT_OK && isLogin()) {
					startActivity(Intent(this, MainActivity::class.java))
					finish()
				}
			}
		}
	}
}