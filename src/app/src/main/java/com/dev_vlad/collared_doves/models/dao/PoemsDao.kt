package com.dev_vlad.collared_doves.models.dao

import androidx.lifecycle.LiveData
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

    @Query("SELECT * FROM poems WHERE poemId =:poemId ")
    fun getPoemById(poemId: Int): LiveData<Poems?>


    //favorites
    @Query("SELECT * FROM poems WHERE isFavorite =:isFavorite  AND title LIKE '%' || :searchQuery || '%' OR body LIKE '%' || :searchQuery || '%'  ORDER BY updated DESC LIMIT :limit")
    fun searchMyFavorites(
        searchQuery: String,
        limit: Int,
        isFavorite: Boolean = true
    ): Flow<List<Poems>>

    @Query("SELECT * FROM poems WHERE isFavorite =:isFavorite ORDER BY updated DESC LIMIT :limit")
    fun getMyFavorites(limit: Int, isFavorite: Boolean = true): Flow<List<Poems>>


    //filtered by user id
    @Query("SELECT * FROM poems WHERE writtenBy =:userId  AND title LIKE '%' || :searchQuery || '%' OR body LIKE '%' || :searchQuery || '%'  ORDER BY updated DESC LIMIT :limit")
    fun searchPoemsOfUser(searchQuery: String, limit: Int, userId: String): Flow<List<Poems>>

    @Query("SELECT * FROM poems WHERE  writtenBy =:userId ORDER BY updated DESC LIMIT :limit")
    fun getMyPoems(limit: Int, userId: String): Flow<List<Poems>>

}