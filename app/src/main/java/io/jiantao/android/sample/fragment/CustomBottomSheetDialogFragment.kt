package io.jiantao.android.sample.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.jiantao.android.sample.R
import io.jiantao.android.uikit.dialog.IBottomSheetDialogFragment

/**
 * description
 * @author Created by jiantaoyang
 * @date 2019-06-14
 */
class CustomBottomSheetDialogFragment : IBottomSheetDialogFragment() {

    // 成员变量定义。如果可能为null，则需要加上?
//    private var mView: View? = null
    // object CunstomxxxFragment 实现单例

    // 类型java的静态方法实现
    companion object {
        // 静态常量
        val COUNT = 100;

        fun newInstance(): CustomBottomSheetDialogFragment {
            return CustomBottomSheetDialogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?): View? {
//        if (mView == null) {
//            mView = inflater.inflate(R.layout.test_bottom_dialog_layout, container, false)
//        }
        return inflater.inflate(R.layout.test_bottom_dialog_layout, container, false)
    }

}