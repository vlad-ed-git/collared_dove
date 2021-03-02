package com.dev_vlad.collared_doves.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dev_vlad.collared_doves.databinding.PoemItemBinding
import com.dev_vlad.collared_doves.models.entities.Poems

class PoemsAdapter(private val actionsListener: PoemsAdapter.ActionsListener) :
    ListAdapter<Poems, PoemsAdapter.PoemsViewHolder>(PoemsDiffUtil()) {

    interface ActionsListener {
        fun onClick(poemId: Int)
        fun onLongPress(poem: Poems, isChecked: Boolean)
    }

    class PoemsViewHolder(
        private val poemItemBinding: PoemItemBinding
    ) : RecyclerView.ViewHolder(poemItemBinding.root) {
        fun bind(poem: Poems, actionsListener: ActionsListener) {
            poemItemBinding.apply {
                poemCard.setOnLongClickListener {
                    poemCard.isChecked = !poemCard.isChecked
                    actionsListener.onLongPress(poem = poem, isChecked = poemCard.isChecked)
                    true
                }
                poemCard.setOnClickListener {
                    actionsListener.onClick(poemId = poem.poemId)
                }
                poemTitle.text = poem.title
                poemBody.text = poem.body
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoemsViewHolder {
        val binding = PoemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PoemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PoemsViewHolder, position: Int) {
        val poem = getItem(position)
        holder.bind(poem, actionsListener)
    }

    class PoemsDiffUtil : DiffUtil.ItemCallback<Poems>() {
        override fun areItemsTheSame(oldItem: Poems, newItem: Poems) =
            oldItem.poemId == newItem.poemId

        override fun areContentsTheSame(oldItem: Poems, newItem: Poems) = oldItem == newItem

    }


}