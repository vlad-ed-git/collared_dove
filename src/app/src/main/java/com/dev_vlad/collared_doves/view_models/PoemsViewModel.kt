package com.dev_vlad.collared_doves.view_models

import android.os.Parcelable
import androidx.lifecycle.*
import com.dev_vlad.collared_doves.models.entities.Poems
import com.dev_vlad.collared_doves.models.repo.poems.PoemsRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class PoemsViewModel
@Inject constructor(
    private val poemsRepo: PoemsRepo,
    private val stateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private val TAG = PoemsViewModel::class.java.simpleName
        private const val POEMS_STORED_STATE_KEY = "poems_stored_state"
    }

    private val currentPoemsState = stateHandle.getLiveData<PoemsStateModifiers>( POEMS_STORED_STATE_KEY , PoemsStateModifiers())
    private fun getCurrentPoemsState(): PoemsStateModifiers {
        return currentPoemsState.value?:PoemsStateModifiers()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val poemsFlow = currentPoemsState.asFlow().flatMapLatest {
        poemsRepo.getAllPoems(
            searchQuery = it.query,
            page = it.page,
            onlyFavorites = it.isFavorite,
            ofUserId = it.ofUserId
        )
    }

    val poems = poemsFlow.asLiveData()

    fun searchPoems(queryString: String) {
        val currentState = getCurrentPoemsState()
        val newState = PoemsStateModifiers(
            page = currentState.page,
            query = queryString,
            isFavorite = currentState.isFavorite
        )
        currentPoemsState.value = newState
    }

    fun toggleFavoritesOnly(onlyFavs: Boolean) {
        val currentState = getCurrentPoemsState()
        val newState = PoemsStateModifiers(
            page = currentState.page,
            query = currentState.query,
            isFavorite = onlyFavs
        )
        currentPoemsState.value = newState
    }

    fun toggleMineOnly(onlyMyPoems: Boolean) {
        val currentState = getCurrentPoemsState()
        val userId = if (onlyMyPoems) "1" else "" //TODO use actual users' id
        val newState = PoemsStateModifiers(
            page = currentState.page,
            query = currentState.query,
            isFavorite = currentState.isFavorite,
            ofUserId = userId
        )
        currentPoemsState.value = newState
    }

    private val selectedPoems = ArrayList<Poems>()
    fun toggleSelectedPoems(poem: Poems, checked: Boolean) {
        if (checked)
            selectedPoems.add(poem)
        else
            selectedPoems.remove(poem)
    }

    //TODO only delete my poems
    fun deleteSelectedPoems() {
        if (selectedPoems.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            for (poem in selectedPoems) {
                poemsRepo.deletePoem(poem)
            }
        }
    }

    fun clearSelectedPoems() {
        selectedPoems.clear()
    }

    fun deletePoem(poem: Poems) {
        viewModelScope.launch(Dispatchers.IO) {
            poemsRepo.deletePoem(poem)
        }
    }


}

@Parcelize
data class PoemsStateModifiers(
    val page: Int = 1,
    val query: String = "",
    val isFavorite: Boolean = false,
    val ofUserId: String = "",
): Parcelable