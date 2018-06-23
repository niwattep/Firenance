package com.hobby.niwat.firenance

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hobby.niwat.firenance.adapter.TransactionGroupAdapter
import com.hobby.niwat.firenance.model.CommonUserStat
import com.hobby.niwat.firenance.model.TransactionGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	private val RC_SIGN_IN = 101
	private val TAG = "MainActivity"

	var adapter: TransactionGroupAdapter? = null
	var query: Query? = null
	var firestore: FirebaseFirestore? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		initFirestore()
		initRecyclerView()
		getCurrentBalance()
	}

	override fun onStart() {
		super.onStart()
		if (!isLogin()) {
			startSignIn()
			return
		}

		adapter?.startListening()
	}

	override fun onStop() {
		super.onStop()
		adapter?.stopListening()
	}

	private fun initFirestore() {
		firestore = FirebaseFirestore.getInstance()
		getFirebaseUser()?.let {
			val userUid = it.uid
			firestore?.let {
				query = it.collection(Database.COL_USERS)
						.document(userUid)
						.collection(Database.COL_TRANSACTION_GROUPS)
						.orderBy(TransactionGroup.VALUE, Query.Direction.ASCENDING)
			}
		}
	}

	private fun initRecyclerView() {
		adapter = TransactionGroupAdapter(query, this)

		recyclerView.apply {
			layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
			adapter = this@MainActivity.adapter
		}
	}

	private fun startSignIn() {
		val intent = AuthUI.getInstance().createSignInIntentBuilder()
				.setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build()))
				.setIsSmartLockEnabled(false)
				.build()

		startActivityForResult(intent, RC_SIGN_IN)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode) {
			RC_SIGN_IN -> {
				if (resultCode != RESULT_OK && isLogin()) {
					startSignIn()
				}
			}
		}
	}

	private fun getCurrentBalance() {
		getFirebaseUser()?.let {
			val userUid = it.uid
			firestore?.let {
				it.collection(Database.COL_USERS)
						.document(userUid)
						.collection(Database.DOC_USER_STAT)
						.document(Database.COL_COMMON)
						.get().addOnCompleteListener {
							when {
								it.isSuccessful && it.result.exists()-> {
									fillBalancce(it.result.toObject(CommonUserStat::class.java))
								}
							}
						}
			}
		}
	}

	private fun fillBalancce(userStat: CommonUserStat?) {
		userStat?.let {
			it.balance?.let {
				balanceTextView.text = "$${it}"
			}
		}
	}

	private fun isLogin() = FirebaseAuth.getInstance().currentUser != null

	private fun getFirebaseUser() = FirebaseAuth.getInstance().currentUser
}