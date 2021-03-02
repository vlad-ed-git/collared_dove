package com.dev_vlad.collared_doves.views.poems

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dev_vlad.collared_doves.R
import com.dev_vlad.collared_doves.view_models.PoemsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PoemsFragment : Fragment(R.layout.fragment_poems) {

    private val viewModel : PoemsViewModel by viewModels<PoemsViewModel>()



}