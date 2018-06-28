package com.hobby.niwat.firenance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.firestore.Query
import com.hobby.niwat.firenance.adapter.TransactionAdapter
import com.hobby.niwat.firenance.model.Transaction
import com.hobby.niwat.firenance.model.TransactionGroup
import kotlinx.android.synthetic.main.activity_transaction_group.*

class TransactionGroupActivity : AbstractFirestoreActivity() {

	private var titleText: String? = ""
	private var groupDocName: String? = ""
	private var groupValue: Int? = 0

	private var query: Query? = null
	private var adapter: TransactionAdapter? = null

	companion object {
		private const val EXTRA_TRANSACTION_GROUP_DOC_NAME = "transaction-group-doc-name"
		private const val EXTRA_TRANSACTION_GROUP_NAME = "transaction-group-name"
		private const val EXTRA_TRANSACTION_GROUP_VALUE = "transaction-group-value"
		private const val TAG = "TransactionGroupAct"

		fun createIntent(context: Context, groupDocName: String?, groupName: String?, groupValue: Int?) = Intent(context, TransactionGroupActivity::class.java).apply {
			putExtras(Bundle().apply {
				groupDocName?.let { putString(EXTRA_TRANSACTION_GROUP_DOC_NAME, it) }
				groupName?.let { putString(EXTRA_TRANSACTION_GROUP_NAME, it) }
				groupValue?.let { putInt(EXTRA_TRANSACTION_GROUP_VALUE, it) }
			})
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_transaction_group)

		extractExtras()

		toolbar?.let {
			setSupportActionBar(it)
		}

		supportActionBar?.title = titleText
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		createQuery()
		initRecyclerView()
		loadTransactionGroup()
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

	override fun onBackPressed() {
		super.onBackPressed()
		finish()
	}

	private fun extractExtras() {
		intent.extras.let {
			titleText = it.getString(EXTRA_TRANSACTION_GROUP_NAME, "")
			groupDocName = it.getString(EXTRA_TRANSACTION_GROUP_DOC_NAME, "")
			groupValue = it.getInt(EXTRA_TRANSACTION_GROUP_VALUE, 0)
		}
	}

	private fun createQuery() {
		getFirebaseUser()?.let {
			val userUid = it.uid
			firestore?.let {
				query = it.collection(Database.COL_USERS)
						.document(userUid)
						.collection(Database.COL_TRANSACTIONS)
						.whereEqualTo(Transaction.GROUP, groupDocName)
						.orderBy(Transaction.TIMESTAMP, Query.Direction.DESCENDING)
			}
		}
	}

	private fun initRecyclerView() {
		adapter = TransactionAdapter(query, this)

		recyclerView.apply {
			layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
			adapter = this@TransactionGroupActivity.adapter
		}
	}

	private fun loadTransactionGroup() {
		getFirebaseUser()?.let {
			val userUid = it.uid
			groupDocName?.let {
				val docName = it
				firestore?.let {
					it.collection(Database.COL_USERS)
							.document(userUid)
							.collection(Database.COL_TRANSACTION_GROUPS)
							.document(docName)
							.addSnapshotListener { snapshot, e ->
								if (e == null) {
									fillHeader(snapshot?.toObject(TransactionGroup::class.java))
								} else {
									Log.e(TAG, "error getting transaction group.")
								}
							}
				}
			}

		}
	}

	private fun fillHeader(transactionGroup: TransactionGroup?) {
		transactionGroup?.let {
			actualAmountText.text = it.actual?.toString() ?: ""
			actualAmountLabel.text = it.actualText ?: ""
			targetAmountText.text = it.target?.toString() ?: ""
			targetAmountLabel.text = it.targetText ?: ""
		}
	}
}