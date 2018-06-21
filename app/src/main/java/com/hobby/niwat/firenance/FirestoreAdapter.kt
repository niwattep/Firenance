package com.hobby.niwat.firenance

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*

abstract class FirestoreAdapter<VH : RecyclerView.ViewHolder>(var query: Query?) : RecyclerView.Adapter<VH>(), EventListener<QuerySnapshot> {
	private var listenerRegistration: ListenerRegistration? = null
	private var snapshots = ArrayList<DocumentSnapshot>()
	private val TAG = "FirestoreAdapter"

	abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

	fun getSnapshotAt(position: Int) = snapshots[position]

	fun getSnapshots() = snapshots

	override fun getItemCount(): Int = snapshots.size

	abstract override fun onBindViewHolder(holder: VH, position: Int)

	fun setNewQuery(query: Query?) {
		stopListening()
		snapshots.clear()
		notifyDataSetChanged()
		this.query = query
		startListening()
	}

	override fun onEvent(documentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
		if (e != null) {
			Log.w(TAG, "onEvent:error", e)
			return
		}

		Log.d(TAG, "onEvent:numChanges:" + documentSnapshots?.documentChanges?.size)
		documentSnapshots?.let {
			for (change in it.documentChanges) {
				when (change.type) {
					DocumentChange.Type.ADDED -> onDocumentAdded(change)
					DocumentChange.Type.MODIFIED -> onDocumentModified(change)
					DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
				}
			}
		}

		onDataChanged(documentSnapshots, e)
	}

	fun onDocumentAdded(change: DocumentChange) {
		snapshots.add(change.newIndex, change.document)
		notifyItemInserted(change.newIndex)
	}

	fun onDocumentModified(change: DocumentChange) {
		if (change.oldIndex == change.newIndex) {
			snapshots[change.oldIndex] = change.document
			notifyItemChanged(change.oldIndex)
		} else {
			snapshots.removeAt(change.oldIndex)
			snapshots.add(change.newIndex, change.document)
			notifyItemMoved(change.oldIndex, change.newIndex)
		}
	}

	fun onDocumentRemoved(change: DocumentChange) {
		snapshots.removeAt(change.oldIndex)
		notifyItemRemoved(change.oldIndex)
	}

	open fun onDataChanged(documentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {}

	fun startListening() {
		query?.let {
			listenerRegistration = it.addSnapshotListener(this)
		}
	}

	fun stopListening() {
		listenerRegistration?.remove()

		snapshots.clear()
		notifyDataSetChanged()
	}
}