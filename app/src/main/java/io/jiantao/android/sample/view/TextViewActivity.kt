package io.jiantao.android.sample.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import io.jiantao.android.sample.R
import kotlinx.android.synthetic.main.activity_main.tv_view
import kotlinx.android.synthetic.main.activity_test_textview.*

/**
 * TextView兼容问题
 * @author Created by jiantaoyang
 * @date 2019-08-20
 */
class TextViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_textview)


        var str = "# test_android_text world hello"
        var spannableString = SpannableString(str)
        var imgSpan = ImageSpan(this, R.drawable.order_ic_tag_sharebuy)
        spannableString.setSpan(imgSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        tv_view.text = spannableString

        tv_align_view.text = spannableString;
    }
}
