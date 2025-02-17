package com.wfs.truthsearch.ui.compare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wfs.truthsearch.R
import com.wfs.truthsearch.databinding.FragmentCompareBinding
import com.wfs.truthsearch.databinding.ItemCompareBinding

/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
class CompareFragment : Fragment() {

    private var _binding: FragmentCompareBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val compareViewModel = ViewModelProvider(this).get(CompareViewModel::class.java)
        _binding = FragmentCompareBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerviewTransform
        val adapter = TransformAdapter()
        recyclerView.adapter = adapter
        compareViewModel.texts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class TransformAdapter :
        ListAdapter<String, TransformViewHolder>(object : DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }) {

        private val drawables = listOf(
            R.drawable.holiness,
            R.drawable.shalom,
            R.drawable.joy,
            R.drawable.temple,
            R.drawable.growth,
            R.drawable.agape,
            R.drawable.stewardship,
            R.drawable.prayer,
            R.drawable.faith,
            R.drawable.sabbath,
            R.drawable.wisdom,
            R.drawable.servie_people,
            R.drawable.hope,
            R.drawable.foregivenes,
            R.drawable.courage,
            R.drawable.live_truth,
            R.drawable.compassion,
            R.drawable.humility,
            R.drawable.graditude,
            R.drawable.unity,
            R.drawable.patience,
            R.drawable.warfare



        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransformViewHolder {
            val binding = ItemCompareBinding.inflate(LayoutInflater.from(parent.context))
            return TransformViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TransformViewHolder, position: Int) {
            holder.textView.text = getItem(position)
            holder.imageView.setImageDrawable(
                ResourcesCompat.getDrawable(holder.imageView.resources, drawables[position], null)
            )
        }
    }

    class TransformViewHolder(binding: ItemCompareBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageView: ImageView = binding.imageViewItemTransform
        val textView: TextView = binding.textViewItemTransform
    }
}