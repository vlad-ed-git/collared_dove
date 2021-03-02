package com.dev_vlad.collared_doves.models.repo.poems

import androidx.lifecycle.LiveData
import com.dev_vlad.collared_doves.models.dao.PoemsDao
import com.dev_vlad.collared_doves.models.entities.Poems
import kotlinx.coroutines.flow.Flow


class PoemsRepo(private val poemsDao: PoemsDao) {

    private val MAX_POEMS_PER_PAGE = 30

    fun getAllPoems(searchQuery: String, page: Int, onlyFavorites: Boolean, ofUserId: String): Flow<List<Poems>> {

        val pageNo = if (page < 1) 1 else page
        val limit = pageNo * MAX_POEMS_PER_PAGE
        return when {
            searchQuery.isNotBlank() -> {
                when {
                    onlyFavorites -> poemsDao.searchMyFavorites(searchQuery, limit)
                    ofUserId.isNotBlank() -> poemsDao.searchPoemsOfUser(searchQuery, limit, ofUserId)
                    else -> poemsDao.searchAllPoems(searchQuery, limit)
                }
            }
            else -> {
                when {
                    onlyFavorites -> poemsDao.getMyFavorites(limit)
                    ofUserId.isNotBlank() -> poemsDao.getMyPoems(limit, userId=ofUserId)
                    else -> poemsDao.getAllPoems(limit)
                }
            }
        }
    }

    fun getPoem(poemId: Int): LiveData<Poems?> {
        return poemsDao.getPoemById(poemId)
    }

    suspend fun updatePoem(editedPoem: Poems) {
        poemsDao.update(editedPoem)
    }

    suspend fun addPoem(newPoem: Poems) {
        poemsDao.insert(newPoem)
    }

}