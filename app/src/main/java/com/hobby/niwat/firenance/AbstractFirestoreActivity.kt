package com.hobby.niwat.firenance

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

abstract class AbstractFirestoreActivity : AppCompatActivity() {

	private val RC_SIGN_IN = 101

	protected var firestore: FirebaseFirestore? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		initFirestore()
	}

	private fun initFirestore() {
		firestore = FirebaseFirestore.getInstance()
	}

	protected fun startSignIn() {
		val intent = AuthUI.getInstance().createSignInIntentBuilder()
				.setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build()))
				.setIsSmartLockEnabled(false)
				.setLogo(R.drawable.firenance_logo)
				.build()

		startActivityForResult(intent, RC_SIGN_IN)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode) {
			RC_SIGN_IN -> {
				if (resultCode == RESULT_OK && isLogin()) {
					onLoginSuccess()
				} else {
					startSignIn()
				}
			}
		}
	}

	protected fun isLogin() = FirebaseAuth.getInstance().currentUser != null

	protected fun getFirebaseUser() = FirebaseAuth.getInstance().currentUser

	protected fun logout() {
		FirebaseAuth.getInstance().signOut()
		onLogoutSuccess()
	}

	open fun onLoginSuccess() {}

	open fun onLogoutSuccess() {}
}