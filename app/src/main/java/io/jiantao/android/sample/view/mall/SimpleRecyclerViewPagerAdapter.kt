package io.jiantao.android.sample.view.mall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import io.jiantao.android.sample.databinding.ViewJustRecyclerviewBinding
import io.jiantao.android.sample.util.SimpleRecyclerViewTextAdapter

class SimpleRecyclerViewPagerAdapter : PagerAdapter() {

    private val count = 5

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ViewJustRecyclerviewBinding.inflate(LayoutInflater.from(container.context), container, true)
        binding.root.adapter = SimpleRecyclerViewTextAdapter()

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "标题 $position"
    }

    /**
     * Return the number of views available.
     */
    override fun getCount(): Int {
        return count
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
}
