package com.dev_vlad.collared_doves.models.repo.poems

import com.dev_vlad.collared_doves.models.dao.PoemsDao

class PoemsRepo(private val poemsDao: PoemsDao) {


    fun getAllPoems() = poemsDao.getAllPoems()
}