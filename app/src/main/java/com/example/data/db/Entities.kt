package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.model.BetSelection
import org.json.JSONArray
import org.json.JSONObject

@Entity(tableName = "bet_orders")
data class BetOrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ticketId: String,               // e.g. "GB-8942-781"
    val timestamp: Long = System.currentTimeMillis(),
    val betType: String,               // "Single", "Multiple"
    val totalStake: Double,
    val totalOdds: Double,
    val potentialWin: Double,
    val bonusAmount: Double = 0.0,
    val status: String = "OPEN",        // "OPEN", "WON", "LOST", "CASHED_OUT"
    val cashOutOffer: Double = 0.0,
    val selectionsJson: String          // JSON serialized list of BetSelection
)

@Entity(tableName = "wallet_transactions")
data class WalletTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val txId: String,                   // e.g. "TXN-90214"
    val timestamp: Long = System.currentTimeMillis(),
    val type: String,                   // "DEPOSIT", "WITHDRAWAL", "BET_PLACED", "BET_WON", "CASHOUT"
    val amount: Double,
    val description: String,
    val balanceAfter: Double
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val username: String = "GlobalPlayer_99",
    val phoneNumber: String = "+233 50 123 4567",
    val email: String = "player@globalbetting.com",
    val country: String = "Ghana",
    val balance: Double = 0.00,
    val bonusBalance: Double = 0.00,
    val vipTier: String = "VIP Bronze",
    val isLoggedIn: Boolean = false,
    val currencySymbol: String = "GH₵"
)

class Converters {
    companion object {
        fun selectionsToJson(selections: List<BetSelection>): String {
            val array = JSONArray()
            for (sel in selections) {
                val obj = JSONObject()
                obj.put("matchId", sel.matchId)
                obj.put("homeTeam", sel.homeTeam)
                obj.put("awayTeam", sel.awayTeam)
                obj.put("league", sel.league)
                obj.put("marketId", sel.marketId)
                obj.put("marketName", sel.marketName)
                obj.put("outcomeId", sel.outcomeId)
                obj.put("outcomeLabel", sel.outcomeLabel)
                obj.put("odds", sel.odds)
                obj.put("liveScoreWhenPlaced", sel.liveScoreWhenPlaced)
                obj.put("selectionStatus", sel.selectionStatus)
                array.put(obj)
            }
            return array.toString()
        }

        fun jsonToSelections(jsonStr: String): List<BetSelection> {
            val list = mutableListOf<BetSelection>()
            if (jsonStr.isBlank()) return list
            try {
                val array = JSONArray(jsonStr)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        BetSelection(
                            matchId = obj.optString("matchId"),
                            homeTeam = obj.optString("homeTeam"),
                            awayTeam = obj.optString("awayTeam"),
                            league = obj.optString("league"),
                            marketId = obj.optString("marketId"),
                            marketName = obj.optString("marketName"),
                            outcomeId = obj.optString("outcomeId"),
                            outcomeLabel = obj.optString("outcomeLabel"),
                            odds = obj.optDouble("odds", 1.0),
                            liveScoreWhenPlaced = obj.optString("liveScoreWhenPlaced"),
                            selectionStatus = obj.optString("selectionStatus", "PENDING")
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return list
        }
    }
}
