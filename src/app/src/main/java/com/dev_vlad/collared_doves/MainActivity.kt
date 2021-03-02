package com.dev_vlad.collared_doves

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.dev_vlad.collared_doves.databinding.ActivityMainBinding
import com.dev_vlad.collared_doves.utils.MyLogger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        navController = navHostFragment.findNavController()
        //specify home fragments
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.poemsFragment,
            )
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)


        //hide and show menus depending on fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.poemsFragment -> {
                    binding.apply {
                        toolbar.isVisible = true
                        toolbar.title = getString(R.string.poems_fragment_lbl)
                    }
                }

                R.id.poemDetailsFragment -> {
                    binding.apply {
                        toolbar.isVisible = true
                        toolbar.title = getString(R.string.fragment_poem_details_lbl)
                    }
                }

                R.id.poemsAddEditFragment -> {
                    binding.apply {
                        toolbar.isVisible = true
                        toolbar.title = getString(R.string.fragment_edit_poem_lbl)
                    }
                }

                R.id.aboutAppFragment -> {
                    binding.apply {
                        toolbar.isVisible = false
                    }
                }

                else -> {
                    binding.toolbar.isVisible = true
                }

            }
        }

    }


    //toolbar handle back navigation
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}