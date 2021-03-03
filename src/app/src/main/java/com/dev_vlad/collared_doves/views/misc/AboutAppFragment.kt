package com.dev_vlad.collared_doves.views.misc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dev_vlad.collared_doves.R
import com.dev_vlad.collared_doves.databinding.FragmentAboutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutAppFragment  : Fragment(R.layout.fragment_about){

    companion object {
        private const val TERMS_URL = "https://vlad-ed-git.github.io/collared_dove/terms/terms_and_conditions.html"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAboutBinding.bind(view)
        binding.terms.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(TERMS_URL)
            startActivity(i)
        }
    }
}