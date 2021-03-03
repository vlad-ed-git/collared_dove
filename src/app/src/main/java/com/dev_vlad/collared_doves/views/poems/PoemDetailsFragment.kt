package com.dev_vlad.collared_doves.views.poems

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dev_vlad.collared_doves.R
import com.dev_vlad.collared_doves.databinding.FragmentPoemDetailsBinding
import com.dev_vlad.collared_doves.models.entities.Poems
import com.dev_vlad.collared_doves.view_models.PoemDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PoemDetailsFragment : Fragment(R.layout.fragment_poem_details) {

    private val TAG = PoemsFragment::class.java.simpleName
    private val viewModel: PoemDetailsViewModel by viewModels<PoemDetailsViewModel>()


    private var _binding: FragmentPoemDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPoemDetailsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    private val args: PoemDetailsFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchPoem(args.PoemId).observe(viewLifecycleOwner, {
            if (it != null) {
                viewModel.initPoem(it)
                displayPoem(it)
                if (::actionToggleFav.isInitialized){
                    setFavIcon(it.isFavorite)
                }else{
                    pendingFavStatus = it.isFavorite
                }
            }
        })
    }

    private fun displayPoem(poem: Poems) {
        binding.apply {
            poemTitle.text = poem.title
            poemSubject.text = poem.body
        }
    }


    /************ poems actions ****************/
    private fun toggleIsFavorite() {
        viewModel.toggleIsFavorite()
    }

    private fun setFavIcon(isFavPoem : Boolean){
        if (isFavPoem) {
            actionToggleFav.setIcon(R.drawable.ic_favorite_filled)
        }else{
            actionToggleFav.setIcon(R.drawable.ic_favorite)
        }
    }

    private fun editPoem() {
        //TODO when hook up with server, check if this poem is not user's own poem
        //TODO for now it is theirs...so this is an edit action
        val poemId = args.PoemId
        val action =
            PoemDetailsFragmentDirections.actionPoemDetailsFragmentToPoemsAddEditFragment(poemId)
        findNavController().navigate(action)
    }

    private fun sharePoem(){
       val poem = viewModel.getPoemForSharing()
       if (poem.isNotBlank()){
           val sendIntent: Intent = Intent().apply {
               action = Intent.ACTION_SEND
               putExtra(Intent.EXTRA_TEXT, poem)
               type = "text/plain"
           }

           val shareIntent = Intent.createChooser(sendIntent, null)
           startActivity(shareIntent)
       }
    }

    /************* MENU **************/
    private lateinit var actionToggleFav : MenuItem
    private var pendingFavStatus: Boolean? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.poem_details_menu, menu)
        actionToggleFav = menu.findItem(R.id.action_toggle_fav)
        pendingFavStatus?.let {
            pendingFavState ->
            setFavIcon(pendingFavState)
            pendingFavStatus = null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                editPoem()
                true
            }

            R.id.action_toggle_fav-> {
                toggleIsFavorite()
                true
            }

            R.id.action_share -> {
                sharePoem()
                true
            }


            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }


    /** Other life cycle methods **/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}