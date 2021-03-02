package com.dev_vlad.collared_doves.views.poems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev_vlad.collared_doves.R
import com.dev_vlad.collared_doves.databinding.FragmentPoemsBinding
import com.dev_vlad.collared_doves.utils.MyLogger
import com.dev_vlad.collared_doves.view_models.PoemsViewModel
import com.dev_vlad.collared_doves.views.adapters.PoemsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PoemsFragment : Fragment(R.layout.fragment_poems), PoemsAdapter.ActionsListener {

    private val TAG = PoemsFragment::class.java.simpleName
    private val viewModel : PoemsViewModel by viewModels<PoemsViewModel>()
    private val poemsAdapter : PoemsAdapter by lazy {
        PoemsAdapter(this)
    }

    private var _binding : FragmentPoemsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPoemsBinding.inflate(inflater, container, false)
        initAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.poems.observe(
            viewLifecycleOwner,
            Observer {
                if (it != null) {
                    poemsAdapter.submitList(it)
                    MyLogger.logThis(
                        TAG,
                        "observe poems",
                        "found ${it.size} poems"
                    )
                }else{
                    MyLogger.logThis(
                        TAG,
                        "observe poems",
                        "poems list is null"
                    )
                }
            }
        )
    }

    private fun initAdapter(){
        binding.apply {
            poemsRv.adapter = poemsAdapter
            poemsRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            poemsRv.setHasFixedSize(true)
        }
    }


    /************ poems actions ****************/
    override fun onClick(poemId: Int) {
        MyLogger.logThis(
            TAG, "onClick - override adapter item", " poem $poemId was clicked"
        )
    }

    override fun onLongPress(poemId: Int, isChecked: Boolean) {
        MyLogger.logThis(
            TAG, "onLongPress - override adapter item", " poem $poemId was long pressed to check $isChecked"
        )
    }


    /** Other life cycle methods **/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}