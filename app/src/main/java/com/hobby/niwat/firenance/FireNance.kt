package com.hobby.niwat.firenance

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class FireNance : Application() {
	override fun onCreate() {
		super.onCreate()
		FirebaseFirestore.getInstance().firestoreSettings =
				FirebaseFirestoreSettings.Builder().setTimestampsInSnapshotsEnabled(true).build()
	}
}