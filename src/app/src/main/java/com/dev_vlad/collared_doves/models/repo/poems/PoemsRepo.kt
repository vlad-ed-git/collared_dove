package com.dev_vlad.collared_doves.models.repo.poems

import com.dev_vlad.collared_doves.models.dao.PoemsDao
import com.dev_vlad.collared_doves.models.entities.Poems
import kotlinx.coroutines.flow.Flow


class PoemsRepo(private val poemsDao: PoemsDao) {

    private val MAX_POEMS_PER_PAGE = 30

    fun getAllPoems(searchQuery: String, page : Int): Flow<List<Poems>> {

        val pageNo = if (page < 1) 1 else page
        val limit = pageNo * MAX_POEMS_PER_PAGE
        return if (searchQuery.isNotBlank())
            poemsDao.searchAllPoems(searchQuery, page)
        else
            poemsDao.getAllPoems(limit)

    }

}