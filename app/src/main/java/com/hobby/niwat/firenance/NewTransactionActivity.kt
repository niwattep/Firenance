package com.hobby.niwat.firenance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hobby.niwat.numberkeyboard.NumberKeyboardView
import kotlinx.android.synthetic.main.activity_new_transaction.*

class NewTransactionActivity : AbstractFirestoreActivity() {

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
		numberKeyboardView.apply {
			mNumberClickListener = OnNumberPress()
			mBackSpaceClickListener = OnBackspacePress()
			mDoneClickListener = OnDonePress()
		}
	}

	private fun createNewTransaction() {

	}

	inner class OnNumberPress(): NumberKeyboardView.OnNumberClickListener {
		override fun onNumberClick(number: Int) {
			if (amount.length <= MAX_NUMBER_LENGHT) {
				amountTextView.let {
					it.append(number.toString())
					amount = it.text.toString()
				}
			}
		}
	}

	inner class OnBackspacePress(): NumberKeyboardView.OnBackSpaceClickListener {
		override fun onBackSpaceClick() {
			amount = amount.let {
				if (it.isNotEmpty()) {
					it.subSequence(0, it.lastIndex).toString()
				} else ""
			}
			amountTextView.text = amount
		}

	}

	inner class OnDonePress(): NumberKeyboardView.OnDoneClickListener {
		override fun onDoneClick() {
			createNewTransaction()
		}

	}
}