package com.dev_vlad.collared_doves.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dev_vlad.collared_doves.models.entities.Poems
import com.dev_vlad.collared_doves.models.repo.poems.PoemsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PoemDetailsViewModel
        @Inject constructor(private val poemsRepo: PoemsRepo) : ViewModel() {


    fun fetchPoem(poemId: Int): LiveData<Poems?> {
       return poemsRepo.getPoem(poemId)
    }

    companion object{
        private val TAG = PoemDetailsViewModel::class.java.simpleName
    }

}