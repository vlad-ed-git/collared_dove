package com.dev_vlad.collared_doves.models.dao

import androidx.room.*
import com.dev_vlad.collared_doves.models.entities.Poems
import kotlinx.coroutines.flow.Flow

@Dao
interface PoemsDao {


    @Query("SELECT * FROM poems ORDER BY updated DESC LIMIT :limit")
    fun getAllPoems(limit: Int): Flow<List<Poems>>

    @Query("SELECT * FROM poems WHERE title LIKE '%' || :searchQuery || '%' OR body LIKE '%' || :searchQuery || '%'  ORDER BY updated DESC LIMIT :limit")
    fun searchAllPoems(searchQuery: String, limit: Int): Flow<List<Poems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(poem: Poems)

    @Delete
    suspend fun delete(poem: Poems)

    @Update
    suspend fun update(poem: Poems)
}