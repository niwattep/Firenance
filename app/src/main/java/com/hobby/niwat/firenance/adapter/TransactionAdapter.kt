package com.hobby.niwat.firenance.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.hobby.niwat.firenance.R
import com.hobby.niwat.firenance.model.Transaction
import kotlinx.android.synthetic.main.view_transaction_item.view.*
import java.text.SimpleDateFormat
import java.util.*

open class TransactionAdapter(query: Query?, val context: Context?) : FirestoreAdapter<TransactionAdapter.TransactionViewHolder>(query) {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
		return TransactionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_transaction_item, parent, false))
	}

	override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
		holder.bind(getSnapshotAt(position), position)
	}

	inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(data: DocumentSnapshot, position: Int) {
			val transaction = data.toObject(Transaction::class.java)
			transaction?.let {
				itemView.amountTextView.text = "$${it.amount}"
				val date = SimpleDateFormat("EEEE dd 'at' HH:mm", Locale.US).format(it.timestamp)
				itemView.dateTimeTextView.text = date

				if (it.note.isNotBlank()) {
					itemView.noteLabelTextView.visibility = View.VISIBLE
					itemView.noteTextView.visibility = View.VISIBLE
					itemView.noteTextView.text = it.note
				} else {
					itemView.noteLabelTextView.visibility = View.GONE
					itemView.noteTextView.visibility = View.GONE
				}
			}
		}
	}
}