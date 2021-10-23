package io.jiantao.android.sample.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import io.jiantao.android.sample.databinding.ViewJustTextviewBinding

class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val DEFAULT_HEIGHT = 200

    init {
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DEFAULT_HEIGHT)
    }

    fun bind(color: Int = Color.BLUE, height: Int = DEFAULT_HEIGHT) {
        itemView.setBackgroundColor(color)
        itemView.layoutParams.height = height
    }
}

class TextViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView) {
    @SuppressLint("SetTextI18n")
    fun bind(str: String = "", position: Int = 0) {
        textView.text = "$str + $position"
    }

    companion object {
        fun getSimpleTextView(inflater: LayoutInflater): TextView {
            return ViewJustTextviewBinding.inflate(inflater).textView
        }
    }
}

/**
 * 水平方法ViewPager，简单的Child视图，比如图片、文字等非事件冲突的复杂组件
 */
class ViewPagerWithSimpleChildHolder(viewPager: ViewPager) : RecyclerView.ViewHolder(viewPager) {

    init {
        viewPager.adapter = SimpleViewPagerAdapter()
    }

    fun bind() {

    }

    class SimpleViewPagerAdapter : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val binding = ViewJustTextviewBinding.inflate(LayoutInflater.from(container.context), container, true)
            binding.root.text = "Text In ViewPager :$position"
            return binding.root
        }

        /**
         * Return the number of views available.
         */
        override fun getCount(): Int {
            return 5
        }

        /**
         * Determines whether a page View is associated with a specific key object
         * as returned by [.instantiateItem]. This method is
         * required for a PagerAdapter to function properly.
         *
         * @param view Page View to check for association with `object`
         * @param object Object to check for association with `view`
         * @return true if `view` is associated with the key object `object`
         */
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}