package com.dev_vlad.collared_doves.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dev_vlad.collared_doves.models.repo.poems.PoemsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class PoemsViewModel
    @Inject constructor(private val poemsRepo: PoemsRepo) : ViewModel() {


    private val currentPoemsState = MutableStateFlow(PoemsStateModifiers())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val poemsFlow = currentPoemsState.flatMapLatest {
        poemsRepo.getAllPoems(searchQuery = it.query, page = it.page)
    }

    val poems = poemsFlow.asLiveData()

    fun searchPoems(queryString: String) {
       val currentState = currentPoemsState.value
        val newState = PoemsStateModifiers(
            page = currentState.page,
            query = queryString
        )
       currentPoemsState.value = newState
    }

}

data class PoemsStateModifiers(
    var page : Int = 1,
    var query: String = ""
)