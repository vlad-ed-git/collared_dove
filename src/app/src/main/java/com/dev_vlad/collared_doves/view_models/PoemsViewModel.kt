package com.dev_vlad.collared_doves.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dev_vlad.collared_doves.models.repo.poems.PoemsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PoemsViewModel
    @Inject constructor(private val poemsRepo: PoemsRepo) : ViewModel() {

        val poems = poemsRepo.getAllPoems().asLiveData()


}