package com.hobby.niwat.firenance

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.firestore.Query
import com.hobby.niwat.firenance.adapter.TransactionGroupAdapter
import com.hobby.niwat.firenance.model.CommonUserStat
import com.hobby.niwat.firenance.model.TransactionGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AbstractFirestoreActivity() {

	var adapter: TransactionGroupAdapter? = null
	var query: Query? = null

	companion object {
		private const val TAG = "MainActivity"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		createQuery()
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

	private fun createQuery() {
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
		adapter = TransactionGroupAdapter(query, this, getFirebaseUser()?.uid)

		recyclerView.apply {
			layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
			adapter = this@MainActivity.adapter
		}
	}

	private fun getCurrentBalance() {
		getFirebaseUser()?.let {
			val userUid = it.uid
			firestore?.let {
				it.collection(Database.COL_USERS)
						.document(userUid)
						.addSnapshotListener { snapshot, e ->
							if (e == null) {
								fillBalance(snapshot?.toObject(CommonUserStat::class.java))
							} else {
								Log.e(TAG, "error getting current balance.")
							}
						}
			}
		}
	}

	private fun fillBalance(userStat: CommonUserStat?) {
		userStat?.let {
			it.balance?.let {
				balanceTextView.text = "$${it}"
			}
		}
	}
}