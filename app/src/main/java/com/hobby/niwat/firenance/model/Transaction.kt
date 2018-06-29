package com.hobby.niwat.firenance.model

import java.util.*


class Transaction(
		var amount: Int = 0,
		var note: String = "",
		var day: Int = 0,
		var month: Int = 0,
		var year: Int = 0,
		var timestamp: Date = Date(),
		var group: String = "") {

	companion object {
		const val AMOUNT = "amount"
		const val NOTE = "note"
		const val DAY = "day"
		const val MONTH = "month"
		const val YEAR = "year"
		const val TIMESTAMP = "timestamp"
		const val GROUP = "group"
	}
}