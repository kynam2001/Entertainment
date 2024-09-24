package com.example.ringtuneandwallpaper.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ringtuneandwallpaper.model.RingtoneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RingtoneDataAccessObject {
    @Query("SELECT * FROM ringtones")
    fun getAllRingtones(): Flow<List<RingtoneEntity>>

    @Query("DELETE FROM ringtones")
    suspend fun deleteAllRingtones()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRingtone(ringtoneList: List<RingtoneEntity>)

    @Update
    suspend fun updateRingtone(ringtone: RingtoneEntity)

    @Delete
    suspend fun deleteRingtone(ringtone: RingtoneEntity)

}