package com.hobby.niwat.firenance

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_create_transaction_dialog.*
import java.util.*

class CreateTransactionDialogFragment : DialogFragment() {

	var type: Int? = null
	var uId: String? = null
	lateinit var firestore: FirebaseFirestore

	companion object {

		const val EXTRA_TRANSACTION_TYPE = "extra-transaction_type"
		const val TAG = "CreateTransactionDialogFragment"

		fun newInstance(type: Int?): CreateTransactionDialogFragment {
			return CreateTransactionDialogFragment().apply {
				arguments = Bundle().apply {
					type?.let { putInt(EXTRA_TRANSACTION_TYPE, it) }
				}
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		type = arguments?.getInt(EXTRA_TRANSACTION_TYPE)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_create_transaction_dialog, container, false)
	}

	override fun onStart() {
		super.onStart()
		initView()
		initFirestore()
		initUser()
		fillData()
	}

	override fun onResume() {
		super.onResume()
		dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
	}

	private fun initView() {
		addButton.setOnClickListener { onAddTransactionListener() }
	}

	private fun initFirestore() {
		firestore = FirebaseFirestore.getInstance()
	}

	private fun initUser() {
		FirebaseAuth.getInstance().currentUser?.let {
			uId = it.uid
		}?: dialog.dismiss()
	}

	private fun fillData() {
		titleTextView.text = when (type) {
			1 -> "Add Income"
			2 -> "Add Expense"
			else -> "Create Transaction"
		}
		context?.let {
			val color = when (type) {
				1 -> ContextCompat.getColor(it, R.color.green)
				2 -> ContextCompat.getColor(it, R.color.red)
				else -> 0
			}
			titleTextView.setTextColor(color)
		}

		amountEditText.requestFocus()
		KeyboardUtils.showKeyboard(context, amountEditText)
	}

	private fun onAddTransactionListener() {
		addButton.isEnabled = false
		val amount = if (amountEditText.text.isNotBlank()) {
			Integer.parseInt(amountEditText.text.toString())
		} else {
			0
		}
		val type = type?:0
		val note = if (noteEditText.text.isNotEmpty() && noteEditText.text.isNotBlank()) noteEditText.text.toString() else ""
		val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
		val month = Calendar.getInstance().get(Calendar.MONTH) + 1
		val year = Calendar.getInstance().get(Calendar.YEAR)
		val transaction = Transaction(amount, type, note, day, month, year)

		uId?.let {
			firestore.collection(Database.COL_USER).document(it).collection(Database.COL_TRANSACTION).apply {
				add(transaction).apply {
					addOnSuccessListener { onTransactionAddSuccess() }
					addOnFailureListener { onTransactionAddFail() }
				}
			}
		}
	}

	private fun onTransactionAddSuccess() {
		dismiss()
	}

	private fun onTransactionAddFail() {
		addButton.isEnabled = true
	}
}