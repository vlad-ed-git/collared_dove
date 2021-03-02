package com.dev_vlad.collared_doves.views.poems

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
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
    private val viewModel : PoemDetailsViewModel by viewModels<PoemDetailsViewModel>()


    private var _binding : FragmentPoemDetailsBinding? = null
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

    private val args : PoemDetailsFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchPoem(args.PoemId).observe(viewLifecycleOwner, {
            if (it != null){
                displayPoem(it)
            }
        })
    }

    private fun displayPoem(poem : Poems){
        binding.apply {
            poemTitle.text = poem.title
            poemSubject.text = poem.body
        }
    }



    /************ poems actions ****************/
    private fun addOrEditPrompt(){
        //TODO when hook up with server, check if this poem is not user's own poem
        //TODO for now it is theirs...so this is an edit action
        val poemId = args.PoemId
        val action = PoemDetailsFragmentDirections.actionPoemDetailsFragmentToPoemsAddEditFragment(poemId)
        findNavController().navigate(action)
    }

    /************* MENU **************/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.poem_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_or_edit -> {
                addOrEditPrompt()
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