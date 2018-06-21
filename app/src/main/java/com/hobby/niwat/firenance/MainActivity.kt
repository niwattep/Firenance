package com.hobby.niwat.firenance

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
	private val RC_SIGN_IN = 101
	private val TAG = "MainActivity"

	var adapter: TransactionAdapter? = null
	var query: Query? = null
	var firestore: FirebaseFirestore? = null
	val calendar = Calendar.getInstance()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		initFirestore()
		initRecyclerView()
	}

	override fun onStart() {
		super.onStart()
		if (shouldStartSignIn()) {
			startSignIn()
			return
		}

		adapter?.startListening()
		fillData()
	}

	override fun onStop() {
		super.onStop()
		adapter?.stopListening()
	}

	private fun initFirestore() {
		firestore = FirebaseFirestore.getInstance()
		FirebaseAuth.getInstance().currentUser?.let {
			val userUid = it.uid
			firestore?.let {
				query = it.collection(Database.COL_USER)
						.document(userUid)
						.collection(Database.COL_TRANSACTION)
						.whereEqualTo(Transaction.YEAR, calendar.get(Calendar.YEAR))
						.whereEqualTo(Transaction.MONTH, calendar.get(Calendar.MONTH) + 1)
						.orderBy(Transaction.DAY, Query.Direction.DESCENDING)
						.orderBy(Transaction.TIMESTAMP, Query.Direction.DESCENDING)
			}
		}
	}

	private fun initRecyclerView() {
		adapter = object: TransactionAdapter(query, this) {
			override fun onDataChanged(documentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
				val remaining = calculateRemaining()

				balanceTextView.text = when {
					remaining >= 0 -> "$$remaining"
					remaining < 0 -> "-$${-remaining}"
					else -> "$$remaining"
				}
			}
		}

		recyclerView.apply {
			layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
			adapter = this@MainActivity.adapter
		}
	}

	private fun fillData() {
		val date = SimpleDateFormat("MMM yyyy", Locale.US).format(calendar.time)
		//dateTextView.text = date
	}

	private fun calculateRemaining(): Int {
		val snapshots = adapter?.getSnapshots()
		var total = 0
		snapshots?.let {
			for (snapshot in it) {
				val transaction = snapshot.toObject(Transaction::class.java)
				transaction?.let {
					total = when (it.type) {
						Transaction.TYPE_INCOME -> total + it.amount
						Transaction.TYPE_EXPENSE -> total - it.amount
						else -> total
					}
				}
			}
		}
		return total
	}

	fun onCreateTransactionButtonClicked(type: Int) {
		val createTransactionDialog = CreateTransactionDialogFragment.newInstance(type)
		createTransactionDialog.show(supportFragmentManager, CreateTransactionDialogFragment.TAG)
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
				if (resultCode != RESULT_OK && shouldStartSignIn()) {
					startSignIn()
				}
			}
		}
	}

	private fun shouldStartSignIn() = FirebaseAuth.getInstance().currentUser == null
}