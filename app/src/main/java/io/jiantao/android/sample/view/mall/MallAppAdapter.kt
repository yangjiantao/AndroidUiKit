package io.jiantao.android.sample.view.mall

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.jiantao.android.sample.databinding.ViewJustViewBinding
import io.jiantao.android.sample.databinding.ViewJustViewpagerBinding
import io.jiantao.android.sample.databinding.ViewTablayoutViewpagerBinding
import io.jiantao.android.sample.util.ColorViewHolder
import io.jiantao.android.sample.util.TextViewHolder
import io.jiantao.android.sample.util.ViewPagerWithSimpleChildHolder

/**
 * Created by Jiantao.Yang on 10/22/21.
 */
class MallAppAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val viewTypeColor = 1
    private val viewTypeText = 2
    private val viewTypeViewPagerSimple = 3
    private val viewTypeViewPagerComplicated = 4

    private val count = 20

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == viewTypeText) {
            TextViewHolder(TextViewHolder.getSimpleTextView(inflater))
        } else if (viewType == viewTypeColor) {
            ColorViewHolder(ViewJustViewBinding.inflate(inflater).root)
        } else if (viewType == viewTypeViewPagerSimple) {
            ViewPagerWithSimpleChildHolder(ViewJustViewpagerBinding.inflate(inflater, parent, false).root)
        } else {
            TablayoutViewPagerHolder(ViewTablayoutViewpagerBinding.inflate(inflater))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ColorViewHolder -> {
                holder.bind(height = 500)
            }
            is TextViewHolder -> {
                holder.bind("TextViewHolder >>>", position)
            }
            is ViewPagerWithSimpleChildHolder -> {
                holder.bind()
            }
            is TablayoutViewPagerHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 3 || position == count - 1) {
            viewTypeViewPagerComplicated
        } else if (position % 10 == 0) {
            viewTypeViewPagerSimple
        } else if (position % 2 == 0) {
            viewTypeColor
        } else viewTypeText
    }
}

/**
 * TabLayout + ViewPager(recyclerView)
 * viewPager包裹纵向的RecyclerView
 */
class TablayoutViewPagerHolder(binding: ViewTablayoutViewpagerBinding)
    : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.tabLayout.setupWithViewPager(binding.viewPager)
//        binding.viewPager.adapter = SimpleRecyclerViewPagerAdapter()
        binding.viewPager.adapter = WrappingViewPagerAdapter()
    }

    fun bind() {

    }

}


