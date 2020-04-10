package kz.iitu.iituboardandroid.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.utils.bind

class RecordVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val image: ImageView by bind(itemView, R.id.image)
    private val title: TextView by bind(itemView, R.id.title)

    fun set(record: Record) {
        title.text = record.record_title

        Glide.with(itemView.context)
            .load(record.getImage())
            .into(image)
    }
}