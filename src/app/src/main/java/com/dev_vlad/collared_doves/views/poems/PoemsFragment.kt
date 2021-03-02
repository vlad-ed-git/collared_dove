package com.dev_vlad.collared_doves.views.poems

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev_vlad.collared_doves.R
import com.dev_vlad.collared_doves.databinding.FragmentPoemsBinding
import com.dev_vlad.collared_doves.models.entities.Poems
import com.dev_vlad.collared_doves.utils.MyLogger
import com.dev_vlad.collared_doves.view_models.PoemsViewModel
import com.dev_vlad.collared_doves.views.adapters.PoemsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PoemsFragment : Fragment(R.layout.fragment_poems), PoemsAdapter.ActionsListener {

    private val TAG = PoemsFragment::class.java.simpleName
    private val viewModel: PoemsViewModel by viewModels<PoemsViewModel>()
    private val poemsAdapter: PoemsAdapter by lazy {
        PoemsAdapter(this)
    }

    private var _binding: FragmentPoemsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPoemsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.poems.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
                    poemsAdapter.submitList(it)
                    MyLogger.logThis(
                        TAG,
                        "observe poems",
                        "found ${it.size} poems"
                    )
                } else {
                    MyLogger.logThis(
                        TAG,
                        "observe poems",
                        "poems list is null"
                    )
                }
            }
        )
    }

    private fun initAdapter() {
        binding.apply {
            poemsRv.adapter = poemsAdapter
            poemsRv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            poemsRv.setHasFixedSize(true)
            writeFab.setOnClickListener {
                val action = PoemsFragmentDirections.actionPoemsFragmentToPoemsAddEditFragment()
                findNavController().navigate(action)
            }
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val poem = poemsAdapter.currentList[viewHolder.bindingAdapterPosition]
                    deleteSwipedPoem(poem)
                    poemsAdapter.notifyItemChanged(viewHolder.bindingAdapterPosition)
                }

            }).attachToRecyclerView(poemsRv)

        }
    }


    /************ poems actions ****************/
    private var popUp: AlertDialog? = null
    private fun dismissPopUp() {
        if (popUp != null && popUp!!.isShowing) {
            popUp?.dismiss()
            popUp = null
        }
    }

    private fun deleteSwipedPoem(poem: Poems) {
        dismissPopUp()
        popUp = AlertDialog.Builder(requireActivity())
            .setMessage(R.string.confirm_delete_poem)
            .setPositiveButton(R.string.delete,
                DialogInterface.OnClickListener { dialog, _ ->
                    viewModel.deletePoem(poem)
                    dialog.dismiss()
                })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                }).create()

        popUp?.show()
    }

    private fun deleteSelectedPoems() {
        dismissPopUp()
        popUp = AlertDialog.Builder(requireActivity())
            .setMessage(R.string.confirm_delete_poem)
            .setPositiveButton(R.string.delete,
                DialogInterface.OnClickListener { dialog, _ ->
                    viewModel.deleteSelectedPoems()
                    dialog.dismiss()
                })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                }).create()

        popUp?.show()
    }

    private fun toggleMineOnly(onlyMyPoems: Boolean) {
        viewModel.toggleMineOnly(onlyMyPoems)
    }

    private fun toggleFavoritesOnly(onlyFavs: Boolean) {
        viewModel.toggleFavoritesOnly(onlyFavs)
    }

    fun onSearchTextQueryChange(queryString: String) {
        viewModel.searchPoems(queryString)
    }

    override fun onClick(poemId: Int) {
        val action = PoemsFragmentDirections.actionPoemsFragmentToPoemDetailsFragment(poemId)
        findNavController().navigate(action)
    }

    override fun onLongPress(poem: Poems, isChecked: Boolean) {
        viewModel.toggleSelectedPoems(poem, isChecked)
    }


    /************* MENU **************/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.poems_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                val searchView = item.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        onSearchTextQueryChange(newText.orEmpty())
                        return true
                    }

                })
                return true
            }
            R.id.action_toggle_fav -> {
                item.isChecked = !item.isChecked
                toggleFavoritesOnly(onlyFavs = item.isChecked)
                return true
            }

            R.id.action_show_mine -> {
                item.isChecked = !item.isChecked
                toggleMineOnly(onlyMyPoems = item.isChecked)
                return true
            }

            R.id.action_delete -> {
                deleteSelectedPoems()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }


    /** Other life cycle methods **/
    override fun onDestroyView() {
        super.onDestroyView()
        dismissPopUp()
        viewModel.clearSelectedPoems()
        _binding = null
    }

}