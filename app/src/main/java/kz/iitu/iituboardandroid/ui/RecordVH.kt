package kz.iitu.iituboardandroid.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.RecordsAdapter
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.utils.bind

class RecordVH(itemView: View, val listener: RecordsAdapter.OnProfileInteraction) :
    RecyclerView.ViewHolder(itemView) {
    private val image: ImageView by bind(itemView, R.id.image)
    private val title: TextView by bind(itemView, R.id.title)
    private val author: TextView by bind(itemView, R.id.author)
    private val parentView: ConstraintLayout by bind(itemView, R.id.parent_view)

    fun set(record: Record) {
        title.text = record.record_title

        record.getImage()?.let {
            image.visibility = View.VISIBLE
            Glide.with(itemView.context)
                .load(it)
                .into(image)
        }

        parentView.setOnClickListener {
            listener.onRecordClick(record)
        }

        author.text = "От ${record.author} - ${record.getPrintableCreationDate()}"
    }
}