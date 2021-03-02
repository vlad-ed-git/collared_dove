package com.dev_vlad.collared_doves.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dev_vlad.collared_doves.models.entities.Poems
import com.dev_vlad.collared_doves.models.repo.poems.PoemsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PoemsAddEditViewModel
@Inject constructor(private val poemsRepo: PoemsRepo) : ViewModel() {

    companion object{
        private val TAG = PoemsAddEditViewModel::class.java.simpleName
    }

    fun fetchPoemToEdit(poemId: Int): LiveData<Poems?> {
        return poemsRepo.getPoem(poemId)
    }

    private var poem : Poems? = null
    fun setEditedPoem(poem: Poems){
        this.poem = poem
    }
    private val state = MutableStateFlow<STATE>(STATE.IDLE)
    fun getEditingState() = state.asLiveData()
    fun savePoem(title: String, body: String) {
        state.value = STATE.SAVING
        viewModelScope.launch(Dispatchers.IO) {
            if (poem != null){
                //editing a poem
               val editedPoem = Poems(
                   poemId = poem!!.poemId,
                   title = title,
                   body = body,
                   updated = System.currentTimeMillis(),
                   created = poem!!.created,
                   writtenBy = poem!!.writtenBy,
                   isFavorite = poem!!.isFavorite
               )
                poemsRepo.updatePoem(editedPoem)
                withContext(Dispatchers.Main) {
                    state.value = STATE.SAVED
                }
            }else {

                val newPoem = Poems(
                    writtenBy = "1",
                    title = title,
                    body = body
                )
                poemsRepo.addPoem(
                    newPoem
                )
                withContext(Dispatchers.Main) {
                    state.value = STATE.SAVED
                }
            }
        }
    }
}
enum class STATE{
    IDLE,
    SAVING,
    SAVED
}