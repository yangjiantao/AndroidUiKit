package io.jiantao.android.sample.fragment

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.jiantao.android.sample.R

/**
 * description
 * @author Created by jiantaoyang
 * @date 2019-06-14
 */
class CustomBottomSheetDialogFragment : BottomSheetDialogFragment() {

    // object CunstomxxxFragment 实现单例

    // 类型java的静态方法实现
    companion object {
        // 静态常量
        val COUNT = 100;
        fun newInstance(): CustomBottomSheetDialogFragment {
            return CustomBottomSheetDialogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.test_bottom_dialog_layout, container, false)
    }

}