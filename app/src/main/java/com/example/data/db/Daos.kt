package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BetOrderDao {
    @Query("SELECT * FROM bet_orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<BetOrderEntity>>

    @Query("SELECT * FROM bet_orders WHERE status = 'OPEN' ORDER BY timestamp DESC")
    fun getOpenOrders(): Flow<List<BetOrderEntity>>

    @Query("SELECT * FROM bet_orders WHERE status != 'OPEN' ORDER BY timestamp DESC")
    fun getSettledOrders(): Flow<List<BetOrderEntity>>

    @Query("SELECT * FROM bet_orders WHERE ticketId = :ticketId LIMIT 1")
    suspend fun getOrderByTicketId(ticketId: String): BetOrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: BetOrderEntity): Long

    @Update
    suspend fun updateOrder(order: BetOrderEntity)

    @Query("UPDATE bet_orders SET status = 'CASHED_OUT', cashOutOffer = :cashOutAmount WHERE ticketId = :ticketId")
    suspend fun cashOutOrder(ticketId: String, cashOutAmount: Double)
}

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallet_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<WalletTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(txn: WalletTransactionEntity): Long
}

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET balance = :newBalance WHERE id = 1")
    suspend fun updateBalance(newBalance: Double)

    @Query("UPDATE user_profile SET isLoggedIn = :loggedIn WHERE id = 1")
    suspend fun setLoggedIn(loggedIn: Boolean)
}
