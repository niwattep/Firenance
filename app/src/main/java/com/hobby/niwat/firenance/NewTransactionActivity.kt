package com.hobby.niwat.firenance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.hobby.niwat.firenance.model.Transaction
import kotlinx.android.synthetic.main.activity_new_transaction.*
import java.util.*

class NewTransactionActivity : AbstractFirestoreActivity(), NumberKeyboardView.OnKeyPressedListener {

	private var groupName: String? = null
	private var groupDocName: String? = null
	private var groupValue: Int? = null

	private var amount: String = ""

	companion object {
		private const val EXTRA_TRANSACTION_GROUP_DOC_NAME = "transaction-group-doc-name"
		private const val EXTRA_TRANSACTION_GROUP_NAME = "transaction-group-name"
		private const val EXTRA_TRANSACTION_GROUP_VALUE = "transaction-group-value"
		private const val TAG = "NewTransactionAct"
		private const val MAX_NUMBER_LENGHT = 8

		fun createIntent(context: Context, groupValue: Int?, groupName: String?, groupDocName: String?) = Intent(context, NewTransactionActivity::class.java).apply {
			putExtras(Bundle().apply {
				groupDocName?.let { putString(EXTRA_TRANSACTION_GROUP_DOC_NAME, it) }
				groupName?.let { putString(EXTRA_TRANSACTION_GROUP_NAME, it) }
				groupValue?.let { putInt(EXTRA_TRANSACTION_GROUP_VALUE, it) }
			})
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_new_transaction)

		toolbar?.let {
			setSupportActionBar(it)
			supportActionBar?.setDisplayHomeAsUpEnabled(true)
		}

		extractExtras()
		fillData()
		setupKeyboard()
	}

	private fun extractExtras() {
		intent.extras.let {
			groupName = it.getString(EXTRA_TRANSACTION_GROUP_NAME, "")
			groupDocName = it.getString(EXTRA_TRANSACTION_GROUP_DOC_NAME, "")
			groupValue = it.getInt(EXTRA_TRANSACTION_GROUP_VALUE, 0)
		}
	}

	private fun fillData() {
		groupLabelTextView.text = groupName
	}

	private fun setupKeyboard() {
		numberKeyboardView.setOnKeyPressedListener(this)
	}

	private fun getTransaction(): Transaction? {
		if (amount.isNotBlank() && amount.isValidAmount()) {
			val today = Calendar.getInstance()
			val day = today.get(Calendar.DAY_OF_MONTH)
			val month = today.get(Calendar.MONTH) + 1
			val year = today.get(Calendar.YEAR)
			return groupDocName?.let {
				Transaction().apply {
					amount = this@NewTransactionActivity.amount.toInt()
					note = noteEditText.text.toString()
					this.day = day
					this.month = month
					this.year = year
					timestamp = today.time
					group = it
				}
			}
		}
		return null
	}

	private fun createNewTransaction(transaction: Transaction?) {
		showLoading()
		getFirebaseUser()?.uid?.let { userUid ->
			groupDocName?.let { docName ->
				transaction?.let { transaction ->
					hideInvalidAmountError()
					firestore?.let {
						it.collection(Database.COL_USERS)
								.document(userUid)
								.collection(Database.COL_TRANSACTION_GROUPS)
								.document(docName)
								.collection(Database.COL_TRANSACTIONS)
								.add(transaction)
								.addOnCompleteListener {
									hideLoading()
									if (it.isSuccessful) {
										finish()
									} else {
										Toast.makeText(this, "Error create new transaction", Toast.LENGTH_LONG).show()
									}
								}
					}
				} ?: run {
					showInvalidAmountError()
					hideLoading()
				}

			}

		}
	}

	override fun onNumberPressed(number: Int) {
		if (amount.length <= MAX_NUMBER_LENGHT) {
			amount += number.toString()
			if (amount.isNotBlank()) {
				amountTextView.text = "${getString(R.string.currency)}$amount"
			}
		}
	}

	override fun onBackSpacePressed() {
		amount = amount.let {
			if (it.isNotEmpty()) {
				it.subSequence(0, it.lastIndex).toString()
			} else ""
		}
		amountTextView.text = if (amount.isNotBlank()) "${getString(R.string.currency)}$amount" else ""
	}

	override fun onDonePressed() {
		createNewTransaction(getTransaction())
	}

	private fun showInvalidAmountError() {
		errorTextView.visibility = View.VISIBLE
	}

	private fun hideInvalidAmountError() {
		errorTextView.visibility = View.INVISIBLE
	}

	private fun showLoading() {
		if (!progressBar.isShown) {
			progressBar.visibility = View.VISIBLE
			loadingBackgroundView.visibility = View.VISIBLE
		}
	}

	private fun hideLoading() {
		if (progressBar.isShown) {
			progressBar.visibility = View.INVISIBLE
			loadingBackgroundView.visibility = View.INVISIBLE
		}
	}
}

fun String.isValidAmount() = !this.matches(kotlin.text.Regex("^0+\$"))
