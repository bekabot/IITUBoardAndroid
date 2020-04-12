package kz.iitu.iituboardandroid.ui.record

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.utils.bind

class CarouselItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageView: ImageView by bind(itemView, R.id.imageView)

    fun set(imageUrl: String) {
        Glide.with(itemView.context)
            .load(imageUrl)
            .into(imageView)
    }
}