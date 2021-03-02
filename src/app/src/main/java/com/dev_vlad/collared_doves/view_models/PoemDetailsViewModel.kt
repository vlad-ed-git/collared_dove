package com.dev_vlad.collared_doves.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_vlad.collared_doves.models.entities.Poems
import com.dev_vlad.collared_doves.models.repo.poems.PoemsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PoemDetailsViewModel
@Inject constructor(private val poemsRepo: PoemsRepo) : ViewModel() {


    fun fetchPoem(poemId: Int): LiveData<Poems?> {
        return poemsRepo.getPoem(poemId)
    }


    private var poem: Poems? = null
    fun initPoem(fetchedPoem: Poems){
        poem = fetchedPoem
    }
    fun toggleIsFavorite() {
        poem?.let {
            oldPoem ->
            val updatedPoem = Poems(
                poemId = oldPoem.poemId,
                writtenBy = oldPoem.writtenBy,
                title = oldPoem.title,
                body = oldPoem.body,
                updated = System.currentTimeMillis(),
                isFavorite = !oldPoem.isFavorite
            )
            viewModelScope.launch(Dispatchers.IO) {
                poemsRepo.updatePoem(updatedPoem)
            }
        }
    }

    companion object {
        private val TAG = PoemDetailsViewModel::class.java.simpleName
    }

}