package io.jiantao.android.sample.commonimport android.view.ViewGroupimport androidx.fragment.app.Fragmentimport androidx.fragment.app.FragmentManagerimport androidx.fragment.app.FragmentPagerAdapterinternal class SimplePagerAdapter(fm: FragmentManager,                                  private val fragments: List<Fragment>,                                  private val titles: List<String>)    : FragmentPagerAdapter(fm) {    override fun getCount(): Int {        return fragments.size    }    override fun getPageTitle(position: Int): CharSequence? {        return titles[position]    }    override fun getItem(position: Int): androidx.fragment.app.Fragment {        return fragments[position]    }    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {        // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁        // super.destroyItem(container, position, object);    }    override fun getItemPosition(`object`: Any): Int {        return androidx.viewpager.widget.PagerAdapter.POSITION_NONE    }}