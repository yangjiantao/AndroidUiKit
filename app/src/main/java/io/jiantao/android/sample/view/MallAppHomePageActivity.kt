package io.jiantao.android.sample.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.jiantao.android.sample.databinding.ViewJustRecyclerviewBinding
import io.jiantao.android.sample.view.mall.MallAppAdapter

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
        val binding = ViewJustRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.recyclerview)

//        binding.recyclerview.apply {  }
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = MallAppAdapter()

    }
}