package com.hobby.niwat.firenance.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.hobby.niwat.firenance.R
import com.hobby.niwat.firenance.model.TransactionGroup
import kotlinx.android.synthetic.main.view_transaction_summary_card.view.*

open class TransactionGroupAdapter(query: Query?, val context: Context?) : FirestoreAdapter<TransactionGroupAdapter.TransactionGroupViewHolder>(query) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
			TransactionGroupViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_transaction_summary_card, parent, false))

	override fun onBindViewHolder(holder: TransactionGroupViewHolder, position: Int) {
		holder.bind(getSnapshotAt(position), position)
	}

	inner class TransactionGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(data: DocumentSnapshot, position: Int) {
			val transactionGroup = data.toObject(TransactionGroup::class.java)
			transactionGroup?.let {
				itemView.apply {
					headerText.text = it.name ?: ""
					actualAmountLabel.text = it.actualText ?: ""
					actualAmountText.text = it.actual?.toString() ?: ""
					targetAmountLabel.text = it.targetText ?: ""
					targetAmountText.text = it.target?.toString() ?: ""
				}
			}
		}
	}
}