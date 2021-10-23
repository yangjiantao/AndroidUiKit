package io.jiantao.android.sample.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

/**
 * Created by Jiantao.Yang on 10/23/21.
 */
class SimpleRecyclerViewTextAdapter(private val randomCount: Boolean = false) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val count = if (randomCount) {
        Random.nextInt(1, TEST_COUNT)
    } else TEST_COUNT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TextViewHolder(TextViewHolder.getSimpleTextView(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TextViewHolder).bind("Text In Simple RecylerView $position")
    }

    override fun getItemCount(): Int {
        return count
    }
}