package io.jiantao.android.sample.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.jiantao.android.sample.R

/**
 * 商城类应用首页实现
 *
 * RecyclerView + ViewPager + RecyclerView 三层嵌套实现
 *
 * Created by Jiantao.Yang on 10/22/21.
 */
class MallAppHomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_just_recyclerview)
    }
}