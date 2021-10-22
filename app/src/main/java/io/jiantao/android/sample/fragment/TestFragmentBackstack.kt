package io.jiantao.android.sample.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.jiantao.android.sample.R
import io.jiantao.android.sample.widget.EditTextDialog
import kotlinx.android.synthetic.main.testfragment_backstack.*

/**
 * description
 * @author Created by jiantaoyang
 * @date 2019/5/6
 */
class TestFragmentBackstack : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testfragment_backstack)
        button.setOnClickListener {
            replaceFragment("fragmentB")
        }

        bt_showBottomDialog.setOnClickListener {
            showBottomSheetDialog()
        }

        addFragment("fragmentA")
    }

    private fun showBottomSheetDialog() {
//        IBottomSheetDialogFragment.showWithRetainInstance(supportFragmentManager, "tag") { CustomBottomSheetDialogFragment.newInstance() }
//        val dialog = CustomBottomSheetDialogFragment.newInstance()
//        dialog.show(supportFragmentManager, "tag")
        val dialog = EditTextDialog()
        dialog.show(supportFragmentManager)
    }

    private fun replaceFragment(label: String) {
        var beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.replace(R.id.fl_content, BlankFragment.newInstance(label))
        beginTransaction.addToBackStack(label)
        beginTransaction.commit()
    }

    private fun addFragment(label: String) {
        var beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(R.id.fl_content, BlankFragment.newInstance(label))
        beginTransaction.addToBackStack(label)
        beginTransaction.commit()
    }
}