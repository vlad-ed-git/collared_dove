package com.dev_vlad.collared_doves.views.poems

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.dev_vlad.collared_doves.R
import com.dev_vlad.collared_doves.databinding.FragmentEditPoemBinding
import com.dev_vlad.collared_doves.models.entities.Poems
import com.dev_vlad.collared_doves.utils.hideKeyBoard
import com.dev_vlad.collared_doves.utils.showSnackBarToUser
import com.dev_vlad.collared_doves.view_models.PoemsAddEditViewModel
import com.dev_vlad.collared_doves.view_models.STATE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PoemsAddEditFragment : Fragment(R.layout.fragment_edit_poem) {

    private val TAG = PoemsAddEditFragment::class.java.simpleName
    private val viewModel: PoemsAddEditViewModel by viewModels<PoemsAddEditViewModel>()


    private var _binding: FragmentEditPoemBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPoemBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    private val args: PoemDetailsFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val poemId = args.PoemId
        if (poemId > 0) {
            viewModel.fetchPoemToEdit(poemId).observe(
                viewLifecycleOwner, Observer {
                    if (it != null) {
                        binding.progress.isVisible = false
                        displayPoem(it)
                        viewModel.setEditedPoem(it)
                    }
                }
            )
        } else {
            binding.progress.isVisible = false
        }

        viewModel.getEditingState().observe(
            viewLifecycleOwner, Observer { it ->
                when (it) {
                    STATE.SAVING -> {
                        binding.progress.isVisible = true
                    }
                    STATE.SAVED -> {
                        binding.progress.isVisible = false
                        binding.container.showSnackBarToUser(
                            msgResId = R.string.poem_saved,
                            isErrorMsg = false
                        )
                    }
                    else -> {

                    }
                }
            }
        )
    }

    private fun displayPoem(poem: Poems) {
        binding.apply {
            poemTitleEt.setText(poem.title)
            poemSubjectEt.setText(poem.body)
        }
    }


    /************ poems actions ****************/
    private var displayedSnackbar: Snackbar? = null
    private fun dismissSnackBar() {
        if (displayedSnackbar != null && displayedSnackbar!!.isShown) {
            displayedSnackbar!!.dismiss()
            displayedSnackbar = null
        }
    }

    private fun savePoem() {
        hideKeyBoard(requireContext(), binding.container)
        if (binding.progress.isVisible) return

        val title = binding.poemTitleEt.text.toString()
        val body = binding.poemSubjectEt.text.toString()
        when {
            title.isBlank() -> {
                dismissSnackBar()
                displayedSnackbar = binding.container.showSnackBarToUser(
                    msgResId = R.string.title_empty_err,
                    actionMessage = R.string.got_it,
                    actionToTake = {},
                    isErrorMsg = true
                )
            }
            body.isBlank() -> {
                dismissSnackBar()
                displayedSnackbar = binding.container.showSnackBarToUser(
                    msgResId = R.string.body_empty_err,
                    actionMessage = R.string.got_it,
                    actionToTake = {},
                    isErrorMsg = true
                )
            }
            else -> {
                viewModel.savePoem(title, body)
            }
        }
    }


    /************* MENU **************/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.poems_edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                savePoem()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }


    override fun onPause() {
        super.onPause()
        dismissSnackBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}